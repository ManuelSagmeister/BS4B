package com.example.bs4bspringbackend.model.Player;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Weapon.Weapon;
import org.springframework.web.socket.WebSocketSession;

public class Gunslinger extends AbstractPlayer {

    private static final int GUNSLINGER_HEALTH = 80;
    private static final int GUNSLINGER_SPEED = 7;
    private static final String GUNSLINGER_FRONTEND_ID = "4";

    public Gunslinger(String id, WebSocketSession session, String name, Vector2 position, int rotationAngle, Collider collider, Weapon weapon) {
        super(id, session, name, position, rotationAngle, collider, weapon, GUNSLINGER_HEALTH, GUNSLINGER_HEALTH, GUNSLINGER_SPEED, GUNSLINGER_FRONTEND_ID);
    }

}
