package com.crown.prince.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.crown.prince.*;
import com.crown.prince.components.*;


public class PhysicsSystem extends IteratingSystem {
    public PhysicsSystem() {
        super(Family.all(PositionComponent.class, PhysicsComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = Mappers.position.get(entity);
        PhysicsComponent physics = Mappers.physics.get(entity);

        physics.oldX = pos.x;
        physics.oldY = pos.y;

        physics.accY -= physics.gravity;

        physics.velX *= physics.friction;

        physics.velX += physics.accX;
        physics.velY += physics.accY;


        if (physics.velY > physics.maxVelY) physics.velY = physics.maxVelY;
        if (physics.velY < -physics.maxVelY) physics.velY = -physics.maxVelY;


        pos.x += physics.velX * deltaTime;
        pos.y += physics.velY * deltaTime;

        physics.accX = 0;
        physics.accY = 0;

        ScaleComponent scale = Mappers.scale.get(entity);
        if (scale == null) return;
        scale.x = pos.x;
        scale.y = pos.y;
    }


}
