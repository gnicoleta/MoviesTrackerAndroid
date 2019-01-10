package com.moviestracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MoviesListAdapter extends ArrayAdapter<Movie> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView name;
        TextView seen_date;
        ImageView image;
    }

    public MoviesListAdapter(Context context, int resource, ArrayList<Movie> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }
}



