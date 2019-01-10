package com.moviestracker;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class DisplayMoviesFragment extends Fragment {

    Button button_add_movie;
    DBHelper db;
    String selected_name;

    EditText search_movie;
    SimpleAdapter adapter;

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
            adapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });

        button_add_movie = (Button) v.findViewById(R.id.button_add_movie);
        button_add_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMovie_intent = new Intent(getActivity(), AddMovieActivity.class);
                startActivity(addMovie_intent);
            }
        });

        db = new DBHelper(getActivity());

        mListView = (ListView) v.findViewById(R.id.listView);

        populateListView();

        return v;
    }

    private void populateListView() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //get the data and append to a list
        Cursor data = db.getData(preferences.getString("Username",""));
        ArrayList<String> listData = new ArrayList<>();
        ArrayList<String> listData2 = new ArrayList<>();

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> item;


        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
            listData2.add(data.getString(2));

            item = new HashMap<String,String>();
            item.put( "title", data.getString(1));
            item.put( "date", data.getString(2));
            list.add( item );
        }


        //create the list adapter and set the adapter
        //ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,listData);

        //ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_2, listData, listData2);
        adapter = new SimpleAdapter(getActivity(), list,
                android.R.layout.simple_list_item_2,
                new String[] {"title", "date"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();


                String movie = adapterView.getItemAtPosition(i).toString();

                //adapterView.getItemAtPosition(i).

                StringTokenizer st1 = new StringTokenizer(adapterView.getItemAtPosition(i).toString());

                for (int o = 1; st1.hasMoreTokens(); o++){
                    toastMessage("Token "+o+": "+st1.nextToken());

                }

                // split your string so we can examine each word separately
                String movie_tokens[] = movie.split("=");
                String date = movie_tokens[1].split(" ")[0];
                String title = movie_tokens[2].split("\\}")[0];
                toastMessage("DADA: " + date);

                toastMessage("DADA2: " +  title);

             //   String noVowels = "";

//                for (int i = 0; i < words.length; i++){
//                    char firstChar = words[i].charAt(0);
//                    String temp = words[i].substring(1, words[i].length()).replaceAll("[aeiou]", "");
//                    noVowels += firstChar + temp + " ";
//                }
//
//                return noVowels;
//            }
            toastMessage("MA DIC: " + adapterView.getItemAtPosition(i).toString());

                Cursor data = db.getItemID(title); //get the id associated with that name
                int itemID = -1;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);
                }
                    Intent editScreenIntent = new Intent(getActivity(), EditMovieActivity.class);
                    editScreenIntent.putExtra("id", itemID);
                    editScreenIntent.putExtra("name", title);
                    editScreenIntent.putExtra("selected_name", title);
                    editor.putString("movie_name", title);
                    editor.apply();
                    startActivity(editScreenIntent);
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
