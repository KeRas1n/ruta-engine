package com.keras1n.core;

import org.joml.Vector3f;

public class Camera {

    private Vector3f position, rotation;

    public Camera() {
        position = new Vector3f(0,0,0);
        rotation = new Vector3f(0,0,0);
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void movePosition(float x, float y, float z) {
        if(z!=0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if(x!=0){
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }
        position.y += y;
    }
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void moveRotation(float x, float y, float z) {
        System.out.println(" rotationY: " + rotation.y + "  +: " + y);
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;

        if(rotation.x > 89) rotation.x = 89;
        if(rotation.x < -89) rotation.x = -89;
        /*if(rotation.y < -89) rotation.y = -89;
        if(rotation.y > 89) rotation.y = 89;*/

        /*rotation.y = rotation.y % 360;
        rotation.x = rotation.x % 360;
        rotation.z = rotation.z % 360;*/
        //rotation.y = ((rotation.y % 360) + 360) % 360;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getForwardVector() {
        float pitch = (float) Math.toRadians(getRotation().x);
        float yaw = (float) Math.toRadians(getRotation().y);

        float x = (float) (Math.cos(pitch) * Math.sin(yaw));
        float y = (float) (Math.sin(pitch));
        float z = (float) (-Math.cos(pitch) * Math.cos(yaw));

        return new Vector3f(x, y, z).normalize();
    }
}
