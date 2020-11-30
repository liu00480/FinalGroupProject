package com.example.ticketmaster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    //private long id;
    //private boolean isSend;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
//        id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID );
//        isSend = dataFromActivity.getBoolean((ChatRoomActivity.ITEM_ISSEND));

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        //show the message
        TextView tv_eventName = result.findViewById(R.id.eventName);
        TextView tv_minPrice = result.findViewById(R.id.minPrice);
        TextView tv_maxPrice = result.findViewById(R.id.maxPrice);
        TextView tv_url = result.findViewById(R.id.url);
        TextView tv_startDate = result.findViewById(R.id.startDate);
        ImageView iv_imgUrl = result.findViewById(R.id.eventImage);

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
        //iv_imgUrl.setImageBitmap(ticket.getImgUrl());

//        String imgUrl;
//        public Bitmap downLoadImage(String imgUrl) {
//            Bitmap image = null;
//            try{
//                URL url = new URL(ticket.getImgUrl());
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//                int responseCode = connection.getResponseCode();
//                if (responseCode == 200) {
//                    image = BitmapFactory.decodeStream(connection.getInputStream());
//                }
//                FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
//                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
//                outputStream.flush();
//                outputStream.close();
//            }
//            catch (Exception e) { }
//            return image;
//        }

//        Ticket ticket = new Ticket(dataFromActivity.getString())
//        //message.setText(dataFromActivity.getString(ChatRoomActivity.ITEM_SELECTED));
//        message.setText(getString(R.string.hello_blank_fragment));
//
//        //show the id:
//        TextView idView = (TextView)result.findViewById(R.id.fragment_ID);
//        idView.setText(getString(R.string.fragment_ID) + id);
//
//        CheckBox cb = (CheckBox)result.findViewById(R.id.fragment_CB);
//        if(isSend) { cb.setChecked(true); } else { cb.setChecked(false); }
//
//        // get the delete button, and add a click listener:
//        Button finishButton = (Button)result.findViewById(R.id.fragment_BT);
//        finishButton.setOnClickListener( clk -> {
//
//            //Tell the parent activity to remove
//            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
//        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}
