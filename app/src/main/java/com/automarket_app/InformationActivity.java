package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity {

    private SharedPreferences appData;
    String login_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);

        // btnBack
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                //finish();
            }
        });

        // 자동로그인 - 설정 값 불러오기
        appData = getSharedPreferences("login_info",MODE_PRIVATE);
        login_email = appData.getString("EMAIL","");

        Log.i("automarket_app","login_email : " + login_email);

        TextView nowEmail = (TextView)findViewById(R.id.nowEmail);

        nowEmail.setText(login_email);

        Log.i("automarket_app","login_email : " + nowEmail);

        Button btnLogout = (Button)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // value 값을 null로 할당


            }
        });

    }
}
