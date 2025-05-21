package com.keras1n.core.entity;

import org.joml.Vector3f;

public class Entity {
    private MultiMaterialModel model;
    private Vector3f pos, rotation;
    private float scale;
    private Vector3f size;

    private String modelPath;

    private boolean hasCollision = true;

    public Entity(MultiMaterialModel model, String modelPath, Vector3f pos, Vector3f rotation, float scale, boolean hasCollision) {
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;

        this.hasCollision = hasCollision;
        this.modelPath = modelPath;
    }

    public MultiMaterialModel getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public void incRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }
    public void incPos(float x, float y, float z) {
        this.pos.x += x;
        this.pos.y += y;
        this.pos.z += z;
    }
    private void setPos(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public String getModelPath() {
        return modelPath;
    }

    public Vector3f getSize() {
        return size;
    }

    public void setSize(Vector3f size) {
        this.size = size;
    }

    public boolean hasCollision() { return hasCollision; }
    public void setHasCollision(boolean value) { this.hasCollision = value; }

    public String getType() {
        return "Object"; // базовый тип для обычной сущности
    }
}

