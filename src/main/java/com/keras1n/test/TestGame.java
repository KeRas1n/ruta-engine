package com.keras1n.test;

import com.keras1n.core.*;
import com.keras1n.core.entity.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static com.keras1n.core.utils.Constants.CAMERA_MOVE_SPEED;
import static com.keras1n.core.utils.Constants.MOUSE_SENSITIVITY;


public class TestGame implements ILogic{

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private final GameManager gameManager;

    private final List<Entity> entities = new ArrayList<>();

    private Entity entity;
    private Entity tree;
    private Player player;

    private Entity terrain;


    Vector3f cameraInc;

    public TestGame(){
        player = new Player();

        renderer = new RenderManager();
        window = Launcher.getWindow();

        loader = new ObjectLoader();
        cameraInc = new Vector3f();

        gameManager = new GameManager(loader);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        terrain = loader.createFlatTerrain(50, -1);
        Texture terrainTexture = new Texture(loader.loadTexture("textures/white.jpg"));
        for (Model part : terrain.getModel().getSubmodels()) {
            part.setTexture(terrainTexture);
        }


        gameManager.createEntity("/models/geo_dead.obj", new Vector3f(0,2,-5), new Vector3f(), 0.5f);
        gameManager.createEntity("/models/geo_dead.obj", new Vector3f(5,0,-5), new Vector3f(), 1.0f);
    }

    @Override
    public void input() {
        cameraInc.set(0,0,0);

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
        player.update(interval, 0);

        Camera camera = player.getCamera();

        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED * interval,
                cameraInc.y * CAMERA_MOVE_SPEED * interval,
                cameraInc.z * CAMERA_MOVE_SPEED * interval);

        if(mouseInput.isRightButtonPress()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY * interval, rotVec.y * MOUSE_SENSITIVITY * interval, 0);
        }
        //entity.incRotation(0.0f, 0.5f,0.0f);
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

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
