package com.crown.prince;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.crown.prince.components.*;
import com.crown.prince.interfaces.HealthListener;
import com.crown.prince.systems.CollisionSystem;
import com.crown.prince.systems.PlayerSystem;
import com.crown.prince.systems.RenderSystem;

public class World {

    private PooledEngine engine;
    private CameraManager cameraManager;
    private Assets assets;

    private MapLoader mapLoader;
    public TileMap map;


    public static int width = 0;
    public static int height = 0;

    public static int tileNumX = 0;
    public static int tileNumY = 0;

    public World(PooledEngine engine, CameraManager cameraManager, Assets assets){
        this.engine = engine;
        this.cameraManager = cameraManager;
        this.assets = assets;
        mapLoader = new MapLoader(this);
        map = new TileMap(assets.tiles);
    }

    public void create(){
        loadMap("test");
        createTestEnemy();
    }

    public void loadMap(String name){
        engine.removeAllEntities();
        mapLoader.load(name);

        engine.getSystem(RenderSystem.class).setTileMap(map);
        engine.getSystem(CollisionSystem.class).grid = mapLoader.createCollisionGrid("collision");

    }


    public void createPlatform(int x,int y,int w,int h,int velX,int velY,int changeX,int changeY){
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

        texture.region = new TextureRegion(assets.platform);
        //texture.region.setRegion(0,0,32,32);

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

    public void createTestEnemy(){
        Entity entity = engine.createEntity();

        PositionComponent position = engine.createComponent(PositionComponent.class);
        PhysicsComponent physics = engine.createComponent(PhysicsComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        CollideComponent collide = engine.createComponent(CollideComponent.class);
        HealthComponent health = engine.createComponent(HealthComponent.class);
        TextureComponent tex = engine.createComponent(TextureComponent.class);

        bounds.setBounds(32,32);

        collide.init(bounds.w, bounds.h);
        position.x = physics.oldX = 200;
        position.y = physics.oldY = 300;

        physics.friction = 0.8f;

        health.healthListener = new HealthListener() {
            @Override
            public void hit() {
                System.out.println("enemy got hit");
            }

            @Override
            public void die() {
                engine.removeEntity(entity);
            }
        };

        entity.add(position);
        entity.add(physics);
        entity.add(bounds);
        entity.add(collide);
        entity.add(health);
        entity.add(tex);

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
        HealthComponent health = engine.createComponent(HealthComponent.class);

        //scale değerlerini gir ve player systeminde işlemleri yap

        scale.drawWidth = assets.adventurerIdling.getKeyFrame(0f).getRegionWidth() +20;
        scale.drawHeight = assets.adventurerIdling.getKeyFrame(0f).getRegionHeight() +20;

        bounds.setBounds(Constants.playerBoundW,Constants.playerBoundH);

        collide.init(bounds.w, bounds.h);
        collide.collideWithPlatform = true;

        animation.animations.put(PlayerComponent.RUN, assets.adventurerRun);
        animation.animations.put(PlayerComponent.IDLE, assets.adventurerIdling);
        animation.animations.put(PlayerComponent.JUMP, assets.adventurerJump);
        animation.animations.put(PlayerComponent.FALL, assets.adventurerFall);
        animation.animations.put(PlayerComponent.SLIDE,assets.adventurerWallSlide);
        animation.animations.put(PlayerComponent.CORNER_GRAB,assets.adventurerCornerGrab);
        animation.animations.put(PlayerComponent.CROUCH,assets.adventurerCrouch);
        animation.animations.put(PlayerComponent.DIE,assets.adventurerDie);
        animation.animations.put(PlayerComponent.ATTACK1,assets.adventurerAttack1);
        animation.animations.put(PlayerComponent.ATTACK2,assets.adventurerAttack2);
        animation.animations.put(PlayerComponent.ATTACK3,assets.adventurerAttack3);
        animation.state = PlayerComponent.IDLE;

        position.x = physics.oldX = x;
        position.y = physics.oldY = y;


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
        entity.add(health);

        engine.addEntity(entity);

        engine.getSystem(PlayerSystem.class).setPlayer(entity);

        cameraManager.setTarget(position,true);
    }




}
