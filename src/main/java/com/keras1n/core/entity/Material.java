package com.keras1n.core.entity;

public class Material {
    private final String name;
    private final String texturePath;
    private final Texture texture;

    public Material(String name, String texturePath, Texture texture) {
        this.name = name;
        this.texturePath = texturePath;
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public Texture getTexture() {
        return texture;
    }
}

