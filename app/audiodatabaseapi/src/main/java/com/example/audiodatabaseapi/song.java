package com.example.audiodatabaseapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.finalgroupproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Song- a class for showing the menu and navigational element.
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class song extends AppCompatActivity {
    EditText Edit1;
    ArrayAdapter<String> p;
    List<String> list;
    ALBUM_DATABASE db;
    ListView listSve;
    Cursor cursor;

    /**
     * here we set up variables
     * create activity movement with intent
     * added dialogs and toast
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        list = new ArrayList<String>();
        EditText Edit = (EditText) findViewById(R.id.textView7);
        Button google = (Button) findViewById(R.id.button2);
        db = new ALBUM_DATABASE(this);
        EditText albumadd = (EditText) findViewById(R.id.textView9);
        Button add = (Button) findViewById(R.id.button3);
        listSve = (ListView) findViewById(R.id.listview);
        Button saved = (Button) findViewById(R.id.button3);
        ReadDATA();
        listSve.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder alrtBldr = new AlertDialog.Builder(song.this);
                AlertDialog alertDialog = alrtBldr.setTitle("Do you want to delete this message?")
                        .setMessage("The message position is " + listSve.getItemIdAtPosition(position))
                        .setPositiveButton("Delete", (click, args) -> {
                            list.remove(position);
                            p.notifyDataSetChanged();
                        })
                        .setNegativeButton("Close", (click, arg) -> {
                            finish();
                        })
                        .create();

                alertDialog.show();

            }
        });
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = albumadd.getText().toString();
                if (name.equals("") || db.insertData(name)) {
                    Toast.makeText(getApplicationContext(), " text added", Toast.LENGTH_SHORT).show();
                    albumadd.setText("");

                } else {
                    Toast.makeText(getApplicationContext(), " text not added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        google.setOnClickListener(new View.OnClickListener() {


            public void onClick(View arg0) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

                intent.setData(Uri.parse("http://www.google.com/search?q=" + Edit.getText().toString()));
                startActivity(intent);



            }
        });
    }


    /**
     *read database element sequentially
     * check if elements are empty or not
     */
    public  void ReadDATA() {
        cursor = db.ReadDATA();
        if(cursor.getCount()==0){
            Toast.makeText(getApplicationContext(), "empty", Toast.LENGTH_SHORT).show(); }
        else{ while (cursor.moveToNext()){
            list.add(cursor.getString(1)); }
            p=new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            listSve.setAdapter( p); }
    }
}
