package com.crown.prince.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.crown.prince.Calculation;
import com.crown.prince.Mappers;
import com.crown.prince.Touch;
import com.crown.prince.World;
import com.crown.prince.components.*;

import static com.crown.prince.Constants.TILE_SIZE;

public class PhysicsSystem extends IntervalIteratingSystem {
    public static final float Fixed_Timestep = 1 / 50f;
    public int[][] grid;
    private ImmutableArray<Entity> colliders;

    public PhysicsSystem() {
        super(Family.all(PositionComponent.class, PhysicsComponent.class).get(), Fixed_Timestep);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        colliders = engine.getEntitiesFor(Family.all(ColliderComponent.class,PositionComponent.class,BoundsComponent.class, PhysicsComponent.class).get());
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
        colliderCollision(pos, physics, bounds, collide);
    }

    private void colliderCollision(PositionComponent pos, PhysicsComponent physics, BoundsComponent bounds, CollideComponent collide){
        for(int i = 0; i < colliders.size(); ++i){
            Entity entity = colliders.get(i);
            PositionComponent cPos = Mappers.position.get(entity);
            BoundsComponent cBounds = Mappers.bounds.get(entity);

            if(!Calculation.overlaps(pos,bounds,cPos,cBounds))continue;

            ColliderComponent cCollider = Mappers.collider.get(entity);
            PhysicsComponent cPhysics = Mappers.physics.get(entity);

            if(Calculation.doesAllow(cCollider.allowCollision,Touch.FLOOR) && pos.y+bounds.h >= cPos.y && physics.oldY+bounds.h < cPhysics.oldY){
                pos.y = cPos.y - bounds.h - 0.1f;
                physics.velY = cPhysics.velY;
                collide.touching |= Touch.CEILING;
            }else if(Calculation.doesAllow(cCollider.allowCollision,Touch.CEILING) && pos.y <= cPos.y+cBounds.h && physics.oldY > cPhysics.oldY+cBounds.h){
                pos.y = cPos.y+cBounds.h+0.1f;
                physics.velY = cPhysics.velY;
                if(cPhysics.velX != 0 && !physics.controlled)physics.velX = cPhysics.velX / physics.friction;
                collide.touching |= Touch.FLOOR;
            }else if(Calculation.doesAllow(cCollider.allowCollision,Touch.LEFT_SIDE) && pos.x+bounds.w >= cPos.x && physics.oldX+bounds.w < cPhysics.oldX){
                pos.x = cPos.x-bounds.w-0.1f;
                physics.velX = cPhysics.velX;
                collide.touching |= Touch.RIGHT_SIDE;
            }else if(Calculation.doesAllow(cCollider.allowCollision,Touch.RIGHT_SIDE) && pos.x <= cPos.x+cBounds.w && physics.oldX > cPhysics.oldX+cBounds.w){
                pos.x = cPos.x+cBounds.w+0.1f;
                physics.velX = cPhysics.velX;
                collide.touching |= Touch.LEFT_SIDE;
            }
        }
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
