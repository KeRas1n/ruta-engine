package com.keras1n.core;

import com.keras1n.core.entity.Entity;
import com.keras1n.core.entity.MultiMaterialModel;
import org.joml.Vector3f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LevelLoaderTest {

    private ObjectLoader mockLoader;
    private LevelLoader loader;

    @BeforeEach
    void setUp() {
        mockLoader = mock(ObjectLoader.class);
        loader = new LevelLoader(mockLoader);
    }

    @Test
    void testToVector3fConvertsCorrectly() throws Exception {
        var arr = new org.json.JSONArray("[1.5, -2.0, 0.25]");
        var vec = loader.getClass()
                .getDeclaredMethod("toVector3f", org.json.JSONArray.class)
                .invoke(loader, arr);

        assertEquals(new Vector3f(1.5f, -2.0f, 0.25f), vec);
    }

    @Test
    void testLoadEntitiesFromJsonCreatesEntities() throws Exception {
        // prepare mock model
        MultiMaterialModel fakeModel = mock(MultiMaterialModel.class);
        when(mockLoader.loadOBJModel(anyString())).thenReturn(fakeModel);

        // temp jspn file
        String json = """
            {
              "entities": [
                {
                  "model": "models/tree.obj",
                  "type": "Object",
                  "position": [1.0, 2.0, 3.0],
                  "rotation": [0.0, 90.0, 0.0],
                  "scale": 1.5
                },
                {
                  "model": "models/tree.obj",
                  "type": "Object",
                  "position": [4.0, 5.0, 6.0],
                  "rotation": [0.0, 0.0, 0.0],
                  "scale": 1.0
                }
              ]
            }
            """;

        Path tempFile = Files.createTempFile("test-level", ".json");
        Files.writeString(tempFile, json);

        //load
        List<Entity> entities = loader.loadEntitiesFromJson(tempFile.toString());

        assertEquals(2, entities.size(), "Should load 2 entities");
        verify(mockLoader, times(1)).loadOBJModel("models/tree.obj");
    }

    @Test
    void testThrowsOnInvalidJson() {
        assertThrows(Exception.class, () -> loader.loadEntitiesFromJson("nonexistent-file.json"));
    }
}
