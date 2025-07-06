package com.example.bs4bspringbackend.network.server;

import com.example.bs4bspringbackend.model.Physics.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ZombieNPCDTO {
    private String id;
    private Vector2 position;
    private int rotationAngle;
    private int health;
    private String state; // "Walking", "Attacking", "Dead"
}
