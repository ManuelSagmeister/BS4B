package com.example.bs4bspringbackend.model.Physics;

public interface Collider {

    void setPosition(Vector2 position);

    Vector2 getPosition();

    boolean CollidesWith(Collider other);

    boolean CollidesWithProp(Collider other);

    void SetWidth(float width);

    void SetHeight(float height);
}
