package com.crown.prince;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;

import static com.crown.prince.World.tileNumX;
import static com.crown.prince.World.tileNumY;

public class MapLoader {
    private XmlReader.Element root;

    public MapLoader() {

    }

    public void load(String name, TileMap map) {
        XmlReader reader = new XmlReader();
        FileHandle file = Gdx.files.internal("maps/" + name + ".oel");
        root = reader.parse(file.readString());

        World.width = root.getIntAttribute("width");
        World.height = root.getIntAttribute("height");

        tileNumX = (int) Math.floor(World.width / Constants.TILE_SIZE);
        tileNumY = (int) Math.floor(World.height / Constants.TILE_SIZE);

        map.backgrounTiles = createLayer("background");
        map.mainTiles = createLayer("tiles");
        map.foregroundTiles = createLayer("foreground");

    }

    public int[][] createLayer(String layerName) {
        int[][] tiles = new int[tileNumX][tileNumY];

        String tileData = root.getChildByName(layerName).getText();

        String[] parsed1 = tileData.split("\n");

        for (int j = 0; j < parsed1.length; j++) {
            String[] parsed2 = parsed1[j].split(",");
            for (int i = 0; i < parsed2.length; i++) {
                tiles[i][tileNumY - 1 - j] = Integer.parseInt(parsed2[i]);
            }
        }

        return tiles;
    }

    public int[][] createCollisionGrid(String layerName) {
        int[][] tiles = new int[tileNumX][tileNumY];

        String tileData = root.getChildByName(layerName).getText();

        String[] parsed = tileData.split("\n");

        for (int j = 0; j < parsed.length; j++) {
            for (int i = 0; i < parsed[j].length(); i++) {
                tiles[i][tileNumY - 1 - j] = parsed[j].charAt(i) - '0';
            }
        }

        return tiles;
    }
}
