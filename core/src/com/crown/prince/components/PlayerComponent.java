package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Component, Pool.Poolable {
    public static final int IDLE = 0;
    public static final int RUN = 1;
    public static final int JUMP = 2;
    public static final int FALL = 3;
    public static final int SLIDE = 4;
    public static final int CORNER_GRAB = 5;
    public static final int CROUCH = 6;
    public static final int DIE = 7;
    public static final int ATTACK1 = 8;
    public static final int ATTACK2 = 9;
    public static final int ATTACK3 = 10;


    public boolean keyRight = false;
    public boolean keyLeft = false;
    public boolean keyUp = false;
    public boolean keyDown = false;
    public boolean keyAttackLight = false;
    public boolean keyRoll = false;
    public boolean keyAttackRanged = false;

    public boolean canJump = false;
    public boolean facingRight = true;
    public boolean willHang = false;

    public int jumpTimer = 0;
    public float timer = 0f;
    public float rangedTimer = 0f;

    public int hangTile = 0;

    public int lightAttackDamage = 1;
    public int rangedAttackDamage = 1;

    public float lightAttackSpeed = 1f;
    public float rangedAttackSpeed = 0.4f;

    public int combo = 0;

    @Override
    public void reset() {
        combo = 0;
        keyRight = false;
        keyLeft = false;
        keyUp = false;
        keyDown = false;
        keyAttackLight = false;
        keyRoll = false;

        canJump = false;
        facingRight = true;
        willHang = false;

        hangTile = 0;
        jumpTimer = 0;
        timer = 0f;
    }
}
