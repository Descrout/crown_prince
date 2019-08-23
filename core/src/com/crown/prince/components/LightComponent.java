package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

public class LightComponent implements Component, Pool.Poolable {
    public Vector3 color = null;
    public float power = 0f;
    public boolean dynamic = false;
    public boolean sentOnce = false;

    public void set(Vector3 color,float power,boolean dynamic){
        this.color = color;
        this.power = power;
        this.dynamic = dynamic;
    }

    @Override
    public void reset() {
        power = 0f;
        color = null;
        dynamic = false;
        sentOnce = false;
    }
}
