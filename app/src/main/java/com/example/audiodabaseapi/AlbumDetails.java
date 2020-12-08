package com.example.audiodabaseapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalgroupproject.R;
import com.squareup.picasso.Picasso;

/**
 * ArtistSearch - a class where user can freely search for artist Name through the
 * the browser
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class AlbumDetails extends AppCompatActivity {
    String albumName,albumID,sName,sDesc;
    private TextView albumId,albumNames,singerName,description;
    private ImageView imageView2;


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

        getSupportActionBar().hide();

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.black));


        singerName=findViewById(R.id.singerName);
        albumId=findViewById(R.id.albumids);
        albumNames=findViewById(R.id.albumNames);
        imageView2=findViewById(R.id.imageView2);
        description=findViewById(R.id.description);

        Bundle extras = getIntent().getExtras();

        albumID=extras.getString("AlbumID");
        albumName=extras.getString("AlbumName");
        sName=extras.getString("ArtistName");
        sDesc=extras.getString("Description");
        System.out.println(sDesc);
        System.out.println("Data Recieved: "+albumName);

        albumId.setText("Album ID: "+albumID);
        albumNames.setText("Album: "+albumName);
        singerName.setText(sName.toUpperCase());
        description.setText(sDesc);
        try {
            Picasso.get().load(extras.getString("URL")).into(imageView2);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }
}
