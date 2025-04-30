package com.codalab.platformer.screens;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.codalab.platformer.CloudManager;
import com.codalab.platformer.graphics.FadeInOut;
import com.codalab.platformer.graphics.FontRenderer;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.levels.GameWorld;
import com.codalab.platformer.levels.LevelManager;
import com.codalab.platformer.ui.layouts.PlayingLayout;
import com.codalab.platformer.ui.layouts.WinLayout;

import java.util.List;

public class GameScreen extends Screen {

    public enum GameState {
        playing,
        dead,
        won,
    }

    private final LevelManager levelManager;
    private final GameWorld gameWorld;
    private final FadeInOut fadeAnimation;

    private GameState currentState = GameState.playing;
    private boolean forceReset = false;

    private final PlayingLayout playingLayout;
    private final WinLayout winLayout;

    private final CloudManager cloudManager = new CloudManager();

    public GameScreen(Game game) {
        super(game);

        this.levelManager = new LevelManager();
        this.fadeAnimation = new FadeInOut();

        this.gameWorld = new GameWorld(this);

        cloudManager.randomize();
        this.gameWorld.reset(levelManager.getCurrentLevel());

        playingLayout = new PlayingLayout(() -> {
            forceReset = true;
            fadeAnimation.start();
        });
        winLayout = new WinLayout(fadeAnimation::start);


        fadeAnimation.fadeInCompletedCallback = () -> {
            if(!gameWorld.player.isAlive || forceReset) {
                this.gameWorld.reset(null);
                this.forceReset = false;
            } else {
                if(this.levelManager.isLastLevel()) {
                    game.setScreen(new WinScreen(game));
                } else {
                    this.gameWorld.reset(this.levelManager.nextLevel());
                }
            }

            cloudManager.randomize();

            changeState(GameState.playing);
        };
    }

    @Override
    public void update(float deltaTime) {
        final List<Input.TouchEvent> events = game.getInput().getTouchEvents();

        this.cloudManager.update(deltaTime);

        switch(currentState) {
            case playing:
                playingLayout.update(deltaTime);
                playingLayout.updateChildren(events);

                gameWorld.onInput(events);
                gameWorld.update(deltaTime);
                break;
            case won:
                winLayout.update(deltaTime);
                winLayout.updateChildren(events);
                break;
        }

        fadeAnimation.update(deltaTime);
    }

    @Override
    public void render(MyGraphics g) {
        g.clear(0xFF3A88FE);

        this.gameWorld.render(g);

        this.cloudManager.render(g);

        this.gameWorld.renderUi(g);

        this.playingLayout.render(g);
        this.winLayout.render(g);

        if(currentState == GameState.playing) {
            // Ugly but does his job lol
            int levelIndex = levelManager.getCurrentLevelIndex();
            FontRenderer.draw(g, "SHOT " + gameWorld.currentLevelShots + " LVL " + (levelIndex+1), 260, 4);

            if(levelIndex == 0) {
                FontRenderer.draw(g, "    TAP AND HOLD    ", 64, 32);
                FontRenderer.draw(g, "TO SHOT A NEW CLONE ", 64, 44);
            } else if(levelIndex == 1) {
                FontRenderer.draw(g, "    BE AWARE    ", 124, 80);
                FontRenderer.draw(g, " AMMO'S LIMITED ", 124, 92);
            }
        }

        fadeAnimation.render(g);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

    public void changeState(GameState newState) {
        if(currentState == newState) {
            return;
        }

        this.currentState = newState;

        switch(newState) {
            case playing:
                this.winLayout.hide();
                this.playingLayout.show();
                break;
            case dead:
                this.fadeAnimation.start();
                break;
            case won:
                String msg = "YOU WON";
                if(gameWorld.currentLevelShots == 1) {
                    msg = "!! WON IN ONE SHOT!!";
                }
                this.winLayout.set(msg);
                this.winLayout.show();
                break;
        }
    }
}
