package com.example.bs4bspringbackend.model.Props;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;

public class HealPropStatic extends Prop {
    private static final float HEAL_PER_SECOND = 20f; // Renamed to match constant naming convention

    public HealPropStatic(Collider collider) {
        this.collider = collider;
    }

    @Override
    public void onPlayerInside(AbstractPlayer player, float deltaTime) {
        player.takeHeal((int) (HEAL_PER_SECOND * deltaTime));
    }
}