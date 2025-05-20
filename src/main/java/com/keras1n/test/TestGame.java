package com.keras1n.test;

import com.keras1n.core.*;
import com.keras1n.core.entity.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static com.keras1n.core.utils.Constants.CAMERA_MOVE_SPEED;
import static com.keras1n.core.utils.Constants.MOUSE_SENSITIVITY;


public class TestGame implements ILogic{

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private final GameManager gameManager;


    private Entity entity;
    private Entity tree;
    private Player player;

    private Entity terrain;

    private boolean wasMousePressed = false;

    private GameHUD gameHUD;

    Vector3f cameraInc;

    public TestGame(){
        player = new Player();


        renderer = new RenderManager();
        window = Launcher.getWindow();

        loader = new ObjectLoader();
        cameraInc = new Vector3f();

        gameManager = new GameManager(loader, "src/main/resources/levels/level.json");

        gameHUD = new GameHUD();


    }

    @Override
    public void init() throws Exception {
        renderer.init();

        terrain = loader.createFlatTerrain(50, -1);
        Texture terrainTexture = new Texture(loader.loadTexture("textures/white.jpg"));
        for (Model part : terrain.getModel().getSubmodels()) {
            part.setTexture(terrainTexture);
        }


        //gameManager.createEntity("/models/geo_dead.obj", new Vector3f(0,2,-5), new Vector3f(), 0.5f);
        //gameManager.createEntity("/models/Tree.obj", new Vector3f(5,0,-5), new Vector3f(), 1.0f);

        //gameManager.createEntity("/models/Drone.obj", new Vector3f(0,0,0), new Vector3f(), 1.0f);
    }

    @Override
    public void input() {
        cameraInc.set(0,0,0);
        window.lockCursor();

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


        //SHOOT LOGIC???? TEMPRORARy

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
                    System.out.println("DEBUG: Урон оружия = " + damage);

                    if (enemy.getHealth() <= 0) {
                        System.out.println("ENEMY DEAD — удаляем");
                        gameManager.removeEntity(hit);
                    }
                }
            }
        }

        wasMousePressed = isPressed;


        //ROTATE ENTITY
        if (window.isKeyPressed(GLFW.GLFW_KEY_Z))
            entity.incRotation(1f, 0f, 0f); // Вращение по X

        if (window.isKeyPressed(GLFW.GLFW_KEY_X))
            entity.incRotation(0f, 1f, 0f); // Вращение по Y

        if (window.isKeyPressed(GLFW.GLFW_KEY_C))
            entity.incRotation(0f, 0f, 1f); // Вращение по Z

        if (window.isKeyPressed(GLFW.GLFW_KEY_V))
            entity.incPos(0, 0.1f, 0);

        if (window.isKeyPressed(GLFW.GLFW_KEY_B))
            entity.incPos(0, -0.1f, 0);

        if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            player.jump();
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        //System.out.printf(">>> UPDATE CALLED with deltaTime = %.8f\n", interval);

        player.update(interval, 0);

        //update enemies logic
        for (Entity e : gameManager.getEntities()) {
            if (e instanceof Enemy enemy) {
                enemy.update(player, interval);
            }
        }

        Camera camera = player.getCamera();

        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED * interval,
                cameraInc.y * CAMERA_MOVE_SPEED * interval,
                cameraInc.z * CAMERA_MOVE_SPEED * interval);

        Vector2f rotVec = mouseInput.getDisplVec();
        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY * interval, rotVec.y * MOUSE_SENSITIVITY * interval, 0);
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
            renderer.render(e, camera);
        }

        if (player.getWeapon() != null) {
            player.getWeapon().render(player.getCamera(), renderer);
        }

        gameHUD.renderPlayerHealth(getPlayer().getHealth(), 30, window.getHeight() - 50, window.getWidth(), window.getHeight());
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
