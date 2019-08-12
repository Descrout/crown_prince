package com.crown.prince.components;

import com.badlogic.ashley.core.Component;

public class BoundsComponent implements Component {
    public int w = 0;
    public int h = 0;

    public int halfX = 0;
    public int halfY = 0;

    public void setBounds(int w, int h){
        this.w = w;
        this.h = h;
        halfX = w / 2;
        halfY = h / 2;
    }
}
