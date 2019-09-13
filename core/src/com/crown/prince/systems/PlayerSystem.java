package com.crown.prince.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.crown.prince.*;
import com.crown.prince.components.*;

public class PlayerSystem extends EntitySystem {
    Entity player;
    Entity damage;
    private PooledEngine engine;

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
        this.engine = (PooledEngine) engine;
        collisionSystem = engine.getSystem(CollisionSystem.class);
    }

    private void normalState() {
        canJumpController();
        playerControl();
        rangedAttackController();
        animControl();
    }

    private void rangedAttackController() {
        playerComponent.rangedTimer += FixedUpdate.Fixed_Timestep;
        if (playerComponent.rangedTimer >= playerComponent.rangedAttackSpeed && playerComponent.keyAttackRanged) {
            createRangedAttack();
            playerComponent.rangedTimer = 0;
        }
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
            physics.accX += (playerComponent.facingRight ? -750f : 750f);
            brain.currentState = this::normalState;
            return;
        }

        if (playerComponent.hangTile != 1) {
            brain.currentState = this::normalState;
            return;
        }

        if (playerComponent.facingRight) {
            if (playerComponent.keyLeft) {
                brain.currentState = this::normalState;
                physics.accX -= 100f;
            }
        } else if (playerComponent.keyRight) {
            brain.currentState = this::normalState;
            physics.accX += 100f;
        }


    }

    private void crouchState() {
        anim.state = PlayerComponent.CROUCH;

        if (!playerComponent.keyDown) {
            bounds.h = Constants.playerBoundH;
            brain.currentState = this::normalState;
            return;
        }

        rangedAttackController();

        if (playerComponent.keyAttackLight) {
            anim.time = 0f;
            playerComponent.timer = 0f;
            brain.currentState = this::crouchAttackPrepare;
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

    private void lightAttackPrepare() {
        if (playerComponent.combo == 1) {
            anim.state = PlayerComponent.ATTACK1;
        } else if (playerComponent.combo == 2) {
            anim.state = PlayerComponent.ATTACK2;
        } else {
            anim.state = PlayerComponent.ATTACK3;
        }

        playerComponent.timer += FixedUpdate.Fixed_Timestep;

        if (anim.time > 0.1) anim.time = 0.1f;

        if (playerComponent.timer > 0.3) {
            createLightAttack();
            playerComponent.combo++;
            if (playerComponent.combo > 3) playerComponent.combo = 1;
            physics.accX += (playerComponent.facingRight ? 120 : -120);
            brain.currentState = this::lightAttackState;
            return;
        }
    }

    private void lightAttackState() {
        PositionComponent posD = Mappers.position.get(damage);
        posD.x = pos.x + (playerComponent.facingRight ? 16 : -20);
        posD.y = pos.y + bounds.h - Constants.lightAttackH;


        if (anim.animations.get(anim.state).isAnimationFinished(anim.time)) {
            engine.removeEntity(damage);
            brain.currentState = this::normalState;
        }
    }

    private void crouchAttackPrepare() {
        anim.state = PlayerComponent.ATTACK1;

        playerComponent.timer += FixedUpdate.Fixed_Timestep;

        if (anim.time > 0.1) anim.time = 0.1f;

        if (playerComponent.timer > 0.3) {
            createLightAttack();
            brain.currentState = this::crouchAttackState;
            return;
        }
    }


    private void crouchAttackState() {
        PositionComponent posD = Mappers.position.get(damage);
        posD.x = pos.x + (playerComponent.facingRight ? 16 : -20);
        posD.y = pos.y + bounds.h - Constants.lightAttackH;


        if (anim.animations.get(anim.state).isAnimationFinished(anim.time)) {
            brain.currentState = this::crouchState;
            engine.removeEntity(damage);
        }
    }

    private void jumpAttackState() {
        anim.state = PlayerComponent.ATTACK1;

        if (Utils.isTouching(collide.touching, Touch.FLOOR)) {
            brain.currentState = this::normalState;
            engine.removeEntity(damage);
            return;
        }
        if (!playerComponent.keyUp && physics.velY > 0) physics.velY *= 0.8;


        PositionComponent posD = Mappers.position.get(damage);
        posD.x = pos.x + (playerComponent.facingRight ? 16 : -20);
        posD.y = pos.y + bounds.h - Constants.lightAttackH;

        if (playerComponent.facingRight) {
            if (playerComponent.keyRight) {
                physics.accX += Constants.playerSpeed;
            }
        } else {
            if (playerComponent.keyLeft) {
                physics.accX -= Constants.playerSpeed;
            }
        }


        if (anim.animations.get(anim.state).isAnimationFinished(anim.time)) {
            brain.currentState = this::normalState;
            engine.removeEntity(damage);

        }
    }


    private void createRangedAttack() {
        Entity entity = engine.createEntity();

        PositionComponent posD = engine.createComponent(PositionComponent.class);
        BoundsComponent boundsD = engine.createComponent(BoundsComponent.class);
        TextureComponent tex = engine.createComponent(TextureComponent.class);
        DamageComponent damageComponent = engine.createComponent(DamageComponent.class);
        ProjectileComponent projectileComponent = engine.createComponent(ProjectileComponent.class);
        PhysicsComponent physicsD = engine.createComponent(PhysicsComponent.class);
        CollideComponent collideD = engine.createComponent(CollideComponent.class);

        physicsD.gravity = 14f;
        physicsD.friction = 0.99f;
        physicsD.velX = (playerComponent.facingRight ? 600 : -600);
        physicsD.velY = 200f;

        physicsD.elasticity = 0.7f;

        damageComponent.id = 1;
        damageComponent.knockbackX = 220;
        damageComponent.knockbackY = 150;
        damageComponent.facingRight = playerComponent.facingRight;
        damageComponent.canDamage = true;

        boundsD.setBounds(16, 16);

        collideD.collideWithPlatform = false;
        collideD.init(boundsD.w, boundsD.h);


        posD.x = pos.x + (playerComponent.facingRight ? 16 : -20);
        posD.y = pos.y + bounds.h - 16;

        entity.add(posD);
        entity.add(boundsD);
        entity.add(damageComponent);
        entity.add(tex);
        entity.add(projectileComponent);
        entity.add(collideD);
        entity.add(physicsD);

        engine.addEntity(entity);
    }


    private void createLightAttack() {
        damage = engine.createEntity();

        PositionComponent posD = engine.createComponent(PositionComponent.class);
        BoundsComponent boundsD = engine.createComponent(BoundsComponent.class);
        TextureComponent tex = engine.createComponent(TextureComponent.class);
        DamageComponent damageComponent = engine.createComponent(DamageComponent.class);

        damageComponent.id = 1;
        damageComponent.knockbackX = 220;
        damageComponent.knockbackY = 150;
        damageComponent.facingRight = playerComponent.facingRight;
        //damageComponent.canDamage = false;

        boundsD.setBounds(Constants.lightAttackW, Constants.lightAttackH);

        posD.x = pos.x + (playerComponent.facingRight ? 16 : -20);
        posD.y = pos.y + bounds.h - Constants.lightAttackH;

        damage.add(posD);
        damage.add(boundsD);
        damage.add(damageComponent);
        damage.add(tex);

        engine.addEntity(damage);
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


        if (playerComponent.keyAttackLight) {
            anim.time = 0f;
            playerComponent.timer = 0f;
            if (playerComponent.canJump) {
                brain.currentState = this::lightAttackPrepare;
            } else {
                createLightAttack();
                playerComponent.timer = 0f;
                brain.currentState = this::jumpAttackState;
            }
            return;
        }

        if (!playerComponent.keyUp && physics.velY > 0) physics.velY *= 0.8;

        if (playerComponent.keyDown && playerComponent.canJump) {
            if (physics.onPlatform) {
                pos.y -= 2;
                playerComponent.canJump = false;
            } else {
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
        } else playerComponent.willHang = false;

    }

    private void keyRegister() {
        physics.controlled = false;
        playerComponent.keyLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        playerComponent.keyRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        playerComponent.keyUp = Gdx.input.isKeyPressed(Input.Keys.UP);
        playerComponent.keyDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        playerComponent.keyAttackLight = Gdx.input.isKeyPressed(Input.Keys.A);
        playerComponent.keyRoll = Gdx.input.isKeyPressed(Input.Keys.S);
        playerComponent.keyAttackRanged = Gdx.input.isKeyPressed(Input.Keys.D);
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
