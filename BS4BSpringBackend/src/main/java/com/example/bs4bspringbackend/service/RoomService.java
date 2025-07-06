package com.example.bs4bspringbackend.service;

import com.example.bs4bspringbackend.enums.GameState;
import com.example.bs4bspringbackend.enums.SpawnPosition;
import com.example.bs4bspringbackend.model.Physics.BoxCollider;
import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import com.example.bs4bspringbackend.model.Player.PlayerFactory;
import com.example.bs4bspringbackend.model.Props.Prop;
import com.example.bs4bspringbackend.interfaces.RoomListener;
import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.model.Props.ShrinkingGasCloudProp;
import com.example.bs4bspringbackend.model.Weapon.Bullet;
import com.example.bs4bspringbackend.model.ZombieNPC;
import com.example.bs4bspringbackend.util.GltfColliderLoader;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService {
    private static final Logger logger = LogManager.getLogger(RoomService.class);
    private final Map<WebSocketSession, String> sessionToRoomId = new ConcurrentHashMap<>();
    private final Map<String, GameRoom> roomMap = new ConcurrentHashMap<>();

    @Setter
    private RoomListener roomListener;

    public void createGameRoom(List<WebSocketSession> sessions, String level, List<String> characterIds, List<String> names, List<String> weapon) {
        logger.info("Creating new game room for sessions: {}", sessions);
        List<AbstractPlayer> players = new ArrayList<>();
        List<Bullet> bullets = new ArrayList<>();
        String roomId = UUID.randomUUID().toString();

        int i = 0;
        SpawnPosition[] spawnPositions = SpawnPosition.values();
        for (WebSocketSession session : sessions) {
            Vector2 spawnPosition = spawnPositions[i % spawnPositions.length].getPosition();
            String characterId = characterIds.get(i);
            String name = names.get(i);
            AbstractPlayer player = PlayerFactory.createPlayer(characterId, UUID.randomUUID().toString(), name, session, spawnPosition, weapon.get(i));
            players.add(player);
            i++;
        }


        // load map colliders
        List<Collider> colliders = GltfColliderLoader.loadColliders("levels/" + level + ".json");
        logger.info("Collider found: {}", colliders.size());

        // Load static props if level == GoldMine
        List<Prop> props = new ArrayList<>();
        if(level.equals("GoldMine")){
            props = GltfColliderLoader.loadProps("levels/" + level + ".json");
            logger.info("Props found: {}", props.size());
        }

        List<ZombieNPC> zombies = new ArrayList<>();

        if(level.equals("GhostTown")){
            try {
                props.add(new ShrinkingGasCloudProp());

                zombies.add(new ZombieNPC(new Vector2(0,0), new BoxCollider("ZombieNPC",new Vector2(0,0),0.5f,0.5f)));
            } catch (Exception e) {
                logger.error("Failed to add ShrinkingGasCloudProp", e);
            }
        }

        GameRoom room = new GameRoom(roomId, GameState.COUNTDOWN, players,zombies, bullets, sessions,colliders, 3, props,0,0,0);

        for(AbstractPlayer player : players) {
            player.setRoom(room);
        }

        roomMap.put(roomId, room);
        for (WebSocketSession session : sessions) {
            sessionToRoomId.put(session, roomId);
        }

        if (roomListener != null) {
            roomListener.onRoomCreated(room);
        }

    }

    public void removePlayerFromRoom(WebSocketSession session) {
        String roomId = sessionToRoomId.remove(session);
        if (roomId == null) return;
        GameRoom room = roomMap.get(roomId);
        if (room == null) return;

        room.getPlayers().removeIf(player -> player.getSessionId().equals(session));
        room.getSessions().remove(session);

        if (room.getSessions().isEmpty()) {
            destroyGameRoom(roomId);
        }
    }

    public void destroyGameRoom(String roomId) {
        GameRoom room = roomMap.remove(roomId);
        if (room == null) return;

        for (WebSocketSession session : room.getSessions()) {
            sessionToRoomId.remove(session);
        }

        logger.info("Destroyed game room: {}", roomId);
    }
}