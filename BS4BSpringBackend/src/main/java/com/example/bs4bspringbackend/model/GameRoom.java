package com.example.bs4bspringbackend.model;

import java.util.List;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import com.example.bs4bspringbackend.model.Props.Prop;
import com.example.bs4bspringbackend.model.Weapon.Bullet;
import com.example.bs4bspringbackend.enums.GameState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
@AllArgsConstructor
public class GameRoom {
    private String roomId;
    private GameState state;
    private List<AbstractPlayer> players;
    private List<ZombieNPC> zombies;
    private List<Bullet> bullets;
    private List<WebSocketSession> sessions;
    private List<Collider> colliders;
    private int countdownSeconds;
    private List<Prop> props;
    private float ammoReloadTimer = 0f;
    private float gasCloudWidth = 0f;
    private float gasCloudHeight = 0f;
}
