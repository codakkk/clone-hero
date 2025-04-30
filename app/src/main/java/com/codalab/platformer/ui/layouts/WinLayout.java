package com.codalab.platformer.ui.layouts;

import com.codalab.platformer.data.Constants;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.ui.IClickedCallback;
import com.codalab.platformer.ui.Layout;
import com.codalab.platformer.ui.TextButton;
import com.codalab.platformer.ui.TextWidget;

public class WinLayout extends Layout {
    private final TextWidget wonText;
    private float winTime;

    public WinLayout(IClickedCallback onNext) {
        TextButton nextButton = new TextButton("NEXT");
        nextButton.onClicked = onNext;
        nextButton.setPosition((int)(Constants.VIRTUAL_WIDTH * 0.5f - nextButton.getWidth() * 0.5f), 72);
        this.addChild(nextButton);

        this.wonText = new TextWidget("YOU WON", TextWidget.TextAlignment.center, 0xFF000000);
        this.addChild(wonText);

        this.setEnabled(false);
    }

    @Override
    public void update(float deltaTime) {
        if(!enabled) {
            return;
        }

        if(winTime < 1.0f) {
            winTime += deltaTime * 5.5f;
        }
        super.update(deltaTime);
    }

    @Override
    public void render(MyGraphics g) {
        if(!enabled) {
            return;
        }

        final int y = 32;
        final int x = 0;
        final int width = Constants.VIRTUAL_WIDTH;
        final int height = 32;

        g.drawRect(x+ (1-winTime)*width*0.5f, y, width * winTime, height, 0xFFffa300);

        super.render(g);
    }

    public void set(String text) {
        final int y = 32;
        final int x = 0;
        final int width = Constants.VIRTUAL_WIDTH;
        final int height = 32;

        wonText.setText(text);
        this.wonText.setPosition(x + width / 2, y + height / 2);
    }
}
