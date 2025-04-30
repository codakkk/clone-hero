package com.codalab.platformer.graphics;

import com.badlogic.androidgames.framework.Color;
import com.codalab.platformer.data.Constants;

public class FadeInOut {
    public interface FadeCompletedCallback {
        void onCompleted();
    }

    public FadeCompletedCallback fadeInCompletedCallback;
    public FadeCompletedCallback fadeOutCompletedCallback;

    private boolean isRunning = false;
    private float resetTime = 0.0f;
    private boolean resetForward = true;

    public void update(float deltaTime) {
        if(!isRunning) {
            return;
        }

        float resetSpeed = 1.0f;
        this.resetTime -= deltaTime * resetSpeed;

        if(this.resetTime <= 0.0f) {
            if(resetForward) {
                this.resetTime = 1.0f;
                this.resetForward = false;
                if(fadeInCompletedCallback != null) {
                    fadeInCompletedCallback.onCompleted();
                }
                // this.reset();
            } else {
                this.isRunning = false;

                if(fadeOutCompletedCallback != null) {
                    fadeOutCompletedCallback.onCompleted();
                }
            }
        }
    }

    public void render(MyGraphics g) {
        if(!isRunning) {
            return;
        }

        float multiplier = resetForward ? (1-resetTime) : resetTime;
        g.drawRect(-20, -20, Constants.VIRTUAL_WIDTH + 20, Constants.VIRTUAL_HEIGHT + 20, Color.convert(0, 0, 0, (int)(255 * multiplier)));
    }

    public void start() {
        if(isRunning) {
            return;
        }
        this.isRunning = true;
        this.resetTime = 0.0f;
        this.resetForward = true;
    }
}
