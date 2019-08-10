package com.crown.prince.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.crown.prince.Mappers;
import com.crown.prince.components.PhysicsComponent;
import com.crown.prince.components.PositionComponent;

public class PhysicsSystem extends IntervalIteratingSystem {
    public static final float Fixed_Timestep = 1/50f;
    public PhysicsSystem(){
        super(Family.all(PositionComponent.class, PhysicsComponent.class).get(),Fixed_Timestep);
    }
    @Override
    protected void processEntity(Entity entity) {

        PositionComponent pos = Mappers.position.get(entity);
        PhysicsComponent physics = Mappers.physics.get(entity);

        physics.oldX = pos.x;
        physics.oldY = pos.y;

        physics.accY += physics.gravity;

        physics.velX *= physics.friction;
        physics.velX += physics.accX;
        physics.velY += physics.accY;
        pos.x += physics.velX * Fixed_Timestep;
        pos.y += physics.velY * Fixed_Timestep;
        physics.accX = 0;
        physics.accY = 0;
    }
}
