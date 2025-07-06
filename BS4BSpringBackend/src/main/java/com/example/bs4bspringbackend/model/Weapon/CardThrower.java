package com.example.bs4bspringbackend.model.Weapon;

import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CardThrower implements Weapon {
    private int ammo = 2;
    private final int maxAmmo = 2;

    @Override
    public void shoot(AbstractPlayer player, GameRoom room, Vector2 shootDirection) {
        if (ammo > 0) {
            ammo--;

            Bullet card = Bullet.spawnExploding(
                    UUID.randomUUID().toString(),
                    player.getSessionId(),
                    player.getPosition(),
                    shootDirection,
                    2.0f, // splash radius
                    10    ,// damage
                    player.getWeapon().getWeaponId()
            );

            room.getBullets().add(card);
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
        return "CardThrower";
    }
}
