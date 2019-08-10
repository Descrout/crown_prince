package com.crown.prince;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.crown.prince.components.*;

public class World {

    private PooledEngine engine;
    private OrthographicCamera cam;
    private Assets assets;

    public World(PooledEngine engine, OrthographicCamera cam, Assets assets){
        this.engine = engine;
        this.cam = cam;
        this.assets = assets;
    }

    public void create(){
        Entity player = createPlayer(100f,100f);
        createCamera(player);
    }

    private void createCamera(Entity target) {
        Entity entity = engine.createEntity();

        CameraComponent camera = new CameraComponent();
        camera.camera = this.cam;
        camera.target = target;

        entity.add(camera);

        engine.addEntity(entity);
    }

    private Entity createPlayer(float x, float y){
        Entity entity = engine.createEntity();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        PhysicsComponent physics = engine.createComponent(PhysicsComponent.class);

        animation.animations.put(PlayerComponent.RUN, assets.adventurerRun);
        animation.animations.put(PlayerComponent.IDLE, assets.adventurerIdling);
        animation.animations.put(PlayerComponent.JUMP, assets.adventurerJump);
        animation.animations.put(PlayerComponent.FALL, assets.adventurerFall);
        animation.state = PlayerComponent.IDLE;

        position.x = physics.oldX = x;
        position.y = physics.oldY = y;

        entity.add(position);
        entity.add(texture);
        entity.add(animation);
        entity.add(playerComponent);
        entity.add(physics);

        engine.addEntity(entity);

        return entity;
    }
}
