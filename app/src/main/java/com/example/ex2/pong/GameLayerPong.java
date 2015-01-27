package com.example.ex2.pong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import sheep.game.Layer;
import sheep.graphics.Color;
import sheep.graphics.Font;
import sheep.graphics.Image;
import sheep.math.BoundingBox;

/**
 * Created by markuslund92 on 27.01.15.
 */
public class GameLayerPong extends Layer implements Observer {

    private final PongPaddle player1;
    private final PongPaddle player2;
    private final PongBall ball;
    private Boolean init;
    private int canvasWidth, canvasHeight;
    private float dt, ySpeed;
    private final Font scoreFont;
    private int gameWon;
    private final ScoreSystem scoreSystem;

    public GameLayerPong() {
        init = true;
        Image pong_paddle = new Image(R.drawable.pong_paddle);

        int winningScore = 21;
        scoreSystem = new ScoreSystem(winningScore);
        scoreSystem.addObserver(this);

        gameWon = -1;

        scoreFont = new Font(255, 255, 255, 50, Typeface.MONOSPACE, Typeface.NORMAL);
        scoreFont.setTextAlign(Paint.Align.CENTER);
        player1 = new PongPaddle(pong_paddle, 1);
        player2 = new PongPaddle(pong_paddle, 2);
        ball = PongBall.getInstance();
        ball.setSpeed(3, 0);
        ball.setPosition(50, 50);
        ySpeed = Util.getRandSpeed(1, 3).getY();

    }


    @Override
    public void update(float v) {

        if (gameWon == -1) {
            Util.moveSprite(player1);
            Util.moveSprite(player2);
            Util.moveSprite(ball);


            if ( dt>0.05 ){
                if(ball.collides(player1) ){
                    changeBallSpeed(player1.getPosition().getY()-ball.getPosition().getY());
                }else if(ball.collides(player2)){
                    changeBallSpeed(player2.getPosition().getY()-ball.getPosition().getY());
                }

                //Bounce roof/floor
                if (ball.getPosition().getY() < (10 + ball.getHeight() / 2) || ball.getPosition().getY() > canvasHeight - 10 - ball.getHeight() / 2) {
                    ball.setSpeed(ball.getSpeed().getX(), -ball.getSpeed().getY());
                    dt = 0;
                }
            }
            //check if someone has scored
            if (ball.getPosition().getX() < 0) {
                scoreSystem.addOnePoint(2);
                resetBall();
            } else if (ball.getPosition().getX() > canvasWidth) {
                scoreSystem.addOnePoint(1);
                resetBall();
            }

            dt += v;
            player1.update(v);
            player2.update(v);
            ball.update(v);

        }


    }

    private void changeBallSpeed(float diff){
        if(diff>10){
            ball.setSpeed(ball.getSpeed().getX(),ball.getSpeed().getY()-(diff/10));
        }else if(diff<-10){
            ball.setSpeed(ball.getSpeed().getX(),ball.getSpeed().getY()-(diff/10));
        }
        if (ball.getSpeed().getX() > 0){
            ball.setSpeed(ball.getSpeed().getX() + 1, ball.getSpeed().getY());
        }else{
            ball.setSpeed(ball.getSpeed().getX() - 1, ball.getSpeed().getY());
        }
        ball.setSpeed(-ball.getSpeed().getX(),ball.getSpeed().getY());
        dt=0;
    }

    private void resetBall() {
        ball.setPosition(canvasWidth / 2, canvasHeight / 2);
        float xSpeed;
        if (java.lang.Math.abs(-ball.getSpeed().getX()) < 3) {
            xSpeed = -ball.getSpeed().getX();
        } else {
            xSpeed = -ball.getSpeed().getX() / 2;
        }
        ball.setSpeed(xSpeed, Util.getRandInt(-3, 3));
    }

    @Override
    public void draw(Canvas canvas, BoundingBox boundingBox) {

        //Draws score
        canvas.drawText(String.valueOf(scoreSystem.getP1Score()), -50 + canvasWidth / 2, 100, scoreFont);
        canvas.drawText(String.valueOf(scoreSystem.getP2Score()), 50 + canvasWidth / 2, 100, scoreFont);

        if (gameWon > 0) {
            if (gameWon == 1) {
                canvas.drawText("Player 1 won!", canvasWidth / 2, 300, scoreFont);
            } else {
                canvas.drawText("Player 2 won!", canvasWidth / 2, 300, scoreFont);
            }
            canvas.drawText("Touch to start a new game.", canvasWidth / 2, 400, scoreFont);
        } else {
            player1.draw(canvas);
            player2.draw(canvas);
            ball.draw(canvas);

            //Midfield
            for (int i = 15; i < canvasHeight; i = i + 30) {
                canvas.drawRect(canvasWidth / 2 - 3, i, canvasWidth / 2 + 3, i + 10, Color.WHITE);
            }
        }


        //DRAWS GAME AREA

        //Upper line
        canvas.drawRect(0, 0, canvasWidth, 10, Color.WHITE);

        //Right Line
        canvas.drawRect(canvasWidth - 10, 0, canvasWidth, canvasHeight, Color.WHITE);

        //Lower Line
        canvas.drawRect(0, canvasHeight - 10, canvasWidth, canvasHeight, Color.WHITE);

        //Left Line
        canvas.drawRect(0, 0, 10, canvasHeight, Color.WHITE);


        if (init) {
            Log.i("init", "Init is run");
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            player1.setPosition(80, canvasHeight / 2);
            player2.setPosition(canvasWidth - 80, canvasHeight / 2);
            ball.setPosition(canvasWidth / 2, canvasHeight / 2);

            scoreSystem.resetScore();


            ySpeed = Util.getRandSpeed(1, 5).getY();
            init = false;
        }

    }

    public void onTouch(View v, MotionEvent event) {
        int eventAction = event.getActionMasked();

        int num = event.getPointerCount();

        // For every touch
        for (int a = 0; a < num; a++) {

            int X = 0;
            int Y = 0;

            try {
                X = (int) event.getX(event.getPointerId(a));
                Y = (int) event.getY(event.getPointerId(a));
            } catch (Exception e) {
                e.printStackTrace();
            }

            switch (eventAction) {
                case MotionEvent.ACTION_MOVE:
                    //Move player1
                    if (X < canvasWidth / 2) {
                        player1.setPosition(player1.getPosition().getX(), Y);
                    }

                    //Move player2
                    if (X > canvasWidth / 2) {
                        player2.setPosition(player2.getPosition().getX(), Y);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    Log.i("Touch", "Action up");
                    if (gameWon > 0) {
                        gameWon = -1;
                        scoreSystem.resetScore();
                    }
                    break;


                default:
                    break;
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        gameWon = (int) data;
    }
}
