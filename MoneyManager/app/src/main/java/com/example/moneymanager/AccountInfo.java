package com.example.moneymanager;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AccountInfo extends Fragment {

    TextView Userid,name,email,username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view =  inflater.inflate(R.layout.fragment_account_info, container, false);

    try {
        Userid = view.findViewById(R.id.txt_account_info_userid);
        name = view.findViewById(R.id.txt_account_info_name);
        email = view.findViewById(R.id.txt_account_info_email);
        username = view.findViewById(R.id.txt_account_info_username);

        Bundle bundle = getArguments();
        UserDbController userDbController = new UserDbController(getContext());
        Cursor cursor = userDbController.getUserInfo(String.valueOf(bundle.getInt("ID")));

        cursor.moveToFirst();

        Userid.setText(String.valueOf(cursor.getInt(0)));
        name.setText(cursor.getString(1));
        email.setText(cursor.getString(2));
        username.setText(cursor.getString(3));
    }
    catch (Exception e)
    {
        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
    }
    return view;
    }
}
