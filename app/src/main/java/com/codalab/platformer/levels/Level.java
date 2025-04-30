package com.codalab.platformer.levels;

import com.badlogic.androidgames.framework.math.Vector2;
import com.codalab.platformer.CollisionMasks;
import com.codalab.platformer.entities.BaseEntity;
import com.codalab.platformer.entities.EntityFactory;
import com.codalab.platformer.entities.IActivable;
import com.codalab.platformer.entities.GateEntity;
import com.codalab.platformer.entities.GoalEntity;
import com.codalab.platformer.entities.player.PlayerEntity;
import com.codalab.platformer.tiles.BaseTile;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.Filter;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Level {

    protected String name;

    protected int startingShells;
    protected Vector2 spawnPoint;
    protected Vector2 flagPoint;

    protected int[] tiles;

    public final List<Body> tileBodies = new ArrayList<>();

    protected final Map<Vector2, Class<? extends BaseEntity>> entities = new HashMap<>();


    // Entity Position, Gates
    protected final Map<Vector2, Vector2[]> gates = new HashMap<>();

    protected int width;
    protected int height;

    Level() {}

    public void injectInto(GameWorld gameWorld) {
        PlayerEntity player = EntityFactory.createEntity(PlayerEntity.class, this.spawnPoint);
        player.shells = getStartingShells();

        EntityFactory.createEntity(GoalEntity.class, flagPoint);

        int skippedTiles = createTileColliders(gameWorld);
        int noOfEntities = loadEntities();

        System.out.println("===== Level loaded =====");
        System.out.println("Level name: " + name);
        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        System.out.println("Shots: " + startingShells);
        System.out.println("Spawn: " + spawnPoint);
        System.out.println("Flag: " + flagPoint);
        System.out.println("Entities: " + noOfEntities);
        System.out.println("= Debug =");
        System.out.println("Skipped tiles: " + skippedTiles);
        System.out.println("========================");
    }

    private int createTileColliders(GameWorld gameWorld) {
        this.tileBodies.clear();

        int skippedTiles = 0;
        // Create entities of tile only for touchable tiles (externals only)
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                if(tiles[x + y * width] != BaseTile.solid.id) {
                    continue;
                }

                // Check if the collision must be created
                boolean up = getTile(x, y - 1) == BaseTile.solid;
                boolean left = getTile(x - 1, y) == BaseTile.solid;
                boolean right = getTile(x + 1, y) == BaseTile.solid;
                boolean down = getTile(x, y + 1) == BaseTile.solid;
                boolean createCollider = !(up && left && down && right);

                if(!createCollider) {
                    skippedTiles++;
                    continue;
                }

                Body body = createTileCollider(gameWorld, x, y);
                tileBodies.add(body);
            }
        }

        return skippedTiles;
    }

    private int loadEntities() {
        int noOfEntities = 0;
        for(Map.Entry<Vector2, Class<? extends BaseEntity>> entry : entities.entrySet()) {
            Vector2 pos = entry.getKey();
            Class<? extends BaseEntity> type = entry.getValue();

            BaseEntity baseEntity = EntityFactory.createEntity(type, pos);
            noOfEntities++;

            if(baseEntity instanceof IActivable && gates.containsKey(pos)) {
                IActivable button = (IActivable) baseEntity;

                for(Vector2 item : gates.get(pos)) {
                    GateEntity gate = EntityFactory.createEntity(GateEntity.class, item);
                    button.addGate(gate);
                }
            }
        }

        return noOfEntities;
    }

    public BaseTile getTile(int x, int y) {
        if(x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        int id = tiles[x + y * width];
        return BaseTile.tiles[id];
    }

    public String getName() {
        return this.name;
    }

    public int getStartingShells() {
        return this.startingShells;
    }

    private static Body createTileCollider(GameWorld gw, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(x, y);
        bodyDef.setType(BodyType.staticBody);

        Body body = gw.world.createBody(bodyDef);
        body.setSleepingAllowed(false);

        PolygonShape box = new PolygonShape();
        box.setAsBox(0.5f, 0.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(box);
        fixtureDef.setFriction(8.0f);
        fixtureDef.setRestitution(0.0f);
        fixtureDef.setDensity(8.0f);

        Filter filter = new Filter();
        filter.setCategoryBits(CollisionMasks.Tile);
        filter.setMaskBits(CollisionMasks.Player);

        fixtureDef.setFilter(filter);
        body.createFixture(fixtureDef);

        fixtureDef.delete();
        box.delete();
        filter.delete();

        bodyDef.delete();

        return body;
    }
}
