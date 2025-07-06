package com.example.bs4bspringbackend.model.Weapon;

import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Revolver implements Weapon {
    private int ammo = 6;
    private final int maxAmmo = 6;

    @Override
    public void shoot(AbstractPlayer player, GameRoom room, Vector2 shootDirection) {
        if(ammo >0){
            ammo--;

            Bullet bullet = Bullet.spawn(
                    UUID.randomUUID().toString(),
                    player.getSessionId(),
                    player.getPosition(),
                    shootDirection,
                    player.getWeapon().getWeaponId()
            );

            room.getBullets().add(bullet);
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
        return "Revolver";
    }
}
