package com.example.moneymanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class MainActivity extends AppCompatActivity {

    Button btn_signin,btn_signup;
    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_signup = findViewById(R.id.btnSignUp);
        btn_signin = findViewById(R.id.btnSignin);
        username = findViewById(R.id.edtTxtUsername);
        password = findViewById(R.id.edtTxtPassword);

        password.setText(null);
        username.setText(null);

        btn_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivityForResult(intent,1001);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

                UserDbController userDbController = new UserDbController(MainActivity.this);
                Cursor cursor = userDbController.AuthenticateUser(username.getText().toString(),password.getText().toString());

                if(cursor.getCount()!=0)
                {
                    cursor.moveToFirst();

                    Intent i = new Intent(MainActivity.this, FirstScreen.class);
                    i.putExtra("Name",cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_User_Name)));
                    i.putExtra("Email",cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_User_Email)));
                    i.putExtra("ID",cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_User_Id)));
//                    i.putExtra("key","Nothing");
                    startActivityForResult(i,1000);
                }

                else Toast.makeText(getApplicationContext(),"Invalid Input!",Toast.LENGTH_SHORT).show();
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }}
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == 1000 || resultCode==1001) && resultCode == Activity.RESULT_OK)
        if(data.getStringExtra("do").equals("signout"))
        {
            username.setText(null);
            password.setText(null);
        }
    }
}
