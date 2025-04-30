package com.codalab.platformer.levels;

import com.badlogic.androidgames.framework.Input;
import com.codalab.platformer.MyContactListener;
import com.codalab.platformer.data.Collision;
import com.codalab.platformer.data.Constants;
import com.codalab.platformer.entities.EntityFactory;
import com.codalab.platformer.entities.EntityManager;
import com.codalab.platformer.entities.BaseEntity;
import com.codalab.platformer.entities.GateEntity;
import com.codalab.platformer.entities.GoalEntity;
import com.codalab.platformer.entities.GoldEntity;
import com.codalab.platformer.entities.IActivable;
import com.codalab.platformer.entities.ICollectable;
import com.codalab.platformer.entities.IDamageable;
import com.codalab.platformer.entities.KeyEntity;
import com.codalab.platformer.entities.MovingTileEntity;
import com.codalab.platformer.entities.ParticleEntity;
import com.codalab.platformer.entities.player.PlayerCopyEntity;
import com.codalab.platformer.entities.player.PlayerEntity;
import com.badlogic.androidgames.framework.math.Box;
import com.codalab.platformer.entities.PressurePlateEntity;
import com.codalab.platformer.entities.SpikeEntity;
import com.codalab.platformer.entities.TextEntity;
import com.codalab.platformer.graphics.FontRenderer;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.input.TouchConsumer;
import com.codalab.platformer.screens.GameScreen;
import com.codalab.platformer.tiles.BaseTile;
import com.codalab.platformer.utils.ReadOnlyList;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.World;

import java.util.Collection;
import java.util.List;

public class GameWorld {
    // Simulation
    private final EntityManager entityManager = new EntityManager();

    public World world;

    private final MyContactListener contactListener;

    final Box currentView;
    private final TouchConsumer touchConsumer;

    private final GameScreen gameScreen;


    public PlayerEntity player;
    public int currentLevelShots = 1;

    public Level currentLevel;

    public GameWorld(GameScreen gameScreen) {
        this.gameScreen = gameScreen;

        this.currentView = Constants.physicalSize;

        this.contactListener = new MyContactListener();

        touchConsumer = new TouchConsumer(this);

        registerEntities();
    }

    private void registerEntities() {
        EntityFactory.initialize(this);

        EntityFactory.registerEntity(PlayerEntity.class, PlayerEntity::factory);
        EntityFactory.registerEntity(GateEntity.class, GateEntity::factory);
        EntityFactory.registerEntity(GoalEntity.class, GoalEntity::factory);
        EntityFactory.registerEntity(GoldEntity.class, GoldEntity::factory);
        EntityFactory.registerEntity(ParticleEntity.class, ParticleEntity::factory);
        EntityFactory.registerEntity(PlayerCopyEntity.class, PlayerCopyEntity::factory);
        EntityFactory.registerEntity(PressurePlateEntity.class, PressurePlateEntity::factory);
        EntityFactory.registerEntity(SpikeEntity.class, SpikeEntity::factory);
        EntityFactory.registerEntity(TextEntity.class, TextEntity::factory);
        EntityFactory.registerEntity(MovingTileEntity.class, MovingTileEntity::factory);
        EntityFactory.registerEntity(KeyEntity.class, KeyEntity::factory);
    }

    // Adds an entity and sets it alive
    public void addEntity(BaseEntity obj) {
        obj.gameWorld = this;
        obj.isAlive = true;
        entityManager.addEntity(obj);

        if(obj instanceof PlayerEntity) {
            player = (PlayerEntity) obj;
        }
    }

    public void removeEntity(BaseEntity obj) {
        entityManager.removeEntity(obj.getId());

        Body body = obj.getBody();
        if(body != null) {
            world.destroyBody(body);
            body.delete();
        }
    }

    public void removeAllEntities() {
        for(BaseEntity e : entityManager.getEntities()) {
            Body body = e.getBody();

            if(body == null) {
                continue;
            }

            world.destroyBody(body);
            body.delete();
            e.setBody(null);
        }

        for(Body b : currentLevel.tileBodies) {
            world.destroyBody(b);
        }

        currentLevel.tileBodies.clear();

        this.entityManager.clear();
    }

    public void reset(Level nextLevelIfAny) {
        if(this.world != null) {
            removeAllEntities();
            this.world.clearForces();
        } else {
            this.world = new World(0, 10);  // gravity vector
            this.world.setContactListener(contactListener);
        }

        if(nextLevelIfAny != null) {
            this.currentLevelShots = 1;
            this.currentLevel = nextLevelIfAny;
        } else {
            currentLevelShots++;
        }

        this.currentLevel.injectInto(this);
    }

    public void onInput(List<Input.TouchEvent> items) {
        for (Input.TouchEvent event : items) {
            touchConsumer.consumeTouchEvent(event);
        }
    }

    public void update(float elapsedTime) {
        if(player.isCharging) {
            elapsedTime *= 0.35f;
        }

        BaseTile.time += elapsedTime;

        ReadOnlyList<BaseEntity> entities = entityManager.getEntities();

        // Entities are in reverse order so that we can add/remove
        // new entities between updates
        for(int i = entities.size()-1; i >= 0; --i) {
            entities.get(i).update(elapsedTime);
        }

        // advance the physics simulation
        world.step(elapsedTime, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS, Constants.PARTICLE_ITERATIONS);

        // Prevent collision handling if player is dead
        if(!player.isAlive) {
            return;
        }

        Collection<Collision> collisions = contactListener.use();
        updateCollisions(collisions);
        contactListener.finish(collisions);
    }

    public void render(MyGraphics g) {
        // First render the tile map

        for(int y = 0; y < currentLevel.height; ++y) {
            for(int x = 0; x < currentLevel.width; ++x) {
                BaseTile tile = currentLevel.getTile(x, y);

                if(tile == null) {
                    continue; // air
                }

                tile.render(g, this, currentLevel, x, y);
            }
        }

        for (BaseEntity obj : entityManager.getEntities()) {
            if(obj == player) {
                continue;
            }

            obj.draw(g);
        }

        // We always want the player drawn at the end
        // To prevent overlapping
        player.draw(g);
    }

    public void renderUi(MyGraphics g) {
        if(currentLevel != null) {
            final int shellStartX = 28;
            final int shellStartY = 8;

            FontRenderer.draw(g, player.shells + "x", 8, shellStartY - 4);
            for(int i = 0; i < currentLevel.getStartingShells(); ++i) {

                float x = shellStartX + i * 6;

                g.draw(x, shellStartY, 0 + 8 * 32);

                final boolean isFull = i < player.shells;

                if(isFull) {
                    g.draw(x, shellStartY, 1 + 8 * 32);
                }
            }
        }
    }

    private void updateCollisions(Collection<Collision> collisions) {
        if(!player.isAlive) {
            return;
        }

        for(Collision c : collisions) {
            IActivable activable = c.getEntityOfType(IActivable.class);
            if(activable != null && activable.canActivate()) {
                activable.onActivated();
            } else if(c.containsEntityOfType(PlayerEntity.class)) {
                if(c.containsEntityOfType(IDamageable.class)) {
                    onDie();
                } else if(c.containsEntityOfType(GoalEntity.class)) {
                    gameScreen.changeState(GameScreen.GameState.won);
                } else {
                    ICollectable collectable = c.getEntityOfType(ICollectable.class);
                    if(collectable != null) {
                        collectable.onCollect();
                    }
                }
            }
        }

        Body playerBody = player.getBody();
        if(!Constants.physicalSize.isInBounds(playerBody.getPositionX(), playerBody.getPositionY())) {
            onDie();
        }
    }


    @Override
    protected void finalize() {
        world.delete();
    }

    private void onDie() {
        player.kill();
        gameScreen.changeState(GameScreen.GameState.dead);
    }
}
