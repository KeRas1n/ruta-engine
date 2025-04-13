package com.keras1n.core.entity;

import com.keras1n.core.Camera;

import static com.keras1n.core.utils.Constants.GRAVITY;
import static com.keras1n.core.utils.Constants.JUMP_POWER;

public class Player {
    private final Camera camera;
    private float health;
    private float velocityY = 0f;
    private boolean isInAir = false;

    public void update(float deltaTime, float terrainHeight) {
        velocityY += GRAVITY * deltaTime;
        camera.movePosition(0, velocityY * deltaTime, 0);

        if(camera.getPosition().y <= terrainHeight) {
            isInAir = false;
            velocityY = 0f;
            camera.getPosition().y = terrainHeight;
        }

    }

    public void jump() {
        if (!isInAir) {
            velocityY = JUMP_POWER;
            isInAir = true;
        }
    }



    public Player() {
        this.camera = new Camera();
        this.health = 100;
    }

    public Camera getCamera() {
        return camera;
    }

    public float getHealth() {
        return health;
    }

    public void takeDamage(float damage) {
        this.health -= damage;
        if(this.health <= 0) {
            this.health = 0;
        }
    }

    public void heal(float amount) {
        this.health += amount;
        if(this.health > 100f) {
            this.health = 100f;
        }
    }
}
