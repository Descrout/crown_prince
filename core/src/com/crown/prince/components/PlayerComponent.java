package com.crown.prince.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {
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
}
