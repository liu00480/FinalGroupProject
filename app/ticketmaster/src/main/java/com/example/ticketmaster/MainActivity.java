package com.example.ticketmaster;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Junfeng Liu
 * 040954725
 * CST2335_010
 * Ticket Master
 */

/**
 * Main activity class
 */
public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    ProgressBar pb_main;
    public boolean isFavourite = false;
    static final int REQUEST_FAVORITE_EDIT = 1;
    MyListAdapter myAdapter = new MyListAdapter();
    private ArrayList<Ticket> elements = new ArrayList<>();
    SQLiteDatabase db;

    /**
     * Called when the activity is starting
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_main = findViewById(R.id.progressBar_Main);

        ListView myList = findViewById(R.id.listViewMain);
        myList.setAdapter(myAdapter);

        myList.setOnItemClickListener((parent, view, position, id) -> {
            Bundle goToTicket = new Bundle();
            goToTicket.putLong("id", myAdapter.getItem(position).getId());
            goToTicket.putString("eventName", myAdapter.getItem(position).getEventName());
            goToTicket.putString("startDate", myAdapter.getItem(position).getStartDate());
            goToTicket.putString("minPrice", myAdapter.getItem(position).getMinPrice());
            goToTicket.putString("maxPrice", myAdapter.getItem(position).getMaxPrice());
            goToTicket.putString("url", myAdapter.getItem(position).getUrl());
            goToTicket.putString("imgUrl", myAdapter.getItem(position).getImgUrl());
            goToTicket.putBoolean("isFavourite", isFavourite);

            Intent nextActivity = new Intent(MainActivity.this, EmptyActivity.class);
            nextActivity.putExtras(goToTicket);
            if(isFavourite){ startActivityForResult(nextActivity, REQUEST_FAVORITE_EDIT);
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
            Toast.makeText(MainActivity.this,"City:"+cityToSearch+" Radius:"+radiusToSearch,Toast.LENGTH_SHORT).show();
            doSearch(cityToSearch, radiusToSearch);
        });

        Button favouriteButton = findViewById(R.id.favouriteButton);
        favouriteButton.setOnClickListener(bt ->
        {
            loadDataFromDatabase();
            myAdapter.notifyDataSetChanged();
            isFavourite = true;
            String msg = getResources().getString(R.string.displayFavorite);
            Snackbar snackbar = Snackbar.make(findViewById(R.id.main), msg, Snackbar.LENGTH_LONG);
            snackbar.show();
        });
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     * @param menu The options menu in which you place your items.
     * @return return true for the menu to be displayed.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * @param item The menu item that was selected.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
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

    /**
     * Called when an activity you launched exits.
     * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_FAVORITE_EDIT && resultCode == RESULT_OK) {
            loadDataFromDatabase();
            myAdapter.notifyDataSetChanged();
        }
    }

    /**
     * A method handle ticket query with city and radius
     * @param city The first parameter
     * @param radius THe second parameter
     */
    private void doSearch(String city, String radius) {
        isFavourite = false;
        TicketQuery query = new TicketQuery(city, radius); //creates a background thread
        query.execute("https://app.ticketmaster.com/discovery/v2/events.json?apikey=rzBTum5fKPpuObmhHA6gkkZuRTYFnR0G " + "&city=" + city + "&radius=" + radius); //Type 1
        String msg = getResources().getString(R.string.displaySearch);
        Snackbar snackbar = Snackbar.make(findViewById(R.id.main), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * A method which load data from database
     */
    private void loadDataFromDatabase() {
        elements.clear();
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        String [] columns = {MyOpener.COL_ID, MyOpener.COL_EVENT_NAME, MyOpener.COL_START_DATE, MyOpener.COL_PRICE_MAX, MyOpener.COL_PRICE_MIN, MyOpener.COL_URL, MyOpener.COL_IMG_URL};
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int eventNameColumnIndex = results.getColumnIndex(MyOpener.COL_EVENT_NAME);
        int startDateColumnIndex = results.getColumnIndex(MyOpener.COL_START_DATE);
        int priceMaxColumnIndex = results.getColumnIndex(MyOpener.COL_PRICE_MAX);
        int priceMinColumnIndex = results.getColumnIndex(MyOpener.COL_PRICE_MIN);
        int urlColumnIndex = results.getColumnIndex(MyOpener.COL_URL);
        int imgUrlColumnIndex = results.getColumnIndex(MyOpener.COL_IMG_URL);

        while(results.moveToNext())
        {
            long id = results.getLong(idColIndex);
            String eventName = results.getString(eventNameColumnIndex);
            String startDate = results.getString(startDateColumnIndex) ;
            String max = results.getString(priceMaxColumnIndex);
            String min = results.getString(priceMinColumnIndex);
            String url = results.getString(urlColumnIndex);
            String imgUrl = results.getString(imgUrlColumnIndex);
            elements.add(new Ticket(id, eventName, startDate, min, max, url, imgUrl));
        }
    }

    /**
     * AsyncTask for ticket query
     */
    private class TicketQuery extends AsyncTask<String, Integer, String> {

        private String eventName;
        private String startDate;
        private String minPrice;
        private String maxPrice;
        private String eventUrl;
        private String imgUrl;
        private static final String TICKET_MASTER_URL = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=rzBTum5fKPpuObmhHA6gkkZuRTYFnR0G ";
        private String city;
        private String radius;

        /**
         * TicketQurey constructor
         * @param city
         * @param radius
         */
        public TicketQuery(String city, String radius) {
            this.city = city;
            this.radius = radius;
        }

        /**
         * Override this method to perform a computation on a background thread.
         * @param args The parameters of the task.
         * @return A result, defined by the subclass of this task.
         */
        protected String doInBackground(String ... args) {
            String fullUrl = TICKET_MASTER_URL+"&city="+this.city+"&radius="+this.radius;
            try {
                Log.e("Start","Start loading information ..");
                elements.clear();
                URL url = new URL(fullUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
                    eventName = event.getString("name");
                    eventUrl = event.getString("url");
                    startDate = event.getJSONObject("dates").getJSONObject("start").getString("localDate");
                    double minPriceDouble = event.getJSONArray("priceRanges").getJSONObject(0).getDouble("min");
                    minPrice = minPriceDouble + "";
                    double maxPriceDouble = event.getJSONArray("priceRanges").getJSONObject(0).getDouble("max");
                    maxPrice = maxPriceDouble + "";
                    imgUrl = event.getJSONArray("images").getJSONObject(0).getString("url");
                    Ticket ticket = new Ticket(eventName, startDate, minPrice+"", maxPrice+"", eventUrl, imgUrl);
                    elements.add(ticket);
                }
            }
            catch (Exception e) {
                Log.e("Error","Error happens");
                e.printStackTrace();
            }
            return "Done";
        }

        /**
         * Runs on the UI thread after publishProgress is invoked.
         * @param args The values indicating progress.
         */
        public void onProgressUpdate(Integer ... args) {
            Log.i("Calling back for data", "Integer=" + args[0]);
            pb_main.setVisibility(View.VISIBLE);
            int a = args[0];
            pb_main.setProgress(args[0]);
        }

        /**
         * Runs on the UI thread after doInBackground.
         * @param fromDoInBackground The result of the operation computed by doInBackground.
         */
        public void onPostExecute(String fromDoInBackground) {
            pb_main.setVisibility(View.INVISIBLE);
            int i=0;
            for(Ticket t: elements){
                i++;
                Log.e("e"+i, t.toString());
            }
            myAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Called as part of the activity lifecycle when the user no longer actively interacts with the activity,
     * but it is still visible on screen.
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("city", ((EditText)findViewById(R.id.editViewCity)).getText().toString());
        editor.putString("radius", ((EditText)findViewById(R.id.editViewRadius)).getText().toString());
        editor.commit();
    }

    class MyListAdapter extends BaseAdapter {

        /**
         * The number of elements in the data set represented by this Adapter.
         * @return count of elements.
         */
        @Override
        public int getCount() { return elements.size(); }

        /**
         * Get the data item associated with the specified position in the data set.
         * @param position Position of the item whose data we want within the adapter's data set.
         * @return The data at the specified position.
         */
        @Override
        public Ticket getItem(int position) { return elements.get(position); }

        /**
         * The element associated with database.
         * @param position Position of the item whose data we want within database.
         * @return the database id of row
         */
        @Override
        public long getItemId(int position) { return elements.get(position).getId();}

        /**
         * Get the row id associated with the specified position in the list.
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @param convertView
         * @param parent
         * @return The id of the item at the specified position.
         */
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