package com.crown.prince.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.crown.prince.Constants;
import com.crown.prince.Mappers;
import com.crown.prince.World;
import com.crown.prince.components.CameraComponent;
import com.crown.prince.components.PositionComponent;
import com.crown.prince.components.ScaleComponent;

public class CameraSystem extends IteratingSystem {
    public CameraSystem(){
        super(Family.all(CameraComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraComponent cam = Mappers.camera.get(entity);


        if(cam.target == null) return;

        PositionComponent target = Mappers.position.get(cam.target);

        if(target == null) return;

        if(cam.lerp == 0f){
            cam.camera.position.x = target.x;
            cam.camera.position.y = target.y;
        }else{
            cam.camera.position.x += ((target.x-cam.camera.position.x)*cam.lerp);
            cam.camera.position.y += ((target.y-cam.camera.position.y)*cam.lerp);
        }
        /*
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) cam.camera.translate(-10,0);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) cam.camera.translate(10,0);
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) cam.camera.translate(0,10);
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) cam.camera.translate(0,-10);*/

        if(cam.camera.position.x< Constants.ScreenW/2) cam.camera.position.x = Constants.ScreenW/2;
        if(cam.camera.position.y<Constants.ScreenH/2) cam.camera.position.y = Constants.ScreenH/2;
        if(cam.camera.position.x> World.width/2) cam.camera.position.x = World.width/2;
        if(cam.camera.position.y>World.height/2) cam.camera.position.y = World.height/2;
        cam.camera.update();
    }
}
