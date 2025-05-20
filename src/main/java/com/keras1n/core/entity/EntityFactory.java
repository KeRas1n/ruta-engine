package com.keras1n.core.entity;

import org.joml.Vector3f;

public class EntityFactory {
    public static Entity create(String type,
                                MultiMaterialModel model,
                                Vector3f pos,
                                Vector3f rot,
                                float scale,
                                float health,
                                float damage,
                                int healAmount
    ) {
        switch (type) {
            case "Object": return new Entity(model, pos, rot, scale); // just Mesh
            case "Enemy": return new Enemy(model, pos, rot, scale, health, damage);
            case "HealthPack": return new HealthPack(model, pos, rot, scale, healAmount);
            default: throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}

