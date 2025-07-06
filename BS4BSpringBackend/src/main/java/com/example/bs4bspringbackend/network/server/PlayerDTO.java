package com.example.bs4bspringbackend.network.server;

import com.example.bs4bspringbackend.model.Physics.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDTO {
    private String sessionId;
    private String name;
    private Vector2 position;
    private int rotationAngle;
    private int health;
    private String selectedCharacterId;
    private String selectedWeaponId;
    private int ammo;
    private int maxAmmo;
}
