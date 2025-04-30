package com.codalab.platformer.entities;

import androidx.annotation.NonNull;

import com.badlogic.androidgames.framework.math.Box;
import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.graphics.MyGraphics;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;

public abstract class BaseEntity {
    protected int id;
    protected String name;
    protected Body body;

    public GameWorld gameWorld;
    public boolean isAlive;

    public final Vector2 position;

    protected BaseEntity(Vector2 position) {
        this.position = new Vector2(position);
    }

    public void draw(MyGraphics g) {}

    /// ALWAYS CALL super.update
    public void update(float deltaTime) {
        if(body != null) {
            position.set(body.getPositionX(), body.getPositionY());
        }
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public boolean isVisible(Box view) {
        return (position.x < view.xmin || position.x > view.xmax || position.y < view.ymin || position.y > view.ymax);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Body getBody() {
        return this.body;
    }
    public void setBody(Body body) {
        this.body = body;

        if(body != null) {
            this.body.setTransform(new Vec2(position.x, position.y), 0.0f);
        }
    }
}
