package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;

public class AnimationComponent implements Component, Pool.Poolable {
    public IntMap<Animation<TextureRegion>> animations = new IntMap<>();
    public float time = 0f;
    public int state = 0;
    public boolean play = true;

    @Override
    public void reset() {
        time = 0f;
        state = 0;
        play = true;
    }
}
