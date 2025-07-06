package com.example.bs4bspringbackend.model.Props;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;

public class GasPropStatic extends Prop {
    private static final float DAMAGE_PER_SECOND = 20f; // Renamed to match constant naming convention

    public GasPropStatic(Collider collider) {
        this.collider = collider;
    }

    @Override
    public void onPlayerInside(AbstractPlayer player, float deltaTime) {
        player.takeDamage((int) (DAMAGE_PER_SECOND * deltaTime));
    }
}
