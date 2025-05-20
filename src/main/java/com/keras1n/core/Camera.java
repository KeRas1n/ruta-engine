package com.keras1n.core;

import org.joml.Vector3f;

/**
 * Represents a 3D camera with position and rotation.
 * Provides movement methods and direction calculation.
 */
public class Camera {

    private Vector3f position, rotation;

    /**
     * Creates a camera at origin (0,0,0) with no rotation.
     */
    public Camera() {
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
    }

    /**
     * Creates a camera with the given position and rotation.
     *
     * @param position Initial position in world space
     * @param rotation Initial rotation in degrees (pitch, yaw, roll)
     */
    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    /**
     * Moves the camera based on its current rotation (yaw).
     * Movement is relative to the facing direction.
     *
     * @param x Movement to the right/left
     * @param y Vertical movement (up/down)
     * @param z Forward/backward movement
     */
    public void movePosition(float x, float y, float z) {
        if(z!=0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if(x!=0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }
        position.y += y;
    }

    /**
     * Sets the camera's absolute position.
     */
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    /**
     * Sets the camera's absolute rotation.
     */
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    /**
     * Adds to the current camera rotation.
     * Clamps pitch (x) to avoid flipping.
     */
    public void moveRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;

        // Limit vertical look to avoid flipping camera
        if(rotation.x > 89) rotation.x = 89;
        if(rotation.x < -89) rotation.x = -89;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    /**
     * Computes the forward direction the camera is looking in world space.
     *
     * @return A normalized direction vector
     */
    public Vector3f getForwardVector() {
        float pitch = (float) Math.toRadians(getRotation().x);
        float yaw = (float) Math.toRadians(getRotation().y);

        float x = (float) (Math.cos(pitch) * Math.sin(yaw));
        float y = (float) (Math.sin(pitch));
        float z = (float) (-Math.cos(pitch) * Math.cos(yaw));

        return new Vector3f(x, y, z).normalize();
    }
}
