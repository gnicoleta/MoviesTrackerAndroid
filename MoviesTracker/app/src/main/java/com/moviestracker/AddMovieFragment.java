package com.moviestracker;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMovieFragment extends Fragment {
    private Button btnAdd, btnViewMovies;
    private EditText movie_title;

    DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add_movie, null, false);
        super.onCreate(savedInstanceState);

        db = new DBHelper(getActivity());

        btnAdd = (Button) v.findViewById(R.id.btnAdd);
        btnViewMovies = (Button) v.findViewById(R.id.btnView);
        movie_title = (EditText) v.findViewById(R.id.add_movie_title);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                toastMessage(preferences.getString("Username", ""));
                String item = movie_title.getText().toString();
                if (!item.equals("")) {
                    db.addMovie(item, preferences.getString("Username", ""));
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), DisplayMoviesActivity.class));
                } else {
                    toastMessage("You must enter a name");
                }
            }
        });

        btnViewMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(getActivity(), DisplayMoviesActivity.class);
                startActivity(I);
            }
        });


        return v;
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
