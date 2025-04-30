package com.codalab.platformer.ui;

import com.codalab.platformer.graphics.MyGraphics;

public class IconButton extends Widget implements IWidgetClickable {
    private final int tile;

    public IClickedCallback onClicked;

    public IconButton(int tile) {
        super();
        this.tile = tile;

        super.width = 8;
        super.height = 8;
    }

    @Override
    public void render(MyGraphics g) {
        g.draw((int)position.x, (int)position.y, tile);
    }

    @Override
    public void onClick() {
        if(onClicked != null) {
            onClicked.onClicked();
        }
    }
}
