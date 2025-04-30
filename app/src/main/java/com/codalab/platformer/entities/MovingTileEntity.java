package com.codalab.platformer.entities;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.CollisionMasks;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.utils.Converter;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;

public class MovingTileEntity extends BaseEntity {
    private static final int SIZE = 4;

    public static MovingTileEntity factory(GameWorld gw, Vector2 position) {
        MovingTileEntity go = new MovingTileEntity(position);

        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(position.x, position.y);
        bodyDef.setType(BodyType.staticBody);
        bodyDef.setGravityScale(0.0f);

        Body body = gw.world.createBody(bodyDef);
        body.setSleepingAllowed(false);
        body.setUserData(go);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, SIZE / 2.0f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(box);

        Filter filter = new Filter();
        filter.setCategoryBits(CollisionMasks.Tile);
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

    private float time;

    private Vector2 minMax;

    MovingTileEntity(Vector2 position) {
        super(position);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(minMax == null) {
            minMax = new Vector2(this.position.y - SIZE, this.position.y + SIZE);
        }

        time += deltaTime * 0.5f;
        time %= 2.0f; // keeps alpha in range [0, 2)

        // ping-pong between 0 and 1
        float alpha = 1.0f - Math.abs(this.time - 1.0f);

        float minTileY = minMax.x;
        float maxTileY = minMax.y;

        // lerp from minTileY to maxTileY based on alpha
        float y = minTileY + (maxTileY - minTileY) * alpha;


        this.body.setTransform(this.position.x, y, 0.0f);
        this.position.y = y;
    }

    @Override
    public void draw(MyGraphics g) {
        final float sx = Converter.toScreenX(position.x);
        final float sy = Converter.toScreenY(position.y);

        if(minMax != null) {
            for(int y = (int)minMax.x; y < (int)minMax.y+1; ++y) {
                final float bsy = Converter.toScreenY(y);
                g.draw(sx, bsy, 4 + 5 * 32);
            }
        }

        for(int i = 0; i < SIZE; ++i) {
            int index = 0;

            if(i == 0) index = 4;
            else if(i == SIZE-1) index = 1;
            else index = 5;

            g.draw(sx, sy+8*(i-1), index + 4 * 32);
        }
        //g.draw(sx, sy, 5 + 4 * 32);
        // g.draw(sx, sy+8, 1 + 4 * 32);
    }
}
