package com.keras1n.core;

import com.keras1n.core.entity.Entity;
import com.keras1n.core.entity.MultiMaterialModel;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class GameManager {
    private final ObjectLoader loader;
    private final List<Entity> entities = new ArrayList<>();

    public GameManager(final ObjectLoader loader, String levelPath) {
        this.loader = loader;


        LevelLoader levelLoader = new LevelLoader(loader);
        try{
            List<Entity> loaded = levelLoader.loadEntitiesFromJson(levelPath);
            entities.addAll(loaded);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load level: " + levelPath, e);
        }

    }

    public Entity createEntity(String modelPath, Vector3f position, Vector3f rotation, float scale) throws Exception {
        MultiMaterialModel model = loader.loadMultiMaterialModel(modelPath);
        Entity entity = new Entity(model, position, rotation, scale);
        entities.add(entity);
        return entity;
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
