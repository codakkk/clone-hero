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

public class GoalEntity extends BaseEntity {
    public static GoalEntity factory(GameWorld gameWorld, Vector2 position) {
        GoalEntity go = new GoalEntity(position);

        BodyDef bodyDef = new BodyDef();
        bodyDef.setType(BodyType.staticBody);

        Body body = gameWorld.world.createBody(bodyDef);
        body.setSleepingAllowed(false);
        body.setUserData(go);

        PolygonShape box = new PolygonShape();
        box.setAsBox(2.5f, 0.25f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(box);
        fixtureDef.setIsSensor(true);

        Filter filter = new Filter();
        filter.setCategoryBits(CollisionMasks.Goal);
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

    private float animTime = 0.0f;

    GoalEntity(Vector2 position) {
        super(position);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.animTime += deltaTime;
    }

    @Override
    public void draw(MyGraphics g) {
        final float worldX = body.getPositionX();
        final float worldY = body.getPositionY();

        float screenX = Converter.toScreenX(worldX);
        float screenY = Converter.toScreenY(worldY);

        final float animDuration = 0.7f;
        int frameNumber = ((int)(this.animTime / animDuration)) % 2;

        for(int i = -2; i < 2; ++i) {
            g.draw(screenX + i*8, screenY, 6 + 2 * 32);
        }

        g.draw(screenX-4, screenY-2,  (4 + frameNumber) + 2 * 32);
    }

}
