package com.keras1n.core;

import com.keras1n.test.Launcher;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseInput {
    private final Vector2d previousPos, currentPos;
    private final Vector2f displVec;
    private final WindowManager window;

    private boolean inWindow = false, leftButtonPress = false, rightButtonPress = false;


    public MouseInput(WindowManager window) {
        this.window = window;
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
    }

    public void init(){
        GLFW.glfwSetCursorPosCallback(Launcher.getWindow().getWindowHandle(), (window, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });

        GLFW.glfwSetCursorEnterCallback(Launcher.getWindow().getWindowHandle(), (window, entered) -> {
            inWindow = entered;
        });

        GLFW.glfwSetMouseButtonCallback(Launcher.getWindow().getWindowHandle(), (window, button, action, mods) -> {
            leftButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS;
            rightButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS;
        });
    }

    public void input(){
        displVec.set(0, 0);

        if (inWindow) {
            double centerX = window.getWidth() / 2.0;
            double centerY = window.getHeight() / 2.0;

            double dx = currentPos.x - centerX;
            double dy = currentPos.y - centerY;

            displVec.x = (float) dy;
            displVec.y = (float) dx;

            // reset cursor position to center
            GLFW.glfwSetCursorPos(window.getWindowHandle(), centerX, centerY);

            currentPos.x = centerX;
            currentPos.y = centerY;
        }


    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public boolean isLeftButtonPress() {
        return leftButtonPress;
    }

    public boolean isRightButtonPress() {
        return rightButtonPress;
    }
}
