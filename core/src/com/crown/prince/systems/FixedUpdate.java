package com.crown.prince.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;

public class FixedUpdate extends EntitySystem {

    public static final float Fixed_Timestep = 1 / 60f;
    private float accumulator;

    private Array<EntitySystem> systems;

    public FixedUpdate() {
        systems = new Array<>();
        accumulator = 0f;
    }

    public void addSystem(EntitySystem system){
        system.setProcessing(false);
        systems.add(system);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        for(int i = 0; i < systems.size; i++){
            engine.addSystem(systems.get(i));
        }
    }

    @Override
    public void update(float deltaTime) {
        accumulator += deltaTime;
        while (accumulator >= Fixed_Timestep) {
            for(int i = 0; i < systems.size; i++){
                systems.get(i).update(Fixed_Timestep);
            }
            accumulator -= Fixed_Timestep;
        }
        RenderSystem.drawAlpha = accumulator / Fixed_Timestep;
    }
}
