package com.keras1n.core;

import com.keras1n.core.entity.*;
import com.keras1n.core.utils.Utils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class ObjectLoader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();


    public Entity createFlatTerrain(float size, float y) {
        float half = size / 2f;

        float[] positions = {
                -half, y, -half,
                half, y, -half,
                half, y,  half,
                -half, y,  half
        };

        float[] texCoords = {
                0, 0,
                1, 0,
                1, 1,
                0, 1
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        MultiMaterialModel model = loadOBJModel(positions, texCoords, indices);
        return new Entity(model, new Vector3f(0, 0, 0), new Vector3f(), 1f);
    }

    public MultiMaterialModel loadOBJModel(String objPath) throws Exception {
        List<String> lines = Utils.readAllLines(objPath);
        Map<String, Material> materials = new HashMap<>();

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> texCoords = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();

        Map<String, List<String>> materialToFaces = new HashMap<>();
        String currentMaterial = null;
        String mtlFile = null;

        for (String line : lines) {
            if (line.startsWith("mtllib ")) {
                mtlFile = line.split("\\s+")[1];
                materials = loadMTLFile("models/" + mtlFile);
            } else if (line.startsWith("usemtl ")) {
                currentMaterial = line.split("\\s+")[1];
            } else if (line.startsWith("v ")) {
                String[] tokens = line.split("\\s+");
                vertices.add(new Vector3f(
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]),
                        Float.parseFloat(tokens[3])));
            } else if (line.startsWith("vt ")) {
                String[] tokens = line.split("\\s+");
                texCoords.add(new Vector2f(
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2])));
            } else if (line.startsWith("vn ")) {
                String[] tokens = line.split("\\s+");
                normals.add(new Vector3f(
                        Float.parseFloat(tokens[1]),
                        Float.parseFloat(tokens[2]),
                        Float.parseFloat(tokens[3])));
            } else if (line.startsWith("f ")) {
                if (currentMaterial == null) continue;
                materialToFaces.computeIfAbsent(currentMaterial, k -> new ArrayList<>()).add(line);
            }
        }

        MultiMaterialModel multiModel = new MultiMaterialModel();

        for (String material : materialToFaces.keySet()) {
            List<String> faces = materialToFaces.get(material);
            Model model = buildModelFromFaces(faces, vertices, texCoords, normals);

            if (materials.containsKey(material)) {
                Material mat = materials.get(material);
                int texId = loadTexture("textures/" + mat.getTexturePath());
                model.setTexture(new Texture(texId));
            }

            multiModel.add(model);
        }

        return multiModel;
    }
    public MultiMaterialModel loadOBJModel(float[] vertices, float[] textureCoords, int[] indices) {
        Model model = loadModel(vertices, textureCoords, indices);
        MultiMaterialModel multiModel = new MultiMaterialModel();
        multiModel.add(model);
        return multiModel;
    }

    private Model buildModelFromFaces(List<String> faceLines, List<Vector3f> vertices,
                                      List<Vector2f> texCoords, List<Vector3f> normals) {
        Map<String, Integer> uniqueVertices = new HashMap<>();
        List<Float> finalVertices = new ArrayList<>();
        List<Float> finalTexCoords = new ArrayList<>();
        List<Float> finalNormals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        for (String faceLine : faceLines) {
            String[] tokens = faceLine.split("\\s+");
            for (int i = 1; i <= 3; i++) {
                String[] parts = tokens[i].split("/");
                int vIdx = Integer.parseInt(parts[0]) - 1;
                int vtIdx = parts.length > 1 && !parts[1].isEmpty() ? Integer.parseInt(parts[1]) - 1 : -1;
                int vnIdx = parts.length > 2 ? Integer.parseInt(parts[2]) - 1 : -1;

                String key = vIdx + "/" + vtIdx + "/" + vnIdx;

                if (!uniqueVertices.containsKey(key)) {
                    Vector3f pos = vertices.get(vIdx);
                    finalVertices.add(pos.x);
                    finalVertices.add(pos.y);
                    finalVertices.add(pos.z);

                    if (vtIdx >= 0) {
                        Vector2f tex = texCoords.get(vtIdx);
                        finalTexCoords.add(tex.x);
                        finalTexCoords.add(1 - tex.y);
                    } else {
                        finalTexCoords.add(0f);
                        finalTexCoords.add(0f);
                    }

                    if (vnIdx >= 0) {
                        Vector3f norm = normals.get(vnIdx);
                        finalNormals.add(norm.x);
                        finalNormals.add(norm.y);
                        finalNormals.add(norm.z);
                    } else {
                        finalNormals.add(0f);
                        finalNormals.add(0f);
                        finalNormals.add(0f);
                    }

                    uniqueVertices.put(key, uniqueVertices.size());
                }

                indices.add(uniqueVertices.get(key));
            }
        }

        float[] verticesArr = listToArray(finalVertices);
        float[] texCoordsArr = listToArray(finalTexCoords);
        int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();

        return loadModel(verticesArr, texCoordsArr, indicesArr);
    }
    private float[] listToArray(List<Float> list) {
        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    public Model loadModel(float[] vertices, float[] textureCoords, int[] indices) {
        int id = createVao();
        storeIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 2, textureCoords);
        unbind();
        return new Model(id, indices.length);
    }

    public Map<String, Material> loadMTLFile(String mtlPath) throws Exception {
        List<String> lines = Utils.readAllLines(mtlPath);
        Map<String, Material> materialMap = new HashMap<>();

        String currentName = null;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] tokens = line.split("\\s+");
            switch (tokens[0]) {
                case "newmtl":
                    currentName = tokens[1];
                    break;
                case "map_Kd":
                    if (currentName != null) {
                        String texturePath = tokens[1];
                        int textureId = loadTexture("textures/" + texturePath);
                        Texture texture = new Texture(textureId);
                        Material material = new Material(currentName, texturePath, texture);
                        materialMap.put(currentName, material);
                    }
                    break;
            }
        }

        return materialMap;
    }

    public int loadTexture(String filename) throws Exception {
        int width, height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if (buffer == null) {
                throw new Exception("Image file " + filename + " not loaded! " + STBImage.stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }

    private int createVao() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attributeNum, int vertexCount, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNum, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL30.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }
}
