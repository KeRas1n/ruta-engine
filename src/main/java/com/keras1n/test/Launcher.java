package com.keras1n.test;

import com.keras1n.core.EngineManager;
import com.keras1n.core.WindowManager;
import com.keras1n.core.utils.Constants;


public class Launcher {
    private static WindowManager window;
    private static TestGame game;

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }

    public static void main(String[] args){
        window = new WindowManager(800, 600, Constants.TITLE);
        game = new TestGame();
        EngineManager engine = new EngineManager();

        try {
            engine.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
