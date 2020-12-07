package com.example.recipesearchpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RecipeMainActivity extends AppCompatActivity {

    ProgressBar pb;
    public  boolean isFavorite=false;
    static final int REQUEST_FAVORITE_EDIT = 1;

    MyListAdapter myAdapter;
    private ArrayList<RecipeInfo> elements = new ArrayList<>();

    private SharedPreferences prefs=null;

    SQLiteDatabase db;

    /**
     *Called as part of the activity lifecycle when the user no longer
     * actively interacts with the activity, but it is still visible on screen.
     * The counterpart to onResume()
     */
    @Override
    protected void onPause() {
        super.onPause();

        SearchView search_vw=findViewById(R.id.search_view);
        String stringToSave = search_vw.getQuery().toString();

        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastSearch", stringToSave);
        editor.commit();

    }

    /**
     *
     * Called when the activity is starting. This is where most initialization should go:
     * calling setContentView(int) to inflate the activity's UI,
     * using findViewById(int) to programmatically interact with widgets in the UI,
     * calling managedQuery(android.net.Uri, java.lang.String[], java.lang.String,
     * java.lang.String[], java.lang.String) to retrieve cursors for data being displayed, etc.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipemain);
        pb=findViewById(R.id.pBar);
        //pb.setVisibility(View.VISIBLE);


        ListView myList = findViewById(R.id.theListView);
        myAdapter = new MyListAdapter();
        myList.setAdapter(myAdapter);

        myList.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            Bundle goToRecipe = new Bundle();
            goToRecipe.putString("title", myAdapter.getItem(position).title());
            goToRecipe.putString("url", myAdapter.getItem(position).url());
            goToRecipe.putString("ingredients", myAdapter.getItem(position).ingredients());
            goToRecipe.putBoolean("isFavorite", isFavorite);

            Intent nextActivity = new Intent(RecipeMainActivity.this, Empty.class);
            nextActivity.putExtras(goToRecipe); //send data to next activity
            if(isFavorite){
                startActivityForResult(nextActivity,REQUEST_FAVORITE_EDIT);
            }else {
                startActivity(nextActivity);
            }

        });

        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);

        String savedString = prefs.getString("lastSearch", "");

        SearchView search_vw=findViewById(R.id.search_view);
        Button goButton = findViewById(R.id.go);

        goButton.setEnabled(false);
        goButton.setOnClickListener( click -> {
            CharSequence query=search_vw.getQuery();
            doSearch(query.toString());
        });

        Button favButton = findViewById(R.id.favorite);
        favButton.setOnClickListener( click -> {
            loadDataFromDatabase();
            myAdapter.notifyDataSetChanged();
            isFavorite=true;
            String msg = getResources().getString(R.string.displayFavorite);
            Snackbar snackbar = Snackbar.make(findViewById(R.id.CoordinatorLayout), msg, Snackbar.LENGTH_LONG);
            snackbar.show();
        });

        search_vw.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /**
             * Called when the user submits the query. This could be due to a key press on the keyboard
             * or due to pressing a submit button. The listener can override the standard behavior by
             * returning true to indicate that it has handled the submit request. Otherwise return false
             * to let the SearchView handle the submission by launching any associated intent.
             * @param query String: the query text that is to be submitted
             * @return true if the query has been handled by the listener, false to let the SearchView perform the default action.
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                CharSequence qry=search_vw.getQuery();
                search_vw.clearFocus();
                doSearch(qry.toString());
                return false;
            }

            /**
             * Called when the query text is changed by the user.
             * @param newText String: the new content of the query text field.
             * @return false if the SearchView should perform the default action of showing any suggestions if available, true
             * if the action was handled by the listener.
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                CharSequence qry=search_vw.getQuery();
                goButton.setEnabled(qry.toString().trim().length()!=0);

                return false;
            }
        });

        search_vw.setQuery(savedString,true);
    }

    /**
     * Initialize the contents of the Activity's standard options menu. You should place your menu items in to menu.
     * @param menu Menu: The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected. The default implementation
     * simply returns false to have the normal processing happen (calling the item's Runnable or sending
     * a message to its Handler as appropriate). You can use this method for any items for which you would
     * like to do processing without those other facilities.
     * @param item MenuItem: The menu item that was selected. This value cannot be null.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
        switch (item.getItemId()){
            case R.id.help_menu:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                String msg = getResources().getString(R.string.helpText);
                alertDialogBuilder.setTitle(R.string.helptitle)
                        .setMessage(msg)
                        .setPositiveButton("ok",(clickButton, arg) -> {
                        });
                alertDialogBuilder.create().show();
                return true;
        }
        return false;
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it. The resultCode will be RESULT_CANCELED
     * if the activity explicitly returned that, didn't return any result, or crashed during its operation.
     * @param requestCode int: The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode int: The integer result code returned by the child activity through its setResult().
     * @param data Intent: An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_FAVORITE_EDIT && resultCode == RESULT_OK) {
            loadDataFromDatabase();
            myAdapter.notifyDataSetChanged();
        }
    }

    private class MyListAdapter extends BaseAdapter {

        /**
         * How many items are in the data set represented by this Adapter.
         * @return 	Count of items.
         */
    @Override
    public int getCount() {
        return elements.size() ;
    }

        /**
         * Get the data item associated with the specified position in the data set.
         * @param i int: Position of the item whose data we want within the adapter's data set.
         * @return 	The data at the specified position.
         */
    @Override //shows what string is at row i (0-9)
    public RecipeInfo getItem(int i) {
        return elements.get(i);
    }

        /**
         *
         * @param i get item id
         * @return
         */
    @Override //returns the database id of row i
    public long getItemId(int i) { return 0;} //worry about this next week

        /**
         * Get the row id associated with the specified position in the list.
         * @param i int: The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
    @Override //how to show row i
    public View getView(int i, View view, ViewGroup viewGroup) {
        RecipeInfo msg=getItem(i);
        LayoutInflater inflater = getLayoutInflater(); //this loads xml layouts
        View thisRow;

        thisRow= inflater.inflate(R.layout.row_title, viewGroup, false);
        TextView tv = thisRow.findViewById(R.id.texttitle);
        tv.setText( getItem(i).title() ); //what goes in row i
        return thisRow;
    }
}
    private class RecipeQuery extends AsyncTask< String, Integer, String> {

        /**
         * Override this method to perform a computation on a background thread.
         * The specified parameters are the parameters passed to execute(Params...)
         * by the caller of this task. This will normally run on a background thread.
         * But to better support testing frameworks, it is recommended that this also
         * tolerates direct execution on the foreground thread, as part of the
         * execute(Params...) call. This method can call publishProgress(Progress...)
         * to publish updates on the UI thread.
         * @param args Params: The parameters of the task.
         * @return A result, defined by the subclass of this task.
         */
        //Type3                      Type1
        public String doInBackground(String... args) {
            try{
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

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
                JSONArray resultArray= jObject.getJSONArray("results");
                if(resultArray!=null && resultArray.length()>0) {
                    elements.clear();
                    for (int i = 0; i < resultArray.length(); i++) {
                        publishProgress( (i*100/resultArray.length()));
                         Thread.sleep(100);
                        JSONObject recipe = resultArray.getJSONObject(i);
                        RecipeInfo newRecipe = new RecipeInfo(recipe.getString("title"),
                                recipe.getString("href"),
                                recipe.getString("ingredients"));
                        elements.add(newRecipe);

                    }
                }

            }catch(Exception ex){
                int i=0;
            }

            publishProgress(100);

            return "Done";
        }

        /**
         * Runs on the UI thread after publishProgress(Progress...) is invoked.
         * The specified values are the values passed to publishProgress(Progress...).
         * The default version does nothing.
         * @param args Progress: The values indicating progress.
         */
        //Type2
        public void onProgressUpdate(Integer... args) {
            //updating your GUI
            Log.i("Calling back for data", "Integer=" + args[0]);
            pb.setVisibility(View.VISIBLE);
            int a = args[0];
            pb.setProgress(args[0]);

        }

        /**
         * Runs on the UI thread after doInBackground(Params...). The specified result
         * is the value returned by doInBackground(Params...). To better support testing
         * frameworks, it is recommended that this be written to tolerate direct execution
         * as part of the execute() call. The default version does nothing.
         * @param fromDoInBackground Result: The result of the operation computed by doInBackground(Params...).
         */
        //Type3
        public void onPostExecute(String fromDoInBackground) {
            //means doInBackground is done, and no more progress updates.
            pb.setVisibility(View.INVISIBLE);
            myAdapter.notifyDataSetChanged();
        }

    }

    /**
     * search
     * @param searchStr search query string
     */
    private void doSearch(String searchStr){
        isFavorite=false;
        RecipeQuery req = new RecipeQuery(); //creates a background thread
        req.execute("http://www.recipepuppy.com/api/?q=" +searchStr); //Type 1
        String msg = getResources().getString(R.string.displaySearch);
        Snackbar snackbar = Snackbar.make(findViewById(R.id.CoordinatorLayout), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * load data from database
     */
    private void loadDataFromDatabase()
    {
        elements.clear();
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MyOpener.COL_TITLE, MyOpener.COL_URL,MyOpener.COL_INGREDIENTS};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int titleColumnIndex = results.getColumnIndex(MyOpener.COL_TITLE);
        int urlColIndex = results.getColumnIndex(MyOpener.COL_URL);
        int ingredientsColIndex = results.getColumnIndex(MyOpener.COL_INGREDIENTS);


        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String title = results.getString(titleColumnIndex);
            String url = results.getString(urlColIndex) ;
            String ingredients = results.getString(ingredientsColIndex);

            //add the new Contact to the array list:
            elements.add(new RecipeInfo(title, url, ingredients));
        }
        //printCursor(results,5);
        //At this point, the contactsList array has loaded every row from the cursor.
    }

}