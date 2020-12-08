package com.example.audiodatabaseapi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




/**
 * AlbumFragment- just created a simple fragment with imageview animation
 * the browser
 * @author BRAHIM
 * @title Audio database Api
 * @version 1.0
 */
public class AlbumFragment extends Fragment {

    /**
     * no arg construct public constructor
     */
    public AlbumFragment() {

    }

    /**
     * @param inflater  the layout for this fragment
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_album, container, false);
    }
}
