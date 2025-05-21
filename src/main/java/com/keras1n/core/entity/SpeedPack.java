package com.keras1n.core.entity;

import org.joml.Vector3f;

public class SpeedPack extends PickupItem {
    private float speed;
    private float rotationSpeed = 100f;

    public SpeedPack(MultiMaterialModel model, Vector3f position, Vector3f rotation, float scale, float speed) {
        super(model, position, rotation, scale);
        this.pickUpMessage = "Speed boosted!";
        this.speed = speed;
    }

    @Override
    public boolean onPickup(Player player) {

        if(!player.isHasSpeedBoost()){
            player.applySpeedBoost(speed, 15000);
            return true;
        }
        return false;
    }

    public void update(float deltaTime) {
        this.getRotation().y += rotationSpeed * deltaTime;


        if (this.getRotation().y >= 360f) {
            this.getRotation().y -= 360f;
        }
    }
}
