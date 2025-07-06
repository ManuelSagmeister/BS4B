package com.example.bs4bspringbackend.model.Props;

import com.example.bs4bspringbackend.model.Physics.Collider;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;

public abstract class Prop {
    protected Collider collider;
    public Collider getCollider() { return collider; }
    public abstract void onPlayerInside(AbstractPlayer player, float deltaTime);
}
