package com.codalab.platformer.tiles;

import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.levels.Level;

public class WaterTile extends BaseTile {

    protected WaterTile(int id) {
        super(id);
    }

    @Override
    public void render(MyGraphics g, GameWorld world, Level level, int x, int y) {
        // it's needed so we have some kind of determinism based on time and x/y
        // this is useful because we don't need to store any value per tile
        // and with this seed we prevent flickering
        // Those are just random numbers lol
        rnd.setSeed(((int)time*60 + (x / 2 - y) * 1234) / 10 * 5554321l + x * 2314141l + y * 129341781l);
        if(level.getTile(x, y-1) == null) {
            int index = 1 + rnd.nextInt(2);

            g.draw(x * 8, y * 8, index + 5 * 32);
        } else {
            g.draw(x * 8, y * 8, 0 + 5 * 32);
        }
    }
}
