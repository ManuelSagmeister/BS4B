package com.example.bs4bspringbackend.model.Physics;

public class CollisionResult {
    public Collider hitCollider;
    public Vector2 collisionNormal;

    public CollisionResult(Collider hitCollider, Vector2 collisionNormal) {
        this.hitCollider = hitCollider;
        this.collisionNormal = collisionNormal;
    }
}
