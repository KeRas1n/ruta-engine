package com.keras1n.test;

import com.keras1n.core.EngineManager;
import com.keras1n.core.WindowManager;
import com.keras1n.core.utils.Constants;
import com.keras1n.core.utils.LoggerConfig;

import java.util.Arrays;
import java.util.logging.Logger;


public class Launcher {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private static WindowManager window;
    private static TestGame game;

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }

    public static void main(String[] args){
        LoggerConfig.configureLogging(Arrays.asList(args).contains("--log"));
        logger.info("Game is launching...");

        window = new WindowManager(1024, 0, Constants.TITLE);
        window.init();
        game = new TestGame();
        EngineManager engine = EngineManager.getInstance();

        try {
            engine.start();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
