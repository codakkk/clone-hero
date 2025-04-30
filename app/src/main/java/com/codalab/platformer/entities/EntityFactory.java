package com.codalab.platformer.entities;


import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.levels.GameWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;


public class EntityFactory {

    // TODO(ciro): must kill these statics. Just for fast prototyping
    private static GameWorld world = null;

    private static final Map<Class<? extends BaseEntity>, BiFunction<GameWorld, Vector2, BaseEntity>> entityFuncs = new HashMap<>();

    public static void cleanup() {
        world = null;
    }

    public static void initialize(GameWorld gameWorld) {
        world = gameWorld;
    }

    public static <T extends BaseEntity> void registerEntity(Class<T> id, BiFunction<GameWorld, Vector2, BaseEntity> function) {
        entityFuncs.put(id, function);
    }

    public static boolean hasEntity(Class<BaseEntity> id) {
        return entityFuncs.containsKey(id);
    }

    public static <T extends BaseEntity> T createEntity(Class<T> id, Vector2 position) {
        if (entityFuncs.containsKey(id)) {
            BaseEntity result = entityFuncs.get(id).apply(world, position);
            if(result == null) {
                // What happened?
                throw new UnsupportedOperationException();
            }

            world.addEntity(result);
            return (T) result;
        } else {
            throw new NoSuchElementException("Unable to create entity of type " + id);
        }
    }
}