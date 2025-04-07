package com.keras1n.test;

import com.keras1n.core.*;
import com.keras1n.core.entity.Entity;
import com.keras1n.core.entity.Model;
import com.keras1n.core.entity.Texture;
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

    private Entity entity;
    private Camera camera;

    Vector3f cameraInc;

    public TestGame(){
        renderer = new RenderManager();
        window = Launcher.getWindow();

        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadOBJModel("/models/geo_dead.obj");
        //model.setTexture(new Texture(loader.loadTexture("textures/brickTexture.jpg")));

        entity = new Entity(model, new Vector3f(0, 0, -5), new Vector3f(0,0,0),1);
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

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED, cameraInc.y * CAMERA_MOVE_SPEED, cameraInc.z * CAMERA_MOVE_SPEED);

        if(mouseInput.isRightButtonPress()){
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
        entity.incRotation(0.0f, 0.5f,0.0f);
    }

    @Override
    public void render() {
        if(window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColor(0f, 0f,0f, 0.0f);
        renderer.render(entity, camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
