package com.codalab.platformer.data;

import com.codalab.platformer.entities.BaseEntity;
import com.codalab.platformer.utils.IPoolable;

/** An unordered pair of game objects.
 *
 */
public class Collision implements IPoolable {
    public BaseEntity a, b;

    /// Mainly used by pool
    public Collision() {

    }

    public Collision(BaseEntity a, BaseEntity b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public int hashCode() {
        return a.hashCode() ^ b.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Collision))
            return false;
        Collision otherCollision = (Collision) other;
        return (a.equals(otherCollision.a) && b.equals(otherCollision.b)) ||
               (a.equals(otherCollision.b) && b.equals(otherCollision.a));
    }

    public void set(BaseEntity a, BaseEntity b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void onPool() {
        this.a = null;
        this.b = null;
    }

    public <T> boolean containsEntityOfType(Class<T> clazz) {
        return clazz.isInstance(a) || clazz.isInstance(b);
    }

    public <T> T getEntityOfType(Class<T> clazz) {
        if(clazz.isInstance(a)) {
            return clazz.cast(a);
        }

        if(clazz.isInstance(b)) {
            return clazz.cast(b);
        }

        return null;
    }
}
