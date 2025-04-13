package com.keras1n.core.entity;

public abstract class PickupItem {
    protected float x, y, z;

    public abstract void onPickup(Player player);
}
