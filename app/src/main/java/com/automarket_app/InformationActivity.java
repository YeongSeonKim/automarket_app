package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.automarket_app.VO.OrderInfoVO;
import com.automarket_app.adapter.OrderInfoAdapter;
import com.automarket_app.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class InformationActivity extends AppCompatActivity {

    private String api_url ="";

    private ImageButton btnBack;
    private TextView nowEmail;
    private Button btnLogout;
    private ListView orderInfoList;

    private SharedPreferences appData;
    String login_email;
    String login_userid;

    public InformationActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        // api_url
        api_url = Helper.getMetaData(this, "api_url");

        btnBack = (ImageButton)findViewById(R.id.btnBack);

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


        Log.i("automarket_app" ,"appData : " + appData);
        Log.i("automarket_app","login_email : " + login_email);

        nowEmail = (TextView)findViewById(R.id.nowEmail);

        // 로그인 아이디(이메일)
        nowEmail.setText(login_email);

        Log.i("automarket_app","nowEmail : " + nowEmail);

        // btnLogout - 로그아웃
        btnLogout = (Button)findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // value 값을 null로 할당
//                appData.getStringSet("USERID",null);
//                Log.i("automarket_app" ,"appData : " + appData);
//
//                Intent intent = new Intent(InformationActivity.this, LoginActivity.class);
//                startActivity(intent);

                // 사용자를 아예 지워버리는거
               appData.edit().clear().commit();

                Log.i("automarket_app_appdata" ,"appData 삭제 : " + appData);

                Intent intent = new Intent(InformationActivity.this, LoginActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);

            }
        });

        // 주문내역
        login_userid = appData.getString("USERID",""); // userid값 불러오기
        Log.i("automarket_app_appdata" ,"login_userid : " + login_userid);

                //첫 로드시 데이터 바인딩
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.automarket_app", "com.automarket_app.service.OrderInfoService");
                i.setComponent(cname);
//                i.putExtra("userid","1000007");
                i.putExtra("api_url", api_url);
                startService(i);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        final ArrayList<OrderInfoVO> order_InfoList = intent.getParcelableArrayListExtra("resultData");

        Log.i("automarket_app_data","데이터가 Activity에 도달");

        // 주문내역 리스트
        orderInfoList = (ListView)findViewById(R.id.orderInfoList);

        Log.i("automarket_app_list" ," 뜨나요오오 orderInfoList : " + orderInfoList);

        OrderInfoAdapter adapter = new OrderInfoAdapter();
        orderInfoList.setAdapter(adapter);

        for (OrderInfoVO vo : order_InfoList){
            adapter.addList(vo);
        }
    }
}
