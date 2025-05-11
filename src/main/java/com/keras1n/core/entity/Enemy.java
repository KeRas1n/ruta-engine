package com.keras1n.core.entity;

import org.joml.Vector3f;

public class Enemy extends Entity {
    private float damage;


    public Enemy(MultiMaterialModel model, Vector3f pos, Vector3f rot, float scale) {
        super(model, pos, rot, scale);
        this.damage = 10f;
    }

    public void update(Player player, float deltaTime) {
        //                               player pos - enemy pos
        Vector3f toPlayer = new Vector3f(player.getPosition()).sub(getPos());
        float distance = toPlayer.length();
        if (distance < 0.01f) return;

        float speed = 1.5f;
        float rotationSpeed = 5.0f;



        //length is now 1
        toPlayer.normalize();

        // smooth rotation
        float targetYaw = (float) Math.toDegrees(Math.atan2(toPlayer.x, toPlayer.z)) + 180f;

        float currentYaw = getRotation().y;
        float deltaYaw = targetYaw - currentYaw;

        //normalise angle
        deltaYaw = (deltaYaw + 540f) % 360f - 180f;


        getRotation().y = targetYaw;

        //getRotation().y += deltaYaw * rotationSpeed * deltaTime;

        // moving forward (backward because drons are swapped for now)
        if (distance > 1.0f) {
            Vector3f forward = new Vector3f(
                    -(float) Math.sin(Math.toRadians(getRotation().y)),
                    0,
                    -(float) Math.cos(Math.toRadians(getRotation().y))
            );

            //fma - fast multiply-add   getPos() += forward * (speed * deltaTime);
            getPos().fma(speed * deltaTime, forward);
        } else {
            // attack player7?AS?D
            player.takeDamage(damage * deltaTime);
        }
    }


}
