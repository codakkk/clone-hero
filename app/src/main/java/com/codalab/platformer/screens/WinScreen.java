package com.codalab.platformer.screens;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Screen;
import com.codalab.platformer.data.Constants;
import com.codalab.platformer.graphics.FontRenderer;
import com.codalab.platformer.graphics.MyGraphics;

public class WinScreen extends Screen {
    public WinScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void render(MyGraphics g) {
        g.draw(10, 10, 0);

        final String msg = "YOU COMPLETED THE GAME!";
        FontRenderer.draw(g, msg, Constants.VIRTUAL_WIDTH / 2 - msg.length() / 2 * 8, Constants.VIRTUAL_HEIGHT / 2);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
