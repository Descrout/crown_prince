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
    public Animation<TextureRegion> adventurerCornerGrab;
    public Animation<TextureRegion> adventurerCrouch;
    public Animation<TextureRegion> adventurerDie;
    public Animation<TextureRegion> adventurerAttack1;
    public Animation<TextureRegion> adventurerAttack2;
    public Animation<TextureRegion> adventurerAttack3;


    public TextureRegion tiles;
    public TextureRegion platform;
    public TextureRegion spike;

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
        adventurerWallSlide = new Animation(0.20F, atlas.findRegions("adventurer-wall-slide"), Animation.PlayMode.LOOP);
        adventurerCornerGrab = new Animation(0.14F, atlas.findRegions("adventurer-crnr-grb"), Animation.PlayMode.LOOP);
        adventurerCrouch = new Animation(0.12F, atlas.findRegions("adventurer-crouch"), Animation.PlayMode.LOOP);
        adventurerDie = new Animation(0.12F, atlas.findRegions("adventurer-die"), Animation.PlayMode.NORMAL);
        adventurerAttack1 = new Animation(0.08F, atlas.findRegions("adventurer-attack1"), Animation.PlayMode.NORMAL);
        adventurerAttack2 = new Animation(0.08F, atlas.findRegions("adventurer-attack2"), Animation.PlayMode.NORMAL);
        adventurerAttack3 = new Animation(0.08F, atlas.findRegions("adventurer-attack3"), Animation.PlayMode.NORMAL);


        tiles = atlas.findRegion("tileset");
        platform = atlas.findRegion("platform");
        spike = atlas.findRegion("spikes");
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
