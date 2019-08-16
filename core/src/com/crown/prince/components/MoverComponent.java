package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class MoverComponent implements Component, Pool.Poolable {
    public float velX = 0;
    public float velY = 0;
    public float timerX = 0;
    public float timerY = 0;
    public float changeX = 0;
    public float changeY = 0;

    public void init(int velX,int velY,int changeX, int changeY){
        timerX = 0;
        timerY = 0;
        this.velX = velX;
        this.velY = velY;
        this.changeX = changeX;
        this.changeY = changeY;
    }

    @Override
    public void reset() {
        timerX = 0;
        timerY = 0;
        changeY = 0;
        changeX = 0;
        velX = 0;
        velY = 0;
    }
}
