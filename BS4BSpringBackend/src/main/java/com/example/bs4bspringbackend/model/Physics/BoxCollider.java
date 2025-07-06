package com.example.bs4bspringbackend.model.Physics;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoxCollider implements Collider {
    private String id;
    private Vector2 positionReference;
    private float width;
    private float height;

    public BoxCollider(String id, Vector2 positionReference, float width, float height) {
        this.id = id;
        this.positionReference = positionReference;
        this.width = width;
        this.height = height;
    }

    @Override
    public void setPosition(Vector2 position) {
        this.positionReference = new Vector2(position.getX(), position.getY());
    }

    @Override
    public Vector2 getPosition() {
        return this.positionReference;
    }

    // BLENDER obj pivot middle
    @Override
    public boolean CollidesWith(Collider collider) {
        if (!(collider instanceof BoxCollider other)) return false;

        float thisCenterX = this.positionReference.getX();
        float thisCenterY = this.positionReference.getY();
        float otherCenterX = other.positionReference.getX();
        float otherCenterY = other.positionReference.getY();

        float halfWidthThis = this.width;
        float halfHeightThis = this.height;
        float halfWidthOther = other.width;
        float halfHeightOther = other.height;

        return Math.abs(thisCenterX - otherCenterX) < (halfWidthThis + halfWidthOther) &&
                Math.abs(thisCenterY - otherCenterY) < (halfHeightThis + halfHeightOther);
    }

    @Override
    public boolean CollidesWithProp(Collider collider) {
        if (!(collider instanceof BoxCollider other)) return false;

        float thisCenterX = this.positionReference.getX();
        float thisCenterY = this.positionReference.getY();
        float otherCenterX = other.positionReference.getX();
        float otherCenterY = other.positionReference.getY();

        float halfWidthThis = this.width / 2f;
        float halfHeightThis = this.height/ 2f;
        float halfWidthOther = other.width/ 2f;
        float halfHeightOther = other.height/ 2f;

        return Math.abs(thisCenterX - otherCenterX) < (halfWidthThis + halfWidthOther) &&
                Math.abs(thisCenterY - otherCenterY) < (halfHeightThis + halfHeightOther);
    }

    @Override
    public void SetWidth(float width) {
        this.width = width;
    }

    @Override
    public void SetHeight(float height) {
        this.height = height;
    }

    public static Vector2 calculateCollisionNormal(BoxCollider self, BoxCollider other) {
        float dx = self.getPosition().getX() - other.getPosition().getX();
        float px = (self.getWidth() + other.getWidth()) - Math.abs(dx);

        float dy = self.getPosition().getY() - other.getPosition().getY();
        float py = (self.getHeight() + other.getHeight()) - Math.abs(dy);

        if (px < py) {
            return new Vector2(Math.signum(dx), 0); // Horizontal collision
        } else {
            return new Vector2(0, Math.signum(dy)); // Vertical collision
        }
    }



}
