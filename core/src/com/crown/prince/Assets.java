package com.crown.prince;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

    private AssetManager manager;

    public TextureAtlas atlas;
    public Animation<TextureRegion> adventurerIdling;
    public Animation<TextureRegion> adventurerRun;
    public Animation<TextureRegion> adventurerJump;
    public Animation<TextureRegion> adventurerFall;
    public Animation<TextureRegion> adventurerWallSlide;


    public TextureRegion tiles;

    public Assets(){
        manager = new AssetManager();
        setAssets();
    }

    public void setAssets(){
        manager.load("placeHolder.atlas",TextureAtlas.class);
    }

    private void getAssets(){
        atlas = manager.get("placeHolder.atlas",TextureAtlas.class);
        adventurerIdling = new Animation(0.20f, atlas.findRegions("adventurer-idle"), Animation.PlayMode.LOOP);
        adventurerRun = new Animation(0.12F, atlas.findRegions("adventurer-run"), Animation.PlayMode.LOOP);
        adventurerJump = new Animation(0.12F, atlas.findRegions("adventurer-jump"), Animation.PlayMode.NORMAL);
        adventurerFall = new Animation(0.12F, atlas.findRegions("adventurer-fall"), Animation.PlayMode.LOOP);
        adventurerWallSlide = new Animation(0.12F, atlas.findRegions("adventurer-wall-slide"), Animation.PlayMode.LOOP);
        tiles = atlas.findRegion("tileset");
    }

    public void loadAllAtOnce(){
        manager.finishLoading();
        getAssets();
    }

    public int keepLoading(){
        if(manager.update()){
            getAssets();
            return -1;
        }
        return Math.round(manager.getProgress()*100);
    }

    public void dispose(){
        manager.dispose();
    }

}
