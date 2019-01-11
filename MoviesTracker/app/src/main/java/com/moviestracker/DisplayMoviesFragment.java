package com.moviestracker;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

public class DisplayMoviesFragment extends Fragment {

    DBHelper db;
    EditText search_movie;
    SimpleAdapter adapter;
    MoviesListAdapter adapter2;
    private ListView mListView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_display_movies, null, false);
        super.onCreate(savedInstanceState);
        search_movie = (EditText) v.findViewById(R.id.search_movie_list);
        search_movie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter2.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        db = new DBHelper(getActivity());
        mListView = (ListView) v.findViewById(R.id.listView);
        populateListView();
        return v;
    }

    private void populateListView() {
        ArrayList<Movie> moviesList = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //get the data and append to a list
        Cursor data = db.getData(preferences.getString("Username", ""));
        ArrayList<String> listData = new ArrayList<>();
        ArrayList<String> listData2 = new ArrayList<>();
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> item;

        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1)); //title
            listData2.add(data.getString(2)); //date
            String movie_name = data.getString(1).replaceAll(" ", "_");
            //to do, if url doe not contain a valid img, put empty.png instead.
            String url_pic_cloud = "https://res.cloudinary.com/dbgcvpzrs/image/upload/ur_api_key/samples/movies/" + movie_name + ".jpg";
            moviesList.add(new Movie(data.getString(1), data.getString(2), url_pic_cloud));
            item = new HashMap<String, String>();
            item.put("title", data.getString(1));
            item.put("date", data.getString(2));
            list.add(item);
        }

        adapter2 = new MoviesListAdapter(getActivity(), R.layout.adapter_view_layout, moviesList);
        mListView.setAdapter(adapter2);

        //create the list adapter and set the adapter
        adapter = new SimpleAdapter(getActivity(), list,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "date"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();
                String movie = adapterView.getItemAtPosition(i).toString();
                toastMessage(adapterView.getItemAtPosition(i).getClass().getSimpleName());
                Movie m = (Movie) adapterView.getItemAtPosition(i);
                toastMessage(m.getTitle());
                Cursor data = db.getItemID(m.getTitle()); //get the id associated with that name
                int itemID = -1;
                Intent editScreenIntent = new Intent(getActivity(), EditMovieActivity.class);
                editScreenIntent.putExtra("id", itemID);
                editScreenIntent.putExtra("name", m.getTitle());
                editScreenIntent.putExtra("selected_name", m.getTitle());
                editor.putString("movie_name", m.getTitle());
                editor.apply();
                startActivity(editScreenIntent);
            }
        });

    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
