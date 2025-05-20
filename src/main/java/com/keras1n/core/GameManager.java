package com.keras1n.core;

import com.keras1n.core.entity.Entity;
import com.keras1n.core.entity.MultiMaterialModel;

import java.util.ArrayList;
import java.util.List;

import com.keras1n.core.entity.Player;
import org.joml.Vector3f;

/**
 * Manages the game state: player, level loading, entity tracking and game over logic.
 */
public class GameManager {
    private final ObjectLoader loader;
    private final String levelPath;
    private final List<Entity> entities = new ArrayList<>();

    private boolean isGameOver = false;
    private Player player;

    /**
     * Constructs the game manager and initializes the game immediately.
     *
     * @param loader    The ObjectLoader used to load models
     * @param levelPath The path to the JSON file defining the level
     */
    public GameManager(final ObjectLoader loader, String levelPath) {
        this.loader = loader;
        this.levelPath = levelPath;

        initializeGame();

    }

    /**
     * Initializes or resets the game state:
     * - Clears entities
     * - Creates new player
     * - Loads level from JSON file
     */
    public void initializeGame(){
        System.out.println("Initializing game");
        entities.clear();
        player = new Player();

        LevelLoader levelLoader = new LevelLoader(loader);
        try {
            List<Entity> loaded = levelLoader.loadEntitiesFromJson(levelPath);
            entities.addAll(loaded);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load level: " + levelPath, e);
        }

        isGameOver = false;
    }

    /**
     * Updates the game state, e.g. checks if player has died.
     */
    public void updateGameState(){
        if(player.getHealth() <= 0){
            isGameOver = true;
        }
    }


    public Entity createEntity(String modelPath, Vector3f position, Vector3f rotation, float scale) throws Exception {
        MultiMaterialModel model = loader.loadOBJModel(modelPath);
        Entity entity = new Entity(model, position, rotation, scale);
        entities.add(entity);
        return entity;
    }

    /**
     * Removes an entity from the game.
     *
     * @param entity The entity to remove
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
