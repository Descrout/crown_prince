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

    public int getPosition(int xy) {
        return xy * TILE_SIZE;
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
        int tX, tY, ttX, ttY;
        float sideX, sideY;
        collide.touching = Touch.NONE;

        if (physics.velX > 0) {
            sideX = pos.x + bounds.w;
            ttX = getTileNum(physics.oldX + ((collide.wCount - 1) * TILE_SIZE));
        } else {
            sideX = pos.x;
            ttX = getTileNum(physics.oldX);
        }
        if (physics.velY > 0) sideY = pos.y + bounds.h;
        else sideY = pos.y;

        boolean collDetectHori = false;
        tY = ttY = getTileNum(sideY);
        for (int i = 0; i < collide.wCount; i++) {
            tX = getTileNum(physics.oldX + (i * TILE_SIZE));
            collide.colTilesHori[i] = getTileAt(tX, tY);
            if (getPosition(tX) > physics.oldX + bounds.w || getPosition(tX) + TILE_SIZE < pos.x) continue;
            if (collide.colTilesHori[i] == 1) {
                if (physics.velY < 0) {
                    pos.y = pos.y - (pos.y % TILE_SIZE) + TILE_SIZE + 0.1f;
                    collide.touching |= Touch.FLOOR;
                } else {
                    pos.y = pos.y - ((pos.y + bounds.h) % TILE_SIZE) - 0.1f;
                    collide.touching |= Touch.CEILING;
                }
                physics.velY = 0;
                collDetectHori = true;
                break;
            }
        }
        tX = getTileNum(sideX);
        for (int i = 0; i < collide.hCount; i++) {
            tY = getTileNum(physics.oldY + (i * TILE_SIZE));
            if (collDetectHori && tX == ttX && tY == ttY) continue; // Checking for overlaps with horizontal
            collide.colTilesVerti[i] = getTileAt(tX, tY);
            if (getPosition(tY) > physics.oldY + bounds.h || getPosition(tY) + TILE_SIZE < pos.y) continue;
            if (collide.colTilesVerti[i] == 1) {
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
