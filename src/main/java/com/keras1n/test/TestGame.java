package com.keras1n.test;

import com.keras1n.core.*;
import com.keras1n.core.entity.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.keras1n.core.utils.Constants.MOUSE_SENSITIVITY;


public class TestGame implements IGameLogic {

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private final GameManager gameManager;

    private Player player;

    private Entity terrain;

    private boolean wasMousePressed = false;

    private GameHUD gameHUD;

    Vector3f cameraInc;

    private String nextLevelToLoad = null;
    private List<String> levels = Arrays.asList("level.json", "level2.json");
    private int currentLevelIndex = 0;


    private SaveGameManager saveManager;
    private boolean loadSavedGame = false;


    public TestGame(){
        renderer = new RenderManager();
        window = Launcher.getWindow();

        loader = new ObjectLoader();
        cameraInc = new Vector3f();

        gameManager = new GameManager(loader, "src/main/resources/levels/demo.json");

        player = gameManager.getPlayer();

        saveManager = new SaveGameManager(loader, player, gameManager.getEntities());

        gameHUD = new GameHUD();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        terrain = loader.createFlatTerrain(250, -1);
        Texture terrainTexture = new Texture(loader.loadTexture("textures/ground.jpg"));
        for (Model part : terrain.getModel().getSubmodels()) {
            part.setTexture(terrainTexture);
        }
        terrain.setHasCollision(false);

        gameHUD.showPickupMessage("Find an energy crystal and activate teleport", 10000);


        if(loadSavedGame){
            try {
                saveManager.loadGame("savegame.json");
                System.out.println("Game loaded successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("No savegame found or loading failed, starting fresh.");
            }
        }

    }

    @Override
    public void input() {
        cameraInc.set(0,0,0);
        window.lockCursor();

        if(!gameManager.isGameOver()) {
            if(window.isKeyPressed(GLFW.GLFW_KEY_W))
                cameraInc.z = -1;
            if(window.isKeyPressed(GLFW.GLFW_KEY_S))
                cameraInc.z = 1;

            if(window.isKeyPressed(GLFW.GLFW_KEY_A))
                cameraInc.x = -1;
            if(window.isKeyPressed(GLFW.GLFW_KEY_D))
                cameraInc.x = 1;

            if(window.isKeyPressed(GLFW.GLFW_KEY_Q))
                cameraInc.y = -1;
            if(window.isKeyPressed(GLFW.GLFW_KEY_E))
                cameraInc.y = 1;

            if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
                player.jump();
            }
        }
        else{
            //restart game
            if (window.isKeyPressed(GLFW.GLFW_KEY_R)) {
                gameManager.initializeGame();
                player = gameManager.getPlayer();
            }
        }


        //SHOOT LOGIC

        boolean isPressed = window.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_1);

        if (isPressed && !wasMousePressed) {
            Entity hit = player.getWeapon().shootAndHit(player.getCamera(), gameManager.getEntities());
            if (hit != null) {
                System.out.println("POPALI PO OBJECT");
                if (hit instanceof Enemy enemy) {
                    int damage = player.getWeapon().getDamage();
                    enemy.takeDamage(damage); // вычитаем здоровье
                    System.out.println("MINUS " + damage + " HP → осталось " + enemy.getHealth());
                    System.out.println("Weapon class: " + player.getWeapon().getClass().getSimpleName());
                    System.out.println("DEBUG: weapon damage = " + damage);

                    if (enemy.getHealth() <= 0) {
                        System.out.println("ENEMY DEAD — удаляем");
                        gameManager.removeEntity(hit);
                    }
                }
            }
        }

        wasMousePressed = isPressed;



    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        player.update(interval, 0);
        gameManager.updateGameState();

        List<Entity> toRemove = new ArrayList<>();

        //UPDATE INTERACTION WITH ENEMIES / PICKUPS
        for (Entity e : gameManager.getEntities()) {
            if (e instanceof Enemy enemy) {
                enemy.update(player, interval, gameManager.getEntities());
            }


            if(e instanceof PickupItem item){
                Vector3f toPlayer = new Vector3f(player.getCameraPosition()).sub(item.getPos());
                float distance = toPlayer.length();

                if (item instanceof Teleport tp) {
                    System.out.println(distance);
                }

                if(distance < 1.55f){
                    boolean isPickedUp = item.onPickup(player);
                    if(isPickedUp){
                        gameHUD.showPickupMessage(item.getPickUpMessage(), item instanceof SpeedPack ? 15000 : 2000);
                        toRemove.add(e);

                        if (item instanceof Teleport tp) {
                            // go on nex level
                            loadNextLevel();
                        }

                    }
                    else if (item instanceof Teleport tp) {
                        if(!tp.isMessageShown()){
                            tp.setMessageShown(true);
                            gameHUD.showPickupMessage(tp.getPickUpMessage(), 2000);
                        }

                    }
                }

                if (item instanceof SpeedPack sp ) {
                    sp.update(interval);
                }
                if (item instanceof Teleport tp) {
                    if(distance > 1.55f)
                        tp.setMessageShown(false);
                    if(distance < 20f){
                        if(player.isPlayerHasEnergyCrystal()){
                            tp.update(interval);
                        }
                    }

                }
            }

        }

        for (Entity e : toRemove) {
            gameManager.removeEntity(e);
        }

        Camera camera = player.getCamera();

        Vector3f deltaMove = new Vector3f(cameraInc).mul(player.getSpeed() * interval);

        if (deltaMove.lengthSquared() > 0.0001f) {
            player.tryMove(deltaMove, gameManager.getEntities());
        }


        Vector2f rotVec = mouseInput.getDisplVec();
        if (!mouseInput.isFirstFrame()) {
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY * interval, rotVec.y * MOUSE_SENSITIVITY * interval, 0);
        }
        System.out.println("displVec: " + mouseInput.getDisplVec());
        System.out.println("before moveRotation: " + camera.getRotation());


        if (nextLevelToLoad != null) {
            gameManager.loadLevel(nextLevelToLoad);
            player = gameManager.getPlayer();
            nextLevelToLoad = null;
        }

        System.out.println("PLAYERS HEALTH - " + player.getHealth());

    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void render() {
        Camera camera = player.getCamera();

        if(window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColor(0f, 0f,0f, 0.0f);
        renderer.clear();

        renderer.render(terrain, camera);

        for (Entity e : gameManager.getEntities()) {
            if (e != null) {
                renderer.render(e, camera);
            } else {
                System.err.println("Warning: null entity in entities list!");
            }
        }

        if (player.getWeapon() != null) {
            player.getWeapon().render(player.getCamera(), renderer);
        }

        if(gameManager.isGameOver()) {
            gameHUD.renderText("GAME OVER - Press R to restart", 50, window.getHeight()/2, window.getWidth(), window.getHeight(), 7);
        }

        gameHUD.renderPlayerHealth(getPlayer().getHealth(), 30, window.getHeight() - 50, window.getWidth(), window.getHeight());
        //render some messages for example when we pick up an item
        gameHUD.renderPickupMessages(30, window.getHeight() - 100, window.getWidth(), window.getHeight());
    }

    private void loadNextLevel() {
        currentLevelIndex++;
        if (currentLevelIndex >= levels.size()) {
            currentLevelIndex = 0;
        }
        nextLevelToLoad = "src/main/resources/levels/" + levels.get(currentLevelIndex);
    }

    @Override
    public void cleanup() {
        System.out.println(getPlayer().getWeapon().getModelPath());
        System.out.println("REAL HEALTH _ " + getPlayer().getHealth());

        saveManager.updateReferences(getPlayer(), gameManager.getEntities());
        try {
            saveManager.saveGame("savegame.json");
            System.out.println("Game saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }


        renderer.cleanup();
        loader.cleanup();
    }
}
