package com.example.finalgroupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_liu extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("cityAndRadiusFile", Context.MODE_PRIVATE);

        String city = prefs.getString("city", "");
        EditText editViewCity = findViewById(R.id.editViewCity);
        editViewCity.setText(city);

        String radius = prefs.getString("radius", "");
        EditText editViewRadius = findViewById(R.id.editViewRadius);
        editViewRadius.setText(radius);

        Button searchButton = findViewById(R.id.searchButton);
        Intent goToSearch = new Intent(MainActivity_liu.this, SearchResult_liu.class);
        searchButton.setOnClickListener(bt ->
        {   /*goToProfile.putExtra("email", emailEditText.getText().toString());*/
            startActivity(goToSearch);
        });

        Button favouriteButton = findViewById(R.id.favouriteButton);
        Intent goToFavourite = new Intent(MainActivity_liu.this, FavouriteResult_liu.class);
        searchButton.setOnClickListener(bt ->
        {   /*goToProfile.putExtra("email", emailEditText.getText().toString());*/
            startActivity(goToFavourite);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("city", ((EditText)findViewById(R.id.editViewCity)).getText().toString());
        editor.putString("radius", ((EditText)findViewById(R.id.editViewRadius)).getText().toString());
        editor.commit();
    }
}