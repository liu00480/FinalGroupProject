package com.example.audiodabaseapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.finalgroupproject.R;
import com.google.android.material.snackbar.Snackbar;


/**
 * MainActivity - a class that shows the frontEnd background of the app .
 * it is basically like a Landing page of the app or app introduction
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    Snackbar snack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button toolbar = (Button) findViewById(R.id.button6);
        Button t1= (Button) findViewById(R.id.button4);
        Button t2= (Button) findViewById(R.id.button5);
        Button t3= (Button) findViewById(R.id.button7);


        View view= findViewById(R.id.activity);
        snack= Snackbar.make(view,"welcome my friend",Snackbar.LENGTH_INDEFINITE);
        snack.setDuration(1000);
        snack.show();
        toolbar.setOnClickListener(e -> {
            Toast.makeText(getApplicationContext(), " welcome to toolbar and navigation view !!!!!", Toast.LENGTH_SHORT).show();
            Intent p = new Intent(this, menu_album.class);
            startActivity(p);
        });


        t1.setOnClickListener(e -> {
            Toast.makeText(getApplicationContext(), " come on give me five star !!!!!", Toast.LENGTH_SHORT).show();
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.ticketmaster");
            startActivity( launchIntent );

        });
        t2.setOnClickListener(e -> {
            Toast.makeText(getApplicationContext(), " come on give me five star !!!!!", Toast.LENGTH_SHORT).show();
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.ticketmaster");
            startActivity( launchIntent );

        });
        t3.setOnClickListener(e -> {
            Toast.makeText(getApplicationContext(), " come on give me five star !!!!!", Toast.LENGTH_SHORT).show();
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.ticketmaster");
            startActivity( launchIntent );

        });


    }
}



