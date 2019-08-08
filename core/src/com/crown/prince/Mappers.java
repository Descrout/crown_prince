package com.crown.prince;

import com.badlogic.ashley.core.ComponentMapper;
import com.crown.prince.components.PositionComponent;
import com.crown.prince.components.TextureComponent;

public final class Mappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);

}
