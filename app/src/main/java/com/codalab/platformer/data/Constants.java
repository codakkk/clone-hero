package com.codalab.platformer.data;

import com.badlogic.androidgames.framework.math.Box;

public final class Constants {
    private Constants() {}

    // Virtual Screen Size
    public static final int VIRTUAL_WIDTH = 384;
    public static final int VIRTUAL_HEIGHT = 160;

    // boundaries of the physical simulation
    public static final Box physicalSize = new Box(0, 0, (float) VIRTUAL_WIDTH / 8, (float) VIRTUAL_HEIGHT / 8);

    // Parameters for world simulation
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int POSITION_ITERATIONS = 3;
    public static final int PARTICLE_ITERATIONS = 3;

    public static final float PLAYER_CHARGING_SPEED = 3.0f;
}
