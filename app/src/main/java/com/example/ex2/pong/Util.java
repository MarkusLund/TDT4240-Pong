package com.example.ex2.pong;

import java.util.Random;

import sheep.game.Sprite;
import sheep.math.Vector2;

public final class Util {

    public static Vector2 getRandSpeed(int min, int max){
        return new Vector2(getRandInt(min,max),getRandInt(min,max));
    }

    public static int getRandInt(int min, int max){
        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;
        rand = null;
        return randomNum;
    }

    public static void moveSprite(Sprite sprite){
        sprite.setPosition(sprite.getPosition().getX() + sprite.getSpeed().getX(), sprite.getPosition().getY() + sprite.getSpeed().getY());

    }
}
