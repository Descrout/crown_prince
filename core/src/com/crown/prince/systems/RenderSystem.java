package com.crown.prince.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crown.prince.components.PositionComponent;
import com.crown.prince.components.TextureComponent;

public class RenderSystem extends IteratingSystem {

    private SpriteBatch batch;

    public RenderSystem(SpriteBatch batch){
        super(Family.all(PositionComponent.class, TextureComponent.class).get());

        this.batch = batch;

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
