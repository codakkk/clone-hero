package com.badlogic.androidgames.framework;

import com.codalab.platformer.graphics.MyGraphics;

public abstract class Screen {
    protected final Game game;

    public Screen(Game game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void render(MyGraphics g);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();
}
