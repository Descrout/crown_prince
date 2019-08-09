package com.crown.prince;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.crown.prince.components.AnimationComponent;
import com.crown.prince.components.CameraComponent;
import com.crown.prince.components.PositionComponent;
import com.crown.prince.components.TextureComponent;

public final class Mappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<AnimationComponent> anim = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);

}
