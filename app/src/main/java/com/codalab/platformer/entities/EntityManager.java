package com.codalab.platformer.entities;

import com.codalab.platformer.utils.ReadOnlyList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EntityManager {
    // should be static but who knows, maybe someday we will have more than one EntityManager lol
    private int instances = 1;

    private final List<BaseEntity> entities = new ArrayList<>(10);

    /// 🤮🤮 Unluckly this Integer does boxing 🤮🤮
    private final HashMap<Integer, BaseEntity> entityHashMap = new HashMap<>();
    private final Set<BaseEntity> entitiesSet = new HashSet<>();
    private final ReadOnlyList<BaseEntity> readOnlyEntities = new ReadOnlyList<>(entities);

    /// Registers a new entity
    public void addEntity(BaseEntity e) {
        e.setId(instances++);

        boolean result = this.entitiesSet.add(e);
        if(result) {
            this.entityHashMap.put(e.getId(), e);
            this.entities.add(e);
        }
    }

    public void removeEntity(int entityId) {
        BaseEntity entity = entityHashMap.remove(entityId);
        if(entity != null) {
            entitiesSet.remove(entity);
            entities.remove(entity);
        }
    }

    public void removeEntity(BaseEntity e) {
        removeEntity(e.getId());
    }

    public ReadOnlyList<BaseEntity> getEntities() {
        return this.readOnlyEntities;
    }

    public void clear() {
        this.entities.clear();
        this.entityHashMap.clear();
        this.entitiesSet.clear();
    }
}
