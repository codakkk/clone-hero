package com.codalab.platformer.entities;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.utils.Converter;
import com.codalab.platformer.utils.IPoolable;

public class ParticleEntity extends BaseEntity implements IPoolable {
    public static ParticleEntity factory(GameWorld gameWorld, Vector2 position) {
        return new ParticleEntity(position);
    }

    private float time = 0.0f;

    ParticleEntity(Vector2 position) {
        super(position);
        this.isAlive = true;
        this.time = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        if(!isAlive) {
            return;
        }

        super.update(deltaTime);

        time += deltaTime * 2.0f;

        if(time > 1.0f) {
            isAlive = false;
        }
    }

    @Override
    public void draw(MyGraphics g) {
        if(!isAlive) {
            return;
        }
        final float sx = Converter.toScreenX(position.x);
        final float sy = Converter.toScreenY(position.y);

        int index = (int)(time / 0.33f);
        if(index > 2) index = 2;
        g.draw(sx, sy, (4 + index) + 0 * 32);
    }

    @Override
    public void onPool() {
        this.gameWorld = null;
        this.time = 0.0f;
        this.position.set(-100, -100);
        this.isAlive = false;
    }
}
