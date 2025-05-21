package com.keras1n.core;

import com.keras1n.core.entity.*;
import com.keras1n.core.utils.Constants;
import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Saves and loads the game state (player + entities) from/to a JSON file,
 * using org.json library (like in LevelLoader).
 */
public class SaveGameManager {

    private final ObjectLoader loader;
    private Player player;
    private List<Entity> entities;

    /**
     * Constructs the SaveGameManager.
     *
     * @param loader   ObjectLoader for loading models.
     * @param player   Player instance.
     * @param entities List of all game entities.
     */
    public SaveGameManager(ObjectLoader loader, Player player, List<Entity> entities) {
        this.loader = loader;
        this.player = player;
        this.entities = entities;
    }

    /**
     * Saves the game state to a JSON file.
     *
     * @param filepath Path to the JSON save file.
     * @throws IOException If writing file fails.
     */
    public void saveGame(String filepath) throws IOException {
        System.out.println("PLAYER'S HEALTH - " + player.getHealth());
        JSONObject root = new JSONObject();

        // Save player
        JSONObject playerJson = new JSONObject();
        Vector3f pPos = player.getCamera().getPosition();
        Vector3f pRot = player.getCamera().getRotation();
        playerJson.put("position", vec3ToJsonArray(pPos));
        playerJson.put("rotation", vec3ToJsonArray(pRot));
        playerJson.put("scale", 1.0f);
        playerJson.put("health", player.getHealth());
        root.put("player", playerJson);

        // Save entities
        JSONArray entitiesJson = new JSONArray();
        for (Entity e : entities) {
            if (e.getModelPath() == null || e.getModelPath().isEmpty()) {
                // terrain or some shit
                continue;
            }
            JSONObject entJson = new JSONObject();
            entJson.put("type", e.getType());
            entJson.put("model", e.getModelPath());
            entJson.put("position", vec3ToJsonArray(e.getPos()));
            entJson.put("rotation", vec3ToJsonArray(e.getRotation()));
            entJson.put("scale", e.getScale());

            if (e instanceof Enemy) {
                entJson.put("health", ((Enemy) e).getHealth());
                entJson.put("damage", ((Enemy) e).getDamage());
            }
            if (e instanceof HealthPack) {
                entJson.put("healAmount", ((HealthPack) e).getHealAmount());
            }
            entJson.put("collision", e.hasCollision());

            entitiesJson.put(entJson);
        }
        root.put("entities", entitiesJson);

        // Write JSON string to file
        Files.writeString(Paths.get(filepath), root.toString(4)); // indent 4 spaces
    }

    /**
     * Loads the game state from a JSON file.
     *
     * @param filepath Path to the JSON save file.
     * @throws Exception If reading or parsing fails.
     */
    public void loadGame(String filepath) throws Exception {
        if (!Files.exists(Paths.get(filepath))) {
            System.out.println("Save file not found: " + filepath);
            return;
        }

        String content = Files.readString(Paths.get(filepath));
        JSONObject root = new JSONObject(content);

        // Load player
        JSONObject playerJson = root.getJSONObject("player");

        JSONArray posArr = playerJson.getJSONArray("position");
        player.getCamera().setPosition(
                (float) posArr.getDouble(0),
                (float) posArr.getDouble(1),
                (float) posArr.getDouble(2)
        );

        JSONArray rotArr = playerJson.getJSONArray("rotation");
        player.getCamera().setRotation(
                (float) rotArr.getDouble(0),
                (float) rotArr.getDouble(1),
                (float) rotArr.getDouble(2)
        );

        player.setHealth(playerJson.getInt("health"));

        // Load entities
        entities.clear();
        Map<String, MultiMaterialModel> modelCache = new HashMap<>();
        JSONArray entitiesJson = root.getJSONArray("entities");

        for (int i = 0; i < entitiesJson.length(); i++) {
            JSONObject entJson = entitiesJson.getJSONObject(i);

            String type = entJson.getString("type");
            String modelPath = entJson.getString("model");
            Vector3f pos = jsonArrayToVec3(entJson.getJSONArray("position"));
            Vector3f rot = jsonArrayToVec3(entJson.getJSONArray("rotation"));
            float scale = (float) entJson.getDouble("scale");

            float health = entJson.has("health") ? (float) entJson.getDouble("health") : Constants.DEFAULT_ENEMY_HEALTH;
            float damage = entJson.has("damage") ? (float) entJson.getDouble("damage") : Constants.DEFAULT_ENEMY_DAMAGE;
            int healAmount = entJson.has("healAmount") ? entJson.getInt("healAmount") : 0;
            boolean hasCollision = entJson.has("collision") ? entJson.getBoolean("collision") : true;

            MultiMaterialModel model;
            if (modelCache.containsKey(modelPath)) {
                model = modelCache.get(modelPath);
            } else {
                model = loader.loadOBJModel(modelPath);
                modelCache.put(modelPath, model);
            }

            Entity entity = EntityFactory.create(type, hasCollision, model, pos, rot, scale, health, damage, healAmount, modelPath);

            if (entity == null) {
                System.err.println("WARNING: EntityFactory.create returned null for type: " + type + " model: " + modelPath);
                continue; // пропускаем добавление null
            }

            Vector3f colliderSize = loader.computeBoundingBox(model);
            entity.setSize(colliderSize);
            entities.add(entity);

            System.out.println("Loaded entity: type=" + type + ", model=" + modelPath + ", pos=" + pos + ", rot=" + rot);
        }
    }


    private JSONArray vec3ToJsonArray(Vector3f vec) {
        JSONArray arr = new JSONArray();
        arr.put(vec.x);
        arr.put(vec.y);
        arr.put(vec.z);
        return arr;
    }

    private Vector3f jsonArrayToVec3(JSONArray arr) {
        return new Vector3f(
                (float) arr.getDouble(0),
                (float) arr.getDouble(1),
                (float) arr.getDouble(2)
        );
    }

    public void updateReferences(Player newPlayer, List<Entity> newEntities) {
        this.player = newPlayer;
        this.entities = newEntities;
    }
}
