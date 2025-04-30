package com.codalab.platformer.entities;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.CollisionMasks;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.tiles.BaseTile;
import com.codalab.platformer.utils.Converter;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;

public class SpikeEntity extends BaseEntity implements IDamageable {
    public static SpikeEntity factory(GameWorld gw, Vector2 position) {
        SpikeEntity go = new SpikeEntity(position);

        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(position.x, position.y);
        bodyDef.setType(BodyType.staticBody);

        Body body = gw.world.createBody(bodyDef);
        body.setSleepingAllowed(false);
        body.setUserData(go);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.35f, 0.35f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(box);

        Filter filter = new Filter();
        filter.setCategoryBits(CollisionMasks.Killers);
        filter.setMaskBits(CollisionMasks.Player);

        fixtureDef.setFilter(filter);
        body.createFixture(fixtureDef);

        fixtureDef.delete();
        bodyDef.delete();
        box.delete();
        filter.delete();

        go.setBody(body);
        return go;
    }

    SpikeEntity(Vector2 position) {
        super(position);
    }

    @Override
    public void draw(MyGraphics g) {
        final float sx = Converter.toScreenX(position.x);
        final float sy = Converter.toScreenY(position.y);

        // 0 = upward
        // 1 = right
        // 2 = left
        int direction = 0;

        boolean left = gameWorld.currentLevel.getTile((int)position.x-1, (int)position.y) == BaseTile.solid;
        boolean right = gameWorld.currentLevel.getTile((int)position.x+1, (int)position.y) == BaseTile.solid;
        boolean bottom = gameWorld.currentLevel.getTile((int)position.x, (int)position.y+1) == BaseTile.solid;
        boolean top = gameWorld.currentLevel.getTile((int)position.x, (int)position.y-1) == BaseTile.solid;

        if(bottom) {
            direction = 1;
        } else if(top) {
            direction = 0;
        } else if(right && !left) {
            direction = 3;
        } else if(left && !right) {
            direction = 2;
        }

        g.draw(sx, sy, direction + 1 * 32);
    }
}
