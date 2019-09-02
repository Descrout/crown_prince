package com.crown.prince;

import com.badlogic.ashley.core.ComponentMapper;
import com.crown.prince.components.*;

public final class Mappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<AnimationComponent> anim = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);
    public static final ComponentMapper<PhysicsComponent> physics = ComponentMapper.getFor(PhysicsComponent.class);
    public static final ComponentMapper<CollideComponent> collide = ComponentMapper.getFor(CollideComponent.class);
    public static final ComponentMapper<BoundsComponent> bounds = ComponentMapper.getFor(BoundsComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<ScaleComponent> scale = ComponentMapper.getFor(ScaleComponent.class);
    public static final ComponentMapper<MoverComponent> mover = ComponentMapper.getFor(MoverComponent.class);
    //public static final ComponentMapper<ColliderComponent> collider = ComponentMapper.getFor(ColliderComponent.class);
    public static final ComponentMapper<LightComponent> light = ComponentMapper.getFor(LightComponent.class);
    public static final ComponentMapper<DamageComponent> damage = ComponentMapper.getFor(DamageComponent.class);
    public static final ComponentMapper<HealthComponent> health = ComponentMapper.getFor(HealthComponent.class);

}
