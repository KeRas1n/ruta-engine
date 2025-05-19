package com.keras1n.core;

import com.keras1n.core.entity.MultiMaterialModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*testing purpose*/
public class NewObjectLoader {

    public MultiMaterialModel load3dModel(String filename) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        String[] tokens = line.split("\\s+");
        while((line = br.readLine()) != null) {
            if(line.startsWith("v")) {
                float x = Float.parseFloat(line.split(" ")[1]);
                float x = Float.parseFloat(line.split(" ")[1]);
                float x = Float.parseFloat(line.split(" ")[1]);

            }
        }
    }
}
