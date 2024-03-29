package com.crown.prince.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.crown.prince.Mappers;
import com.crown.prince.TileMap;
import com.crown.prince.World;
import com.crown.prince.components.*;

import static com.crown.prince.Constants.TILE_SIZE;


public class RenderSystem extends EntitySystem {
    public static float drawAlpha;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera cam;
    private ImmutableArray<Entity> entities;


    private TileMap map;
    private int startX, startY;

    public RenderSystem(SpriteBatch batch, OrthographicCamera cam, ShapeRenderer shapeRenderer) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.cam = cam;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, TextureComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        startX = (int)(cam.position.x - cam.viewportWidth / 2) / TILE_SIZE;
        startY = (int)(cam.position.y - cam.viewportHeight / 2) / TILE_SIZE;

        batch.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        batch.begin();

        renderBack();

        for (int i = 0; i < entities.size(); ++i) {
            Entity entity = entities.get(i);
            PositionComponent pos = Mappers.position.get(entity);

            BoundsComponent bounds = Mappers.bounds.get(entity);
            if(bounds!=null) {
                shapeRenderer.setColor(1f, 1f, 1f, 1f);
                shapeRenderer.rect(pos.x, pos.y, bounds.w, bounds.h);
            }

            TextureComponent tex = Mappers.texture.get(entity);
            if (tex.region == null) continue;

            ScaleComponent scale = Mappers.scale.get(entity);
            if(scale==null)
            batch.draw(tex.region, pos.x, pos.y);
            else{
                scale.x += (pos.x - scale.x) * drawAlpha;
                scale.y += (pos.y - scale.y) * drawAlpha;
                batch.draw(tex.region, scale.x+scale.drawX, scale.y+scale.drawY,scale.drawWidth*scale.scaleX,scale.drawHeight*scale.scaleY);
            }

        }

        renderFront();

        batch.end();
        shapeRenderer.end();
    }

    private void renderBack() {
       for (int i = startX; i <= startX + cam.viewportWidth / TILE_SIZE; i++) {
            if (i >= World.tileNumX) break;
            if (i < 0) continue;
            for (int j = startY; j <= 1+ startY + cam.viewportHeight / TILE_SIZE; j++) {
                if (j >= World.tileNumY) break;
                if (j < 0) continue;
                if (map.backgrounTiles[i][j] != -1) {
                    //map.tileset.setRegion(map.offsetX + (map.backgrounTiles[i][j] % map.tileNum) * TILE_SIZE, map.offsetY + (int) Math.floor(map.backgrounTiles[i][j] / map.tileNum) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    batch.draw(map.tileset[map.backgrounTiles[i][j]/map.tileNum][map.backgrounTiles[i][j]%map.tileNum], i * TILE_SIZE, j * TILE_SIZE);
                }

                if (map.mainTiles[i][j] != -1) {
                    //map.tileset.setRegion(map.offsetX + (map.mainTiles[i][j] % map.tileNum) * TILE_SIZE, map.offsetY + (int) Math.floor(map.mainTiles[i][j] / map.tileNum) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    batch.draw(map.tileset[map.mainTiles[i][j]/map.tileNum][map.mainTiles[i][j]%map.tileNum], i * TILE_SIZE, j * TILE_SIZE);
                }
            }
        }
    }

    private void renderFront() {
        for (int i = startX; i <= startX + cam.viewportWidth / TILE_SIZE; i++) {
            if (i >= World.tileNumX) break;
            if (i < 0) continue;
            for (int j = startY; j <= startY + cam.viewportHeight / TILE_SIZE; j++) {
                if (j >= World.tileNumY) break;
                if (j < 0) continue;
                if (map.foregroundTiles[i][j] != -1) {
                    //map.tileset.setRegion(map.offsetX + (map.foregroundTiles[i][j] % map.tileNum) * TILE_SIZE, map.offsetY + (int) Math.floor(map.foregroundTiles[i][j] / map.tileNum) * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    batch.draw(map.tileset[map.foregroundTiles[i][j]/map.tileNum][map.foregroundTiles[i][j]%map.tileNum], i * TILE_SIZE, j * TILE_SIZE);
                }

            }
        }
    }

    public void setTileMap(TileMap map) {
        this.map = map;
    }

}
