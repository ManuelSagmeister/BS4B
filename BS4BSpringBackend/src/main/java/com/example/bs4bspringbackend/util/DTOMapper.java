package com.example.bs4bspringbackend.util;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import com.example.bs4bspringbackend.model.Weapon.Bullet;
import com.example.bs4bspringbackend.model.ZombieNPC;
import com.example.bs4bspringbackend.network.server.*;
import com.example.bs4bspringbackend.model.Physics.BoxCollider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DTOMapper {

    // Private constructor to prevent instantiation
    private DTOMapper() {
        throw new UnsupportedOperationException("DTOMapper is a utility class and cannot be instantiated.");
    }

    public static PlayerDTO mapPlayerToDTO(AbstractPlayer player){
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setSessionId(player.getSessionId());
        playerDTO.setName(player.getName());
        playerDTO.setPosition(player.getPosition());
        playerDTO.setHealth(player.getHealth());
        playerDTO.setRotationAngle(player.getRotationAngle());
        playerDTO.setSelectedCharacterId(player.getSelectedCharacterId());
        playerDTO.setAmmo(player.getWeapon().getAmmo());
        playerDTO.setMaxAmmo(player.getWeapon().getMaxAmmo());
        playerDTO.setSelectedWeaponId(player.getWeapon().getWeaponId());
        return playerDTO;
    }

    public static ZombieNPCDTO mapZombieToDTO(ZombieNPC zombie) {
        return new ZombieNPCDTO(
                zombie.getId(),
                zombie.getPosition(),
                zombie.getRotationAngle(),
                zombie.getHealth(),
                zombie.getState()
        );
    }

    public static BulletDTO mapBulletToDTO(Bullet bullet) {
        BulletDTO dto = new BulletDTO();
        dto.setId(bullet.getId());
        dto.setPosition(bullet.getPosition());
        dto.setVelocity(bullet.getVelocity());
        dto.setWeaponId(bullet.getWeaponId());
        return dto;
    }

    public static BoxColliderDTO mapBoxColliderToDTO(BoxCollider box) {
        return new BoxColliderDTO(
                box.getId(),
                box.getPosition().getX(),
                box.getPosition().getY(),
                box.getWidth(),
                box.getHeight()
        );
    }

    public static GameRoomDTO mapGameRoomToDTO(GameRoom gameRoom) {
        List<PlayerDTO> playerDTOList = new ArrayList<>();
        for (AbstractPlayer player : gameRoom.getPlayers()) {
            playerDTOList.add(mapPlayerToDTO(player));
        }

        List<BulletDTO> bulletDTOList = new ArrayList<>();
        for (Bullet bullet : gameRoom.getBullets()) {
            bulletDTOList.add(mapBulletToDTO(bullet));
        }

        List<BoxColliderDTO> boxColliderDTOList = new ArrayList<>();
        for (Collider collider : gameRoom.getColliders()) {
            if (collider instanceof BoxCollider box) {
                boxColliderDTOList.add(mapBoxColliderToDTO(box));
            }
        }

        List<ZombieNPCDTO> zombieDTOList = new ArrayList<>();
        for (ZombieNPC zombie : gameRoom.getZombies()) {
            zombieDTOList.add(mapZombieToDTO(zombie));
        }

        GameRoomDTO dto = new GameRoomDTO();
        dto.setPlayers(playerDTOList);
        dto.setBullets(bulletDTOList);
        dto.setBoxColliders(boxColliderDTOList);
        dto.setCountdownSeconds(gameRoom.getCountdownSeconds());
        dto.setGameState(gameRoom.getState());
        dto.setGasCloudWidth(gameRoom.getGasCloudWidth());
        dto.setGasCloudHeight(gameRoom.getGasCloudHeight());
        dto.setZombies(zombieDTOList);

        return dto;
    }



}
