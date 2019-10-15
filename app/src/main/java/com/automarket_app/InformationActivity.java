package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class InformationActivity extends AppCompatActivity {

    public SharedPreferences login_info; // 로그인 정보 폰에 저장,가져오기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        login_info = getSharedPreferences("login_info", MODE_PRIVATE);
        Log.i("automarket_app","login_info : " + login_info);

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

        TextView nowEmail = (TextView)findViewById(R.id.nowEmail);


        Button btnLogout = (Button)findViewById(R.id.btnLogout);

    }
}
