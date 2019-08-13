package com.crown.prince.components;

import com.badlogic.ashley.core.Component;

public class PhysicsComponent implements Component {
    public float accX = 0;
    public float accY = 0;
    public float velX = 0;
    public float velY = 0;
    public float oldX = 0;
    public float oldY = 0;
    public float friction = 0.7f; // default for all
    public float gravity = 20f; // default for all
    public int maxVelY = 440; // default for all
}
