package com.keras1n.core.entity;

import com.keras1n.core.utils.Constants;
import org.joml.Vector3f;

public class EntityFactory {
    public static Entity create(String type,
                                boolean hasCollision,
                                MultiMaterialModel model,
                                Vector3f pos,
                                Vector3f rot,
                                float scale,
                                float health,
                                float damage,
                                int healAmount,
                                String modelPath
    ) {
        switch (type) {
            case "Object": return new Entity(model, modelPath, pos, rot, scale, hasCollision); // just Mesh
            case "Enemy": return new Enemy(model, modelPath, pos, rot, scale, health, damage);
            case "HealthPack": return new HealthPack(model, modelPath, pos, rot, scale, healAmount);
            case "SpeedPack": return new SpeedPack(model, modelPath, pos, rot, scale, Constants.CAMERA_MOVE_SPEED_SPRINT);
            case "Teleport": return new Teleport(model, modelPath, pos, rot, scale);
            case "Crystal": return new EnergyCrystal(model, modelPath, pos, rot, scale);
            default: throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}

