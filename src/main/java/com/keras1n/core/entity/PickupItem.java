package com.keras1n.core.entity;

import org.joml.Vector3f;

public class PickupItem extends Entity{
    protected float x, y, z;
    protected String pickUpMessage;

    public PickupItem(MultiMaterialModel model, String modelPath, Vector3f position, Vector3f rotation, float scale) {
        super(model, modelPath, position, rotation, scale, false);
        this.setHasCollision(false);
    }

    public boolean onPickup(Player player) {
        return false;
    }

    public String getPickUpMessage() {
        return pickUpMessage;
    }
}
