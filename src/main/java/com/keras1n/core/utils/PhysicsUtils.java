package com.keras1n.core.utils;

import com.keras1n.core.entity.Entity;
import org.joml.Vector3f;

/**
 * Utility class for collision detection.
 */
public class PhysicsUtils {

    /**
     * Checks collision (AABB) between two entities based on position and collider size.
     *
     * @param a first entity
     * @param b second entity
     * @return true if intersecting, false otherwise
     */
    public static boolean checkAABBCollision(Entity a, Entity b) {
        Vector3f apos = a.getPos();
        Vector3f asize = a.getSize();
        Vector3f bpos = b.getPos();
        Vector3f bsize = b.getSize();

        return Math.abs(apos.x - bpos.x) < (asize.x + bsize.x) / 2f &&
                Math.abs(apos.z - bpos.z) < (asize.z + bsize.z) / 2f;
    }

    public static boolean checkInsideAxis(float posA, float sizeA, float posB, float sizeB) {
        return Math.abs(posA - posB) < (sizeA + sizeB) / 2f;
    }
}
