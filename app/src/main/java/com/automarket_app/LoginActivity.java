package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.automarket_app.util.Helper;

public class LoginActivity extends AppCompatActivity {

    String api_url ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        api_url = Helper.getMetaData(this, "api_url");

        EditText edEmail_login = (EditText)findViewById(R.id.edEmail_login);
        EditText edPassword_login = (EditText)findViewById(R.id.edPassword_login);


        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Log.i("register", "회원가입 하기이이!");

                LoginActivity.this.startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("api_url",api_url);

                Log.i("login", "로그인 성공ㅇㅇ!");
                startActivity(intent);
            }
        });
    }
}
