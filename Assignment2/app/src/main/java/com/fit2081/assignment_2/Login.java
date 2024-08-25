package com.fit2081.assignment_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText userNameEt;
    EditText passwordEt;
    SharedPreferences sP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEt=findViewById(R.id.login_username);
        passwordEt=findViewById(R.id.login_password);

        sP=getSharedPreferences(Keys.FILE_NAME,MODE_PRIVATE);

        String savedUserName=sP.getString(Keys.USER_NAME,"");
        userNameEt.setText(savedUserName);
    }

    public void onLoginClick(View view){

        String savedUserName=sP.getString(Keys.USER_NAME,"");
        String savedPassword=sP.getString(Keys.PASSWORD,"");

        if((savedUserName.equals(userNameEt.getText().toString())&&savedPassword.equals(passwordEt.getText().toString()))){
            Intent intent=new Intent(this, DashboardActivity.class);
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }else{
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();

        }



    }

    public void onRegisterBtnClick(View view){
        Intent intent=new Intent(this,Signup.class);
        startActivity(intent);
    }
}