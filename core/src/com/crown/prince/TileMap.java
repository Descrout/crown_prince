package com.crown.prince;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileMap {
    public int offsetX, offsetY;
    public int tileNumX, tileNumY;
    public int tileNum;
    public TextureRegion tileset = null;

    public int[][] backgrounTiles = null;
    public int[][] mainTiles = null;
    public int[][] foregroundTiles = null;

    public TileMap(TextureRegion tileset){
        this.tileset = new TextureRegion(tileset);
        offsetX = tileset.getRegionX();
        offsetY = tileset.getRegionY();
        tileNum = tileset.getRegionWidth();
    }
}
