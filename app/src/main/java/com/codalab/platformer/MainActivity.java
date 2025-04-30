package com.codalab.platformer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.FileIO;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidAudio;
import com.badlogic.androidgames.framework.impl.AndroidFastRenderView;
import com.badlogic.androidgames.framework.impl.AndroidFileIO;
import com.badlogic.androidgames.framework.impl.AndroidGraphics;
import com.badlogic.androidgames.framework.impl.AndroidInput;
import com.codalab.platformer.data.Constants;
import com.badlogic.androidgames.framework.math.Box;
import com.codalab.platformer.graphics.MyGraphics;
import com.codalab.platformer.screens.GameScreen;
import com.codalab.platformer.utils.Converter;
import com.codalab.R;

public class MainActivity extends Activity implements Game {

    private AndroidFastRenderView renderView;
    private Graphics graphics;
    private Audio audio;
    private Input input;
    public static FileIO fileIO;
    private Screen screen;

    public MyGraphics myGraphics;


    // the tag used for logging
    public static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");

        TAG = getString(R.string.app_name);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Game world
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Box screenSize = new Box(0, 0, metrics.widthPixels, metrics.heightPixels);

        Converter.set(Constants.physicalSize, screenSize);

        Bitmap screenBuffer = Bitmap.createBitmap(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, Bitmap.Config.ARGB_8888);

        // View
        renderView = new AndroidFastRenderView(this, screenBuffer);
        graphics = new AndroidGraphics(getAssets(), screenBuffer);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, 1, 1);

        Pixmap sheet = graphics.newPixmap("fgame.png", Graphics.PixmapFormat.ARGB8888);
        myGraphics = new MyGraphics(graphics, sheet, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
        screen = getStartScreen();
        setContentView(renderView);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Main thread", "pause");
        renderView.pause(); // stops the main loop
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Main thread", "stop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Main thread", "resume");

        renderView.resume(); // starts game loop in a separate thread
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        this.screen = screen;
    }
    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

    @Override
    public Screen getStartScreen() {
        return new GameScreen(this);
    }
}
