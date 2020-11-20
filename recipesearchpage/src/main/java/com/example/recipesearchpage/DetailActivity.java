package com.example.recipesearchpage;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tv_title =findViewById(R.id.title);

        TextView tv_url =findViewById(R.id.url);
        TextView tv_ingredients =findViewById(R.id.ingredients);

        Intent fromMain = getIntent();

        RecipeInfo rcp = new RecipeInfo(fromMain.getStringExtra("title"),
                fromMain.getStringExtra("url"),
                fromMain.getStringExtra("ingredients"));

        tv_title.setText(rcp.title());
        tv_url.setText(Html.fromHtml(rcp.url()));
        tv_ingredients.setText(rcp.ingredients());

        Boolean isFavorite=fromMain.getBooleanExtra("isFavorite",false);

        Button saveButton = findViewById(R.id.savButton);
        if (isFavorite){
            saveButton.setText("Delete");
        }else {
            saveButton.setText("Save");
        }

        saveButton.setOnClickListener( click -> {
            if(isFavorite){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                String msg = getResources().getString(R.string.deleteFavorite);
                alertDialogBuilder.setTitle(R.string.alerttitle)
                        .setMessage(msg)
                        .setPositiveButton("Yes",(clickButton, arg) -> {
                            deleteMessage(rcp);
                            setResult(Activity.RESULT_OK);
                            finish();
                            })
                        .setNegativeButton("No", (clickButton, arg) -> {setResult(RESULT_CANCELED);  });
                alertDialogBuilder.create().show();

            }else{
                insertMessage(rcp);
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG );
                toast.show();
            }

        });


        //Button gotoChatButton = findViewById(R.id.gotochat);

    }

    public void deleteMessage(RecipeInfo rcp)
    {
        MyOpener dbOpener = new MyOpener(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_TITLE + "= ?", new String[] {rcp.title()});
        db.close();
    }

    protected void insertMessage(RecipeInfo rcp)
    {
        MyOpener dbOpener = new MyOpener(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer

        ContentValues newValues = new ContentValues();
        newValues.put(MyOpener.COL_TITLE, rcp.title());
        newValues.put(MyOpener.COL_URL, rcp.url());
        newValues.put(MyOpener.COL_INGREDIENTS, rcp.ingredients());
        db.insert(MyOpener.TABLE_NAME, null,newValues);
        db.close();
    }
}