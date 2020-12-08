package com.example.audiodatabaseapi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * ArtistSearch - a class where user can freely search for artist Name through the
 * {@linktourl https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example}
 * {@linktourl https://www.vogella.com/tutorials/AndroidRecyclerView/article.html}
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    private List<AlbumData> list;
    private Context context;

    /**
     * arg constructor in which I reference my arralist and activity context
     * @param list
     * @param applicationContext
     */
    public AlbumAdapter(List<AlbumData> list, Context applicationContext) {
        this.list=list;
        context=applicationContext;
    }


    /**
     *     using recyclerview instead of asyntask to properly orgainized my listview
     *     with size based on layouts and send data
     *     @link https://www.vogella.com/tutorials/AndroidRecyclerView/article.html
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_card,parent,false);
        System.out.println("Data Card");
        return new ViewHolder(v);
    }

    /**
     *  using android picasso library of androids
     *  {@linktourl https://code.tutsplus.com/tutorials/android-sdk-working-with-picasso--cms-22149}
     *  {@linktourl https://www.vogella.com/tutorials/AndroidRecyclerView/article.html}
     *  to get loaded album  in case the previous one is empty
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        AlbumData albumData = list.get(position);
        System.out.println(albumData.getAlbumURL());
        holder.albumID.setText(albumData.getAlbumID());
        holder.Name.setText(albumData.getAlbumName());

        try {
            if (!albumData.getAlbumURL().isEmpty())

                Picasso.get().load(albumData.getAlbumURL()).into(holder.imageView);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * @return the list size
     */
    @Override
    public int getItemCount() {

        System.out.println("Get Count"+list.size());

        return list.size();

    }

    /**
     *ViewHolder is a class that  describes an item view and metadata about its place within the RecyclerView
     *
     * also , I set a click listener to  push all saved data from my details calss to get the text in my listview
     * {@linktourl  https://www.vogella.com/tutorials/AndroidRecyclerView/article.html}
     * localized when user press the button from th super class of item view to access all views
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Name,albumID;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Name=itemView.findViewById(R.id.albumName);
            albumID=itemView.findViewById(R.id.albumID);
            imageView=itemView.findViewById(R.id.ablumImage);

            itemView.setOnClickListener(view -> {

                Intent i = new Intent(view.getContext(), AlbumDetails.class);
                i.putExtra("AlbumID",albumID.getText());
                System.out.println("Onclick send: "+albumID.getText());
                i.putExtra("AlbumName",Name.getText());


                view.getContext().startActivity(i);
            });
        }
    }
}

