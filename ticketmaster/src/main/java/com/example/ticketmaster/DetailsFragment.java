package com.example.ticketmaster;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
        View result =  inflater.inflate(R.layout.fragment_details_liu, container, false);

        //show the message
        //TextView message = (TextView)result.findViewById(R.id.hello_blank_fragment);
        TextView tv_eventName = (TextView)result.findViewById(R.id.eventName);
        TextView tv_minPrice = (TextView)result.findViewById(R.id.minPrice);
        TextView tv_maxPrice = (TextView)result.findViewById(R.id.maxPrice);
        TextView tv_url = (TextView)result.findViewById(R.id.url);
        TextView tv_startDate = (TextView)result.findViewById(R.id.startDate);

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