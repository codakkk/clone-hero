package com.codalab.platformer.entities.player;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.CollisionMasks;
import com.codalab.platformer.entities.BaseEntity;
import com.codalab.platformer.entities.EntityFactory;
import com.codalab.platformer.data.Constants;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.utils.Converter;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Vec2;

public class PlayerEntity extends BaseEntity {
    public static PlayerEntity factory(GameWorld gameWorld, Vector2 position) {
        PlayerEntity entity = new PlayerEntity(position);

        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(position.x, position.y);
        bodyDef.setType(BodyType.dynamicBody);

        // a body
        Body body = gameWorld.world.createBody(bodyDef);
        body.setSleepingAllowed(false);
        body.setUserData(entity);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.45f);
        // PolygonShape shape = new PolygonShape();
        // shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
        fixtureDef.setFriction(0.01f);
        fixtureDef.setRestitution(0.1f);
        fixtureDef.setDensity(1.0f);

        Filter filter = new Filter();
        filter.setCategoryBits(CollisionMasks.Player);
        filter.setMaskBits(CollisionMasks.Buttons | CollisionMasks.Killers | CollisionMasks.Goal | CollisionMasks.Collectible);

        fixtureDef.setFilter(filter);
        body.createFixture(fixtureDef);

        // clean up native objects
        fixtureDef.delete();
        bodyDef.delete();
        shape.delete();
        filter.delete();

        entity.setBody(body);
        return entity;
    }

    private static final float PARTICLE_SPAWN_TIME = 0.15f;

    public boolean isCharging;

    public float chargingForce = 0.0f;

    public int shells = 0;


    public final Vector2 mouseWorldPosition = new Vector2();

    private final PlayerRange playerRange = new PlayerRange();
    private final PlayerTrail playerTrail = new PlayerTrail(this);


    PlayerEntity(Vector2 position) {
        super(position);
        super.name = "Player";
    }

    @Override
    public void update(float deltaTime) {
        if(!isAlive) {
            return;
        }

        super.update(deltaTime);

        if(isCharging) {
            updateCharging(deltaTime);

            playerRange.update(deltaTime);
        }

        Vec2 linearVelocity = body.getLinearVelocity();
        Vector2 velocity = new Vector2(linearVelocity);
        linearVelocity.delete();

        if(velocity.len() > 0.1f) {
            playerTrail.update(deltaTime);
        }
    }

    @Override
    public void draw(MyGraphics g) {
        final float sx = Converter.toScreenX(this.position.x);
        final float sy = Converter.toScreenY(this.position.y);


        g.draw(sx, sy, isAlive ? 0 : 3);

        if(isCharging && isAlive) {
            final float fullWidth = 20;

            final float width = chargingForce * fullWidth;

            final float startX = sx - 8;
            final float startY = sy - 8;

            final int green = 0xFF99E550;
            final int orange = 0xFFDF7126;
            final int red = 0xFFD95763;
            final int color = chargingForce < 0.3f ? green : chargingForce < 0.6f ? orange : red;

            g.drawRect(startX - 2, startY-1, width, 3, color);

            g.draw(startX, startY, 0 + 9 * 32);
            g.draw(startX+8, startY, 1 + 9 * 32);
            g.draw(startX+8*2, startY, 2 + 9 * 32);


            this.playerRange.draw(g, this.position, this.mouseWorldPosition);
        }
    }

    public void shoot(Vector2 direction) {
        if(shells == 0) {
            return;
        }

        final float force = 1000 * chargingForce;
        Vector2 forceDirection = direction.mul(force);

        Vec2 box2DForce = forceDirection.toBox2D();
        body.setLinearVelocity(0.0f, 0.0f);
        body.applyForceToCenter(box2DForce, false);

        box2DForce.delete();

        forceDirection.x = forceDirection.x * -0.1f;
        forceDirection.y = 1f;

        final PlayerCopyEntity copy = EntityFactory.createEntity(PlayerCopyEntity.class, this.position);
        copy.applyForce(forceDirection);

        // Force spawn particle when shooting himself
        playerTrail.spawnParticle();

        shells--;
    }

    public void startCharging() {
        if(isCharging || shells == 0) {
            return;
        }

        this.isCharging = true;
        this.chargingForce = 0.0f;
    }

    public void stopCharging() {
        if(!isCharging) {
            return;
        }

        this.isCharging = false;
        this.chargingForce = 0.0f;
    }

    private void updateCharging(float deltaTime) {
        if(shells == 0) {
            stopCharging();
            return;
        }
        chargingForce += deltaTime * Constants.PLAYER_CHARGING_SPEED;
        if(chargingForce > 1.0f) {
            chargingForce = 1.0f;
        }
    }

    public void kill() {
        isAlive = false;
        stopCharging();

        Vec2 dir = new Vector2(body.getLinearVelocity())
                .mul(-1.0f)
                .nor()
                .mul(500)
                .toBox2D();

        this.body.applyForceToCenter(dir, false);

        dir.delete();
    }
}
