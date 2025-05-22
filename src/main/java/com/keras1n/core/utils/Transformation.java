package com.keras1n.core.utils;

import com.keras1n.core.Camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Utility class for creating transformation matrices used for rendering objects in 3D space.
 * Provides methods to create transformation and view matrices based on position, rotation, and scale.
 */
public class Transformation {

    /**
     * Creates a transformation matrix from the provided position, rotation, and scale values.
     * This matrix combines translation, rotation (on all axes), and scaling.
     *
     * @param pos The position (translation) of the object
     * @param rotation The rotation of the object in degrees (x, y, z)
     * @param scale The scaling factor for the object
     * @return The resulting transformation matrix
     */
    public static Matrix4f createTransformationMatrix(Vector3f pos, Vector3f rotation, float scale) {
        return new Matrix4f().identity() // Start with identity matrix
                .translate(pos) // Apply translation
                .rotateX((float) Math.toRadians(rotation.x)) // Apply rotation along X axis
                .rotateY((float) Math.toRadians(rotation.y)) // Apply rotation along Y axis
                .rotateZ((float) Math.toRadians(rotation.z)) // Apply rotation along Z axis
                .scale(scale); // Apply scaling
    }

    /**
     * Creates a view matrix based on the camera's position and rotation.
     * This matrix is used to position and rotate the camera in the 3D world.
     *
     * @param camera The camera from which the view matrix will be generated
     * @return The resulting view matrix, which transforms world coordinates to camera view coordinates
     */
    public static Matrix4f getViewMatrix(Camera camera) {
        Vector3f pos = camera.getPosition(); // Get the camera's position
        Vector3f rot = camera.getRotation(); // Get the camera's rotation
        Matrix4f matrix = new Matrix4f();
        matrix.identity(); // Start with identity matrix

        // Apply rotation based on camera's rotation (pitch, yaw, roll)
        matrix.rotate((float) Math.toRadians(rot.x), new Vector3f(1, 0, 0)) // Rotate around X axis (pitch)
                .rotate((float) Math.toRadians(rot.y), new Vector3f(0, 1, 0)) // Rotate around Y axis (yaw)
                .rotate((float) Math.toRadians(rot.z), new Vector3f(0, 0, 1)); // Rotate around Z axis (roll)

        // Translate the camera to its position (invert the camera position for viewing)
        matrix.translate(-pos.x, -pos.y, -pos.z);

        return matrix; // Return the final view matrix
    }
}
