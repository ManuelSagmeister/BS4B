package com.example.bs4bspringbackend.network.server;

import com.example.bs4bspringbackend.model.Physics.Vector2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BulletDTO {
    private String id;
    private Vector2 position;
    private Vector2 velocity;
    private String weaponId;
}
