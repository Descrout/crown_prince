package com.crown.prince.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.crown.prince.Main;
import com.crown.prince.World;
import com.crown.prince.systems.*;

public class GameScreen implements com.badlogic.gdx.Screen {
    private Main game;
    private World world;
    PooledEngine engine;

    public GameScreen(Main game){
        this.game = game;

        engine = new PooledEngine();

        world = new World(engine,game.cam,game.assets);

        engine.addSystem(new AnimationSystem());
        engine.addSystem(new CameraSystem());
        engine.addSystem(new RenderSystem(game.batch,game.cam));
        engine.addSystem(new PhysicsSystem());
        engine.addSystem(new PlayerSystem());

        world.create();
    }

    @Override
    public void show() {

    }

    private void update(float delta){
        delta = Math.min(0.25f,delta);
        engine.update(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);
        //ui stuff
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
