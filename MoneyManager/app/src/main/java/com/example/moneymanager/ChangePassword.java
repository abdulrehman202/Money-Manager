package com.example.moneymanager;

import android.content.Context;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends Fragment {

    EditText current,newpwd,confirmpwd;
    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        current = view.findViewById(R.id.txt_change_password_current);
        newpwd = view.findViewById(R.id.txt_change_password_new);
        confirmpwd = view.findViewById(R.id.txt_change_password_confirm);
        button = view.findViewById(R.id.btn_change_password);

        final String current_password;

        final Bundle bundle = getArguments();
        final int U_id = bundle.getInt("ID");
        final UserDbController userDbController = new UserDbController(getContext());
        current_password = userDbController.getUserPassword(String.valueOf(U_id));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                if(current_password.equals(current.getText().toString()))
                {
                    String new_password,confirm_password;

                    new_password = newpwd.getText().toString();
                    confirm_password = confirmpwd.getText().toString();

                    if(new_password.equals(confirm_password)) {
                        userDbController.updateUserPassword(String.valueOf(U_id), new_password);
                        Toast.makeText(getContext(), "Password Updated", Toast.LENGTH_SHORT).show();

                        current.setText(null);
                        newpwd.setText(null);
                        confirmpwd.setText(null);
                    }

                    else Toast.makeText(getContext(),"Password did not match",Toast.LENGTH_SHORT).show();
                }

                else Toast.makeText(getContext(),"Incorrect Password", Toast.LENGTH_SHORT).show();

            }
                catch (Exception e)
                {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });


    return view;
    }


}
