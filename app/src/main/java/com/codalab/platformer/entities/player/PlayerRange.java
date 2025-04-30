package com.codalab.platformer.entities.player;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.utils.Converter;

public class PlayerRange {

    // This is a way to cache Vector2 and avoid to trigger GC
    private final Vector2 rangeStartPosTemp = new Vector2();

    private float animTime;

    public void update(float deltaTime) {
        animTime += deltaTime;

        if(animTime >= 1.0f) {
            animTime -= 1.0f;
        }
    }

    public void draw(MyGraphics g, Vector2 playerPosition, Vector2 mousePosition) {
        rangeStartPosTemp.set(mousePosition).sub(playerPosition).nor();

        float angle = (float)Math.toRadians(rangeStartPosTemp.angle());
        float startRadius = 3.0f;
        float distanceBetweenPoints = 1.5f;

        // The one that will have animIndex = 1 (cycled over time)
        int activeIndex = (int)(animTime * 3) % 3;

        for(int i = 0; i < 3; ++i) {
            float x = playerPosition.x + startRadius * (float)Math.cos(angle);
            float y = playerPosition.y + startRadius * (float)Math.sin(angle);

            startRadius += distanceBetweenPoints;

            float sx = Converter.toScreenX(x);
            float sy = Converter.toScreenY(y);

            int animIndex = i == activeIndex ? 1 : 0;
            g.draw(sx, sy, (1 + animIndex) + 2 * 32);
        }


    }
}
