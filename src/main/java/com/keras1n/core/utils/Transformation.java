package com.keras1n.core.utils;

import com.keras1n.core.Camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    public static Matrix4f createTransformationMatrix(Vector3f pos, Vector3f rotation, float scale) {
        return new Matrix4f().identity()
                .translate(pos)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(scale);
    }

    public static Matrix4f getViewMatrix(Camera camera) {
        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation();
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.rotate((float)Math.toRadians(rot.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rot.y), new Vector3f(0, 1, 0))
                .rotate((float)Math.toRadians(rot.z), new Vector3f(0, 0, 1));

        matrix.translate(-pos.x, -pos.y, -pos.z);
        return matrix;
    }
}
