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

public class LoginFragment extends Fragment {

    EditText email_txt, pass_txt;
    Button log_in_btn, reg_btn;
    DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_login, null, false);
        super.onCreate(savedInstanceState);

        db = new DBHelper(getActivity());

        email_txt = (EditText) v.findViewById(R.id.email_login);
        pass_txt = (EditText) v.findViewById(R.id.login_password);

        log_in_btn = (Button) v.findViewById(R.id.button_login);
        reg_btn = (Button) v.findViewById(R.id.login_register);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg_intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(reg_intent);
            }
        });

        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_txt.getText().toString();
                String password = pass_txt.getText().toString();

                boolean chekedEmailPassword = db.checkEmailPAssword(email, password);
                if (chekedEmailPassword == true) {
                    Toast.makeText(getActivity(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("Registered", true);
                    editor.putString("Username", email);
                    editor.putString("Password", password);
                    editor.apply();
                    Intent I = new Intent(getActivity(), DisplayMoviesActivity.class);
                    startActivity(I);
                } else {
                    Toast.makeText(getActivity(), "Wrong email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }
}
