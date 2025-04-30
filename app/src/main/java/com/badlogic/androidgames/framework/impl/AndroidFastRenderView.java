package com.badlogic.androidgames.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.badlogic.androidgames.framework.Screen;
import com.codalab.platformer.MainActivity;
import com.codalab.platformer.graphics.MyGraphics;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
    MainActivity game;
    Bitmap frameBuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;
    
    public AndroidFastRenderView(MainActivity game, Bitmap frameBuffer) {
        super(game);
        this.game = game;
        this.frameBuffer = frameBuffer;
        this.holder = getHolder();
    }

    public void resume() { 
        running = true;
        renderThread = new Thread(this);
        renderThread.start();         
    }      
    
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();

        float fpsTime = 0.0f;
        int fps = 0;

        MyGraphics g = game.myGraphics;

        while(running) {  
            if(!holder.getSurface().isValid())
                continue;           
            
            float deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
            startTime = System.nanoTime();

            Screen currentScreen = game.getCurrentScreen();
            currentScreen.update(deltaTime);
            currentScreen.render(g);
            
            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(frameBuffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);

            fpsTime += deltaTime;
            fps++;

            if(fpsTime >= 1.0f) {
                System.out.println("FPS: " + fps);

                fpsTime -= 1.0f;
                fps = 0;
            }
        }
    }

    public void pause() {                        
        running = false;                        
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }        
}