package com.keras1n.core.entity;

import org.joml.Vector3f;

import java.util.List;

public class Enemy extends Entity {
    private float damage;
    private float health;

    private float attackTimer = 0f;
    private final float attackCooldown = 1f;

    private float speed = 3f;


    public Enemy(MultiMaterialModel model, String modelPath, Vector3f pos, Vector3f rot, float scale, float health, float damage) {
        super(model, modelPath, pos, rot, scale, false);
        this.damage = damage;
        this.health = health;
    }

    public void update(Player player, float deltaTime, List<Entity> allEntities) {
        attackTimer += deltaTime;

        Vector3f toPlayer = new Vector3f(player.getCameraPosition()).sub(getPos());
        float distance = toPlayer.length();
        if (distance < 0.01f) return;

        //float speed = 1.5f;

        // rotation
        toPlayer.normalize();
        float targetYaw = (float) Math.toDegrees(Math.atan2(toPlayer.x, toPlayer.z)) + 180f;
        getRotation().y = targetYaw;

        // separation force
        Vector3f separation = new Vector3f();

        for (Entity other : allEntities) {
            if (other == this || !(other instanceof Enemy)) continue;

            float dist = this.getPos().distance(other.getPos());
            if (dist < 2f && dist > 0.001f) {
                Vector3f away = new Vector3f(this.getPos()).sub(other.getPos());
                away.normalize().div(dist);
                separation.add(away);
            }
        }

        Vector3f finalDir = new Vector3f(toPlayer).add(separation).normalize();

        //movement
        getPos().fma(speed * deltaTime, finalDir);

        // attack if near
        if (distance <= 2f && attackTimer >= attackCooldown) {
            player.takeDamage(damage);
            attackTimer = 0f;
        }
    }

    public float getHealth() {
        return health;
    }

    public void takeDamage(float damage) {
        this.health -= damage;
    }

    public float getDamage() {
        return damage;
    }

    @Override
    public String getType() {
        return "Enemy";
    }
}
