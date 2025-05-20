package com.keras1n.core.entity;

import com.keras1n.core.Camera;
import com.keras1n.core.weapon.Pistol;
import com.keras1n.core.weapon.Weapon;
import com.keras1n.core.weapon.WeaponFactory;
import org.joml.Vector3f;

import static com.keras1n.core.utils.Constants.GRAVITY;
import static com.keras1n.core.utils.Constants.JUMP_POWER;
/**
 * Represents the player in the game.
 * Handles health, position (via camera), gravity, jumping and weapon handling.
 */
public class Player {
    private final Camera camera;
    private float health;
    private float velocityY = 0f;
    private boolean isInAir = false;

    private Weapon weapon;

    /**
     * Updates the player's physics each frame (gravity, collision with terrain).
     *
     * @param deltaTime      Time since last frame in seconds
     * @param terrainHeight  Y position of the terrain under the player
     */
    public void update(float deltaTime, float terrainHeight) {
        velocityY += GRAVITY * deltaTime;
        camera.movePosition(0, velocityY * deltaTime, 0);

        if(camera.getPosition().y <= terrainHeight) {
            isInAir = false;
            velocityY = 0f;
            camera.getPosition().y = terrainHeight;
        }

    }

    /**
     * Makes the player jump, if they are not already in the air.
     */
    public void jump() {
        if (!isInAir) {
            velocityY = JUMP_POWER;
            isInAir = true;
        }
    }


    public Player() {
        this.camera = new Camera();
        this.health = 100;

        //give a pistol as a default weapon?
        try {
            this.weapon = WeaponFactory.createWeapon("pistol");
        }catch (Exception e) {
            e.printStackTrace();
            this.weapon = null;
        }
    }

    /**
     * Reduces the player's health by a given amount.
     * If health drops below 0, it is clamped to 0.
     */
    public void takeDamage(float damage) {
        this.health -= damage;
        if(this.health <= 0) {
            this.health = 0;
        }

        System.out.println("Health: " + this.health);
    }
    /**
     * Heals the player by a given amount, up to a maximum of 100.
     */
    public void heal(float amount) {
        this.health += amount;
        if(this.health > 100f) {
            this.health = 100f;
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public Vector3f getPosition() {
        return camera.getPosition();
    }

    public float getHealth() {
        return health;
    }

    public Weapon getWeapon() {
        return weapon;
    }

}
