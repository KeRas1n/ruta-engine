package com.keras1n.core;

import com.keras1n.core.entity.Entity;
import com.keras1n.core.entity.MultiMaterialModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.joml.Vector3f;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectLoaderTest {

    private ObjectLoader loader;

    @BeforeEach
    void setUp() {
        loader = new ObjectLoader();
    }

    @Test
    void testListToArrayConversion() throws Exception {
        var method = loader.getClass().getDeclaredMethod("listToArray", java.util.List.class);
        method.setAccessible(true);

        java.util.List<Float> floats = java.util.List.of(1f, 2f, 3f);
        float[] result = (float[]) method.invoke(loader, floats);

        assertArrayEquals(new float[]{1f, 2f, 3f}, result);
    }
}
