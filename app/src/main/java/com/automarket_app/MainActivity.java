package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final long INTERVAL_TIME = 1000;
    long previousTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // 상태바 없애기
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Toast.makeText(getApplicationContext(),"로그인성공",Toast.LENGTH_SHORT).show();

        ImageButton btnOrder = (ImageButton)findViewById(R.id.btnOrder);
        ImageButton btnCash = (ImageButton)findViewById(R.id.btnCash);
        ImageButton btnInformation = (ImageButton)findViewById(R.id.btnInformation);
        ImageButton btnAlarm = (ImageButton)findViewById(R.id.btnAlarm);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });

        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CashActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });

        btnInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InformationActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("차량이 도착했습니다.");

                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 취소버튼을 눌렀을때 수행할 코드 작성
                    }
                });

                dialog.show();
            }
        });
    }

    // 뒤로가기버튼 눌렀을때 앱 종료
    @Override
    public void onBackPressed() {
        // 뒤로가기 눌렀을떄 뭐할껀지
        long currentTime = System.currentTimeMillis();

        if((currentTime - previousTime) <= INTERVAL_TIME) {
            super.onBackPressed();
        } else {
            previousTime = currentTime;
            Toast.makeText(this, "한번더 누르면 앱이종료됩니다아아아.", Toast.LENGTH_SHORT).show();
        }

    }
}


