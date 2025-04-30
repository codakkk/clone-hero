package com.codalab.platformer.tiles;

import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.levels.Level;

import java.util.Random;

public abstract class BaseTile {

    public static float time = 0.0f;

    protected static Random rnd = new Random();

    public final byte id;

    public final static BaseTile[] tiles = new BaseTile[256];


    public final static BaseTile solid = new SolidTile(1);
    public final static BaseTile water = new WaterTile(2);

    protected BaseTile(int id) {
        assert id != 0;// 0 is air so it must be unused

        this.id = (byte)id;

        assert tiles[id] == null;

        tiles[id] = this;
    }

    public void render(MyGraphics g, GameWorld world, Level level, int x, int y) {}
}
