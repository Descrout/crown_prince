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

    public boolean keyRight = false;
    public boolean keyLeft = false;
    public boolean keyUp = false;
    public boolean keyDown = false;

    public boolean canJump = false;
    public boolean facingRight = true;
    public boolean willHang = false;

    public int hangTile = 0;

    @Override
    public void reset() {
        keyRight = false;
        keyLeft = false;
        keyUp = false;
        keyDown = false;

        canJump = false;
        facingRight = true;
        willHang = false;

        hangTile = 0;
    }
}
