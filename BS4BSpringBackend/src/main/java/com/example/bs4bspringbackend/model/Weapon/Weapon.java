package com.example.bs4bspringbackend.model.Weapon;

import com.example.bs4bspringbackend.model.GameRoom;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;

public interface Weapon {

    void shoot(AbstractPlayer player, GameRoom room, Vector2 shootDirection);

    int getAmmo();
    int getMaxAmmo();
    String getWeaponId();
    void reload();
}
