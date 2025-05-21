package com.keras1n.core.entity;

import org.joml.Vector3f;

public class EnergyCrystal extends PickupItem {

    public EnergyCrystal(MultiMaterialModel model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
        this.pickUpMessage = "You picked up energy crystal!";
    }

    @Override
    public boolean onPickup(Player player) {
        player.setPlayerHasEnergyCrystal(true);
        return true;
    }
}
