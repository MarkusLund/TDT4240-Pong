package com.example.ex2.pong;

import sheep.game.Sprite;
import sheep.graphics.Image;

public class PongBall extends Sprite {
    private static PongBall ourInstance ;
    public static PongBall getInstance() {
        return ourInstance;
    }
    private static Image img = new Image(R.drawable.pong_ball);

    public PongBall() {
        super(img);
        ourInstance = this;
        this.setPosition(100,100);

    }

    public static float getHeight() {
        return img.getHeight();
    }

    public static float getWidth() {
        return img.getWidth();
    }



}