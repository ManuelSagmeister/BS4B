package com.example.bs4bspringbackend.model;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ZombieNPC {
    private String id;
    private Vector2 position;
    private int rotationAngle;
    private int health;
    private String state;
    private static final String WALKING = "Walking";
    private Collider collider;

    private float speed = 1.0f;
    private float attackCooldown = 1.0f;
    private float attackTimer = 0.0f;

    private boolean isAlive = true;
    private float deathTimer = 0.0f;
    private float standUpTimer = 1.5f;

    public ZombieNPC(Vector2 spawnPosition, Collider collider) {
        this.id = UUID.randomUUID().toString();
        this.position = spawnPosition;
        this.rotationAngle = 0;
        this.health = 200;
        this.state = "StandUp";
        this.collider = collider;
    }

    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            this.state = "Death";
            this.isAlive = false;
            this.deathTimer = 2.5f;
        }
    }


    public void updateAI(GameRoom room, float deltaTime) {
        if (!isAlive) {
            deathTimer -= deltaTime;
            return;
        }

        // StandUp phase
        if (state.equals("StandUp")) {
            standUpTimer -= deltaTime;
            if (standUpTimer <= 0) {
                state = WALKING;
            }
            return;
        }

        AbstractPlayer nearestPlayer = findNearestPlayer(room.getPlayers());

        if (nearestPlayer == null) {
            this.state = WALKING;
            return;
        }

        float distance = this.position.Distance(nearestPlayer.getPosition());

        if (distance > 1.5f) {

            this.state = WALKING;
            Vector2 direction = nearestPlayer.getPosition().Subtract(this.position).Normalize();
            Vector2 movement = direction.Scale(speed * deltaTime);
            this.position = this.position.Add(movement);


            this.rotationAngle = (int) direction.angleDegrees();
            this.collider.setPosition(this.position);

            // Reset attack timer if moving
            attackTimer = 0f;
        } else {

            this.state = "Attack";
            attackTimer += deltaTime;

            if (attackTimer >= attackCooldown) {

                nearestPlayer.takeDamage(10);
                attackTimer = 0f;


                if (nearestPlayer.getHealth() <= 0) {
                    this.state = WALKING;
                }
            }
        }
    }

    private AbstractPlayer findNearestPlayer(List<AbstractPlayer> players) {
        AbstractPlayer nearest = null;
        float nearestDistance = Float.MAX_VALUE;

        for (AbstractPlayer player : players) {
            float distance = this.position.Distance(player.getPosition());
            if (distance < nearestDistance && player.getHealth() > 0) {
                nearestDistance = distance;
                nearest = player;
            }
        }

        return nearest;
    }
}
