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

    Button button_add_movie;
    DBHelper db;
    String selected_name;

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
//                String text = search_movie.getText().toString().toLowerCase(Locale.getDefault());
//                toastMessage("PAPAPAP:" + text + "WTF S:" + s);
//                adapter2.getFilter().filter((CharSequence)text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        button_add_movie = (Button) v.findViewById(R.id.button_add_movie);
//        button_add_movie.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addMovie_intent = new Intent(getActivity(), AddMovieActivity.class);
//                startActivity(addMovie_intent);
//            }
//        });

        db = new DBHelper(getActivity());

        mListView = (ListView) v.findViewById(R.id.listView);

        populateListView();

        return v;
    }

    private void populateListView() {


        //Add the Person objects to an ArrayList
        ArrayList<Movie> moviesList = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //get the data and append to a list
        Cursor data = db.getData(preferences.getString("Username", ""));
        ArrayList<String> listData = new ArrayList<>();
        ArrayList<String> listData2 = new ArrayList<>();

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> item;


//        Cursor urls = db.getALLMoviePic();


        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1)); //title
            listData2.add(data.getString(2)); //date

            //takes the pic for the specified movie
            //       Cursor urls = db.getMoviePic(data.getString(1));

            String movie_name = data.getString(1).replaceAll(" ", "_");

//            moviesList.add(new Movie(data.getString(1), data.getString(2), "drawable://" + str + ".jpg"));
//            if(urls==null) {
//                //moviesList.add(new Movie(data.getString(1), data.getString(2), "drawable://" + R.drawable.empty));
//            } else {
//                String url_str = urls.getString(1);
//                toastMessage(url_str);
//                moviesList.add(new Movie(data.getString(1), data.getString(2), url_str));
//            }


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
        //ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,listData);

        //ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_2, listData, listData2);
        adapter = new SimpleAdapter(getActivity(), list,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "date"},
                new int[]{android.R.id.text1,
                        android.R.id.text2});
        //mListView.setAdapter(adapter);

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

                //adapterView.getItemAtPosition(i).

//                StringTokenizer st1 = new StringTokenizer(adapterView.getItemAtPosition(i).toString());
//
//                for (int o = 1; st1.hasMoreTokens(); o++) {
//                    toastMessage("Token " + o + ": " + st1.nextToken());
//
//                }

                // split your string so we can examine each word separately
   /*             String movie_tokens[] = movie.split("=");
                String date = movie_tokens[1].split(" ")[0];
                String title = movie_tokens[2].split("\\}")[0];
                toastMessage("DADA: " + date);

                toastMessage("DADA2: " + title);

                toastMessage("MA DIC: " + adapterView.getItemAtPosition(i).toString());
*/
                Cursor data = db.getItemID(m.getTitle()); //get the id associated with that name
                int itemID = -1;
//                while (data.moveToNext()) {
//                    itemID = data.getInt(2);
//                }
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
