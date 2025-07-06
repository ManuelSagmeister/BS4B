package com.example.bs4bspringbackend.model.Props;

import com.example.bs4bspringbackend.model.Physics.BoxCollider;
import com.example.bs4bspringbackend.model.Physics.Vector2;
import com.example.bs4bspringbackend.model.Player.AbstractPlayer;
import lombok.Getter;

@Getter
public class ShrinkingGasCloudProp extends Prop {
    public static final float INITIAL_SIZE = 50f;
    public static final float SHRINK_RATE = 0.5f;
    public static final float MIN_SIZE = 2f;
    public static final float BASE_DAMAGE = 20f;

    private final Vector2 center = new Vector2(0, 0);
    private float currentSize = INITIAL_SIZE;

    public ShrinkingGasCloudProp() {
        this.collider = new BoxCollider("ShrinkingGasCloud", center, INITIAL_SIZE, INITIAL_SIZE);
    }

    public void update(float deltaTime) {
        currentSize = Math.max(MIN_SIZE, currentSize - SHRINK_RATE * deltaTime);
        collider.SetWidth(currentSize);
        collider.SetHeight(currentSize);
        collider.setPosition(center);
    }

    @Override
    public void onPlayerInside(AbstractPlayer player, float deltaTime) {
        if (!this.collider.CollidesWithProp(player.getCollider())) {
            float damage = BASE_DAMAGE * deltaTime;
            player.takeDamage((int) damage);
        }
    }

}

