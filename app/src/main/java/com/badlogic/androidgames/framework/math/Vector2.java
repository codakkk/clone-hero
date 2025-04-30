package com.badlogic.androidgames.framework.math;

import androidx.annotation.NonNull;

import com.google.fpl.liquidfun.Vec2;

public class Vector2 {
    public final static float TO_RADIANS = (1 / 180.0f) * (float)Math.PI;
    public final static float TO_DEGREES = (1 / (float)Math.PI) * 180;

    public final static Vector2 zero = new Vector2(0, 0);

    public float x;
    public float y;

    public Vector2() {
        this(0, 0);
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(Vec2 v) {
        this.x = v.getX();
        this.y = v.getY();
    }

    public Vector2(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2 copy() {
        return new Vector2(this);
    }

    public Vec2 toBox2D() {
        return new Vec2(this.x, this.y);
    }

    public Vector2 set(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2 add(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2 sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2 sub(Vector2 v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public Vector2 mul(float value) {
        this.x *= value;
        this.y *= value;
        return this;
    }

    public static float dot (float x1, float y1, float x2, float y2) {
        return x1 * x2 + y1 * y2;
    }

    public float dot (Vector2 v) {
        return x * v.x + y * v.y;
    }

    public float dot (float ox, float oy) {
        return x * ox + y * oy;
    }

    public float len() {
        return (float)Math.sqrt(x*x + y*y);
    }

    public Vector2 nor() {
        float len = len();
        if(len != 0) {
            this.x /= len;
            this.y /= len;
        }
        return this;
    }

    public float angle() {
        float angle = (float)Math.atan2(y, x) * TO_DEGREES;
        if(angle < 0)
            angle += 360;
        return angle;
    }

    public Vector2 rotate(float angle) {
        float rad = angle * TO_RADIANS;
        float cos = (float)Math.cos(rad);
        float sin = (float)Math.sin(rad);

        float newX = this.x * cos - this.y * sin;
        float newY = this.x * sin + this.y * cos;

        this.x = newX;
        this.y = newY;

        return this;
    }

    public float dist(Vector2 other) {
        float distX = this.x - other.x;
        float distY = this.y - other.y;
        return (float)Math.sqrt(distX*distX + distY*distY);
    }

    public float dist(float x, float y) {
        float distX = this.x - x;
        float distY = this.y - y;
        return (float)Math.sqrt(distX*distX + distY*distY);
    }

    public float distSquared(Vector2 other) {
        float distX = this.x - other.x;
        float distY = this.y - other.y;
        return distX*distX + distY*distY;
    }

    public float distSquared(float x, float y) {
        float distX = this.x - x;
        float distY = this.y - y;
        return distX*distX + distY*distY;
    }

    @NonNull
    @Override
    public String toString () {
        return "(" + x + "," + y + ")";
    }


    // Stolen from libgdx Vector2 class lol
    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        return result;
    }

    // Stolen from libgdx Vector2 class lol
    @Override
    public boolean equals (Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Vector2 other = (Vector2)obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) return false;
        return true;
    }

}
