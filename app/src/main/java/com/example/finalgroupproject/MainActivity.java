package com.example.finalgroupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the mymenu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        OnItemSelected(item);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        OnItemSelected(menuItem);
        return false;
    }
    private void OnItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId())
        {
            case R.id.ticketmaster:
                //TODO: replace this package name with your ticketmaster project full name. then uncomments
                // the following line.

                // Intent gotoAct=getPackageManager().getLaunchIntentForPackage("com.example.recipesearchpage");
                // startActivity(gotoAct);
                break;
            case R.id.recipesearchpage:
                Intent gotoAct=getPackageManager().getLaunchIntentForPackage("com.example.recipesearchpage");
                startActivity(gotoAct);
                break;
            case R.id.covid19:
                //TODO: replace this package name with your ticketmaster project full name. then uncomments
                // the following line.

                // Intent gotoAct=getPackageManager().getLaunchIntentForPackage("com.example.recipesearchpage");
                // startActivity(gotoAct);
                break;
            case R.id.audiodb:
                //TODO: replace this package name with your ticketmaster project full name. then uncomments
                // the following line.

                // Intent gotoAct=getPackageManager().getLaunchIntentForPackage("com.example.recipesearchpage");
                // startActivity(gotoAct);
                break;
        }
    }
}