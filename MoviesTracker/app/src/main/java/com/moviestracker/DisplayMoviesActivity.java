package com.moviestracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DisplayMoviesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if this activity was created before
        if (savedInstanceState == null) {
            // Create a fragment
            DisplayMoviesFragment fragment = new DisplayMoviesFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName()).commit();
        }

    }


    // Called to lazily initialize the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Called every time user clicks on an action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_add_movie:
                startActivity(new Intent(this, AddMovieActivity.class));
                return true;
            case R.id.action_log_out: {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DisplayMoviesActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(DisplayMoviesActivity.this, LoginActivity.class);
                startActivity(i);
                return true;
            }
            default:
                return false;
        }
    }
}
