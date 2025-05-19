package com.keras1n.core.weapon;

import com.keras1n.core.ObjectLoader;
import com.keras1n.core.entity.MultiMaterialModel;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class WeaponFactory {

    private static final Map<String, MultiMaterialModel> cache = new HashMap<>();
    private static final ObjectLoader loader = new ObjectLoader();

    public static Weapon createWeapon(String name) throws Exception {
        String modelPath;
        switch (name) {
            case "pistol":
                modelPath = "models/hands_pistol.obj";
                break;
            // case "shotgun":
            //     modelPath = "models/weapons/hands_shotgun.obj";
            //     break;
            default:
                throw new IllegalArgumentException("Unknown weapon: " + name);
        }

        MultiMaterialModel model = cache.computeIfAbsent(modelPath, p -> {
            try {
                return loader.loadOBJModel(p);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        //position will be same as player camera i think :)
        //return new Weapon(model, new Vector3f(0f, -0.4f,0.5f), 0.04f);

        switch (name) {
            case "pistol":
                return new Pistol(model, new Vector3f(0f, -0.4f,0.5f), 0.04f); // <-- ВАЖНО
            // case "shotgun":
            //     return new Shotgun(model);
            default:
                throw new IllegalArgumentException("Unknown weapon: " + name);
        }
    }
}
