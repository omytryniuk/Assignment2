package com.example.yugenshtil.torontoparkguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro, menu);
        return true;
    }

    // The method runs when the user click on the picture and choose "To show all parks". The method calls ShowAll activity
    public void ShowAllParks(View v) {
        Intent intent = new Intent(Intro.this, ShowAll.class);
        startActivityForResult(intent, 0);


    }


    // The method runs when the user click on the picture and choose "Search". The method calls Search activity
    public void StartSearch(View v) {
        Intent intent = new Intent(Intro.this, Search.class);
        startActivityForResult(intent, 0);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
