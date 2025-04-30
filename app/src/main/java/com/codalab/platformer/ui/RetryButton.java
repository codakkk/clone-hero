package com.codalab.platformer.ui;

import com.codalab.platformer.graphics.MyGraphics;

public class RetryButton extends Widget implements IWidgetClickable {
    public IClickedCallback onClicked;

    public RetryButton() {
        this.width = 16;
        this.height = 16;
    }

    @Override
    public void render(MyGraphics g) {
        g.draw(position.x, position.y, 7 + 1 * 32);
        g.draw(position.x + 8, position.y, 8 + 1 * 32);
        g.draw(position.x, position.y+8, 7 + 2 * 32);
        g.draw(position.x + 8, position.y+8, 8 + 2 * 32);
    }

    @Override
    public void onClick() {
        if(onClicked != null) {
            this.onClicked.onClicked();
        }
    }
}
