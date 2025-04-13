package com.keras1n.core.collision;

import org.joml.Vector3f;

public interface Collider {
    boolean collides(Vector3f point, float radius);
}
