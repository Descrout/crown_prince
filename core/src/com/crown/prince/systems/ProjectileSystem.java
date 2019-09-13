package com.crown.prince.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.crown.prince.CameraManager;
import com.crown.prince.Mappers;
import com.crown.prince.components.BoundsComponent;
import com.crown.prince.components.CollideComponent;
import com.crown.prince.components.PositionComponent;
import com.crown.prince.components.ProjectileComponent;

public class ProjectileSystem extends IteratingSystem {
    private PooledEngine engine;
    private CameraManager cameraManager;

    public ProjectileSystem(CameraManager cameraManager) {
        super(Family.all(ProjectileComponent.class, CollideComponent.class, PositionComponent.class, BoundsComponent.class).get());
        this.cameraManager = cameraManager;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = (PooledEngine) engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollideComponent collide = Mappers.collide.get(entity);
        PositionComponent position = Mappers.position.get(entity);
        BoundsComponent bounds = Mappers.bounds.get(entity);

        if(!cameraManager.frustm(position,bounds))
            engine.removeEntity(entity);
    }
}
