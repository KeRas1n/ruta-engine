package com.keras1n.core.entity;

import org.joml.Vector3f;

public class PickupItem extends Entity{
    protected float x, y, z;
    protected String pickUpMessage;

    public PickupItem(MultiMaterialModel model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
    }

    public boolean onPickup(Player player) {
        return false;
    }

    public String getPickUpMessage() {
        return pickUpMessage;
    }
}
