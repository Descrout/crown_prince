package com.crown.prince.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.crown.prince.Utils;
import com.crown.prince.Mappers;
import com.crown.prince.components.*;

public class DamageSystem extends IteratingSystem {
    private ImmutableArray<Entity> health;

    public DamageSystem() {
        super(Family.all(DamageComponent.class, BoundsComponent.class, PositionComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        health = engine.getEntitiesFor(Family.all(HealthComponent.class,BoundsComponent.class,PositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DamageComponent damage = Mappers.damage.get(entity);
        PositionComponent pos1 = Mappers.position.get(entity);
        BoundsComponent bounds1 = Mappers.bounds.get(entity);

        for (int i = 0; i < health.size(); ++i) {
            Entity hp = health.get(i);

            HealthComponent hpComponent = Mappers.health.get(hp);
            if(hpComponent.id == damage.id) continue;
            if(damage.damaged) continue;

            PositionComponent pos2 = Mappers.position.get(hp);
            BoundsComponent bounds2 = Mappers.bounds.get(hp);

            if(Utils.overlaps(pos1,bounds1,pos2,bounds2)){
                hpComponent.HP -= damage.damage;
                hpComponent.healthListener.hit();

                if(Mappers.physics.has(hp)){
                    PhysicsComponent physics = Mappers.physics.get(hp);
                    physics.accY += damage.knockbackY/hpComponent.knockbackResY;
                    physics.accX += (pos1.x+bounds1.w/2 > pos2.x+bounds2.w/2 ? -damage.knockbackX : damage.knockbackX)/hpComponent.knockbackResX;
                }

                damage.damaged = true;

                if(hpComponent.HP <= 0){
                    hpComponent.HP = 0;
                    hpComponent.healthListener.die();
                }
            }
        }

    }
}
