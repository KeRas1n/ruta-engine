package com.keras1n.core;

import org.lwjgl.BufferUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class GameHUD {
    private int textureId;

    public void renderPlayerHealth(float health, float x, float y, int windowWidth, int windowHeight) {
        String text = "Health: " + (int) health;
        int size = 128;

        // 1. Создаём изображение с текстом
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.setColor(Color.WHITE);
        g.drawString(text, 10, 40);
        g.dispose();

        // 2. Получаем пиксели в ByteBuffer
        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length);
        buffer.put(pixels);
        buffer.flip();

        // 3. Генерируем и загружаем текстуру
        if (textureId == 0) {
            textureId = glGenTextures();
        }

        glBindTexture(GL_TEXTURE_2D, textureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size, size, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // 4. Ортографическая проекция и отрисовка
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, windowWidth, windowHeight, 0, -1, 1);

        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();

        glBindTexture(GL_TEXTURE_2D, textureId);

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(x, y);
        glTexCoord2f(1, 0); glVertex2f(x + size, y);
        glTexCoord2f(1, 1); glVertex2f(x + size, y + size);
        glTexCoord2f(0, 1); glVertex2f(x, y + size);
        glEnd();

        glPopMatrix();
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
    }

    public void cleanup() {
        if (textureId != 0) {
            glDeleteTextures(textureId);
        }
    }
}
