package com.crown.prince.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.crown.prince.*;
import com.crown.prince.components.*;

public class PlayerSystem extends EntitySystem {
    Entity player;
    Entity damage;
    private Engine engine;

    AnimationComponent anim;
    PhysicsComponent physics;
    public PlayerComponent playerComponent;
    CollideComponent collide;
    BoundsComponent bounds;
    ScaleComponent scale;
    LightComponent light;
    PositionComponent pos;

    CollisionSystem collisionSystem;

    FSM brain;

    public PlayerSystem() {
        brain = new FSM(this::normalState);
        setProcessing(false);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        collisionSystem = engine.getSystem(CollisionSystem.class);
    }

    private void normalState() {
        canJumpController();
        playerControl();
        animControl();
    }

    private void slideState() {
        anim.state = PlayerComponent.SLIDE;

        physics.accX += (playerComponent.facingRight ? 20f : -20f);

        if (Utils.isTouching(collide.touching, Touch.FLOOR)) {
            brain.currentState = this::normalState;
            return;
        }
        if (!playerComponent.keyDown) physics.velY *= 0.6;

        if (playerComponent.keyUp) {
            physics.velY = Constants.playerJump;
            physics.accX += (playerComponent.facingRight ? -650f : 650f);
            brain.currentState = this::normalState;
            return;
        }

        if (playerComponent.hangTile != 1) {
            brain.currentState = this::normalState;
            return;
        }

        if (playerComponent.facingRight) {
            if (playerComponent.keyLeft) brain.currentState = this::normalState;
        } else if (playerComponent.keyRight) brain.currentState = this::normalState;


    }

    private void crouchState() {
        anim.state = PlayerComponent.CROUCH;

        if (!playerComponent.keyDown) {
            bounds.h = Constants.playerBoundH;
            brain.currentState = this::normalState;
        }
    }

    private void hangState() {
        anim.state = PlayerComponent.CORNER_GRAB;
        pos.y = physics.oldY;
        physics.velY = 0;
        if (playerComponent.keyDown) {
            brain.currentState = this::normalState;
            return;
        }
        if (playerComponent.keyUp) {
            physics.velY = Constants.playerJump;
            brain.currentState = this::normalState;
            return;
        }

        if (playerComponent.facingRight) {
            if (playerComponent.keyLeft) brain.currentState = this::normalState;
        } else if (playerComponent.keyRight) brain.currentState = this::normalState;
    }

    private void playerControl() {
        if (playerComponent.keyRight) {
            playerComponent.facingRight = true;
            physics.controlled = true;
            physics.accX += Constants.playerSpeed;
        }
        if (playerComponent.keyLeft) {
            playerComponent.facingRight = false;
            physics.controlled = true;
            physics.accX -= Constants.playerSpeed;
        }
        if (playerComponent.keyUp && playerComponent.canJump) {
            physics.accY += Constants.playerJump;
            anim.time = 0f;
            playerComponent.canJump = false;
        }

        if (!playerComponent.keyUp && physics.velY > 0) physics.velY *= 0.8;

        if (playerComponent.keyDown && playerComponent.canJump) {
            if(physics.onPlatform){
                pos.y -= 2;
                playerComponent.canJump = false;
            }else{
                brain.currentState = this::crouchState;
                bounds.h = Constants.playerCrouchH;
                return;
            }
        }

        slideHangControl();
    }

    private void slideHangControl() {
        if (physics.velY < 0) {
            boolean right = Utils.isTouching(collide.touching, Touch.RIGHT_SIDE);
            boolean left = Utils.isTouching(collide.touching, Touch.LEFT_SIDE);
            if (right) {
                if (playerComponent.hangTile == 1) {
                    if (playerComponent.willHang) {
                        playerComponent.willHang = false;
                        brain.currentState = this::hangState;
                    } else {
                        if (playerComponent.keyRight) brain.currentState = this::slideState;
                    }
                } else {
                    if (playerComponent.keyRight) playerComponent.willHang = true;
                    else playerComponent.willHang = false;
                }
            }

            if (left) {
                if (playerComponent.hangTile == 1) {
                    if (playerComponent.willHang) {
                        playerComponent.willHang = false;
                        brain.currentState = this::hangState;
                    } else {
                        if (playerComponent.keyLeft) brain.currentState = this::slideState;
                    }
                } else {
                    if (playerComponent.keyLeft) playerComponent.willHang = true;
                    else playerComponent.willHang = false;
                }
            }
        }
    }

    private void keyRegister() {
        physics.controlled = playerComponent.keyDown = playerComponent.keyLeft = playerComponent.keyRight = playerComponent.keyUp = false;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) playerComponent.keyLeft = true;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) playerComponent.keyRight = true;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) playerComponent.keyUp = true;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) playerComponent.keyDown = true;
    }

    private void animControl() {
        if (playerComponent.canJump) {
            if (playerComponent.keyRight == playerComponent.keyLeft) anim.state = PlayerComponent.IDLE;
            else anim.state = PlayerComponent.RUN;
        } else {
            if (physics.velY < 0) anim.state = PlayerComponent.FALL;
            else anim.state = PlayerComponent.JUMP;
        }
    }

    private void facingControl() {
        if (playerComponent.facingRight) {
            scale.scaleX = 1;
            scale.drawX = -20;
        } else {
            scale.scaleX = -1;
            scale.drawX = 46;
        }
        scale.drawY = -3;
    }

    private void canJumpController() {
        if (!Utils.isTouching(collide.touching, Touch.FLOOR)) {
            playerComponent.jumpTimer += 1;
            if (playerComponent.jumpTimer >= 5) playerComponent.canJump = false;
        } else {
            playerComponent.canJump = true;
            playerComponent.jumpTimer = 0;
        }
    }

    @Override
    public void update(float deltaTime) {
        playerComponent.hangTile = collisionSystem.getTileAt(collide.sideX, collisionSystem.getTileNum(physics.oldY + bounds.h + 1));
        keyRegister();
        brain.update();
        facingControl();
    }

    public void setPlayer(Entity player) {
        this.player = player;
        anim = Mappers.anim.get(player);
        physics = Mappers.physics.get(player);
        playerComponent = Mappers.player.get(player);
        collide = Mappers.collide.get(player);
        scale = Mappers.scale.get(player);
        light = Mappers.light.get(player);
        bounds = Mappers.bounds.get(player);
        pos = Mappers.position.get(player);
    }
}
