package com.codalab.platformer;

import com.codalab.platformer.data.Collision;
import com.codalab.platformer.entities.BaseEntity;
import com.codalab.platformer.utils.ObjectPool;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

import java.util.Collection;
import java.util.HashSet;


public class MyContactListener extends ContactListener {

    private final ObjectPool<Collision> pool = new ObjectPool<>(10, Collision::new);
    private final Collection<Collision> cache = new HashSet<>();

    public Collection<Collision> use() {
        Collection<Collision> result = new HashSet<>(cache);
        cache.clear();
        return result;
    }

    public void finish(Collection<Collision> collisions) {
        for(Collision c : collisions) {
            pool.free(c);
        }
    }

    /** Warning: this method runs inside world.step
     *  Hence, it cannot change the physical world.
     */
    @Override
    public void beginContact(Contact contact) {
        //Log.d("MyContactListener", "Begin contact");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        Body ba = fa.getBody();
        Body bb = fb.getBody();

        Object userdataA = ba.getUserData();
        Object userdataB = bb.getUserData();

        if(userdataA == null || userdataB == null) {
            return;
        }

        BaseEntity a = (BaseEntity)userdataA;
        BaseEntity b = (BaseEntity)userdataB;

        Collision c = pool.newObject();
        c.set(a, b);

        cache.add(c);
    }
}
