package com.codalab.platformer.entities.player;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.CollisionMasks;
import com.codalab.platformer.entities.BaseEntity;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.utils.Converter;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;

public class PlayerCopyEntity extends BaseEntity {
    public static PlayerCopyEntity factory(GameWorld gameWorld, Vector2 position) {
        PlayerCopyEntity go = new PlayerCopyEntity(position);

        BodyDef bodyDef = new BodyDef();
        bodyDef.setType(BodyType.dynamicBody);
        bodyDef.setAngle(0);

        Body body = gameWorld.world.createBody(bodyDef);
        body.setSleepingAllowed(false);
        body.setUserData(go);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(box);
        fixtureDef.setFriction(0.1f);
        fixtureDef.setRestitution(0.4f);
        fixtureDef.setDensity(0.5f);

        Filter filter = new Filter();
        filter.setCategoryBits(CollisionMasks.Player);
        filter.setMaskBits(CollisionMasks.Buttons);

        fixtureDef.setFilter(filter);

        body.createFixture(fixtureDef);

        fixtureDef.delete();
        bodyDef.delete();
        box.delete();
        filter.delete();

        go.setBody(body);
        return go;
    }

    PlayerCopyEntity(Vector2 position) {
        super(position);
        super.name = "Player (Copy)";
    }

    @Override
    public void draw(MyGraphics g) {
        final float x = body.getPositionX();
        final float y = body.getPositionY();

        final float sx = Converter.toScreenX(x);
        final float sy = Converter.toScreenY(y);

        g.draw(sx, sy, 3 + 0 * 32);
    }

    public void applyForce(Vector2 force) {
        Vec2 box2DForce = force.toBox2D();
        body.applyForceToCenter(box2DForce, false);
        box2DForce.delete();
    }

}
