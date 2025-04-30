package com.codalab.platformer.ui;

import com.codalab.platformer.graphics.FontRenderer;
import com.codalab.platformer.graphics.MyGraphics;

public class TextWidget extends Widget {
    public enum TextAlignment {
        left,
        center,
        right,
    }

    private String text;
    private TextAlignment alignment;

    private int color;

    public TextWidget() {
        this("", TextAlignment.left, 0xFF000000);
    }

    public TextWidget(String text, TextAlignment alignment, int color) {
        this.text = text;
        this.alignment = alignment;
        this.color = color;
    }

    @Override
    public void render(MyGraphics g) {
        int xx = 0;
        int yy = 0;

        switch(alignment) {
            case left:
                xx = (int)(position.x);
                yy = (int)(position.y);
                break;
            case center:
                xx = (int)(position.x + width * 0.5f - text.length() * 4);
                yy = (int)(position.y + height * 0.5f - 4);
                break;
            case right:
                xx = (int)(position.x + text.length() * 8);
                yy = (int)(position.y + height * 0.5f - 4);
                break;
        }
        FontRenderer.draw(g, text, xx, yy, color);
    }

    public void setText(String text) {
        this.text = text;
    }
}
