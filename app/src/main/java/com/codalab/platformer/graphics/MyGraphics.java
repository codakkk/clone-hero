package com.codalab.platformer.graphics;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class MyGraphics {

    /// Canvas width
    public final int width;

    /// Canvas height
    public final int height;

    private final Graphics graphics;

    private final Pixmap pixmap;

    public MyGraphics(Graphics graphics, Pixmap pixmap, int width, int height) {
        this.graphics = graphics;
        this.width = width;
        this.height = height;

        this.pixmap = pixmap;
    }

    public void clear(int color) {
        this.graphics.clear(color);
    }

    public void draw(float x, float y, int tile) {
        int xTile = tile % 32;
        int yTile = tile / 32;

        graphics.draw(pixmap, x-4, y-4, xTile * 8, yTile * 8, 8, 8);
    }

    public void draw(float x, float y, int tile, float angle) {
        int xTile = tile % 32;
        int yTile = tile / 32;

        graphics.draw(pixmap, x-4, y-4, xTile * 8, yTile * 8, 8, 8, angle);
    }

    public void draw(float x, float y, int tile, int color) {
        int xTile = tile % 32;
        int yTile = tile / 32;

        graphics.draw(pixmap, x-4, y-4, xTile * 8, yTile * 8, 8, 8, color);
    }

    // Mainly used by progress bar
    public void drawRect(float x, float y, float width, float height, int color) {
        graphics.drawRect(x, y, width, height, color);
    }
}
