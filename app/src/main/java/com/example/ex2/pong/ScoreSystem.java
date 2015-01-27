package com.example.ex2.pong;

import android.renderscript.Sampler;
import android.util.Log;

import java.util.Observable;

/**
 * Created by markuslund92 on 27.01.15.
 */
public class ScoreSystem extends Observable{
    private int p1, p2;
    private int winScore;

    public ScoreSystem(int wonScore){
        p1 = 0;
        p2 = 0;
        this.winScore = wonScore;
    }

    public void addOnePoint(int player){
        Log.i("addOnePoint", String.valueOf(player));
        if (player == 1){
            p1++;
        }else{
            p2++;
        }
        if (p1 >= winScore){
            notifyObservers(1);
        }else{
            notifyObservers(2);
        }
    }

    public int getP1Score() {
        return p1;
    }

    public int getP2Score() {
        return p2;
    }
}
