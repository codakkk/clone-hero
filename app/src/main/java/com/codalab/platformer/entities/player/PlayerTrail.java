package com.codalab.platformer.entities.player;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.entities.EntityFactory;
import com.codalab.platformer.entities.ParticleEntity;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.utils.ObjectPool;
import com.google.fpl.liquidfun.Vec2;

import java.util.ArrayList;
import java.util.List;

public class PlayerTrail {
    private static final float PARTICLE_SPAWN_TIME = 0.15f;

    private float nextParticleTime = 0.0f;

    private final PlayerEntity player;

    private final ObjectPool<ParticleEntity> particlePool;
    private final List<ParticleEntity> currentActiveParticles = new ArrayList<>();

    public PlayerTrail(PlayerEntity player) {
        this.player = player;
        this.particlePool = new ObjectPool<>(5, () -> EntityFactory.createEntity(ParticleEntity.class, Vector2.zero));
    }

    public void update(float deltaTime) {
        nextParticleTime += deltaTime;
        if(nextParticleTime >= PARTICLE_SPAWN_TIME) {
            nextParticleTime -= PARTICLE_SPAWN_TIME;

            spawnParticle();
        }

        poolParticlesIfDead();
    }

    public void spawnParticle() {
        Vec2 box2DPosition = player.getBody().getPosition();
        Vector2 pos = new Vector2(box2DPosition);
        box2DPosition.delete();

        ParticleEntity entity = particlePool.newObject();
        entity.position.set(pos);
        currentActiveParticles.add(entity);

        if(entity.gameWorld == null) {
            player.gameWorld.addEntity(entity);
        }
    }

    private void poolParticlesIfDead() {
        for(int i = currentActiveParticles.size()-1; i >= 0; --i) {
            ParticleEntity e = currentActiveParticles.get(i);

            if(e.isAlive) {
                continue;
            }

            player.gameWorld.removeEntity(e);
            particlePool.free(e);
        }
    }
}
