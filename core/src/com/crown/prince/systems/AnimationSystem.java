package com.crown.prince.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.crown.prince.Mappers;
import com.crown.prince.components.AnimationComponent;
import com.crown.prince.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {
    public AnimationSystem() {
        super(Family.all(TextureComponent.class, AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TextureComponent tex = Mappers.texture.get(entity);
        AnimationComponent anim = Mappers.anim.get(entity);

        tex.region = anim.animations.get(anim.state).getKeyFrame(anim.time);

        if (anim.play)
            anim.time += deltaTime;
    }
}
