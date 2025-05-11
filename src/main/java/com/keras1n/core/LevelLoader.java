package com.keras1n.core;

import com.keras1n.core.entity.Entity;
import com.keras1n.core.entity.EntityFactory;
import com.keras1n.core.entity.MultiMaterialModel;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LevelLoader {
    private final ObjectLoader loader;

    public LevelLoader(ObjectLoader loader) {
        this.loader = loader;
    }

    public List<Entity> loadEntitiesFromJson(String path) throws Exception {
        List<Entity> entities = new ArrayList<>();
        Map<String, MultiMaterialModel> modelCache = new HashMap<>();

        // 1. Чтение JSON
        String json = Files.readString(Paths.get(path));
        JSONObject level = new JSONObject(json);
        JSONArray ents = level.getJSONArray("entities");

        // 2. Собираем список уникальных моделей
        Set<String> uniqueModelPaths = new HashSet<>();
        for (int i = 0; i < ents.length(); i++) {
            JSONObject obj = ents.getJSONObject(i);
            uniqueModelPaths.add(obj.getString("model"));
        }

        // 3. Загружаем каждую модель только один раз
        for (String modelPath : uniqueModelPaths) {
            MultiMaterialModel model = loader.loadMultiMaterialModel(modelPath);
            modelCache.put(modelPath, model);
        }

        // 4. Создаём сущности с использованием кеша
        for (int i = 0; i < ents.length(); i++) {
            JSONObject obj = ents.getJSONObject(i);

            String modelPath = obj.getString("model");
            String type = obj.getString("type");
            Vector3f pos = toVector3f(obj.getJSONArray("position"));
            Vector3f rot = toVector3f(obj.getJSONArray("rotation"));
            float scale = (float) obj.getDouble("scale");

            MultiMaterialModel model = modelCache.get(modelPath);
            Entity entity = EntityFactory.create(type, model, pos, rot, scale);
            entities.add(entity);
        }

        return entities;
    }

    private Vector3f toVector3f(JSONArray array) {
        return new Vector3f(
                (float) array.getDouble(0),
                (float) array.getDouble(1),
                (float) array.getDouble(2)
        );
    }
}
