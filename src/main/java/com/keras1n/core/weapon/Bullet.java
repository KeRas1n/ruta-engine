package com.keras1n.core.weapon;

import org.joml.Vector3f;

public class Bullet {
    private Vector3f position;
    private Vector3f direction;
    private float speed;
    private float damage;

    public Bullet(Vector3f position, Vector3f direction, float damage, float speed) {
        this.position = position;
        this.direction = direction;
        this.damage = damage;
        this.speed = speed;
    }

    public void update(float deltaTime) {
        Vector3f velocity = new Vector3f(direction).mul(speed * deltaTime);
        position.add(velocity);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getDamage() {
        return damage;
    }

    // можешь добавить проверку на попадание сюда
}
