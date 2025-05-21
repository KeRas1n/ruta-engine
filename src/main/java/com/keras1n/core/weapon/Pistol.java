package com.keras1n.core.weapon;

import com.keras1n.core.entity.MultiMaterialModel;
import org.joml.Vector3f;

public class Pistol extends Weapon{
    private int ammo;

    public Pistol(MultiMaterialModel model, String modelPath, Vector3f offset, float scale) {
        super(model, modelPath, offset, scale);
        this.damage = 20;
        this.ammo = 12;
    }

    @Override
    public void attack() {
        System.out.println("BANG! Ammo left: " + (--ammo));
    }
}
