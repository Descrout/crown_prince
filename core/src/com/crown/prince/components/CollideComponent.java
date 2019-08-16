package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import static com.crown.prince.Constants.TILE_SIZE;

public class CollideComponent implements Component, Pool.Poolable {
    public int touching = 0;
    public Array<Integer> colTilesHori;
    public Array<Integer> colTilesVerti;

    @Override
    public void reset() {
        colTilesVerti = null;
        colTilesHori = null;
        touching = 0;
    }


    public void init(int w, int h){
        int wCount = (int)(Math.ceil((float)w / TILE_SIZE))+1;
        int hCount = (int)(Math.ceil((float)h / TILE_SIZE))+1;

        colTilesHori = new Array<>();
        colTilesVerti = new Array<>();

        for (int i = 0; i < wCount; i++) {
            int pos = i*TILE_SIZE;
            if(pos >= w){
                colTilesHori.add(w);
                break;
            }else colTilesHori.add(pos);
        }

        for (int i = 0; i < hCount; i++) {
            int pos = i*TILE_SIZE;
            if(pos >= h){
                colTilesVerti.add(h);
                break;
            }else colTilesVerti.add(pos);
        }

    }


}
