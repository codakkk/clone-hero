package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Pixmap;

public class PixmapRegion {
    final Pixmap pixmap;

    final int srcX;
    final int srcY;

    final int width;
    final int height;

    /** Constructs a region the size of the specified pixmap. */
    public PixmapRegion (Pixmap pixmap) {
        this(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight());
    }

    public PixmapRegion (Pixmap pixmap, int width, int height) {
        this(pixmap, 0, 0, width, height);
    }

    public PixmapRegion (Pixmap pixmap, int x, int y, int width, int height) {
        if (pixmap == null) {
            throw new IllegalArgumentException("pixmap cannot be null.");
        }
        this.pixmap = pixmap;
        this.srcX = x;
        this.srcY = y;
        this.width = width;
        this.height = height;
    }


}
