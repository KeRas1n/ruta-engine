package com.keras1n.core;

import com.keras1n.test.Launcher;
import com.keras1n.core.utils.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static float frametime = 1.0f / FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        mouseInput = new MouseInput();
        window.init();
        gameLogic.init();
        mouseInput.init();
    }

    public void start() throws Exception {
        init();
        if(isRunning)
            return;
        run();

    }

    public void run() {
        isRunning = true;
        int frames = 0;
        long lastTime = System.nanoTime();
        long frameCounter = 0;

        while (isRunning) {
            long now = System.nanoTime();
            long passedTime = now - lastTime;
            lastTime = now;

            float deltaTime = passedTime / (float) NANOSECOND;
            frameCounter += passedTime;

            input();
            update(deltaTime); // ← Реальный интервал
            render();
            frames++;

            if (frameCounter >= NANOSECOND) {
                setFps(frames);
                window.setTitle(Constants.TITLE + " | FPS: " + frames);
                frames = 0;
                frameCounter = 0;
            }
        }

        cleanup();
    }

    public void stop(){
        if(!isRunning)
            return;
        isRunning = false;
    }

    private void input(){
        mouseInput.input();
        gameLogic.input();
    }

    private void render(){
        gameLogic.render();
        window.update();
    }

    private void update(float interval){
        gameLogic.update(interval, mouseInput);
    }


    private void cleanup(){
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }

}
