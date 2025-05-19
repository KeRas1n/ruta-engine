package com.keras1n.core;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CameraTest {

    @Test
    void testDefaultConstructorInitializesToZero() {
        Camera camera = new Camera();
        assertEquals(new Vector3f(0, 0, 0), camera.getPosition());
        assertEquals(new Vector3f(0, 0, 0), camera.getRotation());
    }

    @Test
    void testMovePositionForward() {
        Camera camera = new Camera();
        camera.setRotation(0, 0, 0); //  -Z
        camera.movePosition(0, 0, 1);
        Vector3f pos = camera.getPosition();

        assertEquals(0, pos.x, 0.001);
        assertEquals(0, pos.y, 0.001);
        assertEquals(1, pos.z, 0.001);
    }

    @Test
    void testMovePositionRight() {
        Camera camera = new Camera();
        camera.setRotation(0, 0, 0);
        camera.movePosition(1, 0, 0);
        Vector3f pos = camera.getPosition();

        assertEquals(1, pos.x, 0.001); // x += 1
        assertEquals(0, pos.z, 0.001);
    }

    @Test
    void testMoveRotationChangesAngles() {
        Camera camera = new Camera();
        camera.moveRotation(10, 20, 30);
        Vector3f rot = camera.getRotation();

        assertEquals(10, rot.x, 0.001);
        assertEquals(20, rot.y, 0.001);
        assertEquals(30, rot.z, 0.001);
    }

    @Test
    void testGetForwardVectorLookingStraight() {
        Camera camera = new Camera();
        camera.setRotation(0, 0, 0);
        Vector3f forward = camera.getForwardVector();

        assertEquals(0.0f, forward.x, 0.001f);
        assertEquals(0.0f, forward.y, 0.001f);
        assertEquals(-1.0f, forward.z, 0.001f); // по -Z
    }

}
