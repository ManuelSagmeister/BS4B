package com.example.bs4bspringbackend.model.Weapon;

import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Shotgun implements Weapon{
    private int ammo = 3;
    private final int maxAmmo = 3;

    @Override
    public void shoot(AbstractPlayer player, GameRoom room, Vector2 shootDirection) {
        if(ammo >0){
            ammo--;

            int bulletShootCount = 4;
            float spreadFactor = 0.2f; // tweak this for wider/narrower spread

            for (int i = 0; i < bulletShootCount; i++) {
                // Create some variation in direction
                float offsetX = (i - bulletShootCount / 2f) * spreadFactor;
                Vector2 spreadDirection = shootDirection.Add(new Vector2(offsetX, 0)).Normalize();

                Bullet bullet = Bullet.spawn(
                        UUID.randomUUID().toString(),
                        player.getSessionId(),
                        player.getPosition(),
                        spreadDirection,
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
        return "Shotgun";
    }
}
