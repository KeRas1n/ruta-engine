package com.keras1n.core;

import com.keras1n.test.Launcher;
import com.keras1n.core.utils.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;


/**
 * The core engine manager that controls the main game loop,
 * timing, input handling, rendering, and initialization of game components.
 *
 * This class follows a singleton pattern and is responsible for:
 * - Setting up the window and input
 * - Running the update-render loop
 * - Managing FPS and deltaTime
 * - Cleaning up resources after the game ends
 */
public class EngineManager {
    private static EngineManager INSTANCE;

    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static float frametime = 1.0f / FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;


    /**
     * Initializes the engine and game components.
     *
     * @throws Exception if initialization fails
     */
    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        mouseInput = new MouseInput();
        gameLogic.init();
        mouseInput.init();

    }

    /**
     * Starts the engine if it is not already running.
     *
     * @throws Exception if initialization or execution fails
     */
    public void start() throws Exception {
        init();
        if(isRunning)
            return;
        run();
    }

    /**
     * The main game loop: processes input, updates game logic, renders, and tracks FPS
     */
    public void run() {
        isRunning = true;

        long lastTime = System.nanoTime();
        int frames = 0;
        long frameCounter = 0;

        while (isRunning && !window.windowShouldClose()) {
            long now = System.nanoTime();
            long passedTime = now - lastTime;
            lastTime = now;

            float deltaTime = passedTime / 1_000_000_000.0f;

            input();
            update(deltaTime);
            render();
            frames++;
            frameCounter += passedTime;

            if (frameCounter >= NANOSECOND) {
                setFps(frames);
                window.setTitle(Constants.TITLE + " | FPS: " + frames);
                frames = 0;
                frameCounter = 0;
            }
        }

        cleanup();
    }
    /**
     * Stops the engine loop.
     */
    public void stop(){
        if(!isRunning)
            return;
        isRunning = false;
    }

    /**
     * Processes user input via mouse and game logic input handling.
     */
    private void input(){
        mouseInput.input();
        gameLogic.input();
    }

    /**
     * Renders the current game frame and updates the window.
     */
    private void render(){
        gameLogic.render();
        window.update();
    }

    /**
     * Updates the game logic using the given delta time and mouse input.
     *
     * @param interval Time since the last frame in seconds
     */
    private void update(float interval){
        gameLogic.update(interval, mouseInput);
    }


    /**
     * Cleans up game and window resources and terminates GLFW.
     */
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

    /**
     * Returns the singleton instance of the engine manager.
     *
     * @return the EngineManager instance
     */
    public static EngineManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new EngineManager();
        }

        return INSTANCE;
    }

}
