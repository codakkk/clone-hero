package com.codalab.platformer.entities;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.CollisionMasks;
import com.codalab.platformer.graphics.FontRenderer;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.utils.Converter;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;

public class TextEntity extends BaseEntity {
    public static TextEntity factory(GameWorld gameWorld, Vector2 position) {
        TextEntity go = new TextEntity(position);

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

    public String text;

    private float lifeTimeInSeconds;

    TextEntity(Vector2 position) {
        super(position);
        this.lifeTimeInSeconds = 0.5f;
    }

    @Override
    public void update(float deltaTime) {
        if(this.lifeTimeInSeconds <= 0.0f) {
            return;
        }

        super.update(deltaTime);

        this.lifeTimeInSeconds -= deltaTime;
        this.position.y -= 4.0f * deltaTime;
    }

    @Override
    public void draw(MyGraphics g) {
        if(this.lifeTimeInSeconds <= 0.0f) {
            return;
        }

        int sx = (int) Converter.toScreenX(this.position.x);
        int sy = (int) Converter.toScreenY(this.position.y);

        FontRenderer.draw(g, text, sx - text.length()/2*8, sy, 0xFFFF0000);
    }
}
