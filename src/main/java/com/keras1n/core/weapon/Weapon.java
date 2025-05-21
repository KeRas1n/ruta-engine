package com.keras1n.core.weapon;

import com.keras1n.core.Camera;
import com.keras1n.core.RenderManager;
import com.keras1n.core.entity.Enemy;
import com.keras1n.core.entity.Entity;
import com.keras1n.core.entity.MultiMaterialModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public class Weapon {
    protected int damage;
    protected float range;


    private final Entity entity;
    private final Vector3f offset;

    private Vector3f swayOffset = new Vector3f();
    private Vector3f prevCameraRot = new Vector3f();
    private float swayAmount = 0.2f;
    private float swaySmoothness = 1.0f;

    private final String modelPath;

    public Weapon(MultiMaterialModel model, String modelPath, Vector3f offset, float scale) {
        this.entity = new Entity(model, modelPath, new Vector3f(), new Vector3f(), scale, false);
        this.offset = offset;
        this.modelPath = modelPath;
    }


    public void attack() {
        // TODO ?
    }
    public int getDamage() { return damage; }
    public float getRange() { return range; }

    public void reload() {
        // TODO
    }



    public void render(Camera camera, RenderManager renderer) {

        Vector3f currentRot = new Vector3f(camera.getRotation());
        Vector3f deltaRot = new Vector3f(currentRot).sub(prevCameraRot);

        swayOffset.lerp(
                new Vector3f(
                        -deltaRot.y * swayAmount,  // по X — движение мышкой влево/вправо (yaw)
                        -deltaRot.x * swayAmount,  // по Y — вверх/вниз (pitch)
                        0
                ),
                swaySmoothness * 0.016f // примерно 60 FPS
        );

        prevCameraRot.set(currentRot);


        // world matrix avoid gimble lock finalltyyyyyy
        Matrix4f transform = new Matrix4f()
                .identity()
                .translate(camera.getPosition())
                .rotateY((float) Math.toRadians(-camera.getRotation().y + 180f))
                .rotateX((float) Math.toRadians(camera.getRotation().x))
                .translate(new Vector3f(offset).add(swayOffset))  // offset moves weapon leftright up down from camera
                .scale(entity.getScale());


        renderer.render(entity, camera, transform);
    }

    public Entity shootAndHit(Camera camera, List<Entity> targets) {
        Vector3f origin = new Vector3f(camera.getPosition());
        Vector3f direction = new Vector3f(camera.getForwardVector()).normalize();

        for (Entity entity : targets) {
            if (!(entity instanceof Enemy)) continue;

            if (intersects(origin, direction, entity)) {
                return entity;
            }
        }

        return null;
    }

    private boolean intersects(Vector3f rayOrigin, Vector3f rayDir, Entity entity) {
        Vector3f center = entity.getPos();
        float radius = 1.0f; //will change later

        Vector3f L = new Vector3f(center).sub(rayOrigin);
        float tca = L.dot(rayDir);
        if (tca < 0) return false;

        float d2 = L.dot(L) - tca * tca;
        return d2 <= radius * radius;
    }

    public String getModelPath() {
        return modelPath;
    }
}
