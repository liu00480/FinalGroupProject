package com.example.finalgroupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity_liu extends AppCompatActivity {

    private SharedPreferences prefs;
    //ProgressBar pb;
    public boolean isFavorite = false;
    static final int REQUEST_FAVORITE_EDIT = 1;
    MyListAdapter myAdapter = new MyListAdapter();
    private ArrayList<Ticket> elements = new ArrayList<>();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.searchButton);
        Intent goToSearch = new Intent(MainActivity_liu.this, SearchResult_liu.class);
        searchButton.setOnClickListener(bt ->
        {   /*goToProfile.putExtra("email", emailEditText.getText().toString());*/
            startActivity(goToSearch);
        });// ke neng yao gai

        Button favouriteButton = findViewById(R.id.favouriteButton);
        Intent goToFavourite = new Intent(MainActivity_liu.this, FavouriteResult_liu.class);
        searchButton.setOnClickListener(bt ->
        {   /*goToProfile.putExtra("email", emailEditText.getText().toString());*/
            startActivity(goToFavourite);
        });

        ListView myList = findViewById(R.id.listViewMain);
        myList.setAdapter(myAdapter);
        myList.setOnItemClickListener((parent, view, position, id) -> {
            //Intent goToSearch = new Intent(MainActivity_liu.this, SearchResult_liu.class);
            Ticket selectedTicket = myAdapter.getItem(position);
            goToSearch.putExtra("eventName", selectedTicket.getEventName());
            goToSearch.putExtra("startDate", selectedTicket.getStartDate());
            goToSearch.putExtra("priceRange", selectedTicket.getPriceRange());
            goToSearch.putExtra("url", selectedTicket.getUrl());
            goToSearch.putExtra("isFavorite", isFavorite);
            if(isFavorite){
                startActivityForResult(goToSearch, REQUEST_FAVORITE_EDIT);
            }else {
                startActivity(goToSearch);
            }
        });

        prefs = getSharedPreferences("cityAndRadiusFile", Context.MODE_PRIVATE);

        String city = prefs.getString("city", "");
        EditText editViewCity = findViewById(R.id.editViewCity);
        editViewCity.setText(city);

        String radius = prefs.getString("radius", "");
        EditText editViewRadius = findViewById(R.id.editViewRadius);
        editViewRadius.setText(radius);



    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("city", ((EditText)findViewById(R.id.editViewCity)).getText().toString());
        editor.putString("radius", ((EditText)findViewById(R.id.editViewRadius)).getText().toString());
        editor.commit();
    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() { return elements.size(); }

        @Override
        public Ticket getItem(int position) { return elements.get(position); }

        @Override
        public long getItemId(int position) { return 0;}//gai

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Ticket ticket = getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View currentRow;
            currentRow = inflater.inflate(R.layout.row_event_name_liu, parent, false);
            TextView rowEventName = currentRow.findViewById(R.id.rowEventName);
            rowEventName.setText(getItem(position).getEventName());
            return currentRow;
        }
    }
}