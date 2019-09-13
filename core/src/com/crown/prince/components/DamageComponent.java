package com.crown.prince.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DamageComponent implements Component, Pool.Poolable {
    public int damage = 1;
    public int knockbackX = 0;
    public int knockbackY = 0;
    public int id = 0;
    public boolean damaged = false;
    public boolean canDamage = true;
    public boolean facingRight = true;

    @Override
    public void reset() {
        damage = 1;
        id = 0;
        knockbackX = 0;
        knockbackY = 0;
        damaged = false;
        canDamage = true;
    }
}
