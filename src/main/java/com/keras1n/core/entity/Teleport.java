package com.keras1n.core.entity;

import org.joml.Vector3f;

public class Teleport extends PickupItem {
    private float rotationSpeed = 100f;

    private boolean activated = false;

    private boolean messageShown = false;

    public Teleport(MultiMaterialModel model, String modelPath, Vector3f position, Vector3f rotation, float scale) {
        super(model, modelPath, position, rotation, scale);
        this.pickUpMessage = "Find a diamond to activate the teleport!";
    }

    @Override
    public boolean onPickup(Player player) {
        return player.isPlayerHasEnergyCrystal();
    }

    public void update(float deltaTime) {
        this.getRotation().y += rotationSpeed * deltaTime;
        if (this.getRotation().y >= 360f) {
            this.getRotation().y -= 360f;
        }
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
        if (activated) {
            this.pickUpMessage = "Teleport activated! Step on it to speed boost.";
        } else {
            this.pickUpMessage = "Find a diamond to activate the teleport";
        }
    }

    public boolean isMessageShown() {
        return messageShown;
    }

    public void setMessageShown(boolean messageShown) {
        this.messageShown = messageShown;
    }

    @Override
    public String getType() {
        return "Teleport";
    }
}
