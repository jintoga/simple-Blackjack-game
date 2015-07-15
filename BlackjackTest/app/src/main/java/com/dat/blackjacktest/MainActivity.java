package com.dat.blackjacktest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dat.blackjacktest.model.Player;

import org.w3c.dom.Text;


public class MainActivity extends Activity implements View.OnClickListener {
    public static final String tag_play_position = "play_position";
    public static final String tag_money = "money";
    Button buttonNewGame, buttonExit;
    Spinner spinnerPosition;
    int playPosition;
    TextView textViewAccountInMain;

    private int money = 1000;

    public static final int tag_return_money = 999;

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
        textViewAccountInMain = (TextView) findViewById(R.id.textViewAcountInMain);
        textViewAccountInMain.setText(money + "$");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == tag_return_money) {
            money = data.getIntExtra(tag_money, 0);
            textViewAccountInMain.setText(money + "$");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonNewGame) {
            Intent intent = new Intent(this, GameplayActivity.class);
            intent.putExtra(tag_play_position, playPosition);
            intent.putExtra(tag_money, money);
            startActivityForResult(intent, tag_return_money);
        }
        if (view.getId() == R.id.buttonExit) {
            Toast.makeText(this, "EXIT", Toast.LENGTH_SHORT).show();
        }
    }
}
