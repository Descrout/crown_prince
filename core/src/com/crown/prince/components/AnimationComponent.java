package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;

public class AnimationComponent implements Component {
    public IntMap<Animation<TextureRegion>> animations = new IntMap<>();
    public float time = 0f;
    public int state = 0;
}
