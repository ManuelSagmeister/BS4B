package com.example.bs4bspringbackend.model.Weapon;

import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DoubleActionRevolver implements Weapon {
    private int ammo = 4;
    private final int maxAmmo = 4;

    @Override
    public void shoot(AbstractPlayer player, GameRoom room, Vector2 shootDirection) {
        if (ammo > 0) {
            ammo--;

            Vector2 forwardOffset = shootDirection.Normalize().Scale(-0.9f); // small offset backwards
            Vector2 spawnPos = player.getPosition();

            for (int i = 0; i < 2; i++) {
                Vector2 bulletPosition = spawnPos.Add(forwardOffset.Scale(i)); // first = normal, second = offset

                Bullet bullet = Bullet.spawn(
                        UUID.randomUUID().toString(),
                        player.getSessionId(),
                        bulletPosition,
                        shootDirection.Normalize(),
                        player.getWeapon().getWeaponId()
                );

                room.getBullets().add(bullet);
            }

        }
    }

    @Override
    public void reload() {
        if (ammo < maxAmmo) {
            ammo++;
        }
    }

    @Override
    public String getWeaponId() {
        return "DoubleActionRevolver";
    }
}
