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
import com.crown.prince.World;
import com.crown.prince.components.LightComponent;
import com.crown.prince.components.PositionComponent;

public class LightSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ShaderProgram shader;
    public boolean reload;

    public LightSystem(SpriteBatch batch){
        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(Gdx.files.internal("shaders/lightVertex.glsl").readString(), Gdx.files.internal("shaders/lightFragment.glsl").readString());
        System.out.println(shader.isCompiled() ? "Shader Compiled." : shader.getLog());
        batch.setShader(shader);
        reload = false;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, LightComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        int num_lights = entities.size();
        shader.begin();
        if(!reload){
            shader.setUniformi("num_lights",num_lights);
            shader.setUniformf("world",World.width, World.height);
            reload = true;
        }
        for(int i = 0; i < num_lights; i++){
            Entity entity = entities.get(i);

            LightComponent lightComp = Mappers.light.get(entity);

            if(lightComp.sentOnce  && !lightComp.dynamic) continue;

            PositionComponent pos = Mappers.position.get(entity);

            String light = "lights["+i+"]";
            shader.setUniformf(light+".position",pos.x+10,pos.y+10);
            shader.setUniformf(light+".diffuse",lightComp.color);
            shader.setUniformf(light+".power",lightComp.power);
            lightComp.sentOnce = true;
        }

        shader.end();
    }

}
