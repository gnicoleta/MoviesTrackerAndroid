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

public class EditMovieFragment extends Fragment {
    private Button btnSave, btnDelete;
    private EditText editable_item;
    DBHelper db;
    private String selectedName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_edit_movie, null, false);
        super.onCreate(savedInstanceState);
        db = new DBHelper(getActivity());
        btnSave = (Button) v.findViewById(R.id.btnSave);
        btnDelete = (Button) v.findViewById(R.id.btnDelete);
        editable_item = (EditText) v.findViewById(R.id.editable_item);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //get the intent extra from the ListDataActivity
        final Intent receivedIntent = new Intent(getActivity(), DisplayMoviesActivity.class);
        selectedName = preferences.getString("movie_name", "");

        //set the text to show the current selected name
        editable_item.setText(selectedName);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editable_item.getText().toString();
                if (!item.equals("")) {
                    db.updateMovie(selectedName, item);
                    toastMessage("Movie updated");
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), DisplayMoviesActivity.class));
                } else {
                    toastMessage("You must enter a name");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                toastMessage(selectedName);
                db.deleteMovie(selectedName, preferences.getString("Username", ""));
                editable_item.setText("");
                toastMessage("removed from database");
                getActivity().finish();
                startActivity(new Intent(getActivity(), DisplayMoviesActivity.class));
            }
        });
        return v;
    }

    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
