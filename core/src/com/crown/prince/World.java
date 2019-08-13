package com.crown.prince;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.crown.prince.components.*;
import com.crown.prince.systems.CollisionSystem;
import com.crown.prince.systems.RenderSystem;

public class World {

    private PooledEngine engine;
    private OrthographicCamera cam;
    private Assets assets;

    private MapLoader mapLoader;
    private TileMap map;


    public static int width = 0;
    public static int height = 0;

    public static int tileNumX = 0;
    public static int tileNumY = 0;

    public World(PooledEngine engine, OrthographicCamera cam, Assets assets){
        this.engine = engine;
        this.cam = cam;
        this.assets = assets;
        mapLoader = new MapLoader();
    }

    public void create(){
        Entity player = createPlayer(100f,100f);

        createCamera(player);

        loadMap("test");
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
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        CollideComponent collide = engine.createComponent(CollideComponent.class);

        bounds.setBounds(26,39);

        collide.init(bounds.w, bounds.h);

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
        entity.add(bounds);
        entity.add(collide);

        engine.addEntity(entity);

        return entity;
    }



    public void loadMap(String name){
        map = new TileMap(assets.tiles);

        mapLoader.load(name, map);

        engine.getSystem(RenderSystem.class).setTileMap(map);
        engine.getSystem(CollisionSystem.class).grid = mapLoader.createCollisionGrid("collision");

    }
}
