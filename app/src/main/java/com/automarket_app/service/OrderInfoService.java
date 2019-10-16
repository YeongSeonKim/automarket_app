package com.automarket_app.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.automarket_app.InformationActivity;
import com.automarket_app.VO.OrderInfoVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrderInfoService extends Service {

    private String api_url;

    class OrderInfoRunnable implements Runnable {

        String userid="";
        String receive_data;
        private SharedPreferences appData;
        String login_userid="";

        public OrderInfoRunnable() {       }

        @Override
        public void run() {

            appData = getSharedPreferences("login_info", MODE_PRIVATE);
            login_userid= appData.getString("USERID","");

            // http://localhost:8080/automarket/api/order/info.do?uid=1000007
            String url = api_url + "/api/order/info.do?uid=" + login_userid;

            try{
                URL urlObj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                con.setRequestMethod("GET");
//            con.setRequestProperty("Content-Type", "application/json");
//            con.setRequestProperty("charset", "utf-8");


                //기본적으로 stream은 bufferedReader형태로 생성
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null){
                    sb.append(line);
                }

                receive_data = sb.toString();
                Log.i("automarket_app_order","receive_data :"+ receive_data);

                br.close();

                //jackson library를 이용하여 json데이터 처리
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<OrderInfoVO> orderObject = mapper.readValue(receive_data, new TypeReference<List<OrderInfoVO>>() {});

                Log.i("automarket_app_order","orderObject :"+ orderObject);

                Intent i = new Intent(getApplicationContext(), InformationActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                i.putParcelableArrayListExtra("resultData", orderObject);

                startActivity(i);


            }catch (Exception e){
                Log.e("automarket_app",e.toString());
            }
        }
    }

    public OrderInfoService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("automarket_app","onCreate 호출");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("automarket_app","onBind 호출");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // onCreate()후에 자동적으로 호출되며
        // startService()에 의해서 호출된다.!!
        // 실제 로직처리는 onStartCommand()에서 진행
        Log.i("automarket_app","onStartCommand 호출");

        // 전달된 키워드를 이용해서 외부 네트워크 접속을 위한
        // Thread를 하나 생성해야 한다.

        api_url = intent.getExtras().getString("api_url");

        // Thread를 만들기 위한 Runnable 객체부터 생성
        OrderInfoService.OrderInfoRunnable runnable = new OrderInfoService.OrderInfoRunnable();
        Thread t = new Thread(runnable);
        t.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //서비스 객체가 메모리상에서 삭제될때 한번 호출
        //사용한 리소스를 정리하는 과정
        Log.i("automarket_app","onDestroy 호출");
        super.onDestroy();
    }
}
