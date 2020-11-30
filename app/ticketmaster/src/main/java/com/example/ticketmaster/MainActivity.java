package com.example.ticketmaster;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;
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

public class MainActivity extends AppCompatActivity {

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
            goToTicket.putLong("id", myAdapter.getItem(position).getId());
            goToTicket.putString("eventName", myAdapter.getItem(position).getEventName());
            goToTicket.putString("startDate", myAdapter.getItem(position).getStartDate());
            goToTicket.putString("minPrice", myAdapter.getItem(position).getMinPrice());
            goToTicket.putString("maxPrice", myAdapter.getItem(position).getMaxPrice());
            goToTicket.putString("url", myAdapter.getItem(position).getUrl());
            goToTicket.putString("imgUrl", myAdapter.getItem(position).getImgUrl());
            goToTicket.putBoolean("isFavorite", isFavorite);

            Intent nextActivity = new Intent(MainActivity.this, EmptyActivity.class);
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

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(bt ->
        {
            String cityToSearch = editViewCity.getText().toString();
            String radiusToSearch = editViewRadius.getText().toString();
            //TicketQuery query = new TicketQuery(cityToSearch, radiusToSearch);
            Toast.makeText(MainActivity.this,"City:"+cityToSearch+" Radius:"+radiusToSearch,Toast.LENGTH_SHORT).show();
            doSearch(cityToSearch, radiusToSearch);
            //query.execute();
        });// ke neng yao gai

        Button favouriteButton = findViewById(R.id.favouriteButton);
        //Intent goToFavourite = new Intent(MainActivity.this, FavouriteResult.class);
        favouriteButton.setOnClickListener(bt ->
        {   /*goToProfile.putExtra("email", emailEditText.getText().toString());*/
            loadDataFromDatabase();
            myAdapter.notifyDataSetChanged();
            isFavorite = true;
            String msg = getResources().getString(R.string.displayFavorite);
            Snackbar snackbar = Snackbar.make(findViewById(R.id.main), msg, Snackbar.LENGTH_LONG);
            snackbar.show();
            //startActivity(goToFavourite);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.help_menu:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                String msg = getResources().getString(R.string.helpInstruction);
                alertDialogBuilder.setTitle(R.string.helpTitle)
                        .setMessage(msg)
                        .setPositiveButton("ok",(clickButton, arg) -> {
                        });
                alertDialogBuilder.create().show();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_FAVORITE_EDIT && resultCode == RESULT_OK) {
            loadDataFromDatabase();
            myAdapter.notifyDataSetChanged();
        }
    }

    private void doSearch(String city, String radius) {
        isFavorite = false;
        TicketQuery query = new TicketQuery(city, radius); //creates a background thread
        query.execute("https://app.ticketmaster.com/discovery/v2/events.json?apikey=rzBTum5fKPpuObmhHA6gkkZuRTYFnR0G " + "&city=" + city + "&radius=" + radius); //Type 1
        String msg = getResources().getString(R.string.displaySearch);
        Snackbar snackbar = Snackbar.make(findViewById(R.id.main), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void loadDataFromDatabase() {
        elements.clear();
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        String [] columns = {MyOpener.COL_EVENT_NAME, MyOpener.COL_START_DATE, MyOpener.COL_PRICE_MAX, MyOpener.COL_PRICE_MIN, MyOpener.COL_URL, MyOpener.COL_IMG_URL};
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int eventNameColumnIndex = results.getColumnIndex(MyOpener.COL_EVENT_NAME);
        int startDateColumnIndex = results.getColumnIndex(MyOpener.COL_START_DATE);
        int priceMaxColumnIndex = results.getColumnIndex(MyOpener.COL_PRICE_MAX);
        int priceMinColumnIndex = results.getColumnIndex(MyOpener.COL_PRICE_MIN);
        int urlColumnIndex = results.getColumnIndex(MyOpener.COL_URL);
        int imgUrlColumnIndex = results.getColumnIndex(MyOpener.COL_IMG_URL);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String eventName = results.getString(eventNameColumnIndex);
            String startDate = results.getString(startDateColumnIndex) ;
            String max = results.getString(priceMaxColumnIndex);
            String min = results.getString(priceMinColumnIndex);
            String url = results.getString(urlColumnIndex);
            String imgUrl = results.getString(imgUrlColumnIndex);
            //add the new Contact to the array list:
            elements.add(new Ticket(eventName, startDate, min, max, url, imgUrl ));
        }
    }

    private class TicketQuery extends AsyncTask<String, Integer, String> {

        private String eventName;
        private String startDate;
        private String minPrice;
        private String maxPrice;
        private String url;
        private Bitmap bitmap;
        private static final String TICKET_MASTER_URL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=rzBTum5fKPpuObmhHA6gkkZuRTYFnR0G ";
        private String city;
        private String radius;

        public TicketQuery(String city, String radius) {
            this.city = city;
            this.radius = radius;
        }
        //Type3                     Type1
        protected String doInBackground(String ... args) {
            String fullUrl = TICKET_MASTER_URL+"&city="+this.city+"&radius="+this.radius;
            try {
                Log.e("Start","Start loading information ..");
                elements.clear();
                //create a URL object of what server to contact:
                URL url = new URL(fullUrl);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(response);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder resultStr = new StringBuilder();
                String line = null;
                while((line=reader.readLine())!=null){
                    resultStr.append(line);
                }
                String resultJSONStr = resultStr.toString();
                Log.e("JSON String",resultJSONStr);
                JSONObject obj = new JSONObject(resultJSONStr);
                JSONObject embedded = obj.getJSONObject("_embedded");
                JSONArray events = embedded.getJSONArray("events");
                for(int i=0;i<events.length();i++){
                    JSONObject event = events.getJSONObject(i);
                    String name = event.getString("name");
                    String eventUrl = event.getString("url");
                    String startDate = event.getJSONObject("dates").getJSONObject("start").getString("localDate");
                    double minPrice = event.getJSONArray("priceRanges").getJSONObject(0).getDouble("min");
                    double maxPrice = event.getJSONArray("priceRanges").getJSONObject(0).getDouble("max");
                    String bitmap = event.getJSONArray("images").getJSONObject(0).getString("url");
                    Ticket ticket = new Ticket(name, startDate, minPrice+"", maxPrice+"", eventUrl, bitmap);
                    elements.add(ticket);
                }
            }
            catch (Exception e) {
                Log.e("Error","Error happens");
                e.printStackTrace();
            }
            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args) {
            Log.i("Calling back for data", "Integer=" + args[0]);
            pb_main.setVisibility(View.VISIBLE);
            int a = args[0];
            pb_main.setProgress(args[0]);
        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {
            pb_main.setVisibility(View.INVISIBLE);
            int i=0;
            for(Ticket t: elements){
                i++;
                Log.e("e"+i, t.toString());
            }
            myAdapter.notifyDataSetChanged();
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
        public long getItemId(int position) { return elements.get(position).getId();}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Ticket ticket = getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View currentRow;
            currentRow = inflater.inflate(R.layout.row_event_name, parent, false);
            TextView rowEventName = currentRow.findViewById(R.id.rowEventName);
            rowEventName.setText(ticket.getEventName());// other url image and so on
            return currentRow;
        }
    }
}