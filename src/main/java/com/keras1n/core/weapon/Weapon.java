package com.keras1n.core.weapon;

import com.keras1n.core.entity.MultiMaterialModel;
import org.joml.Vector3f;

public abstract class Weapon {
    protected int damage;
    protected float range;

    public abstract void attack();
    public int getDamage() { return damage; }
    public float getRange() { return range; }

    public void reload() {
        // TODO
    }
}
