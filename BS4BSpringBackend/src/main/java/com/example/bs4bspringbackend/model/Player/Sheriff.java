package com.example.bs4bspringbackend.model.Player;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Weapon.Weapon;
import org.springframework.web.socket.WebSocketSession;

public class Sheriff extends AbstractPlayer{

    private static final int SHERIFF_HEALTH = 100;
    private static final int SHERIFF_SPEED = 5;
    private static final String SHERIFF_FRONTEND_ID = "1";

    public Sheriff(String id, WebSocketSession session,String name, Vector2 position, int rotationAngle, Collider collider, Weapon weapon) {
        super(id, session, name ,position, rotationAngle, collider, weapon, SHERIFF_HEALTH, SHERIFF_HEALTH, SHERIFF_SPEED, SHERIFF_FRONTEND_ID);
    }
}
