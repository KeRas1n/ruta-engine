package com.keras1n.core.entity;

import com.keras1n.core.Camera;
import com.keras1n.core.utils.Constants;
import com.keras1n.core.utils.PhysicsUtils;
import com.keras1n.core.weapon.Weapon;
import com.keras1n.core.weapon.WeaponFactory;
import org.joml.Vector3f;

import java.util.List;

import static com.keras1n.core.utils.Constants.GRAVITY;
import static com.keras1n.core.utils.Constants.JUMP_POWER;
/**
 * Represents the player in the game.
 * Handles health, position (via camera), gravity, jumping and weapon handling.
 */
public class Player extends Entity {
    private final Camera camera;
    private int health;
    private float velocityY = 0f;
    private boolean isInAir = false;

    private float speed = Constants.CAMERA_MOVE_SPEED;
    private boolean hasSpeedBoost = false;
    private long speedBoostEndTime = 0;

    private Weapon weapon;

    private boolean playerHasEnergyCrystal = false;

    private float headHeightOffset = 0.2f;

    private Vector3f size = new Vector3f(0.6f, 1.8f, 0.6f);


    /**
     * Player constructor. It creates enttity and extends it
     *
     */
    public Player() {
        super(null,null, new Vector3f(), new Vector3f(), 1f, false);
        this.camera = new Camera(new Vector3f(), new Vector3f(5,0,0));
        this.health = 100;

        //give a pistol as a default weapon
        try {
            this.weapon = WeaponFactory.createWeapon("pistol");
        }catch (Exception e) {
            e.printStackTrace();
            this.weapon = null;
        }
    }

    /**
     * Updates the player's physics each frame (gravity, collision with terrain).
     *
     * @param deltaTime      Time since last frame in seconds
     * @param terrainHeight  Y position of the terrain under the player
     */
    public void update(float deltaTime, float terrainHeight) {
        System.out.println(camera.getRotation());
        velocityY += GRAVITY * deltaTime;

        incPos(0, velocityY * deltaTime, 0);
        //camera.movePosition(0, velocityY * deltaTime, 0);

        // Sync camera to player position + offset (e.g., head height)
        camera.setPosition(getPos().x, getPos().y + headHeightOffset, getPos().z);

        //set players rotation where camera currently looks
        setRotation(camera.getRotation());

        if(getPos().y <= terrainHeight) {
            isInAir = false;
            velocityY = 0f;
            getPos().y = terrainHeight;
        }


        if (hasSpeedBoost && System.currentTimeMillis() > speedBoostEndTime) {
            this.speed = Constants.CAMERA_MOVE_SPEED;
            hasSpeedBoost = false;
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


    /**
     * Tries to move the player by a given delta vector.
     * Cancels movement if colliding with any obstacles.
     *
     */
    public void tryMove(Vector3f delta, List<Entity> obstacles) {
        Vector3f currentPos = getCameraPosition();
        Vector3f newPos = new Vector3f(currentPos).add(delta);

        Entity tempPlayer = new Entity(null, null, newPos, new Vector3f(), 1f, false);
        tempPlayer.setSize(getSize());

        for (Entity e : obstacles) {
            if (!e.hasCollision()) continue;
            if (PhysicsUtils.checkAABBCollision(tempPlayer, e)) {
                System.out.println("❌ BLOCKED MOVE: collision with " + e.getClass().getSimpleName() +
                        " at " + e.getPos() + " size " + e.getSize());
                return; // Движение блокируем сразу
            }
        }

        // Если коллизий нет — двигаем
        movePosition(delta.x, 0, delta.z);
        System.out.println("✅ MOVED: " + delta);
    }

    public void movePosition(float x, float y, float z) {
        if(z!=0){
            getPos().x += (float) Math.sin(Math.toRadians(getRotation().y)) * -1.0f * z;
            getPos().z += (float) Math.cos(Math.toRadians(getRotation().y)) * z;
        }
        if(x!=0){
            getPos().x += (float) Math.sin(Math.toRadians(getRotation().y - 90)) * -1.0f * x;
            getPos().z += (float) Math.cos(Math.toRadians(getRotation().y - 90)) * x;
        }
        getPos().y += y;
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
    public void heal(int amount) {
        this.health += amount;
        if(this.health > 100) {
            this.health = 100;
        }
    }

    /**
     * Applies speed boost to player on some time
     * */
    public void applySpeedBoost(float newSpeed, long durationMillis) {
        this.speed = newSpeed;
        this.speedBoostEndTime = System.currentTimeMillis() + durationMillis;
        this.hasSpeedBoost = true;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Camera getCamera() {
        return camera;
    }

    public Vector3f getCameraPosition() {
        return camera.getPosition();
    }

    public float getHealth() {
        return health;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isHasSpeedBoost() {
        return hasSpeedBoost;
    }

    public Vector3f getSize() {
        return size;
    }

    public boolean isPlayerHasEnergyCrystal() {
        return playerHasEnergyCrystal;
    }

    public void setPlayerHasEnergyCrystal(boolean playerHasEnergyCrystal) {
        this.playerHasEnergyCrystal = playerHasEnergyCrystal;
    }
}
