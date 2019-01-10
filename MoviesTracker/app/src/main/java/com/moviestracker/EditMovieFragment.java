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
    //private int selectedID;

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

        //Intent receivedIntent = new Intent(getActivity(), EditMovieActivity.class);

        //now get the itemID we passed as an extra
        //selectedID = receivedIntent.getIntExtra("id", -1); //NOTE: -1 is just the default value

        //now get the name we passed as an extra
        //selectedName = receivedIntent.getStringExtra("selected_name");
        selectedName = preferences.getString("movie_name","");

        //set the text to show the current selected name
        editable_item.setText(selectedName);
        toastMessage("CE PLM: "+selectedName);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastMessage("CE PLM: "+selectedName);
                String item = editable_item.getText().toString();
                if (!item.equals("")) {
                    //toastMessage(receivedIntent.getStringExtra("selected_name"));
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
                toastMessage("CE PLM: "+selectedName);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                toastMessage(selectedName);
                db.deleteMovie(selectedName, preferences.getString("Username",""));
                editable_item.setText("");
                toastMessage("removed from database");
                getActivity().finish();
                startActivity(new Intent(getActivity(), DisplayMoviesActivity.class));
            }
        });


        return v;
    }

    /**
     * customizable toast
     *
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
