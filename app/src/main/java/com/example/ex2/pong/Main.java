package com.example.ex2.pong;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import sheep.game.Game;


public class Main extends Activity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        super.onCreate(savedInstanceState);
        // Create the game.
        game = new Game(this, null);
        // Push the main state.
        Task4Pong state = new Task4Pong();
        game.pushState(state);
        game.setOnTouchListener(state);
        // View the game.
        setContentView(game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*
        }else if (id == R.id.pong) {
            game.popState();
            Task4Pong state = new Task4Pong();
            game.pushState(state);
            game.setOnTouchListener(state);

        }
*/
        return super.onOptionsItemSelected(item);
    }
}
