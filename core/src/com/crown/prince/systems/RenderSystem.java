package com.crown.prince.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.crown.prince.Mappers;
import com.crown.prince.components.PositionComponent;
import com.crown.prince.components.TextureComponent;

public class RenderSystem extends EntitySystem {

    private SpriteBatch batch;
    private OrthographicCamera cam;
    private ImmutableArray<Entity> entities;

    public RenderSystem(SpriteBatch batch, OrthographicCamera cam){
        this.batch = batch;
        this.cam = cam;
    }

    public void addedToEngine(Engine engine){
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, TextureComponent.class).get());
    }

    @Override
    public void update(float deltaTime){
        batch.setProjectionMatrix(cam.combined);

        batch.begin();

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);

            TextureComponent tex = Mappers.texture.get(entity);
            if(tex.region == null) continue;
            PositionComponent pos = Mappers.position.get(entity);

            batch.draw(tex.region,pos.x,pos.y);
        }

        batch.end();
    }

}
