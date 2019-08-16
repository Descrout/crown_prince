package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool;

public class CameraComponent implements Component, Pool.Poolable {
    public Entity target;
    public OrthographicCamera camera;
    public float lerp = 0f;

    @Override
    public void reset() {
        lerp = 0f;
    }
}
