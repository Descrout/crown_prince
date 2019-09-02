package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.crown.prince.interfaces.HealthListener;

public class HealthComponent implements Component, Pool.Poolable {
    public int maxHP = 20;
    public int HP = 20;
    public int knockbackResX = 1;
    public int knockbackResY = 1;
    public int id = 0;

    public HealthListener healthListener = null;

    @Override
    public void reset() {
        id = 0;
        HP = 0;
        maxHP = 20;
        healthListener = null;
        knockbackResX = 1;
        knockbackResY = 1;
    }
}
