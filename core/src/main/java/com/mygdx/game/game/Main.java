package com.mygdx.game.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Main {
    private static final int MIN_WIDTH = 1670;
    private static final int MIN_HEIGHT = 1020;

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setResizable(true);
        config.setTitle("Pokemon Crystal");
        config.useVsync(true);
        config.setWindowSizeLimits(MIN_WIDTH, MIN_HEIGHT, -1, -1);
        new Lwjgl3Application(new GameClass(), config);
    }
}
