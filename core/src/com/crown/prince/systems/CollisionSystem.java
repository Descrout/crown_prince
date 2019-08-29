package com.crown.prince.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.crown.prince.Calculation;
import com.crown.prince.Mappers;
import com.crown.prince.Touch;
import com.crown.prince.World;
import com.crown.prince.components.*;

import static com.crown.prince.Constants.TILE_SIZE;

public class CollisionSystem extends IteratingSystem {
    public int[][] grid;
    private ImmutableArray<Entity> colliders;

    public CollisionSystem() {
        super(Family.all(PositionComponent.class, PhysicsComponent.class, CollideComponent.class, BoundsComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        colliders = engine.getEntitiesFor(Family.all(ColliderComponent.class, PositionComponent.class, BoundsComponent.class, PhysicsComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = Mappers.position.get(entity);
        PhysicsComponent physics = Mappers.physics.get(entity);
        CollideComponent collide = Mappers.collide.get(entity);
        BoundsComponent bounds = Mappers.bounds.get(entity);

        collide.touching = Touch.NONE;

        if (physics.velX > 0) collide.sideX = getTileNum(pos.x + bounds.w);
        else collide.sideX = getTileNum(pos.x);

        if (physics.velY > 0) collide.sideY = getTileNum(pos.y + bounds.h);
        else collide.sideY = getTileNum(pos.y);

        int tX = -1;
        for (int i = 0, tile; i < collide.colTilesHori.size; i++) {
            tX = getTileNum(physics.oldX + collide.colTilesHori.get(i));
            tile = getTileAt(tX, collide.sideY);
            if (tile == 1) {
                if (physics.velY < 0) {
                    pos.y = pos.y - ((pos.y) % TILE_SIZE) + TILE_SIZE + 0.1f;
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
        for (int i = 0, tile; i < collide.colTilesVerti.size; i++) {
            tY = getTileNum(physics.oldY + collide.colTilesVerti.get(i));
            if (tX == collide.sideX && tY == collide.sideY) break; // check horizontal x vertical overlaps
            tile = getTileAt(collide.sideX, tY);
            if (tile == 1) {
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

        physics.onPlatform = false;

        for (int i = 0; i < colliders.size(); ++i) {
            Entity collider = colliders.get(i);
            PositionComponent cPos = Mappers.position.get(collider);
            BoundsComponent cBounds = Mappers.bounds.get(collider);

            if (!Calculation.overlaps(pos, bounds, cPos, cBounds)) continue;

            PhysicsComponent cPhysics = Mappers.physics.get(collider);

            if (pos.y <= cPos.y + cBounds.h && physics.oldY > cPhysics.oldY + cBounds.h) {
                pos.y = cPos.y + cBounds.h + 0.1f;
                physics.velY = cPhysics.velY;
                if (cPhysics.velX != 0 && !physics.controlled) physics.velX = cPhysics.velX / physics.friction;
                collide.touching |= Touch.FLOOR;
                physics.onPlatform = true;
            }
        }
    }

    public int getTileNum(float xy) {
        return (int) (xy / TILE_SIZE);
    }

    public int getTileAt(int x, int y) {
        if (x < 0 || y < 0 || x >= World.tileNumX || y >= World.tileNumY) return 1;
        return grid[x][y];
    }


}
