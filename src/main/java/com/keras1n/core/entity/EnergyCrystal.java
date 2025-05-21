package com.keras1n.core.entity;

import org.joml.Vector3f;

public class EnergyCrystal extends PickupItem {

    public EnergyCrystal(MultiMaterialModel model, String modelPath, Vector3f position, Vector3f rotation, float scale) {
        super(model, modelPath, position, rotation, scale);
        this.pickUpMessage = "You picked up energy crystal!";
    }

    @Override
    public boolean onPickup(Player player) {
        player.setPlayerHasEnergyCrystal(true);
        return true;
    }

    @Override
    public String getType() {
        return "Crystal";
    }
}
