package com.crown.prince.screens;

import com.badlogic.ashley.core.PooledEngine;
import com.crown.prince.CameraManager;
import com.crown.prince.Main;
import com.crown.prince.World;
import com.crown.prince.systems.*;

public class GameScreen implements com.badlogic.gdx.Screen {
    private CameraManager cameraManager;
    private World world;
    PooledEngine engine;

    public GameScreen(Main game){
        this.cameraManager = game.cameraManager;

        engine = new PooledEngine();

        world = new World(engine,cameraManager,game.assets);

        FixedUpdate fixed = new FixedUpdate();

        engine.addSystem(new RenderSystem(game.batch,cameraManager.getCamera(),game.shapeRenderer));
        engine.addSystem(new AnimationSystem());
        fixed.addSystem(new PhysicsSystem());
        fixed.addSystem(new CollisionSystem());
        fixed.addSystem(new PlayerSystem());
        engine.addSystem(new MoverSystem());
        engine.addSystem(new DamageSystem());
        fixed.addSystem(new ProjectileSystem(cameraManager));
        //engine.addSystem(new LightSystem(game.batch));

        engine.addSystem(fixed);

        world.create();
    }

    @Override
    public void show() {

    }

    private void update(float delta){
        delta = Math.min(0.25f,delta);
        engine.update(delta);
        cameraManager.update();
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
