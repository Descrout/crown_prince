package com.crown.prince;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import static com.crown.prince.Constants.TILE_SIZE;
import static com.crown.prince.World.tileNumX;
import static com.crown.prince.World.tileNumY;

public class MapLoader {
    private Element root;
    private Element objects;
    private World world;
    public MapLoader(World world) {
        this.world = world;
    }

    public void load(String name) {
        XmlReader reader = new XmlReader();
        FileHandle file = Gdx.files.internal("maps/" + name + ".oel");
        root = reader.parse(file.readString());

        World.width = root.getIntAttribute("width");
        World.height = root.getIntAttribute("height");

        tileNumX = (int) Math.floor(World.width / TILE_SIZE);
        tileNumY = (int) Math.floor(World.height / TILE_SIZE);

        world.map.backgrounTiles = createLayer("background");
        world.map.mainTiles = createLayer("tiles");
        world.map.foregroundTiles = createLayer("foreground");

        loadObjects();
    }

    private void loadObjects(){
        objects = root.getChildByName("objects");
        loadPlatforms();
        loadPlayer();
    }

    private void loadPlayer(){
        Element player = objects.getChildByName("player");
        int x = player.getIntAttribute("x");
        int y = World.height - player.getIntAttribute("y") - TILE_SIZE;
        world.createPlayer(x,y);
    }

    private void loadPlatforms(){
        Array<Element> colliders = objects.getChildrenByName("platform");
        for(Element collider: colliders){
            int w = collider.getIntAttribute("width");
            int h = collider.getIntAttribute("height");
            int x = collider.getIntAttribute("x");
            int y = World.height - collider.getIntAttribute("y") - h;
            int velX = collider.getIntAttribute("velX");
            int velY = collider.getIntAttribute("velY");
            int changeX = collider.getIntAttribute("changeX");
            int changeY = collider.getIntAttribute("changeY");


            world.createPlatform(x,y,w,h,velX,velY,changeX,changeY);
        }
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
