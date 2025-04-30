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

import java.util.ArrayList;
import java.util.List;

public class KeyEntity extends BaseEntity implements IActivable {
    public static KeyEntity factory(GameWorld gameWorld, Vector2 position) {
        KeyEntity go = new KeyEntity(position);

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
        fixtureDef.setIsSensor(true);
        body.createFixture(fixtureDef);

        fixtureDef.delete();
        bodyDef.delete();
        box.delete();
        filter.delete();

        go.setBody(body);
        return go;
    }
    private boolean state = false;

    private final List<IGate> gates;

    KeyEntity(Vector2 position) {
        super(position);

        this.gates = new ArrayList<>();
    }

    @Override
    public void draw(MyGraphics g) {
        if(state) {
            return;
        }

        final float worldX = body.getPositionX();
        final float worldY = body.getPositionY();

        final float screenX = Converter.toScreenX(worldX);
        final float screenY = Converter.toScreenY(worldY);

        g.draw(screenX, screenY, 3 + 2 * 32);
    }

    @Override
    public void onActivated() {
        state = true;
        for(IGate gate : gates) {
            gate.onShouldOpen();
        }
    }

    @Override
    public boolean canActivate() {
        return !state && gates != null && !gates.isEmpty();
    }

    @Override
    public void addGate(IGate gate) {
        this.gates.add(gate);
    }
}
