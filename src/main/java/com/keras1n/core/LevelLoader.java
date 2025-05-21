package com.keras1n.core;

import com.keras1n.core.entity.Entity;
import com.keras1n.core.entity.EntityFactory;
import com.keras1n.core.entity.MultiMaterialModel;
import com.keras1n.core.utils.Constants;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Class responsible for loading game entities from a JSON level file.
 * Uses ObjectLoader to load and cache models, and EntityFactory to create entities.
 */
public class LevelLoader {
    private final ObjectLoader loader;

    /**
     * Constructs a new LevelLoader with the specified ObjectLoader.
     *
     * @param loader the ObjectLoader used to load models from .obj files
     */
    public LevelLoader(ObjectLoader loader) {
        this.loader = loader;
    }

    /**
     * Loads entities from a JSON file at the given path.
     * The JSON is expected to contain an array of entities with model, type, position, rotation, and scale.
     * Models are cached to avoid loading duplicates.
     *
     * @param path the path to the JSON level file
     * @return a list of created Entity objects
     * @throws Exception if the file can't be read or parsing fails
     */
    public List<Entity> loadEntitiesFromJson(String path) throws Exception {
        List<Entity> entities = new ArrayList<>();
        Map<String, MultiMaterialModel> modelCache = new HashMap<>();

        // read JSON
        String json = Files.readString(Paths.get(path));
        JSONObject level = new JSONObject(json);
        JSONArray ents = level.getJSONArray("entities");

        // find all unique models
        Set<String> uniqueModelPaths = new HashSet<>();
        for (int i = 0; i < ents.length(); i++) {
            JSONObject obj = ents.getJSONObject(i);
            uniqueModelPaths.add(obj.getString("model"));
        }

        // load unique models
        for (String modelPath : uniqueModelPaths) {
            MultiMaterialModel model = loader.loadOBJModel(modelPath);
            modelCache.put(modelPath, model);
        }

        // create entitites
        for (int i = 0; i < ents.length(); i++) {
            JSONObject obj = ents.getJSONObject(i);

            String modelPath = obj.getString("model");
            String type = obj.getString("type");
            Vector3f pos = toVector3f(obj.getJSONArray("position"));
            Vector3f rot = toVector3f(obj.getJSONArray("rotation"));
            float scale = (float) obj.getDouble("scale");

            //enemy properties
            float health = obj.has("health") ? obj.getFloat("health") : Constants.DEFAULT_ENEMY_HEALTH;
            float damage = obj.has("damage") ? obj.getFloat("damage") : Constants.DEFAULT_ENEMY_DAMAGE;

            //pickup properties
            int healAmount = obj.has("healAmount") ? obj.getInt("healAmount") : (int) Constants.DEFAULT_ENEMY_DAMAGE;

            boolean hasCollision = obj.has("collision") ? obj.getBoolean("collision") : true;

            MultiMaterialModel model = modelCache.get(modelPath);
            Entity entity = EntityFactory.create(type, hasCollision, model, pos, rot, scale, health, damage, healAmount);
            Vector3f colliderSize = loader.computeBoundingBox(model);
            entity.setSize(colliderSize);
            entities.add(entity);
        }

        return entities;
    }

    /**
     * Converts a JSONArray of three numbers to a Vector3f.
     *
     * @param array a JSONArray with 3 float-compatible values
     * @return a Vector3f instance with the same values
     */
    public Vector3f toVector3f(JSONArray array) {
        return new Vector3f(
                (float) array.getDouble(0),
                (float) array.getDouble(1),
                (float) array.getDouble(2)
        );
    }
}
