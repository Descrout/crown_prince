package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.crown.prince.Constants;

public class PhysicsComponent implements Component, Pool.Poolable {
    public float accX = 0;
    public float accY = 0;
    public float velX = 0;
    public float velY = 0;
    public float oldX = 0;
    public float oldY = 0;
    public float elasticity = 0;
    public float friction = Constants.globalFriction;
    public float gravity = Constants.globalGravity;
    public int maxVelY = Constants.globalMaxVelY;
    public boolean controlled = false;
    public boolean onPlatform = false;

    @Override
    public void reset() {
        accX = 0;
        accY = 0;
        velX = 0;
        velY = 0;
        oldX = 0;
        oldY = 0;
        elasticity = 0;
        controlled = false;
        onPlatform = false;
    }
}
