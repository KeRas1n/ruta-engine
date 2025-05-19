package com.keras1n.core;

import com.keras1n.core.entity.Model;
import com.keras1n.core.entity.Entity;
import com.keras1n.core.utils.Transformation;
import com.keras1n.core.utils.Utils;
import com.keras1n.test.Launcher;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import static com.keras1n.core.utils.Transformation.createTransformationMatrix;


/**
 * Manages the rendering process of entities using OpenGL.
 * Responsible for initializing shaders, setting up uniforms,
 * and rendering textured 3D models to the screen.
 */
public class RenderManager {
    private final WindowManager window;
    private ShaderManager shader;


    /**
     * Constructs the RenderManager and links it with the current window instance.
     */
    public RenderManager() {
        window = Launcher.getWindow();
    }

    /**
     * Initializes the rendering system by compiling shaders and creating uniforms.
     *
     * @throws Exception if shader creation or linking fails
     */
    public void init() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        shader.link();
        shader.createUniform("textureSampler");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
    }

    /**
     * Renders the given entity using its transformation (position, rotation, scale)
     * and the specified camera view.
     *
     * @param entity The entity to render
     * @param camera The camera providing the view matrix
     */
    public void render(Entity entity, Camera camera) {
        Matrix4f defaultTransform = createTransformationMatrix(entity.getPos(), entity.getRotation(), entity.getScale());
        render(entity, camera, defaultTransform);
    }

    /**
     * Renders the given entity using a custom transformation matrix and camera.
     * Useful for advanced control over transformations.
     *
     * @param entity The entity to render
     * @param camera The camera providing the view matrix
     * @param transformationMatrix The transformation matrix to apply to the model
     */
    public void render(Entity entity, Camera camera, Matrix4f transformationMatrix) {
        shader.bind();
        shader.setUniform("textureSampler", 0);
        shader.setUniform("projectionMatrix", window.getProjectionMatrix());
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));

        for (Model model : entity.getModel().getSubmodels()) {
            shader.setUniform("transformationMatrix", transformationMatrix);

            GL30.glBindVertexArray(model.getId());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
            GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL30.glBindVertexArray(0);
        }
    }
    /**
     * Clears the color and depth buffers to prepare for rendering a new frame.
     */
    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
    /**
     * Cleans up the shader resources after the rendering system is no longer needed.
     */
    public void cleanup() {
        shader.cleanup();
    }
}
