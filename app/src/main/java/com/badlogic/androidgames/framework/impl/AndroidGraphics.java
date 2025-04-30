package com.badlogic.androidgames.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.math.Box;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    RectF dstRect = new RectF();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        Config config = null;
        if (format == PixmapFormat.RGB565)
            config = Config.RGB_565;
        else if (format == PixmapFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = PixmapFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;

        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void drawPixel(float x, float y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(float x, float y, float x2, float y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawLine(float x, float y, float x2, float y2, int color, float angle) {
        canvas.save();
        canvas.rotate(angle, x, y);
        drawLine(x, y, x2, y2, color);

        canvas.restore();
    }

    @Override
    public void drawRect(float x, float y, float width, float height, int color, boolean filled) {
        paint.setColor(color);
        if(filled) {
            paint.setStyle(Style.FILL);
        } else {
            paint.setStyle(Style.STROKE);
        }
        canvas.drawRect(x, y, x + width, y + height, paint);
    }

    @Override
    public void drawRect(float x, float y, float width, float height, int color) {
        drawRect(x, y, width, height, color, true);
    }

    @Override
    public void drawRect(float x, float y, float width, float height, int color, float angle) {
        canvas.save();
        canvas.rotate(angle, x+width/2, y+height/2);
        drawRect(x, y, width, height, color, true);

        canvas.restore();
    }

    @Override
    public void drawRect(Box box, int color) {
        paint.setColor(color);
        paint.setStyle(Style.STROKE);
        canvas.drawRect(box.xmin, box.ymin, box.xmax, box.ymax, paint);
    }

    @Override
    public void draw(Pixmap pixmap, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);
    }

    @Override
    public void draw(Pixmap pixmap, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, float angle) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        canvas.rotate(angle);
        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);
        canvas.rotate(-angle);
    }

    @Override
    public void draw(Pixmap pixmap, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight, int color) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;

        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, paint);
        paint.setColorFilter(null);
    }

    @Override
    public void draw(Pixmap pixmap, float x, float y) {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
    }

    @Override
    public void draw(PixmapRegion pixmapRegion, float x, float y) {
        AndroidPixmap pixmap = (AndroidPixmap) pixmapRegion.pixmap;

        srcRect.left = pixmapRegion.srcX;
        srcRect.top = pixmapRegion.srcY;
        srcRect.right = srcRect.left + pixmapRegion.width;
        srcRect.bottom = srcRect.top + pixmapRegion.height;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + pixmapRegion.width;
        dstRect.bottom = y + pixmapRegion.height;

        canvas.drawBitmap(pixmap.bitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawText(String text, float x, float y, int color) {
        Paint paint = new Paint();
        paint.setStyle(Style.FILL);
        paint.setColor(0xFF000000);
        paint.setTextSize(10);
        canvas.drawText(text, x-1, y-1, paint);

        paint.setColor(color);
        paint.setTextSize(8);
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }
}
