package com.example.ex2.pong;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import sheep.game.State;
import sheep.game.World;

/**
 * Created by markuslund92 on 27.01.15.
 */
public class Task4Pong extends State implements View.OnTouchListener {

    private World gameWorld;
    private GameLayerPong gameLayer;

    public Task4Pong() {
        this.gameWorld = new World();
        this.gameLayer = new GameLayerPong();
        gameWorld.addLayer(gameLayer);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        gameLayer.update(dt);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        gameWorld.draw(canvas);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.gameLayer.onTouch(v, event);
        return false;
    }
}
