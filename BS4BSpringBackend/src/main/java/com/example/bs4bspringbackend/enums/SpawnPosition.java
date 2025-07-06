package com.example.bs4bspringbackend.enums;

import com.example.bs4bspringbackend.model.Physics.Vector2;

public enum SpawnPosition {
    TOP_RIGHT(new Vector2(10, 10)),
    BOTTOM_RIGHT(new Vector2(10, -10)),
    TOP_LEFT(new Vector2(-10, 10)),
    BOTTOM_LEFT(new Vector2(-10, -10));

    private final Vector2 position;

    SpawnPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }
}
