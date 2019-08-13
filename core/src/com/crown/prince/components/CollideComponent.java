package com.crown.prince.components;

import com.badlogic.ashley.core.Component;

import static com.crown.prince.Constants.TILE_SIZE;

public class CollideComponent implements Component {
    public int touching = 0;
    public int[] colTilesHori;
    public int[] colTilesVerti;
    public int wCount;
    public int hCount;

    public void init(int w, int h){
        wCount = (Math.round(w / TILE_SIZE)) + 1;
        hCount = (Math.round(h / TILE_SIZE)) + 1;

        colTilesHori = new int[wCount];
        colTilesVerti = new int[hCount];
    }
}
