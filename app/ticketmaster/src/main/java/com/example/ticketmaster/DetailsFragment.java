package com.example.ticketmaster;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import static android.app.Activity.RESULT_CANCELED;

public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private AppCompatActivity parentActivity;
    ImageView iv_imgUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        View result = inflater.inflate(R.layout.fragment_details, container, false);

        //show the message
        TextView tv_eventName = result.findViewById(R.id.eventName);
        TextView tv_minPrice = result.findViewById(R.id.minPrice);
        TextView tv_maxPrice = result.findViewById(R.id.maxPrice);
        TextView tv_url = result.findViewById(R.id.url);
        TextView tv_startDate = result.findViewById(R.id.startDate);
        iv_imgUrl = result.findViewById(R.id.eventImage);

        Ticket ticket = new Ticket(dataFromActivity.getString("eventName"),
                dataFromActivity.getString("startDate"),
                dataFromActivity.getString("minPrice"),
                dataFromActivity.getString("maxPrice"),
                dataFromActivity.getString("url"),
                dataFromActivity.getString("imgUrl"));

        tv_eventName.setText(ticket.getEventName());
        tv_minPrice.setText(ticket.getMinPrice());
        tv_maxPrice.setText(ticket.getMaxPrice());
        tv_startDate.setText(ticket.getStartDate());
        tv_url.setText(ticket.getUrl());
        Image img =new Image();
        img.execute(ticket.getImgUrl());

        Boolean isFavourite = dataFromActivity.getBoolean("isFavourite",false);

        Button saveButton = result.findViewById(R.id.resultPageButton);
        if(isFavourite) {
            saveButton.setText(getResources().getString(R.string.delete));
        }else {
            saveButton.setText(getResources().getString(R.string.save));
        }

        saveButton.setOnClickListener(click -> {
            if(isFavourite) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parentActivity);
                String msg = getResources().getString(R.string.deleteFavorite);
                alertDialogBuilder.setTitle(R.string.alerttitle)
                        .setMessage(msg)
                        .setPositiveButton("Yes",(clickButton, arg) -> {
                            DeleteTicket(ticket);
                            parentActivity.setResult(Activity.RESULT_OK);
                            parentActivity.finish();
                        })
                        .setNegativeButton("No", (clickButton, arg) -> {parentActivity.setResult(RESULT_CANCELED);  });
                alertDialogBuilder.create().show();
            }else {
                InsertTicket(ticket);
                Context context = parentActivity.getApplicationContext();
                Toast toast = Toast.makeText(context, getResources().getString(R.string.saved_message), Toast.LENGTH_LONG );
                toast.show();
            }
            //parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });
        return result;
    }

    private class Image extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            return getBitmapFromURL(url);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            iv_imgUrl.setImageBitmap (result);
            super.onPostExecute(result);
        }

    }

    private static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void DeleteTicket(Ticket ticket)
    {
        MyOpener dbOpener = new MyOpener(parentActivity);
        SQLiteDatabase db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_EVENT_NAME + "= ?", new String[] {ticket.getEventName()});
        db.close();
    }

    protected void InsertTicket(Ticket ticket)
    {
        MyOpener dbOpener = new MyOpener(parentActivity);
        SQLiteDatabase db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer

        ContentValues newValues = new ContentValues();
        newValues.put(MyOpener.COL_EVENT_NAME, ticket.getEventName());
        newValues.put(MyOpener.COL_START_DATE, ticket.getStartDate());
        newValues.put(MyOpener.COL_PRICE_MAX, ticket.getMaxPrice());
        newValues.put(MyOpener.COL_PRICE_MIN, ticket.getMinPrice());
        newValues.put(MyOpener.COL_URL, ticket.getUrl());
        newValues.put(MyOpener.COL_IMG_URL, ticket.getImgUrl());
        db.insert(MyOpener.TABLE_NAME, null,newValues);
        db.close();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity) context;
    }



}
