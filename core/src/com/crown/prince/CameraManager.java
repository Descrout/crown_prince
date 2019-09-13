package com.crown.prince;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.crown.prince.components.BoundsComponent;
import com.crown.prince.components.PositionComponent;

public class CameraManager {
    private OrthographicCamera camera;
    private PositionComponent target;
    private boolean lerp;

    public CameraManager() {
        camera = new OrthographicCamera();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setTarget(PositionComponent pos, boolean lerp) {
        target = pos;
        this.lerp = lerp;
    }

    public boolean frustm(PositionComponent pos, BoundsComponent bounds) {
        if (pos.x > camera.position.x + camera.viewportWidth / 2 ||
                pos.x + bounds.w < camera.position.x - camera.viewportWidth / 2 ||
                pos.y + bounds.h < camera.position.y - camera.viewportHeight / 2 ||
                pos.y > camera.position.y + camera.viewportHeight / 2) return false;

        return true;
    }


    public void update() {

        if (target != null) {
            if (lerp) {
                camera.position.x += ((target.x - camera.position.x) * 0.1f);
                camera.position.y += ((target.y - camera.position.y) * 0.1f);

            } else {
                camera.position.x = target.x;
                camera.position.y = target.y;
            }
        }
        /*
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) cam.camera.translate(-10,0);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) cam.camera.translate(10,0);
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) cam.camera.translate(0,10);
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) cam.camera.translate(0,-10);*/

        if (camera.position.x < Constants.ScreenW / 2) camera.position.x = Constants.ScreenW / 2;
        if (camera.position.y < Constants.ScreenH / 2) camera.position.y = Constants.ScreenH / 2;
        if (camera.position.x > World.width / 2) camera.position.x = World.width / 2;
        if (camera.position.y > World.height / 2) camera.position.y = World.height / 2;
        camera.update();
    }
}
