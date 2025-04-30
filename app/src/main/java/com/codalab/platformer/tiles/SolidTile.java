package com.codalab.platformer.tiles;

import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.levels.Level;

public class SolidTile extends BaseTile {
    protected SolidTile(int id) {
        super(id);
    }

    @Override
    public void render(MyGraphics g, GameWorld world, Level level, int x, int y) {

        // Whole algorithm is based on one of the best post
        // I ever reed about tile-maps
        // https://web.archive.org/web/20130410235113/http://www.saltgames.com/2010/a-bitwise-method-for-applying-tilemaps/

        final int top = level.getTile(x, y-1) == BaseTile.solid ? 1 : 0;
        final int right = level.getTile(x+1, y) == BaseTile.solid ? 1 : 0;
        final int bottom = level.getTile(x, y+1) == BaseTile.solid ? 1 : 0;
        final int left = level.getTile(x-1, y) == BaseTile.solid ? 1 : 0;

//        final boolean topLeft = level.getTile(x-1, y-1) == BaseTile.solid;
//        final boolean topRight = level.getTile(x+1, y-1) == BaseTile.solid;
//        final boolean bottomLeft = level.getTile(x-1, y+1) == BaseTile.solid;
//        final boolean bottomRight = level.getTile(x+1, y+1) == BaseTile.solid;

        final int index = top + (right << 1) + (bottom << 2) + (left << 3);

        int tile = index + 4 * 32;

        g.draw(x * 8, y * 8, tile);
    }
}
