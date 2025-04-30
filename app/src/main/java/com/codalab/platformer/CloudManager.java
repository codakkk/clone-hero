package com.codalab.platformer;

import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.data.Constants;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.utils.IPoolable;
import com.codalab.platformer.utils.ObjectPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CloudManager {
    private final Random random = new Random();

    private final float CLOUD_SPEED = 4.0f;

    private final int MIN_CLOUDS = 5;
    private final int MAX_CLOUDS = 20;


    private static class Cloud implements IPoolable {

        private final Vector2 position = new Vector2();

        private float baseY = 0.0f;

        private float speed = 0.0f;

        private float craziness = 0.0f;
        private float amplitude = 0.0f;

        @Override
        public void onPool() {
            this.position.set(-100, -100);
            this.baseY = 0.0f;
            this.speed = 0.0f;
            this.craziness = 0.0f;
            this.amplitude = 0.0f;
        }
    }


    private final ObjectPool<Cloud> cloudPool =  new ObjectPool<>(10, Cloud::new);

    private final List<Cloud> clouds = new ArrayList<>(10);

    private float cloudTime = 0.0f;

    public void randomize() {
        cloudPool.freeAll(clouds);
        clouds.clear();

        final int sz = MIN_CLOUDS + random.nextInt(MAX_CLOUDS-MIN_CLOUDS);
        for(int i = 0; i < sz; ++i) {
            spawnCloud();
        }
    }

    public void update(float deltaTime) {
        cloudTime += deltaTime;

        for(int i = clouds.size()-1; i >= 0; --i) {
            Cloud cloud = clouds.get(i);

            cloud.position.x += cloud.speed * CLOUD_SPEED * deltaTime;
            cloud.position.y = cloud.baseY + (float)(Math.sin(cloudTime * cloud.craziness) * cloud.amplitude);

            if(cloud.position.x < -16) {
                cloud.position.x = Constants.VIRTUAL_WIDTH + 10;
            }
        }
    }

    public void render(MyGraphics g) {
        for(int i = clouds.size()-1; i >= 0; --i) {
            Cloud cloud = clouds.get(i);

            g.draw(cloud.position.x, cloud.position.y, 3 + 5 * 32);
        }
    }

    private void spawnCloud() {
        Cloud c = cloudPool.newObject();

        final float y = random.nextFloat() * ((float) Constants.VIRTUAL_HEIGHT / 3);
        final float x = random.nextFloat() * Constants.VIRTUAL_WIDTH;



        // Define a minimum speed and variation
        final float minSpeed = 0.5f;
        final float maxSpeed = 1.5f;

        c.craziness = 0.5f + random.nextFloat() * 2.0f; // Range: [0.5, 2.5]
        c.amplitude = 0.05f + random.nextFloat() * 0.2f; // Range: [0.05, 0.25]
        c.speed = -1 * (minSpeed + random.nextFloat() * (maxSpeed - minSpeed));
        c.position.set(x, y);
        c.baseY = y;

        clouds.add(c);
    }
}
