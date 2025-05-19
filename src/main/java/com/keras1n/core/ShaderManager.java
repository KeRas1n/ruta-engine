package com.keras1n.core;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class that manages an OpenGL shader program.
 * Supports creating, compiling, linking, binding shaders and managing uniforms.
 */
public class ShaderManager {
    private final int programID;

    private int vertexShaderID, fragmentShaderID;

    private final Map<String, Integer> uniforms;

    /**
     * Creates a new shader program and initializes the uniform map.
     *
     * @throws Exception if the shader program could not be created
     */
    public ShaderManager() throws Exception {
        this.programID = GL20.glCreateProgram();
        if(programID == 0){
            throw new Exception("Could not create shader");
        }

        uniforms = new HashMap<>();
    }

    /**
     * Creates a uniform variable by its name and stores its location.
     *
     * @param uniformName the name of the uniform in the GLSL shader
     * @throws Exception if the uniform location cannot be found
     */
    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = GL20.glGetUniformLocation(programID, uniformName);
        if(uniformLocation < 0){
            throw new Exception("Could not find uniform: " + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    /**
     * Sets a Matrix4f value to a uniform.
     *
     * @param uniformName the name of the uniform
     * @param value the matrix to set
     */
    public void setUniform(String uniformName, Matrix4f value)  {
        try(MemoryStack stack = MemoryStack.stackPush()){
            GL20.glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    /**
     * Sets a Vector4f value to a uniform.
     *
     * @param uniformName the name of the uniform
     * @param value the vector to set
     */
    public void setUniform(String uniformName, Vector4f value){
        GL20.glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    /**
     * Sets a Vector3f value to a uniform.
     *
     * @param uniformName the name of the uniform
     * @param value the vector to set
     */
    public void setUniform(String uniformName, Vector3f value){
        GL20.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    /**
     * Sets a boolean value to a uniform (as float: 0.0 or 1.0).
     *
     * @param uniformName the name of the uniform
     * @param value the boolean value to set
     */
    public void setUniform(String uniformName, boolean value){
        float res = 0;

        if(value){
            res = 1.0f;
        }
        GL20.glUniform1f(uniforms.get(uniformName), res);
    }


    /**
     * Sets an integer value to a uniform.
     *
     * @param uniformName the name of the uniform
     * @param value the integer to set
     */
    public void setUniform(String uniformName, int value) {
        GL20.glUniform1i(uniforms.get(uniformName), value);
    }

    /**
     * Sets a float value to a uniform.
     *
     * @param uniformName the name of the uniform
     * @param value the float to set
     */
    public void setUniform(String uniformName, float value) {
        GL20.glUniform1f(uniforms.get(uniformName), value);
    }


    /**
     * Compiles and attaches a vertex shader from source code.
     *
     * @param shaderCode GLSL source code for the vertex shader
     * @throws Exception if shader compilation fails
     */
    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderID = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }
    /**
     * Compiles and attaches a fragment shader from source code.
     *
     * @param shaderCode GLSL source code for the fragment shader
     * @throws Exception if shader compilation fails
     */
    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderID = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }


    /**
     * Creates, compiles, and attaches a shader of the given type.
     *
     * @param shaderCode the GLSL shader code
     * @param shaderType the type (GL_VERTEX_SHADER or GL_FRAGMENT_SHADER)
     * @return the shader ID
     * @throws Exception if shader creation or compilation fails
     */
    public int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderID = GL20.glCreateShader(shaderType);
        if(shaderID == 0){
            throw new Exception("Could not create shader. Type : " + shaderType);
        }
        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0){
            throw new Exception("Could not compile shader. Type : " + shaderType
            + ". Info : " + GL20.glGetShaderInfoLog(shaderID, 1024));
        }

        GL20.glAttachShader(programID, shaderID);

        return shaderID;
    }

    /**
     * Links the shader program and validates it.
     *
     * @throws Exception if linking or validation fails
     */
    public void link() throws Exception {
        GL20.glLinkProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0){
            throw new Exception("Could not link shader code "
                    + ". Info : " + GL20.glGetProgramInfoLog(programID, 1024));
        }
        if(vertexShaderID != 0){
            GL20.glDetachShader(programID, vertexShaderID);
        }
        if(fragmentShaderID != 0){
            GL20.glDetachShader(programID, fragmentShaderID);
        }

        GL20.glValidateProgram(programID);
        if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0){
            throw new Exception("Could not validate shader code " + GL20.glGetProgramInfoLog(programID, 1024));
        }
    }

    /**
     * Activates (binds) this shader program for rendering.
     */
    public void bind(){
        GL20.glUseProgram(programID);
    }
    /**
     * Deactivates any current shader program.
     */
    public void unbind(){
        GL20.glUseProgram(0);
    }
    /**
     * Cleans up shader resources and deletes the program.
     */
    public void cleanup(){
        unbind();
        if(programID != 0){
            GL20.glDeleteProgram(programID);
        }
    }
}
