package com.example.ex2.pong;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import sheep.game.Layer;
import sheep.graphics.Color;
import sheep.graphics.Font;
import sheep.graphics.Image;
import sheep.math.BoundingBox;

/**
 * Created by markuslund92 on 27.01.15.
 */
public class GameLayerPong extends Layer {

    private PongPaddle player1, player2;
    private PongBall ball;
    private Boolean init;
    private Image pong_paddle;
    private int canvasWidth, canvasHeight;
    private float dt, ySpeed;
    private Font scoreFont;
    private static int p1Score, p2Score, winningScore;
    private boolean gameWon;

    public GameLayerPong() {
        init = true;
        pong_paddle = new Image(R.drawable.pong_paddle);

        p1Score = 0;
        p2Score = 0;
        winningScore = 21;
        gameWon = false;

        scoreFont = new Font(255, 255, 255, 50, Typeface.MONOSPACE, Typeface.NORMAL);
        scoreFont.setTextAlign(Paint.Align.CENTER);
        player1 = new PongPaddle(pong_paddle, 1);
        player2 = new PongPaddle(pong_paddle, 2);
        ball = new PongBall();
        ball.setSpeed(3,0);
        ball.setPosition(50,50);
        ySpeed = Util.getRandSpeed(1, 3).getY();

    }


    @Override
    public void update(float v) {

        if (!gameWon){
            Util.moveSprite(player1);
            Util.moveSprite(player2);
            Util.moveSprite(ball);


            if ( dt>0.05 ){
                if(ball.collides(player1) ){
                    float diff = player1.getPosition().getY()-ball.getPosition().getY();
                    if(diff>10){
                        ball.setSpeed(ball.getSpeed().getX(),ball.getSpeed().getY()+(diff/5)-10);
                    }else if(diff<-10){
                        ball.setSpeed(ball.getSpeed().getX(),ball.getSpeed().getY()+(diff/5)+10);
                    }
                    if (ball.getSpeed().getX() > 0){
                        ball.setSpeed(ball.getSpeed().getX() + 1, ball.getSpeed().getY());
                    }else{
                        ball.setSpeed(ball.getSpeed().getX() - 1, ball.getSpeed().getY());
                    }
                    ball.setSpeed(-ball.getSpeed().getX(),ball.getSpeed().getY());
                    dt=0;
                }else if(ball.collides(player2)){
                    float diff = player2.getPosition().getY()-ball.getPosition().getY();
                    if(diff>10){
                        ball.setSpeed(ball.getSpeed().getX(),ball.getSpeed().getY()+(diff/5)-10);
                    }else if(diff<-10){
                        ball.setSpeed(ball.getSpeed().getX(),ball.getSpeed().getY()+(diff/5)+10);
                    }
                    if (ball.getSpeed().getX() > 0){
                        ball.setSpeed(ball.getSpeed().getX() + 1, ball.getSpeed().getY());
                    }else{
                        ball.setSpeed(ball.getSpeed().getX() - 1, ball.getSpeed().getY());
                    }
                    ball.setSpeed(-ball.getSpeed().getX(),ball.getSpeed().getY());
                    dt=0;
                }

                //Bounce roof/floor
                if(ball.getPosition().getY()<(10+ball.getHeight()/2) || ball.getPosition().getY()>canvasHeight-10-ball.getHeight()/2){
                    ball.setSpeed(ball.getSpeed().getX(),-ball.getSpeed().getY());
                    dt=0;
                }
            }
            //check if someone has scored
            if( ball.getPosition().getX()<0){
                addP2Score();
                resetBall();
            }else if(ball.getPosition().getX()>canvasWidth){
                addP1Score();
                resetBall();
            }

            if(p1Score >= winningScore || p2Score >= winningScore){
                gameWon = true;
            }

            dt+=v;
            player1.update(v);
            player2.update(v);
            ball.update(v);

        }


    }

    private void resetBall() {
        ball.setPosition(canvasWidth / 2, canvasHeight / 2);
        float xSpeed;
        if(java.lang.Math.abs(-ball.getSpeed().getX())<3){
            xSpeed = -ball.getSpeed().getX();
        }else{
            xSpeed = -ball.getSpeed().getX()/2;
        }
        ball.setSpeed(xSpeed, Util.getRandInt(-3, 3));
    }

    private void addP1Score() {
        p1Score++;
    }

    private void addP2Score() {
        p2Score++;
    }

    @Override
    public void draw(Canvas canvas, BoundingBox boundingBox) {
        canvas.drawText(String.valueOf(getP1Score()), -50 + canvasWidth / 2, 100, scoreFont);
        canvas.drawText(String.valueOf(getP2Score()), 50 + canvasWidth / 2, 100, scoreFont);

        if (gameWon){
            if (getP1Score() > getP2Score()){
                canvas.drawText("Player 1 won!", canvasWidth / 2, 300, scoreFont);
            }else{
                canvas.drawText("Player 2 won!", canvasWidth / 2, 300, scoreFont);
            }
            canvas.drawText("Touch to start a new game.", canvasWidth / 2, 400, scoreFont);
        }else{
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
            ball.setPosition(canvasWidth/2, canvasHeight/2);
            p1Score = 0;
            p2Score = 0;
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
                        player1.setPosition(player1.getPosition().getX(),Y);
                    }

                    //Move player2
                    if (X > canvasWidth / 2) {
                        player2.setPosition(player2.getPosition().getX(),Y);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    Log.i("Touch", "Action up");
                    if (gameWon){
                        p1Score = 0;
                        p2Score = 0;
                        gameWon = false;
                        Log.i("gameWon", "Set to false after action up");
                    }
                    break;


                default:
                    break;
            }
        }
    }

    public int getP2Score() {
        return p2Score;
    }

    public int getP1Score() {
        return p1Score;
    }
}
