package com.moviestracker;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    EditText email_txt, name_txt,
            password_txt,
            cpassword_txt;
    Button reg_btn, lgn_btn;
    DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_register, null, false);
        super.onCreate(savedInstanceState);

        db = new DBHelper(getActivity());

        email_txt = (EditText) v.findViewById(R.id.email);
        name_txt = (EditText) v.findViewById(R.id.name);
        password_txt = (EditText) v.findViewById(R.id.password);
        cpassword_txt = (EditText) v.findViewById(R.id.confirm_password);
        reg_btn = (Button) v.findViewById(R.id.button_register);
        lgn_btn = (Button) v.findViewById(R.id.button2_login);

        lgn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lgn_intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(lgn_intent);
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email_str = email_txt.getText().toString();
                String name_str = name_txt.getText().toString();
                String pass_str = password_txt.getText().toString();
                String cpass_str = cpassword_txt.getText().toString();

                if (email_str.equals("") || name_str.equals("") || pass_str.equals("") || cpass_str.equals("")) {
                    Toast.makeText(getActivity(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass_str.equals(cpass_str)) {
                        boolean checkedEmail = db.checkEmail(email_str);
                        if (checkedEmail == true) {
                            boolean insert_qry = db.insert(email_str, name_str, pass_str);
                            if (insert_qry == true) {
                                Toast.makeText(getActivity(), "Registered succesfully", Toast.LENGTH_SHORT).show();
                                Intent I = new Intent(getActivity(), DisplayMoviesActivity.class);
                                startActivity(I);
                            } else {
                                Toast.makeText(getActivity(), "Could not register", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Email already exists", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT);

                    }
                }
            }
        });
        return v;
    }
}
