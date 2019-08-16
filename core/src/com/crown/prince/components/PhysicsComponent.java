package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PhysicsComponent implements Component, Pool.Poolable {
    public float accX = 0;
    public float accY = 0;
    public float velX = 0;
    public float velY = 0;
    public float oldX = 0;
    public float oldY = 0;
    public float friction = 0.7f; // default for all
    public float gravity = 20f; // default for all
    public int maxVelY = 440; // default for all
    public boolean controlled = false;

    @Override
    public void reset() {
        accX = 0;
        accY = 0;
        velX = 0;
        velY = 0;
        oldX = 0;
        oldY = 0;
        friction = 0.7f;
        gravity = 20f;
        maxVelY = 440;
        controlled = false;
    }
}
