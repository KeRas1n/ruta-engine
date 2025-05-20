package com.keras1n.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.stb.STBEasyFont.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.BufferUtils;

/**
 * A simple 2D HUD renderer that uses stb_easy_font to draw text on screen.
 * Used primarily to display player health or debug info.
 */
public class GameHUD {

    // Buffer to store raw vertex data for text rendering
    private final ByteBuffer charBuffer;

    private static class Message {
        String text;
        long expireAt;

        Message(String text, long durationMillis) {
            this.text = text;
            this.expireAt = System.currentTimeMillis() + durationMillis;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expireAt;
        }
    }

    private final List<Message> messages = new ArrayList<>();

    /**
     * Initializes the HUD and allocates a reusable buffer for text vertices.
     */
    public GameHUD() {
        charBuffer = BufferUtils.createByteBuffer(99999); // буфер под символы
    }

    /**
     * Renders a text string at a specific screen position using OpenGL immediate mode.
     *
     * @param text          The text to render
     * @param x             X coordinate on screen (in pixels)
     * @param y             Y coordinate on screen (from bottom)
     * @param screenWidth   Width of the window (for projection setup)
     * @param screenHeight  Height of the window
     * @param scale         Size multiplier (e.g. 1 = small, 3 = large)
     */
    public void renderText(String text, int x, int y, int screenWidth, int screenHeight, float scale) {
        charBuffer.clear();
        int quads = stb_easy_font_print(0, 0, text, null, charBuffer);


        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, screenWidth, 0, screenHeight, -1, 1); // <--- это и есть "зеркало" Y

        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();


        glTranslatef(x, y, 0);
        glScalef(scale, -scale, 1);

        // Set text color (white) and disable shaders
        glColor3f(1f, 1f, 1f);
        glUseProgram(0);

        // Draw vertex data as quads
        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);
        glDrawArrays(GL_QUADS, 0, quads * 4);
        glDisableClientState(GL_VERTEX_ARRAY);

        // Restore matrices to avoid affecting the 3D rendering
        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
    }

    /**
     * Renders the player's health as on-screen text.
     *
     * @param health         Current player health (0–100)
     * @param x              X position on screen
     * @param y              Y position on screen
     * @param screenWidth    Width of the screen
     * @param screenHeight   Height of the screen
     */
    public void renderPlayerHealth(float health, int x, int y, int screenWidth, int screenHeight) {
        renderText("Health: " + (int) health, x, y, screenWidth, screenHeight, 3);
    }

    /**
    * Adds a Message to list for displaying
    */
    public void showPickupMessage(String text, long durationMillis) {
        messages.add(new Message(text, durationMillis));
    }

    /**
     * Render all active messages on screen
     *
     */
    public void renderPickupMessages(int startX, int startY, int screenWidth, int screenHeight) {
        Iterator<Message> iter = messages.iterator();
        int lineHeight = 30;
        int yOffset = 0;

        while (iter.hasNext()) {
            Message msg = iter.next();
            if (msg.isExpired()) {
                iter.remove();
            } else {
                renderText(msg.text, startX, startY - yOffset, screenWidth, screenHeight, 2);
                yOffset += lineHeight;
            }
        }
    }



}
