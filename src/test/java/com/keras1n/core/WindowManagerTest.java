package com.keras1n.core;

import org.joml.Matrix4f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WindowManagerTest {

    @Test
    void testWindowManagerInitialValues() {
        WindowManager wm = new WindowManager(1280, 720, "Test Window", true);

        assertEquals(1280, wm.getWidth());
        assertEquals(720, wm.getHeight());
        assertEquals("Test Window", wm.getTitle());
        assertTrue(wm.isVSyncEnabled());
    }

    @Test
    void testResizeFlag() {
        WindowManager wm = new WindowManager(800, 600, "Resize Test");
        assertFalse(wm.isResize());

        wm.setResize(true);
        assertTrue(wm.isResize());
    }

    @Test
    void testProjectionMatrixIsValid() {
        WindowManager wm = new WindowManager(800, 600, "Matrix Test");
        Matrix4f proj = wm.updateProjectionMatrix();

        assertNotNull(proj);
        float[] matrix = new float[16];
        proj.get(matrix);

        //check if matrix does not contain NaN/Inf
        for (float v : matrix) {
            assertFalse(Float.isNaN(v));
            assertFalse(Float.isInfinite(v));
        }
    }
}
