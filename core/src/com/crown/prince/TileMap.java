package com.crown.prince;

import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class TileMap {
    public int tileNum;
    public TextureRegion [][]tileset = null;

    public int[][] backgrounTiles = null;
    public int[][] mainTiles = null;
    public int[][] foregroundTiles = null;

    public TileMap(TextureRegion tileset){
        this.tileset = tileset.split(Constants.TILE_SIZE,Constants.TILE_SIZE);
        tileNum = tileset.getRegionWidth() / Constants.TILE_SIZE;
    }
}
