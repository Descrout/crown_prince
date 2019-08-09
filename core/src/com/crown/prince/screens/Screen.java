package com.crown.prince.screens;


import com.crown.prince.Main;

public enum Screen {

    INTRO {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance(Main game) {
            return new IntroScreen(game);
        }
    },

    MAIN_MENU {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance(Main game) {
            return new MainMenuScreen(game);
        }
    },

    GAME {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance(Main game) {
            return new GameScreen(game);
        }
    };

    protected abstract com.badlogic.gdx.Screen getScreenInstance(Main game);

}