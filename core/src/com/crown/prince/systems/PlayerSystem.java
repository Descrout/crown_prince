package com.crown.prince.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.crown.prince.*;
import com.crown.prince.components.*;

public class PlayerSystem extends EntitySystem {
    Entity player;

    AnimationComponent anim;
    PhysicsComponent physics;
    PlayerComponent playerComponent;
    CollideComponent collide;
    ScaleComponent scale;
    LightComponent light;

    FSM brain;

    public PlayerSystem() {
        brain = new FSM(this::normalState);
    }

    private void normalState() {
        playerControl();
        animControl();
    }

    private void playerControl() {
        if (playerComponent.keyRight) {
            playerComponent.facingRight = true;
            physics.accX += Constants.playerSpeed;
        }
        if (playerComponent.keyLeft) {
            playerComponent.facingRight = false;
            physics.accX -= Constants.playerSpeed;
        }
        if (playerComponent.keyUp && playerComponent.canJump) {
            physics.accY += Constants.playerJump;
            anim.time = 0f;
        }

        playerComponent.sliding = false;
        if(physics.velY < 0){
            if(Calculation.isTouching(collide.touching,Touch.RIGHT_SIDE)){
                physics.velY *= 0.6;
                playerComponent.sliding = true;
                playerComponent.facingRight = true;
                if(playerComponent.keyUp){
                    physics.velY = Constants.playerJump;
                    physics.accX -= 650f;
                }
            }
            if(Calculation.isTouching(collide.touching,Touch.LEFT_SIDE)){
                physics.velY *= 0.6;
                playerComponent.sliding = true;
                playerComponent.facingRight = false;
                if(playerComponent.keyUp){
                    physics.velY = Constants.playerJump;
                    physics.accX += 650f;
                }
            }
        }

    }

    private void keyRegister() {
        physics.controlled = playerComponent.keyDown = playerComponent.keyLeft = playerComponent.keyRight = playerComponent.keyUp = false;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerComponent.keyLeft = true;
            physics.controlled = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerComponent.keyRight = true;
            physics.controlled = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerComponent.keyUp = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerComponent.keyDown = true;
        }
    }

    private void animControl() {
        if(playerComponent.sliding){
            anim.state = PlayerComponent.SLIDE;
        }else if (playerComponent.canJump) {
            if (physics.controlled) anim.state = PlayerComponent.RUN;
            else anim.state = PlayerComponent.IDLE;
        } else {
            if (physics.velY < 0) anim.state = PlayerComponent.FALL;
            else anim.state = PlayerComponent.JUMP;
        }

        if(playerComponent.facingRight){
            scale.scaleX = 1;
            scale.drawX = -20;
        }else{
            scale.scaleX = -1;
            scale.drawX = 48;
        }
    }

    @Override
    public void update(float deltaTime) {
        playerComponent.canJump = Calculation.isTouching(collide.touching, Touch.FLOOR);
        keyRegister();
        brain.update();
    }

    public void setPlayer(Entity player) {
        this.player = player;
        anim = Mappers.anim.get(player);
        physics = Mappers.physics.get(player);
        playerComponent = Mappers.player.get(player);
        collide = Mappers.collide.get(player);
        scale = Mappers.scale.get(player);
        light = Mappers.light.get(player);
    }
}
