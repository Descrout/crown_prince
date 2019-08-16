package com.crown.prince.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.crown.prince.Mappers;
import com.crown.prince.components.MoverComponent;
import com.crown.prince.components.PhysicsComponent;

public class MoverSystem extends IteratingSystem {
    public MoverSystem(){
        super(Family.all(PhysicsComponent.class, MoverComponent.class).get());
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MoverComponent mover = Mappers.mover.get(entity);
        PhysicsComponent physics = Mappers.physics.get(entity);

        if(mover.velX != 0) {
            mover.timerX += deltaTime;
            if (mover.timerX > mover.changeX) {
                mover.velX *= -1;
                physics.velX = mover.velX;
                mover.timerX  = 0;
            }
        }

        if(mover.velY != 0) {
            mover.timerY += deltaTime;
            if (mover.timerY > mover.changeY) {
                mover.velY *= -1;
                physics.velY = mover.velY;
                mover.timerY = 0;
            }
        }
    }
}
