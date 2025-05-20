package com.keras1n.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.stb.STBEasyFont.*;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

public class GameHUD {

    private final ByteBuffer charBuffer;

    public GameHUD() {
        charBuffer = BufferUtils.createByteBuffer(99999); // буфер под символы
    }

    public void renderPlayerHealth(float health, int x, int y, int screenWidth, int screenHeight) {
        String text = "Health: " + (int) health;

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
        glScalef(1, -1, 1);

        glColor3f(1f, 1f, 1f);
        glUseProgram(0);

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);
        glDrawArrays(GL_QUADS, 0, quads * 4);
        glDisableClientState(GL_VERTEX_ARRAY);

        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
    }




}
