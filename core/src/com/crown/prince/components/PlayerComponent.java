package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Component, Pool.Poolable {
    public static final int IDLE = 0;
    public static final int RUN = 1;
    public static final int JUMP = 2;
    public static final int FALL = 3;

    public boolean keyRight = false;
    public boolean keyLeft = false;
    public boolean keyUp = false;
    public boolean keyDown = false;

    public boolean canJump = false;
    public boolean facingRight = true;

    @Override
    public void reset() {
        keyRight = false;
        keyLeft = false;
        keyUp = false;
        keyDown = false;

        canJump = false;
        facingRight = true;
    }
}
