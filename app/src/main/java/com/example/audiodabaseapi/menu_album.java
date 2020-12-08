package com.example.audiodabaseapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.finalgroupproject.R;
import com.google.android.material.navigation.NavigationView;


/**
 * menu_album - a class for showing the menu and navigational element.
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class menu_album extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * here we are declaring  our variables environmment  and setting up
     * variables actions
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_album);
        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar2);
//        setSupportActionBar(tBar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * we create a menu options for selecting whatever he/she wants
     * Inflate the menu items for use in the action bar
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        return true;
    }

    /**
     *  we create a menu options for selecting whatever he/she wants
     *  we ut a case for every id in that file in xml file
     *   we created switch case concept for user option to choose what to do when the menu item is selected:
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;

        switch(item.getItemId())
        {
            //what to do when the menu item is selected:

            case R.id.item1:
                message = "You clicked on the search artist";

                break;
            case R.id.item4:
                message = "You clicked on exit app";
                AlertDialog.Builder alrtBldr = new AlertDialog.Builder(menu_album.this);
                AlertDialog alertDialog = alrtBldr .setTitle("Do you want to exit app?" )

                        .setPositiveButton("yes", (click, args)-> {
                            finish();
                        })
                        .setNegativeButton("no", (click,arg)-> {

                        })
                        .create();

                alertDialog.show();
                break;

            case R.id.item3:
                message = "You clicked on song";
                Intent p3 = new Intent(this, song.class);
                startActivity(p3);
                break;
            case R.id.help:
                message = "You clicked on help";
                AlertDialog.Builder alrtBldr1 = new AlertDialog.Builder(menu_album.this);
                AlertDialog alertDialog1 = alrtBldr1 .setTitle("Do you need help with somthing?" )

                        .setPositiveButton("yes", (click, args)-> {
                            View view= findViewById(R.id.activity);
                            for (int a=0; a < 1000; a++) { Toast.makeText(this, "this application takes information from the user and stores it in a database. " +
                                    "They can then view the data saved to a list of favourites and delete items from that list. for more details please message the author at :" +
                                    "brahimtoure99@gmail.com", Toast.LENGTH_SHORT).show(); }
                        })
                        .setNegativeButton("no", (click,arg)-> {
                            finish();
                        })
                        .create();

                alertDialog1.show();
                break;

        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * we create a navigation option menu with same id notation  options for selecting whatever he/she wants
     * we created  smooth transtion between activities and throw alert dialogs to know user desires
     * @param item
     * @return false by the end
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String message = null;

        switch(item.getItemId())
        {

            case R.id.item1:
                message = "You clicked on the search";

                Intent p1 = new Intent(this, ArtistSearch.class);
                startActivity(p1);
                break;
            case R.id.item4:
                message = "You clicked on exit";
                AlertDialog.Builder alrtBldr = new AlertDialog.Builder(menu_album.this);
                AlertDialog alertDialog = alrtBldr .setTitle("Do you want to exit app?" )

                        .setPositiveButton("yes", (click, args)-> {
                          finish();
                        })
                        .setNegativeButton("no", (click,arg)-> {

                        })
                        .create();

                alertDialog.show();

                break;
            case R.id.item3:
                message = "You clicked on song";
                Intent p3 = new Intent(this, song.class);;
                startActivity(p3);
                break;


            case R.id.help:
                message = "You clicked on help";
                AlertDialog.Builder alrtBldr1 = new AlertDialog.Builder(menu_album.this);
                AlertDialog alertDialog1 = alrtBldr1 .setTitle("Do you need help with somthing?" )

                        .setPositiveButton("yes", (click, args)-> {
                            for (int a=0; a < 300; a++) { Toast.makeText(this, "this application takes information from the user and stores it in a database. " +
                                    "They can then view the data saved to a list of favourites and delete items from that list. for more details please message the author at :" +
                                    "brahimtoure99@gmail.com", Toast.LENGTH_SHORT).show(); }
                        })
                        .setNegativeButton("no", (click,arg)-> {

                        })
                        .create();

                alertDialog1.show();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
        return false;
    }
}