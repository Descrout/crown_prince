package com.crown.prince.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.crown.prince.Mappers;
import com.crown.prince.components.LightComponent;
import com.crown.prince.components.PositionComponent;

public class LightSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ShaderProgram shader;

    public LightSystem(SpriteBatch batch){
        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(Gdx.files.internal("shaders/lightVertex.glsl").readString(), Gdx.files.internal("shaders/lightFragment.glsl").readString());
        System.out.println(shader.isCompiled() ? "Shader Compiled." : shader.getLog());
        batch.setShader(shader);
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, LightComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        int num_lights = entities.size();

        shader.setUniformi("num_lights",num_lights);

        for(int i = 0; i < num_lights; i++){
            Entity entity = entities.get(i);

            PositionComponent pos = Mappers.position.get(entity);
            String light = "lights["+i+"]";
            shader.setUniformf(light+".position",pos.x,pos.y);
            shader.setUniformf(light+".diffuse",1f,1f,1f);
            shader.setUniformf(light+".power",64f);
        }

    }

    public void setScreen(int width, int height){
        shader.setUniformf("screen",width,height);
    }
}
