package com.example.bs4bspringbackend.service;

import com.example.bs4bspringbackend.enums.GameState;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import com.example.bs4bspringbackend.model.Props.Prop;
import com.example.bs4bspringbackend.interfaces.RoomListener;
import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.model.Props.ShrinkingGasCloudProp;
import com.example.bs4bspringbackend.model.Weapon.Bullet;
import com.example.bs4bspringbackend.model.ZombieNPC;
import com.example.bs4bspringbackend.network.client.InputData;
import com.example.bs4bspringbackend.security.InputDataValidator;
import com.example.bs4bspringbackend.security.RateLimiterService;
import com.example.bs4bspringbackend.util.Broadcaster;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class GameEngineService {

    private static final Logger logger = LogManager.getLogger(GameEngineService.class);
    private final Map<String, ScheduledFuture<?>> roomLoops = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final InputService inputService;

    // Security
    @Autowired
    private RateLimiterService rateLimiterService;

    @Setter
    private RoomListener roomListener;



    @Autowired
    public GameEngineService(InputService inputService) {
        this.inputService = inputService;
    }

    public void startGameLoop(GameRoom room) {
        room.setState(GameState.COUNTDOWN);
        room.setCountdownSeconds(3);
        logger.info("Starting countdown for room: {}", room.getRoomId());

        final ScheduledFuture<?>[] countdownTaskRef = new ScheduledFuture<?>[1];

        countdownTaskRef[0] = scheduler.scheduleAtFixedRate(() -> {
            int secondsLeft = room.getCountdownSeconds();

            if (secondsLeft <= 1) {
                room.setCountdownSeconds(0);
                room.setState(GameState.RUNNING);
                logger.info("Countdown finished. Starting game for room: {}", room.getRoomId());

                Broadcaster.sendGameRoomDTO(room.getSessions(), room);

                ScheduledFuture<?> loop = scheduler.scheduleAtFixedRate(() -> {
                    try {
                        roomUpdate(room, 0.05f);
                    } catch (Exception e) {
                        logger.error("Exception in game loop for room {}: ", room.getRoomId(), e);
                    }
                }, 0, 50, TimeUnit.MILLISECONDS);
                roomLoops.put(room.getRoomId(), loop);

                countdownTaskRef[0].cancel(false);
            } else {
                room.setCountdownSeconds(secondsLeft - 1);
                Broadcaster.sendGameRoomDTO(room.getSessions(), room);
            }
        }, 0, 1, TimeUnit.SECONDS);


    }

    private void roomUpdate(GameRoom room, float deltaTime) {
        if (room.getState() != GameState.RUNNING) return;

        handlePlayerInput(room, deltaTime);
        handleProps(room, deltaTime);
        handleBullets(room, deltaTime);
        handleAmmoReload(room, deltaTime);
        handleZombies(room, deltaTime);

        Broadcaster.sendGameRoomDTO(room.getSessions(), room);
    }

    private void handlePlayerInput(GameRoom room, float deltaTime) {
        for (AbstractPlayer player : room.getPlayers()) {
            if (!rateLimiterService.allowMessage(player.getSessionId())) {
                logger.warn("Rate limit exceeded for player {}", player.getName());
                continue;
            }
            InputData input = inputService.getInputBySessionId(player.getSessionId());
            if (input == null) {
                logger.debug("No input received for player {}", player.getName()); // Or info-level if useful
                continue;
            }
            if (!InputDataValidator.isValid(input)) {
                logger.warn("Invalid input received from player {}", player.getName());
                continue;
            }

            player.Move(input.getMoveDirection(), deltaTime, room.getColliders());
            handlePlayerShooting(player, input, room);
            handleShootRotation(player);
        }
    }

    private void handlePlayerShooting(AbstractPlayer player, InputData input, GameRoom room) {
        boolean hasShotBefore = inputService.getHasShotLastFrame()
                .getOrDefault(player.getSessionId(), false);

        if (input.isShoot() && !hasShotBefore) {
            player.shoot(room, input.getShootDirection());
            player.setShootFacingAngle(input.getShootDirection().angleDegrees());
            player.setLastShotTime(System.currentTimeMillis());
            player.setForceShootRotation(true);
        }

        inputService.getHasShotLastFrame().put(player.getSessionId(), input.isShoot());
    }

    private void handleShootRotation(AbstractPlayer player) {
        if (!player.isForceShootRotation()) return;

        long timeSinceShot = System.currentTimeMillis() - player.getLastShotTime();
        if (timeSinceShot <= 200) {
            player.setRotationAngle((int) player.getShootFacingAngle());
        } else {
            player.setForceShootRotation(false);
        }
    }

    private void handleProps(GameRoom room, float deltaTime) {
        for (Prop prop : room.getProps()) {
            if (prop instanceof ShrinkingGasCloudProp cloud) {
                updateGasCloud(room, cloud, deltaTime);
            }

            for (AbstractPlayer player : room.getPlayers()) {
                if (prop.getCollider().CollidesWithProp(player.getCollider())) {
                    prop.onPlayerInside(player, deltaTime);
                    if (player.getHealth() <= 0) {
                        handlePlayerLost(room, player);
                    }
                }
            }
        }
    }

    private void updateGasCloud(GameRoom room, ShrinkingGasCloudProp cloud, float deltaTime) {
        cloud.update(deltaTime);
        room.setGasCloudHeight(cloud.getCurrentSize());
        room.setGasCloudWidth(cloud.getCurrentSize());

        for (AbstractPlayer player : room.getPlayers()) {
            cloud.onPlayerInside(player, deltaTime);
            if (player.getHealth() <= 0) {
                handlePlayerLost(room, player);
            }
        }
    }

    private void handleBullets(GameRoom room, float deltaTime) {
        List<Bullet> bulletsToRemove = new CopyOnWriteArrayList<>();

        for (Bullet bullet : room.getBullets()) {
            bullet.move(deltaTime);

            if (bullet.onBulletCollision(room.getColliders()) || bullet.getTimeToLife() <= 0) {
                bulletsToRemove.add(bullet);
                continue;
            }

            if (handleBulletHitsPlayer(room, bullet) || handleBulletHitsZombie(room, bullet)) {
                bulletsToRemove.add(bullet);
            }
        }

        room.getBullets().removeAll(bulletsToRemove);
    }

    private boolean handleBulletHitsPlayer(GameRoom room, Bullet bullet) {
        for (AbstractPlayer player : room.getPlayers()) {
            if (bullet.onBulletHitPlayer(player)) {
                logger.info("Bullet {} hit player {}!", bullet.getOwnerId(), player.getName());
                if (player.getHealth() <= 0) {
                    handlePlayerLost(room, player);
                }
                return true;
            }
        }
        return false;
    }

    private boolean handleBulletHitsZombie(GameRoom room, Bullet bullet) {
        for (ZombieNPC zombie : room.getZombies()) {
            if (!zombie.isAlive()) continue;

            if (bullet.onBulletHitZombie(zombie)) {
                logger.info("Bullet {} hit zombie {}!", bullet.getId(), zombie.getId());
                if (!zombie.isAlive()) {
                    logger.info("Zombie {} died!", zombie.getId());
                }
                return true;
            }
        }
        return false;
    }

    private void handleAmmoReload(GameRoom room, float deltaTime) {
        room.setAmmoReloadTimer(room.getAmmoReloadTimer() + deltaTime);

        if (room.getAmmoReloadTimer() >= 1.0f) {
            for (AbstractPlayer player : room.getPlayers()) {
                player.getWeapon().reload();
            }
            room.setAmmoReloadTimer(0f);
        }
    }

    private void handleZombies(GameRoom room, float deltaTime) {
        for (ZombieNPC zombie : room.getZombies()) {
            zombie.updateAI(room, deltaTime);
        }

        room.getZombies().removeIf(z -> !z.isAlive() && z.getDeathTimer() <= 0);
    }

    public void stopGameLoop(GameRoom room) {
        ScheduledFuture<?> loop = roomLoops.remove(room.getRoomId());
        if (loop != null) loop.cancel(true);
    }


    private void handlePlayerLost(GameRoom room, AbstractPlayer player) {
        // Clone sessions before removing player
        List<WebSocketSession> currentSessions = List.copyOf(room.getSessions());

        Broadcaster.sendPlayerLost(List.of(player.getSession()), player.getSessionId(), 50, 10);
        Broadcaster.sendPlayerLost(currentSessions, player.getSessionId(), 50, 10);

        room.getPlayers().removeIf(p -> p.getSessionId().equals(player.getSessionId()));
        room.getSessions().remove(player.getSession());

        List<AbstractPlayer> alivePlayers = room.getPlayers();
        if (alivePlayers.size() == 1 && room.getState() == GameState.RUNNING) {
            AbstractPlayer winner = alivePlayers.get(0);
            room.setState(GameState.ENDED);
            Broadcaster.sendGameOver(currentSessions, winner.getSessionId(), 100, 25);
            stopGameLoop(room);
            if (roomListener != null) roomListener.onRoomDestroyed(room);
        }
    }
}
