package com.codalab.platformer.ui;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.graphics.MyGraphics;

public abstract class Widget {

    protected boolean enabled;

    protected int width;
    protected int height;

    protected Vector2 position;

    protected Widget() {
        this.position = new Vector2(0, 0);
        this.enabled = true;
    }

    public void update(float delta) {}

    public abstract void render(MyGraphics g);

    public boolean contains(Vector2 point) {
        // In inlining we trust :pray:
        return contains((int)point.x, (int)point.y);
    }

    public boolean contains(int x, int y) {
        final int halfW = width;
        final int halfH = height;
        return x >= position.x - halfW && x <= position.x + halfW && y >= position.y - halfH && y <= position.y + halfH;
    }

    public void setPosition(int x, int y) {
        this.position.set(x, y);
    }
    public void setPosition(Vector2 position) {
        if(position == null) {
            return;
        }
        this.position = position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void show() {
        this.enabled = true;
    }

    public void hide() {
        this.enabled = false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
