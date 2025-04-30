package com.codalab.platformer.entities;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.CollisionMasks;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.utils.Converter;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;

public class GateEntity extends BaseEntity implements IGate {
    public static GateEntity factory(GameWorld gameWorld, Vector2 position) {
        GateEntity go = new GateEntity(position);

        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(position.x, position.y);
        bodyDef.setType(BodyType.staticBody);

        Body body = gameWorld.world.createBody(bodyDef);
        body.setSleepingAllowed(false);
        body.setUserData(go);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(box);

        Filter filter = new Filter();
        filter.setCategoryBits(CollisionMasks.Buttons);
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

    private boolean isUnlocked = false;

    private float unlockingTime = 0.0f;

    GateEntity(Vector2 position) {
        super(position);
    }

    @Override
    public void update(float deltaTime) {
        if(this.unlockingTime > 1.0f) {
            return;
        }

        super.update(deltaTime);

        this.unlockingTime += deltaTime;
    }

    @Override
    public void draw(MyGraphics g) {
        final float worldX = body.getPositionX();
        final float worldY = body.getPositionY();

        final float screenX = Converter.toScreenX(worldX);
        final float screenY = Converter.toScreenY(worldY);

        if(isUnlocked) {
            if(unlockingTime >= 1.0f) {
                return;
            }
            int tileFrameNumber = (int)(unlockingTime / 0.25f);
            final int tileAnim = (0 + tileFrameNumber) + 3 * 32;

            int lockFrameNumber = (int)(unlockingTime / 0.33f);
            final int lockAnim = (6 + lockFrameNumber) + 3 * 32;

            g.draw(screenX, screenY, tileAnim);
            g.draw(screenX, screenY, lockAnim);
        } else {
            g.draw(screenX, screenY, 0 + 3 * 32);
            g.draw(screenX, screenY, 6 + 3 * 32);
        }
    }

    @Override
    public void onShouldOpen() {
        this.isUnlocked = true;
        this.unlockingTime = 0.0f;

        this.body.setActive(false);
    }
}
