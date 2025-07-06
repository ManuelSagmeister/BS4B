package com.example.bs4bspringbackend.model.Physics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vector2 {
    private float x;
    private float y;

    public Vector2 Add(Vector2 vector2){
        return new Vector2(this.x + vector2.x, this.y + vector2.y);
    }

    // Subtraction - returns a new vector, does NOT modify this
    public Vector2 Subtract(Vector2 vector2){
        return new Vector2(this.x - vector2.x, this.y - vector2.y);
    }

    // Scale by a factor
    public Vector2 Scale(float factor){
        return new Vector2(this.x * factor, this.y * factor);
    }

    // Distance between this vector and another
    public float Distance(Vector2 vector2){
        float dx = this.x - vector2.x;
        float dy = this.y - vector2.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    // Squared distance (quarr)
    public float DistanceSquared(Vector2 vector2){
        float dx = this.x - vector2.x;
        float dy = this.y - vector2.y;
        return dx * dx + dy * dy;
    }

    // Dot product with another vector
    public float Dot(Vector2 vector2){
        return this.x * vector2.x + this.y * vector2.y;
    }

    // Magnitude (length) of this vector
    public float Magnitude(){
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    // Normalize - returns a new unit vector in same direction
    public Vector2 Normalize(){
        float mag = Magnitude();
        if(mag == 0) return new Vector2(0,0); 
        return new Vector2(this.x / mag, this.y / mag);
    }


    public boolean isZero() {
        return this.x == 0 && this.y == 0;
    }

    public float angleRadians() {
        return (float) Math.atan2(this.y, this.x);
    }

    public float angleDegrees() {
        float angle = (float) Math.toDegrees(Math.atan2(this.y, this.x));
        if (angle < 0) angle += 360;
        return angle;
    }

    public Vector2 projectOnPlane(Vector2 normal) {
        // normal must be normalized
        float dot = this.Dot(normal);
        return this.Subtract(normal.Scale(dot));
    }

}
