package com.codalab.platformer.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObjectPool<T extends IPoolable> {
    public interface PoolObjectFactory<T> {
        public T createObject();
    }

    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;

    public ObjectPool(int initialSize, PoolObjectFactory<T> factory) {
        this.factory = factory;
        this.freeObjects = new ArrayList<T>(initialSize);

        for(int i = 0; i < initialSize; ++i) {
            T item = newObject();
            item.onPool();
            this.freeObjects.add(item);
        }
    }

    public T newObject() {
        T object = null;

        if (freeObjects.isEmpty()) {
            object = factory.createObject();
        }
        else {
            object = freeObjects.remove(freeObjects.size() - 1);
        }

        return object;
    }

    public void free(T object) {
        assert(object != null);

        object.onPool();
        freeObjects.add(object);
    }

    public void freeAll(Collection<T> collection) {
        for(T item : collection)
            free(item);
    }
}
