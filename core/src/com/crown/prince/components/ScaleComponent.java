package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ScaleComponent implements Component, Pool.Poolable {
    public float scaleX = 1f;
    public float scaleY = 1f;
    public int drawWidth = 0;
    public int drawHeight = 0;
    public int drawX = 0;
    public int drawY = 0;

    @Override
    public void reset() {
        scaleX = 1f;
        scaleY = 1f;
        drawWidth = 0;
        drawHeight = 0;
        drawX = 0;
        drawY = 0;
    }
}
