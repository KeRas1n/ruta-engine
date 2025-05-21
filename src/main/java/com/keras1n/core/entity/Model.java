package com.keras1n.core.entity;

import org.joml.Vector3f;

import java.nio.FloatBuffer;

public class Model {
    private int id;
    private int vertexCount;
    private Texture texture;
    private float[] vertexData;

    public Model(int id, int vertexCount) {
        this.id = id;
        this.vertexCount = vertexCount;
    }
    public Model(int id, int vertexCount, float[] vertexData) {
        this.id = id;
        this.vertexCount = vertexCount;
        this.vertexData = vertexData;
    }

    public Model(int id, int vertexCount, Texture texture) {
        this.id = id;
        this.vertexCount = vertexCount;
        this.texture = texture;
    }

    public Model(Model model, Texture texture) {
        this.id = model.getId();
        this.vertexCount = model.getVertexCount();
        this.texture = texture;
    }

    public int getId() {
        return id;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Calculates the center of the model based on its vertex data.
     *
     * @return The center point of the model's geometry.
     */
    public Vector3f getCenter() {
        if (vertexData == null || vertexData.length == 0) {
            return new Vector3f();
        }

        float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < vertexData.length; i += 3) {
            float x = vertexData[i];
            float y = vertexData[i + 1];
            float z = vertexData[i + 2];

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
            maxZ = Math.max(maxZ, z);
        }

        return new Vector3f(
                (minX + maxX) / 2f,
                (minY + maxY) / 2f,
                (minZ + maxZ) / 2f
        );
    }

    public float[] getVertexData() {
        return vertexData;
    }
}
