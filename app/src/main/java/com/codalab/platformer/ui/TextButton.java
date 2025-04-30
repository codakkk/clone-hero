package com.codalab.platformer.ui;

import com.codalab.platformer.graphics.FontRenderer;
import com.codalab.platformer.graphics.MyGraphics;

public class TextButton extends Widget implements IWidgetClickable {

    private final String text;

    public IClickedCallback onClicked;

    public TextButton(String text) {
        super();
        this.text = text;

        super.width = text.length() * 8;
        super.height = 8;
    }

    @Override
    public void render(MyGraphics g) {
        FontRenderer.draw(g, text, (int)position.x, (int)position.y);
    }

    @Override
    public void onClick() {
        if(onClicked != null) {
            onClicked.onClicked();
        }
    }
}
