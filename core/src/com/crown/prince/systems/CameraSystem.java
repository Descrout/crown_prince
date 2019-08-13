package com.crown.prince.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.crown.prince.Mappers;
import com.crown.prince.components.CameraComponent;
import com.crown.prince.components.PositionComponent;

public class CameraSystem extends IteratingSystem {
    public CameraSystem(){
        super(Family.all(CameraComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraComponent cam = Mappers.camera.get(entity);

        /*
        if(cam.target == null) return;

        PositionComponent target = Mappers.position.get(cam.target);

        if(target == null) return;

        if(cam.lerp == 0f){
            cam.camera.position.x = target.x;
            cam.camera.position.y = target.y;
        }else{
            cam.camera.position.x += (target.x-cam.camera.position.x)*0.1f;
            cam.camera.position.y += (target.y-cam.camera.position.y)*0.1f;
        }*/
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) cam.camera.translate(-10,0);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) cam.camera.translate(10,0);
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) cam.camera.translate(0,10);
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) cam.camera.translate(0,-10);
        cam.camera.update();
    }
}
