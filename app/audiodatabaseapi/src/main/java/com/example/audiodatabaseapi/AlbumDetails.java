package com.example.audiodatabaseapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;



/**
 * ArtistSearch - a class where user can freely search for artist Name through the
 * the browser
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class AlbumDetails extends AppCompatActivity {
    String albumName,albumID;
    private TextView albumId,albumNames;

    /**
     * without using sharepreferences but using activity intent
     * here I am basically setting up my variables
     * sending data repsonse  after the resquest received during app execution
     * setting the text of each database elelment
     * returning the intent that started my album detail activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);

        albumId=findViewById(R.id.albumids);
        albumNames=findViewById(R.id.albumNames);

        Intent i=getIntent();

        albumName=i.getStringExtra("Name");
        albumID=i.getStringExtra("Email");

        System.out.println("Data Recieved: "+albumName);

        albumId.setText(albumName);
        albumNames.setText(albumName);

    }
}
