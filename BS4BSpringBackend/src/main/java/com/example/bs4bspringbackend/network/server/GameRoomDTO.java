package com.example.bs4bspringbackend.network.server;

import com.example.bs4bspringbackend.enums.GameState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRoomDTO {
    private final String type = "GameRoomDTO";
    private List<PlayerDTO> players;
    private List<BulletDTO> bullets;
    private List<BoxColliderDTO> boxColliders;
    private GameState gameState;
    private int countdownSeconds;
    private float gasCloudHeight;
    private float gasCloudWidth;
    private List<ZombieNPCDTO> zombies;
}
