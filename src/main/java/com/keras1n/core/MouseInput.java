package com.keras1n.core;

import com.keras1n.test.Launcher;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

/**
 * This class handles mouse input for the game window.
 * It tracks the mouse position, mouse button states, and provides normalized mouse movement.
 */
public class MouseInput {
    private final Vector2d previousPos, currentPos;
    private final Vector2f displVec;
    private final WindowManager window;

    private boolean inWindow = false, leftButtonPress = false, rightButtonPress = false;

    /**
     * Initializes the MouseInput object with a given window manager.
     *
     * @param window The window manager instance used to interact with the window
     */
    public MouseInput(WindowManager window) {
        this.window = window;
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    /**
     * Initializes the mouse input system by setting up GLFW callbacks for cursor position,
     * mouse button presses, and cursor enter/leave events.
     */
    public void init() {
        // Callback to update the mouse position
        GLFW.glfwSetCursorPosCallback(Launcher.getWindow().getWindowHandle(), (window, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });

        // Callback for cursor enter/leave events
        GLFW.glfwSetCursorEnterCallback(Launcher.getWindow().getWindowHandle(), (window, entered) -> {
            inWindow = entered;
        });

        // Callback to track mouse button presses (left and right)
        GLFW.glfwSetMouseButtonCallback(Launcher.getWindow().getWindowHandle(), (window, button, action, mods) -> {
            leftButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS;
            rightButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS;
        });
    }

    /**
     * Updates the mouse input by calculating the displacement vector from the center of the window
     * and resetting the cursor position back to the center of the window for continuous tracking.
     */
    public void input() {
        displVec.set(0, 0);

        if (inWindow) {
            // Calculate displacement based on the mouse position relative to the center of the window
            double centerX = window.getWidth() / 2.0;
            double centerY = window.getHeight() / 2.0;

            double dx = currentPos.x - centerX;
            double dy = currentPos.y - centerY;

            // Set the displacement vector with inverted axes (for FPS-like controls)
            displVec.x = (float) dy;
            displVec.y = (float) dx;

            // Reset cursor position to center of the window for continuous tracking
            GLFW.glfwSetCursorPos(window.getWindowHandle(), centerX, centerY);

            // Update the current position to be at the center of the window
            currentPos.x = centerX;
            currentPos.y = centerY;
        }
    }

    /**
     * Gets the displacement vector of the mouse since the last update.
     * The vector represents the change in the mouse position relative to the center of the window.
     *
     * @return The displacement vector of the mouse
     */
    public Vector2f getDisplVec() {
        return displVec;
    }

    /**
     * Checks if the left mouse button is currently pressed.
     *
     * @return true if the left mouse button is pressed, false otherwise
     */
    public boolean isLeftButtonPress() {
        return leftButtonPress;
    }

    /**
     * Checks if the right mouse button is currently pressed.
     *
     * @return true if the right mouse button is pressed, false otherwise
     */
    public boolean isRightButtonPress() {
        return rightButtonPress;
    }
}
