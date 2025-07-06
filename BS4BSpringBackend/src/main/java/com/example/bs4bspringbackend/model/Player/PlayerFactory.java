package com.example.bs4bspringbackend.model.Player;

import com.example.bs4bspringbackend.model.Physics.BoxCollider;
import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Weapon.Weapon;
import com.example.bs4bspringbackend.model.Weapon.WeaponFactory;
import org.springframework.web.socket.WebSocketSession;

public class PlayerFactory {

    // Private constructor to prevent instantiation
    private PlayerFactory() {
        throw new UnsupportedOperationException("PlayerFactory is a utility class and cannot be instantiated.");
    }

    private static final float COLLIDER_WIDTH = 0.5f;
    private static final float COLLIDER_HEIGHT = 0.5f;

    public static AbstractPlayer createPlayer(String characterId, String playerId, String name, WebSocketSession session, Vector2 position, String weaponName) {
        Collider collider = new BoxCollider(playerId, position, COLLIDER_WIDTH, COLLIDER_HEIGHT);
        int rotationAngle = 0;

        Weapon weapon = WeaponFactory.createWeapon(weaponName);

        return switch (characterId) {
            case "1" -> new Sheriff(playerId, session,name, position, rotationAngle, collider, weapon);
            case "2" -> new Bandit(playerId, session,name, position, rotationAngle, collider, weapon);
            case "3" -> new Outlaw(playerId, session,name, position, rotationAngle, collider, weapon);
            case "4" -> new Gunslinger(playerId, session, name, position, rotationAngle, collider, weapon);
            default -> throw new IllegalArgumentException("Unknown character ID: " + characterId);
        };
    }


}
