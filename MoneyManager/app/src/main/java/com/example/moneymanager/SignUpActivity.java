package com.example.moneymanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private Button btn;
    private EditText FirstName,LastName,Email,Password,ConfirmPassword,Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn = findViewById(R.id.btnConfirm);
        FirstName = findViewById(R.id.edtTxtFirstName);
        LastName = findViewById(R.id.edtTxtLastName);
        Email = findViewById(R.id.edtTxtEmail);
        Password = findViewById(R.id.edtTxtPassword_Signup);
        ConfirmPassword = findViewById(R.id.edtTxtConfirmPassword);
        Username = findViewById(R.id.edtTxtUsername_Signup);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                if(valid()){
                if (ConfirmPassword.getText().toString().equals(Password.getText().toString())) {
                    User user = new User();

                    user.setName(FirstName.getText().toString()+" " + LastName.getText().toString());
                    user.setEmail(Email.getText().toString());
                    user.setPassword(ConfirmPassword.getText().toString());
                    user.setUsername(Username.getText().toString());

                    UserDbController userDbController = new UserDbController(SignUpActivity.this);
                    if (userDbController.uniqueUsername(user.getUsername())){
                        if (userDbController.AddUser(user)){

                            Toast.makeText(SignUpActivity.this, "User Added", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUpActivity.this, FirstScreen.class);
                            intent.putExtra("Name", user.getName());
                            intent.putExtra("Email", user.getEmail());
                            intent.putExtra("ID", userDbController.getID());
                            startActivityForResult(intent,1001);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(SignUpActivity.this, "Username not available!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "Password did not match", Toast.LENGTH_SHORT).show();
                }
                }
            }
            catch(Exception e)
            {
                Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            }
        });

    }

    public boolean valid()
    {
        if(FirstName.getText().toString().matches("")|| Username.getText().toString().matches("") || LastName.getText().toString().matches("")||Email.getText().toString().matches("")||Password.getText().toString().matches("")||ConfirmPassword.getText().toString().matches(""))
        {
            Toast.makeText(getApplicationContext(),"Field cannot be left blank",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode == Activity.RESULT_OK)
            if(data.getStringExtra("do").equals("signout"))
            {
                FirstName.setText(null);
                LastName.setText(null);
                Email.setText(null);
                Username.setText(null);
                Password.setText(null);
                ConfirmPassword.setText(null);

                Intent intent1 = new Intent();
                intent1.putExtra("do","signout");
                setResult(AppCompatActivity.RESULT_OK,intent1);
                finish();
            }
    }
}
