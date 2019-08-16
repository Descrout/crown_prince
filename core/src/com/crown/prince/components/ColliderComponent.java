package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ColliderComponent implements Component, Pool.Poolable {
    public int allowCollision = 0;

    @Override
    public void reset() {
        allowCollision = 0;
    }
}
