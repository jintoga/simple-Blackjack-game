package com.dat.blackjacktest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {
    public static final String tag_play_position = "play_position";
    Button buttonNewGame, buttonExit;
    Spinner spinnerPosition;
    int playPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIDs();
        setEvents();
    }

    private void getIDs() {
        spinnerPosition = (Spinner) findViewById(R.id.spinnerPosition);
        buttonNewGame = (Button) findViewById(R.id.buttonNewGame);
        buttonExit = (Button) findViewById(R.id.buttonExit);
    }

    private void setEvents() {
        buttonNewGame.setOnClickListener(this);
        buttonExit.setOnClickListener(this);
        spinnerPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                playPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonNewGame) {
            Intent intent = new Intent(this, GameplayActivity.class);
            intent.putExtra(tag_play_position, playPosition);
            startActivity(intent);
        }
        if (view.getId() == R.id.buttonExit) {
            Toast.makeText(this, "EXIT", Toast.LENGTH_SHORT).show();
        }
    }
}
