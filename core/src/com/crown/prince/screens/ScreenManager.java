package com.crown.prince.screens;


import com.badlogic.gdx.utils.IntMap;
import com.crown.prince.Main;

public final class ScreenManager {

    private static ScreenManager instance = new ScreenManager();
    private Main game;
    private IntMap<com.badlogic.gdx.Screen> screens;

    private ScreenManager() {
        screens = new IntMap<com.badlogic.gdx.Screen>();
    }

    public static ScreenManager getInstance() {
        return instance;
    }

    public void initialize(Main game) {
        this.game = game;
    }

    public void show(Screen screen) {
        if (null == game) return;
        if (!screens.containsKey(screen.ordinal())) {
            screens.put(screen.ordinal(), screen.getScreenInstance(game));
        }
        game.setScreen(screens.get(screen.ordinal()));
    }

    public void dispose(Screen screen) {
        if (!screens.containsKey(screen.ordinal())) return;
        screens.remove(screen.ordinal()).dispose();
    }

    public void dispose() {
        for (com.badlogic.gdx.Screen screen : screens.values()) {
            screen.dispose();
        }
        screens.clear();
        instance = null;
    }
}
