package com.example.ex2.pong;

import sheep.game.Sprite;
import sheep.graphics.Image;

public class PongBall extends Sprite {
    private static final Image img = new Image(R.drawable.pong_ball);
    private static final PongBall INSTANCE = new PongBall(img);

     private PongBall(Image img) {
         super(img);
    }

    public static PongBall getInstance() {
        return INSTANCE;
    }
    public static float getHeight() {
        return img.getHeight();
    }

    public static float getWidth() {
        return img.getWidth();
    }



}