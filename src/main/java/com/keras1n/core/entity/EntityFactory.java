package com.keras1n.core.entity;

import org.joml.Vector3f;

public class EntityFactory {
    public static Entity create(String type,
                                MultiMaterialModel model,
                                Vector3f pos,
                                Vector3f rot,
                                float scale,
                                float health,
                                float damage
    ) {
        switch (type) {
            case "Object": return new Entity(model, pos, rot, scale); // just Mesh
            case "Enemy": return new Enemy(model, pos, rot, scale, health, damage);
            default: throw new IllegalArgumentException("Unknown entity type: " + type);
        }
    }
}

