package com.fit2081.assignment_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    EditText userNameET;
    EditText passwordEt1;
    EditText passwordEt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        userNameET=findViewById(R.id.signup_username);
        passwordEt1=findViewById(R.id.signup_password);
        passwordEt2=findViewById(R.id.signup_password2);
    }

    public void onSignupLoginBtnClick(View view){
        Intent intent=new Intent(this,Login.class);
        startActivity(intent);
    }

    public void onSignupRegisterBtnClick(View view){
        if(passwordEt1.getText().toString().equals(passwordEt2.getText().toString())){
            SharedPreferences sP=getSharedPreferences(Keys.FILE_NAME,MODE_PRIVATE);
            SharedPreferences.Editor editor=sP.edit();
            editor.putString(Keys.USER_NAME,userNameET.getText().toString());
            editor.putString(Keys.PASSWORD,passwordEt1.getText().toString());
            editor.apply();
            Toast.makeText(this,"Registration Successfully!",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,Login.class);
            startActivity(intent);
        } else {
            Toast.makeText(this,"Invalid username or passwords",Toast.LENGTH_SHORT).show();
        }
    }
}