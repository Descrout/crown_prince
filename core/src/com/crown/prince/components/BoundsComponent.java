package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BoundsComponent implements Component, Pool.Poolable {
    public int w = 0;
    public int h = 0;

    public void setBounds(int w, int h){
        this.w = w;
        this.h = h;
    }

    @Override
    public void reset() {
        w = 0;
        h = 0;
    }
}
