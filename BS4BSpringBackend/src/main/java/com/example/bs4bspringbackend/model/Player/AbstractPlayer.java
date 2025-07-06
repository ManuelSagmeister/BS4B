package com.example.bs4bspringbackend.model.Player;

import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.model.Physics.BoxCollider;
import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Physics.CollisionResult;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Weapon.Weapon;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public abstract class AbstractPlayer {
    protected String id;
    @Getter
    protected WebSocketSession session;
    @Getter
    protected String name;
    @Getter
    protected Vector2 position;
    @Getter
    @Setter
    protected int rotationAngle;
    @Getter
    protected Collider collider;
    @Getter
    protected int health;
    @Getter
    protected Weapon weapon;
    @Getter
    @Setter
    protected GameRoom room;
    @Setter
    @Getter
    private float shootFacingAngle = 0f;
    @Setter
    @Getter
    private long lastShotTime = 0;
    @Setter
    @Getter
    private boolean forceShootRotation = false;

    protected int maxHealth;
    protected int speed;
    protected boolean isAlive = true;

    @Getter
    protected String selectedCharacterId;

    protected AbstractPlayer(String id, WebSocketSession session, String name, Vector2 position, int rotationAngle, Collider collider, Weapon weapon, int health,int maxHealth,int speed, String selectedCharacterId) {
        this.id = id;
        this.session = session;
        this.name = name;
        this.position = position;
        this.rotationAngle = rotationAngle;
        this.collider = collider;
        this.weapon = weapon;
        this.health = health;
        this.maxHealth = maxHealth;
        this.speed = speed;
        this.selectedCharacterId = selectedCharacterId;
    }

    public void Move(Vector2 direction, float deltaTime, List<Collider> obstacles) {
        if (!isAlive || direction.isZero()) return;

        Vector2 normalizedDirection = direction.Normalize();
        Vector2 movement = normalizedDirection.Scale(speed * deltaTime);
        Vector2 currentPosition = this.position;
        int maxSlides = 2;

        for (int slide = 0; slide < maxSlides; slide++) {
            Vector2 targetPosition = currentPosition.Add(movement);
            CollisionResult collision = findCollision(targetPosition, obstacles);

            if (collision == null) {
                // Move freely
                this.position = targetPosition;
                collider.setPosition(targetPosition);
                break;
            } else {
                // Slide: project movement onto the collision plane
                movement = movement.projectOnPlane(collision.collisionNormal);
                if (movement.isZero()) break;
                collider.setPosition(currentPosition); // Stay at current position and slide
            }
        }

        // Set rotation based on direction
        this.rotationAngle = (int) direction.angleDegrees();
    }

    private CollisionResult findCollision(Vector2 targetPosition, List<Collider> obstacles) {
        collider.setPosition(targetPosition);

        for (Collider obstacle : obstacles) {
            if (collider != obstacle && collider.CollidesWith(obstacle)) {
                Vector2 collisionNormal = new Vector2(0, 0); // Default normal
                if (collider instanceof BoxCollider && obstacle instanceof BoxCollider) {
                    collisionNormal = BoxCollider.calculateCollisionNormal(
                            (BoxCollider) collider,
                            (BoxCollider) obstacle
                    );
                }
                return new CollisionResult(obstacle, collisionNormal);
            }
        }
        return null; // No collision
    }

    public void shoot(GameRoom room, Vector2 shootDirection){
        if (!isAlive) return;
        weapon.shoot(this,room, shootDirection);
    }

    public void takeDamage(int amount) {

        health -= amount;
        if (health <= 0) {
            health = 0;
            isAlive = false;
        }
    }

    public void takeHeal(int amount) {
        if (!isAlive) return;
        health = Math.min(health + amount, maxHealth);
    }
    // GETTERS

    public String getSessionId(){
        return this.session.getId();
    }


}
