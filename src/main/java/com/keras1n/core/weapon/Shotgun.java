package com.keras1n.core.weapon;

import com.keras1n.core.entity.MultiMaterialModel;
import org.joml.Vector3f;

public class Shotgun extends Weapon {
    private int ammo;

    public Shotgun(MultiMaterialModel model) {
        super(model, null, new Vector3f(0.3f, -0.3f, -0.7f), 0.5f);
        this.ammo = 12;
    }

    @Override
    public void attack() {
        System.out.println("BANG from shotgun! Ammo left: " + (--ammo));
    }
}
