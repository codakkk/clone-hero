package com.codalab.platformer.utils;

import com.badlogic.androidgames.framework.math.Box;
import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.data.Constants;

public final class Converter {
    private Converter() {}

    public static Box boundaries;
    public static Box screenSize;


    public static void set(Box boundaries, Box screenSize) {
        Converter.boundaries = boundaries;
        Converter.screenSize = screenSize;
    }

    public static Vector2 toWorld(float x, float y) {
        return new Vector2(toWorldX(x), toWorldY(y));
    }

    public static Vector2 toWorld(Vector2 v) {
        return toWorld(v.x, v.y);
    }

    public static float toWorldX(float x) { return boundaries.xmin + x * (boundaries.width/screenSize.width); }
    public static float toWorldY(float y) { return boundaries.ymin + y * (boundaries.height/screenSize.height); }

    public static Vector2 toScreen(float x, float y) {
        return new Vector2(toScreenX(x), toScreenY(y));
    }

    public static Vector2 toScreen(Vector2 v) {
        return toScreen(v.x, v.y);
    }

    public static float toScreenX(float x) { return (x-boundaries.xmin)/boundaries.width * Constants.VIRTUAL_WIDTH; }
    public static float toScreenY(float y) { return (y-boundaries.ymin)/boundaries.height*Constants.VIRTUAL_HEIGHT; }

    public static float toScreenXLength(float x)
    {
        return x/boundaries.width*Constants.VIRTUAL_WIDTH;
    }
    public static float toScreenYLength(float y)
    {
        return y/boundaries.height*Constants.VIRTUAL_HEIGHT;
    }
}
