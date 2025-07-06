package com.example.bs4bspringbackend.model.Player;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Weapon.Weapon;
import org.springframework.web.socket.WebSocketSession;

public class Bandit extends AbstractPlayer {

    private static final int BANDIT_HEALTH = 50;
    private static final int BANDIT_SPEED = 6;
    private static final String BANDIT_FRONTEND_ID = "2";

    public Bandit(String id, WebSocketSession session, String name, Vector2 position, int rotationAngle, Collider collider, Weapon weapon) {
        super(id, session, name, position, rotationAngle, collider,weapon, BANDIT_HEALTH, BANDIT_HEALTH, BANDIT_SPEED, BANDIT_FRONTEND_ID);
    }

}
