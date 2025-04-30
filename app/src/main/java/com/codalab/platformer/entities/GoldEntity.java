package com.codalab.platformer.entities;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.CollisionMasks;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.utils.Converter;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;

public class GoldEntity extends BaseEntity implements ICollectable {
    public static GoldEntity factory(GameWorld gameWorld, Vector2 position) {
        GoldEntity go = new GoldEntity(position);

        // a body definition: position and type
        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(position.x, position.y);
        bodyDef.setType(BodyType.kinematicBody);

        // a body
        Body body = gameWorld.world.createBody(bodyDef);
        body.setSleepingAllowed(false);
        body.setUserData(go);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.45f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
        fixtureDef.setIsSensor(true);

        Filter filter = new Filter();
        filter.setCategoryBits(CollisionMasks.Collectible);
        filter.setMaskBits(CollisionMasks.Player);

        fixtureDef.setFilter(filter);
        body.createFixture(fixtureDef);

        // clean up native objects
        fixtureDef.delete();
        bodyDef.delete();
        shape.delete();
        filter.delete();

        go.setBody(body);
        return go;
    }

    private boolean hasBeenCollected;

    GoldEntity(Vector2 position) {
        super(position);
    }

    @Override
    public void draw(MyGraphics g) {
        if(hasBeenCollected) {
            return;
        }

        final float x = body.getPositionX();
        final float y = body.getPositionY();

        final float sx = Converter.toScreenX(x);
        final float sy = Converter.toScreenY(y);

        g.draw(sx, sy, 4 + 1 * 32);
    }

    @Override
    public void onCollect() {
        this.hasBeenCollected = true;

        this.body.setActive(false);
    }

    @Override
    public boolean isCollected() {
        return hasBeenCollected;
    }
}
