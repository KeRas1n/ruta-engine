package com.keras1n.core.entity;

import org.joml.Vector3f;

public class AmmoPack extends PickupItem {
    private int ammoAmount;

    public AmmoPack(MultiMaterialModel model, Vector3f position, int ammoAmount) {
        super(model, position, new Vector3f(), 0.5f);
        this.ammoAmount = ammoAmount;

        this.pickUpMessage = "+ " + ammoAmount + " Ammo";
    }

    @Override
    public boolean onPickup(Player player) {
        // TODO

        return true;
    }
}