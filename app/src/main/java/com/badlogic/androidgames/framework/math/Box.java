package com.badlogic.androidgames.framework.math;


public class Box {
    public float xmin, ymin, xmax, ymax, width, height;
    public Box(float xmin, float ymin, float xmax, float ymax)
    {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.width = xmax - xmin;
        this.height = ymax - ymin;
    }

    public boolean isInBounds(Vector2 v) {
        return this.isInBounds(v.x, v.y);
    }

    public boolean isInBounds(float x, float y) {
        return x >= xmin && x <= xmax && y >= ymin && y <= ymax;
    }
}
