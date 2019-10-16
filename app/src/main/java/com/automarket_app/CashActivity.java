package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.automarket_app.VO.UserVO;
import com.automarket_app.util.Helper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CashActivity extends AppCompatActivity {
    TextView nowCash;
    String api_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        nowCash = (TextView)findViewById(R.id.nowCash);
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);
        api_url = Helper.getMetaData(this, "api_url");
        // btnBack
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // btnAddCash
        Button btnAddCash = (Button)findViewById(R.id.btnAddCash);

        btnAddCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //회원정보 조회
        Intent i = new Intent();
        ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.service.UserInfoService");
        i.setComponent(cname);
        i.setAction("cashform_call");
        i.putExtra("api_url",api_url);
        startService(i);

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("automarket_app", "데이터가 Activity에 도달");
        if (intent.getAction() != null && intent.getAction().equals("userinfo")) {
            String usermap = intent.getExtras().getString("userResultData");

            try {
                final ObjectMapper mapper = new ObjectMapper();
                UserVO userVO = mapper.readValue(usermap, new TypeReference<UserVO>() {});

                NumberFormat formatter = new DecimalFormat("#,###");
                String cashamtformat = formatter.format(userVO.getCashamt());
                nowCash.setText(cashamtformat);
                //System.out.println("cashamtformat:"+cashamtformat);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
