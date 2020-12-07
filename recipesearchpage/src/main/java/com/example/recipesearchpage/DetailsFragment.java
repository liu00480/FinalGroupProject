


package com.example.recipesearchpage;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null
     * (which is the default implementation). This will be called between
     * onCreate(android.os.Bundle) and onActivityCreated(android.os.Bundle).
     * @param inflater LayoutInflater: The LayoutInflater object that can be
     *                 used to inflate any views in the fragment,
     * @param container ViewGroup: If non-null, this is the parent view that
     *                  the fragment's UI should be attached to. The fragment
     *                  should not add the view itself, but this can be used
     *                  to generate the LayoutParams of the view. This value
     *                  may be null.
     * @param savedInstanceState Bundle: If non-null, this fragment is being
     *                           re-constructed from a previous saved state
     *                           as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        TextView tv_title =result.findViewById(R.id.title);
        TextView tv_url =result.findViewById(R.id.url);
        TextView tv_ingredients =result.findViewById(R.id.ingredients);

        RecipeInfo rcp = new RecipeInfo(dataFromActivity.getString("title"),
                dataFromActivity.getString("url"),
                dataFromActivity.getString("ingredients"));

        tv_title.setText(rcp.title());
        tv_url.setText(Html.fromHtml(rcp.url()));
        tv_ingredients.setText(rcp.ingredients());

        Boolean isFavorite=dataFromActivity.getBoolean("isFavorite",false);

        Button saveButton = result.findViewById(R.id.savButton);
        if (isFavorite){
            saveButton.setText(getResources().getString(R.string.deletebutton));
        }else {
            saveButton.setText(getResources().getString(R.string.savebutton));
        }

        saveButton.setOnClickListener( click -> {
            if(isFavorite){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parentActivity);
                String msg = getResources().getString(R.string.deleteFavorite);
                alertDialogBuilder.setTitle(R.string.alerttitle)
                        .setMessage(msg)
                        .setPositiveButton("Yes",(clickButton, arg) -> {
                            deleterecipe(rcp);
                            parentActivity.setResult(Activity.RESULT_OK);
                            parentActivity.finish();
                        })
                        .setNegativeButton("No", (clickButton, arg) -> {parentActivity.setResult(RESULT_CANCELED);  });
                alertDialogBuilder.create().show();

            }else{
                insertrecipe(rcp);
                Context context = parentActivity.getApplicationContext();
                Toast toast = Toast.makeText(context, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG );
                toast.show();
            }
            //parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });
        return result;
    }

    /**
     * delete recipe info from database
     * @param rcp item to be deleted
     */
    public void deleterecipe(RecipeInfo rcp)
    {
        MyOpener dbOpener = new MyOpener(parentActivity);
        SQLiteDatabase db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_TITLE + "= ?", new String[] {rcp.title()});
        db.close();
    }

    /**
     * insert recipe to database
     * @param rcp item to insert
     */
    protected void insertrecipe(RecipeInfo rcp)
    {
        MyOpener dbOpener = new MyOpener(parentActivity);
        SQLiteDatabase db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer

        ContentValues newValues = new ContentValues();
        newValues.put(MyOpener.COL_TITLE, rcp.title());
        newValues.put(MyOpener.COL_URL, rcp.url());
        newValues.put(MyOpener.COL_INGREDIENTS, rcp.ingredients());
        db.insert(MyOpener.TABLE_NAME, null,newValues);
        db.close();
    }

    /**
     * Called when a fragment is first attached to its context. onCreate(android.os.Bundle) will be called after this.
     * If you override this method you must call through to the superclass implementation.
     * @param context Context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }

    /**
     * display fragment details
     */
    public DetailsFragment() {
        // Required empty public constructor
    }

}


