package com.keras1n.core.weapon;

import com.keras1n.core.entity.MultiMaterialModel;
import org.joml.Vector3f;

public class Pistol extends Weapon{
    private int ammo;

    public Pistol(MultiMaterialModel model) {
        super(model, new Vector3f(0.3f, -0.3f, -0.7f), 0.5f);
        this.ammo = 12;
    }

    @Override
    public void attack() {
        System.out.println("BANG! Ammo left: " + (--ammo));
    }
}
