package com.crown.prince;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.crown.prince.components.*;
import com.crown.prince.systems.PhysicsSystem;
import com.crown.prince.systems.PlayerSystem;
import com.crown.prince.systems.RenderSystem;

public class World {

    private PooledEngine engine;
    private OrthographicCamera cam;
    private Assets assets;

    private MapLoader mapLoader;
    public TileMap map;


    public static int width = 0;
    public static int height = 0;

    public static int tileNumX = 0;
    public static int tileNumY = 0;

    public World(PooledEngine engine, OrthographicCamera cam, Assets assets){
        this.engine = engine;
        this.cam = cam;
        this.assets = assets;
        mapLoader = new MapLoader(this);
        map = new TileMap(assets.tiles);

    }

    public void create(){
        loadMap("test");
    }

    public void loadMap(String name){
        engine.removeAllEntities();
        mapLoader.load(name);

        engine.getSystem(RenderSystem.class).setTileMap(map);
        engine.getSystem(PhysicsSystem.class).grid = mapLoader.createCollisionGrid("collision");

    }

    private void createCamera(Entity target) {
        Entity entity = engine.createEntity();

        CameraComponent camera = engine.createComponent(CameraComponent.class);
        camera.camera = this.cam;
        camera.target = target;
        camera.lerp = 0.1f;

        entity.add(camera);
        engine.addEntity(entity);
    }

    public void createPlatform(int x,int y,int w,int h,int velX,int velY,int changeX,int changeY,int allowCollision){
        Entity entity = engine.createEntity();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PhysicsComponent physics = engine.createComponent(PhysicsComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        ColliderComponent collider = engine.createComponent(ColliderComponent.class);
        ScaleComponent scale = engine.createComponent(ScaleComponent.class);
        MoverComponent mover = engine.createComponent(MoverComponent.class);

        position.x = x;
        position.y = y;
        bounds.setBounds(w,h);

        physics.velX = mover.velX = velX;
        physics.velY = mover.velY = velY;
        physics.gravity = 0;
        physics.friction = 1;

        mover.changeX = changeX;
        mover.changeY = changeY;
        collider.allowCollision = allowCollision;

        texture.region = new TextureRegion(assets.tiles);
        texture.region.setRegion((40*7)+5,0,35,40);

        scale.drawWidth = w;
        scale.drawHeight = h;


        entity.add(position);
        entity.add(texture);
        entity.add(physics);
        entity.add(bounds);
        entity.add(collider);
        entity.add(scale);
        entity.add(mover);

        engine.addEntity(entity);
    }

    public void createStaticLight(float x, float y,Vector3 color,float power){
        Entity entity = engine.createEntity();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        LightComponent light = engine.createComponent(LightComponent.class);

        position.x = x;
        position.y = y;

        light.set(color,power,false);

        entity.add(position);
        entity.add(light);

        engine.addEntity(entity);
    }

    public void createPlayer(float x, float y){
        Entity entity = engine.createEntity();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        PlayerComponent playerComponent = engine.createComponent(PlayerComponent.class);
        PhysicsComponent physics = engine.createComponent(PhysicsComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        CollideComponent collide = engine.createComponent(CollideComponent.class);
        ScaleComponent scale = engine.createComponent(ScaleComponent.class);
        LightComponent light = engine.createComponent(LightComponent.class);

        //scale değerlerini gir ve player systeminde işlemleri yap

        scale.drawWidth = assets.adventurerIdling.getKeyFrame(0f).getRegionWidth() +20;
        scale.drawHeight = assets.adventurerIdling.getKeyFrame(0f).getRegionHeight() +20;

        bounds.setBounds(26,39);

        collide.init(bounds.w, bounds.h);

        animation.animations.put(PlayerComponent.RUN, assets.adventurerRun);
        animation.animations.put(PlayerComponent.IDLE, assets.adventurerIdling);
        animation.animations.put(PlayerComponent.JUMP, assets.adventurerJump);
        animation.animations.put(PlayerComponent.FALL, assets.adventurerFall);
        animation.animations.put(PlayerComponent.SLIDE,assets.adventurerWallSlide);
        animation.state = PlayerComponent.IDLE;

        position.x = physics.oldX = x;
        position.y = physics.oldY = y;

        physics.friction = 0.9f;

        light.set(new Vector3(1f,1f,1f),70f,true);


        entity.add(position);
        entity.add(texture);
        entity.add(animation);
        entity.add(playerComponent);
        entity.add(physics);
        entity.add(bounds);
        entity.add(collide);
        entity.add(scale);
        entity.add(light);

        engine.addEntity(entity);

        engine.getSystem(PlayerSystem.class).setPlayer(entity);
        createCamera(entity);
    }




}
