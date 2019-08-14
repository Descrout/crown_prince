package com.crown.prince.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.crown.prince.Mappers;
import com.crown.prince.Touch;
import com.crown.prince.World;
import com.crown.prince.components.BoundsComponent;
import com.crown.prince.components.CollideComponent;
import com.crown.prince.components.PhysicsComponent;
import com.crown.prince.components.PositionComponent;

import static com.crown.prince.Constants.TILE_SIZE;

public class PhysicsSystem extends IntervalIteratingSystem {
    public static final float Fixed_Timestep = 1 / 50f;
    public int[][] grid;

    public PhysicsSystem() {
        super(Family.all(PositionComponent.class, PhysicsComponent.class).get(), Fixed_Timestep);
    }

    public int getTileNum(float xy) {
        return (int) (xy / TILE_SIZE);
    }

    public int getTileAt(int x, int y) {
        if (x < 0 || y < 0 || x >= World.tileNumX || y >= World.tileNumY) return -1;
        return grid[x][y];
    }

    @Override
    protected void processEntity(Entity entity) {
        PositionComponent pos = Mappers.position.get(entity);
        PhysicsComponent physics = Mappers.physics.get(entity);

        physics.oldX = pos.x;
        physics.oldY = pos.y;

        physics.accY -= physics.gravity;

        physics.velX *= physics.friction;

        physics.velX += physics.accX;
        physics.velY += physics.accY;

        if (physics.velY > physics.maxVelY) physics.velY = physics.maxVelY;
        if (physics.velY < -physics.maxVelY) physics.velY = -physics.maxVelY;

        pos.x += physics.velX * Fixed_Timestep;
        pos.y += physics.velY * Fixed_Timestep;

        physics.accX = 0;
        physics.accY = 0;

        BoundsComponent bounds = Mappers.bounds.get(entity);
        CollideComponent collide = Mappers.collide.get(entity);

        if (collide == null || bounds == null) return;
        tileCollision(pos, physics, bounds, collide);
    }

    private void tileCollision(PositionComponent pos, PhysicsComponent physics, BoundsComponent bounds, CollideComponent collide) {
        int sideX, sideY;

        collide.touching = Touch.NONE;

        if (physics.velX > 0)sideX = getTileNum(pos.x + bounds.w);
        else sideX = getTileNum(pos.x);

        if (physics.velY > 0) sideY = getTileNum(pos.y + bounds.h);
        else sideY = getTileNum(pos.y);

        int tX = -1;
        for(int i = 0, tile; i < collide.colTilesHori.size; i++){
            tX = getTileNum(physics.oldX + collide.colTilesHori.get(i));
            tile = getTileAt(tX,sideY);
            if(tile == 1){
                if (physics.velY < 0) {
                    pos.y = pos.y - ((pos.y) % TILE_SIZE) + TILE_SIZE  + 0.1f;
                    collide.touching |= Touch.FLOOR;
                } else {
                    pos.y = pos.y - ((pos.y + bounds.h) % TILE_SIZE) - 0.1f;
                    collide.touching |= Touch.CEILING;
                }
                physics.velY = 0;
                break;
            }
            //else
            tX = -1;
        }

        int tY;
        for(int i = 0, tile; i < collide.colTilesVerti.size; i++){
            tY = getTileNum(physics.oldY + collide.colTilesVerti.get(i));
            if(tX == sideX && tY == sideY) break; // check horizontal x vertical overlaps
            tile = getTileAt(sideX,tY);
            if(tile == 1){
                if (physics.velX > 0) {
                    pos.x = pos.x - ((pos.x + bounds.w) % TILE_SIZE) - 0.1f;
                    collide.touching |= Touch.RIGHT_SIDE;
                } else {
                    pos.x = pos.x - (pos.x % TILE_SIZE) + TILE_SIZE + 0.1f;
                    collide.touching |= Touch.LEFT_SIDE;
                }
                physics.velX = 0;
                break;
            }
        }

    }
}
