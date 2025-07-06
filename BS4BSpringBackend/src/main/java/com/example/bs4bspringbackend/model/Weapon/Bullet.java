package com.example.bs4bspringbackend.model.Weapon;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Physics.BoxCollider;
import com.example.bs4bspringbackend.model.Physics.Vector2;

import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import com.example.bs4bspringbackend.model.ZombieNPC;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Bullet {
    private String id;
    private String ownerId;
    private Vector2 position;
    private Vector2 velocity;
    private Collider collider;
    private float timeToLife;
    public String weaponId;

    private static final float BULLET_SPEED = 20f;
    private static final float BULLET_LIFETIME = 2f;
    private static int BULLET_DAMAGE = 10;

    // 🔥 Exploding bullet fields
    private boolean isExploding = false;
    private float explosionRadius = 0f;
    private int explosionDamage = 0;

    public static Bullet spawn(String id, String ownerId, Vector2 spawnPosition, Vector2 direction, String weaponId) {
        Bullet bullet = new Bullet();
        bullet.setId(id);
        bullet.setOwnerId(ownerId);
        bullet.setPosition(spawnPosition);
        bullet.setVelocity(direction.Normalize().Scale(BULLET_SPEED));
        bullet.setCollider(new BoxCollider("Bullet", spawnPosition, 0.2f, 0.2f));
        bullet.setTimeToLife(BULLET_LIFETIME);
        bullet.setWeaponId(weaponId);
        return bullet;
    }

    public static Bullet spawnExploding(String id, String ownerId, Vector2 spawnPosition, Vector2 direction, float radius, int damage, String weaponId) {
        Bullet bullet = spawn(id, ownerId, spawnPosition, direction, weaponId);
        bullet.setExploding(true);
        bullet.setExplosionRadius(radius);
        bullet.setExplosionDamage(damage);
        return bullet;
    }

    public void move(float deltaTime) {
        Vector2 newPosition = this.position.Add(this.velocity.Scale(deltaTime));
        this.setPosition(newPosition);
        if (this.collider != null) {
            this.collider.setPosition(newPosition);
        }
        this.timeToLife -= deltaTime;
    }

    public boolean onBulletCollision(List<Collider> staticColliders) {
        for (Collider staticCollider : staticColliders) {
            if (this.collider.CollidesWith(staticCollider)) {
                return true;
            }
        }
        return false;
    }

    public boolean onBulletHitPlayer(AbstractPlayer player) {
        if (!this.ownerId.equals(player.getSessionId()) &&
                this.collider.CollidesWith(player.getCollider())) {

            if (isExploding) {
                applyExplosionDamage(player.getRoom().getPlayers(), this.position);
            } else {
                player.takeDamage(BULLET_DAMAGE);
            }

            return true;
        }
        return false;
    }

    public void applyExplosionDamage(List<AbstractPlayer> players, Vector2 explosionCenter) {
        for (AbstractPlayer target : players) {
            if (target.getSessionId().equals(ownerId)) continue;

            float distance = target.getPosition().Subtract(explosionCenter).Magnitude();
            if (distance <= explosionRadius) {
                target.takeDamage(explosionDamage);
            }
        }
    }

    public boolean onBulletHitZombie(ZombieNPC zombie) {
        if (this.collider.CollidesWith(zombie.getCollider())) {

            if (isExploding) {
                applyExplosionDamageToZombies(List.of(zombie), this.position);
            } else {
                zombie.takeDamage(BULLET_DAMAGE);
            }

            return true;
        }
        return false;
    }

    public void applyExplosionDamageToZombies(List<ZombieNPC> zombies, Vector2 explosionCenter) {
        for (ZombieNPC target : zombies) {
            float distance = target.getPosition().Subtract(explosionCenter).Magnitude();
            if (distance <= explosionRadius) {
                target.takeDamage(explosionDamage);
            }
        }
    }

}
