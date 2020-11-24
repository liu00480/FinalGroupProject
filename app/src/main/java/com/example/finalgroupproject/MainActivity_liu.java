package com.example.finalgroupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity_liu extends AppCompatActivity {

    private SharedPreferences prefs;
    ProgressBar pb_main;
    public boolean isFavorite = false;
    static final int REQUEST_FAVORITE_EDIT = 1;
    MyListAdapter myAdapter = new MyListAdapter();
    private ArrayList<Ticket> elements = new ArrayList<>();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_main = findViewById(R.id.progressBar_Main);

        ListView myList = findViewById(R.id.listViewMain);
        myList.setAdapter(myAdapter);

        myList.setOnItemClickListener((parent, view, position, id) -> {
            //Intent goToSearch = new Intent(MainActivity_liu.this, SearchResult_liu.class);
            Bundle goToTicket = new Bundle();
            //Ticket selectedTicket = myAdapter.getItem(position);
            goToTicket.putString("eventName", myAdapter.getItem(position).getEventName());
            goToTicket.putString("startDate", myAdapter.getItem(position).getStartDate());
            goToTicket.putString("priceRange", myAdapter.getItem(position).getMinPrice());
            goToTicket.putString("priceRange", myAdapter.getItem(position).getMaxPrice());
            goToTicket.putString("url", myAdapter.getItem(position).getUrl());
            goToTicket.putBoolean("isFavorite", isFavorite);

            Intent nextActivity = new Intent(MainActivity_liu.this, EmptyActivity_liu.class);
            nextActivity.putExtras(goToTicket);
            if(isFavorite){ startActivityForResult(nextActivity, REQUEST_FAVORITE_EDIT);
            }else { startActivity(nextActivity); }
        });

        prefs = getSharedPreferences("cityAndRadiusFile", Context.MODE_PRIVATE);

        String city = prefs.getString("city", "");
        EditText editViewCity = findViewById(R.id.editViewCity);
        editViewCity.setText(city);

        String radius = prefs.getString("radius", "");
        EditText editViewRadius = findViewById(R.id.editViewRadius);
        editViewRadius.setText(radius);

        Button search_bt = findViewById(R.id.searchButton);







        Button searchButton = findViewById(R.id.searchButton);

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


    }

    private class TicketQuery extends AsyncTask<String, Integer, String> {

        private String eventName;
        private String startDate;
        private String minPrice;
        private String maxPrice;
        private String url;
        private Bitmap bitmap;

        //Type3                     Type1
        protected String doInBackground(String ... args) {
            try {
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8"); //response is data from the server
                //From part 3, slide 20
                String parameter = null;
                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT
                while(eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_TAG) {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("events")) {
                            //If you get here, then you are pointing to a <Weather> start tag
                            String eventName = xpp.getAttributeValue(null, "name");
                            String url_text = xpp.getAttributeValue(null, "url");
                            if(xpp.getName().equals("images")) {
                                if(xpp.getAttributeValue(null, "ratio").equals("4_3")) {
                                    String imageUrl = xpp.getAttributeValue(null, "url");
                                    if (fileExistance(imageUrl)) {
                                        Log.i("Image", "Image exists so read from file.");
                                        bitmap = readFile(imageUrl);
                                    }else {
                                        Log.i("Image", "Image doesn't exists so download from internet.");
                                        bitmap = downLoadImage(imageUrl);
                                    }
                                }
                            }
                            else if(xpp.getName().equals("dates")) {
                                if(xpp.getName().equals("start")) {
                                    String startDate = xpp.getAttributeValue(null, "localDate");
                                }
                            }
                            else if(xpp.getName().equals("priceRanges")) {
                                String min = xpp.getAttributeValue(null, "min");
                                String max = xpp.getAttributeValue(null, "max");
                            }
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }
            }
            catch (Exception e) { }
            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args) {
            Log.i("Calling back for data", "Integer=" + args[0]);
        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {
            //Log.i("HTTP", fromDoInBackground);
            minTemperature.setText(min);
            maxTemperature.setText(max);
            currentTemperature.setText(current_Temperature);
            //image
            currentWeather.setImageBitmap(bitmap);
            //uv
            uvRating.setText(uv);
            progressBar.setVisibility(View.INVISIBLE);
        }

        public Bitmap downLoadImage(String iconName) {
            Bitmap image = null;
            try{
                URL url = new URL("http://openweathermap.org/img/w/" + iconName);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                }
                FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();
            }
            catch (Exception e) { }
            return image;
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public Bitmap readFile(String fname) {
            FileInputStream fis = null;
            try {fis = openFileInput(fname);}
            catch (FileNotFoundException e) {e.printStackTrace();}
            Bitmap bm = BitmapFactory.decodeStream(fis);
            return bm;
        }

        public String getUv(String urlString) {
            String value = null;
            try{
                //create a URL object of what server to contact:
                URL url = new URL(urlString);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for date:
                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result= sb.toString();
                JSONObject jObject = new JSONObject(result);
                value=Double.toString(jObject.getDouble("value"));
            }catch(Exception ex){}
            return value;
        }
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