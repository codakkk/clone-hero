package com.badlogic.androidgames.framework;

import com.badlogic.androidgames.framework.impl.PixmapRegion;
import com.badlogic.androidgames.framework.math.Box;

public interface Graphics {
    static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    Pixmap newPixmap(String fileName, PixmapFormat format);

    void clear(int color);

    void drawPixel(float x, float y, int color);

    void drawLine(float x, float y, float x2, float y2, int color);

    void drawLine(float x, float y, float x2, float y2, int color, float angle);

    void drawRect(float x, float y, float width, float height, int color, boolean filled);

    void drawRect(float x, float y, float width, float height, int color);

    void drawRect(float x, float y, float width, float height, int color, float angle);

    void drawRect(Box box, int color);

    void draw(Pixmap pixmap, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight);
    void draw(Pixmap pixmap, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, float angle);
    void draw(Pixmap pixmap, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, int color);

    void draw(Pixmap pixmap, float x, float y);

    void draw(PixmapRegion pixmapRegion, float x, float y);

    void drawText(String text, float x, float y, int color);

    int getWidth();

    int getHeight();
}
