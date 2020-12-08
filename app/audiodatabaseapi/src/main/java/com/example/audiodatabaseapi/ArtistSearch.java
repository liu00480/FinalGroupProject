package com.example.audiodatabaseapi;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




import org.json.JSONArray;

import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * ArtistSearch - a class where user can freely search for artist Name through the
 * the browser
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class ArtistSearch extends AppCompatActivity {


    ArrayList<HashMap<String, String>> ArtistList;
    RecyclerView list;
    SQLiteDatabase db;
    String artist_search;
    Button ArtistSearch;
    EditText searchBar;
    List<AlbumData> l;
    AlbumAdapter adapter;
    String BASE_URL = "https://www.theaudiodb.com/api/v1/json/1/";


    /**
     * verifying the  system version and detecting and generate my Preference Activity version for sdk
     * things you might be doing by accident and brings them to your attention so you can fix them.
     *  {@linktourl  https://stackoverflow.com/questions/26942753/strictmode-threadpolicy-policy-new-strictmode-threadpolicy-builder-permitall}
     * manipulating my database and url element by connecting my activity classes all together
     * to Json url elements properly
     *  {@linktourl  https://stackoverflow.com/questions/30978457/how-to-show-snackbar-when-activity-starts}
     *we Also used okhhtp to send connection
     *    {@linktourl https://www.vogella.com/tutorials/JavaLibrary-OkHttp/article.html}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ArtistSearch = (Button) findViewById(R.id.button);
        searchBar = (EditText) findViewById(R.id.edittext11);
        list = findViewById(R.id.listview);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));

        ArtistList = new ArrayList<>();
        l = new ArrayList<>();


        ALBUM_DATABASE Database = new ALBUM_DATABASE(this);
        db = Database.getWritableDatabase();
        ArtistSearch.setOnClickListener(click -> {
            closeKeyboard(ArtistSearch);

            artist_search = searchBar.getText().toString();
            artist_search = artist_search.replace(" ", "%20");


            try {

                String searchresponse = new ApiRequests().sendPOSTRequest(BASE_URL + "searchalbum.php?s=" + searchBar.getText().toString().trim());

                JSONObject obj = new JSONObject(searchresponse);
                if (obj.getString("album").equals("null")) {
                    Toast.makeText(this, "No albums found for this artist.", Toast.LENGTH_LONG).show();
                } else {
                    JSONArray arr = obj.getJSONArray("album");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject o = arr.getJSONObject(i);
                        AlbumData album = new AlbumData(
                                o.getString("idAlbum"),
                                o.getString("idArtist"),
                                o.getString("strAlbum"),
                                o.getString("strAlbumThumb")
                        );
                        System.out.println(album.getAlbumName());
                        l.add(album);

                    }

                    adapter = new AlbumAdapter(l, getApplicationContext());
                    list.setAdapter(adapter);
                    System.out.println("adapter set");
                    System.out.println(adapter.getItemCount());

                    Toast.makeText(this, "Fetched " + arr.length() + " Albums", Toast.LENGTH_LONG).show();

                }


                searchBar.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


    /**
     *trying to add url in my listview  in order for the user to access album information after clicking and album item
     * through url intent browser connection
     * {@linktourl   https://www.programcreek.com/java-api-examples/?class=android.content.Intent&method=ACTION_VIEW}
     *
     */
    private void addurlACTION() {
        list = findViewById(R.id.listview);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://theaudiodb.com/api/v1/json/1/track.php?m=2159699" ));
        startActivity(intent);

    }

    /**
     * use softKeyboard, to closed or hide android keyboard
     *  {@linktourl https://stackoverflow.com/questions/1109022/how-do-you-close-hide-the-android-soft-keyboard-using-java}
     * @param input
     */
    private void closeKeyboard(Button input) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }


}


