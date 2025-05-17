package com.keras1n.core;

import com.keras1n.core.utils.Constants;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Manages the creation, initialization and control of the GLFW window.
 * Provides access to the OpenGL context, input states, and projection matrix.
 */
public class WindowManager {
    /**
     * Field of view for the perspective projection (in radians).
     */
    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.001f;
    public static final float Z_FAR = 10000f;

    private long window;
    private int width, height;
    private String title;

    private boolean resize, fullscreen, vSync;

    private final Matrix4f projectionMatrix;

    /**
     * Constructs a WindowManager with specified dimensions, title, and VSync setting.
     *
     * @param width  Width of the window in pixels
     * @param height Height of the window in pixels
     * @param title  Title of the window
     * @param vSync  Whether VSync should be enabled
     */
    public WindowManager(int width, int height, String title, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;

        projectionMatrix = new Matrix4f();
    }

    /**
     * Constructs a WindowManager with specified dimensions, title, without VSync.
     *
     * @param width  Width of the window
     * @param height Height of the window
     * @param title  Window title
     */
    public WindowManager(int width, int height, String title) {
        this(width, height, title, false);

    }

    /**
     * Initializes the GLFW window and OpenGL context.
     * Sets callbacks, creates the window, and enables basic OpenGL features.
     */
    public void init() {
        // Set up an error callback to print GLFW errors to the standard error stream
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW library. If it fails, throw an exception
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); //make it unvisible for now
        GLFW.glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        //set opengl context to 3.2
        GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);

        GLFW.glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        boolean maximized = false;

        if(width == 0 || height == 0){
            width = height = 100;

            GLFW.glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            maximized = true;
        }

        // Create the GLFW window
        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Error while creating the GLFW window");
        }

        // Prevent window from being resized below 1024x576
        GLFW.glfwSetWindowSizeLimits(window, Constants.WINDOW_MIN_WIDTH, Constants.WINDOW_MIN_HEIGHT, GLFW_DONT_CARE, GLFW_DONT_CARE);

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
           if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE){
                GLFW.glfwSetWindowShouldClose(window, true);
           }
        });

        if(maximized){
            GLFW.glfwMaximizeWindow(window);
        }
        else{
            // if not maximized show it on the center of the screen
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        }

        //make the OpenGL context current on this thread, so now we can call GL funcrions
        glfwMakeContextCurrent(window);

        if(isVSyncEnabled()){
            GLFW.glfwSwapInterval(1); // 1 = synchronize with vertical refresh
        }

        glfwShowWindow(window); //finally after initialization, show the window
        GL.createCapabilities(); // Create OpenGL capabilities (must be done after context creation)

        // get maximized window size and set it for projectionmatrix
        IntBuffer fbWidth = BufferUtils.createIntBuffer(1);
        IntBuffer fbHeight = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(window, fbWidth, fbHeight);
        this.width = fbWidth.get(0);
        this.height = fbHeight.get(0);

        // Set the clear color to black
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glEnable(GL_STENCIL_TEST);
        GL11.glEnable(GL_DEPTH_TEST);

        /*GL11.glEnable(GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);*/
    }

    /**
     * Swaps display buffers, updates the projection matrix, and polls window events.
     */
    public void update() {
        glfwSwapBuffers(window); //show the rendered frame by swapping the front and back buffers (in back are our changes)
        updateProjectionMatrix(); //if window waas resized
        glfwPollEvents(); //reaction on input
    }
    /**
     * Frees resources used by the window and terminates GLFW.
     */
    public void cleanup() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    /**
     * Sets the OpenGL clear color
     */
    public void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    /**
     * Checks whether a specific key is currently pressed.
     *
     * @param keyCode GLFW key code
     * @return true if the key is pressed, false if the key is not pressed
     */
    public boolean isKeyPressed(int keyCode){
        return GLFW.glfwGetKey(window, keyCode) == GLFW.GLFW_PRESS;
    }


    /**
     * Checks whether a specific mouse button is currently pressed.
     *
     * @param button GLFW mouse button code
     * @return true if the button is pressed, false if not
     */
    public boolean isMouseButtonPressed(int button) {
        return GLFW.glfwGetMouseButton(window, button) == GLFW.GLFW_PRESS;
    }

    /**
     * Checks whether the window should be closed (ESC pressed, or close clicked).
     *
     * @return true if the window should close
     */
    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public long getWindowHandle() {
        return window;
    }

    /**
     * Indicates whether the window was resized since last update.
     *
     * @return true if resized
     */
    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }
    public boolean isVSyncEnabled() {
        return vSync;
    }
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Recalculates the projection matrix based on the current window size.
     *
     * @return Updated projection matrix
     */
    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / (float) height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    /**
     * Locks the cursor inside the window
     */
    public void lockCursor() {
       // GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

}
