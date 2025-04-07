package com.keras1n.core.utils;

import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utils {
    public static FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static String loadResource(String fileName) throws Exception {
        String result;
        try(InputStream in = Utils.class.getResourceAsStream(fileName);
        Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }

        return result;
    }

    public static List<String> readAllLines(String filename) {
        List<String> list = new ArrayList<>();

        // Убедимся, что путь начинается без /
        if (filename.startsWith("/")) {
            filename = filename.substring(1);
        }

        InputStream in = Utils.class.getClassLoader().getResourceAsStream(filename);
        if (in == null) {
            throw new RuntimeException("Resource not found: " + filename);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resource: " + filename, e);
        }

        return list;
    }

}
