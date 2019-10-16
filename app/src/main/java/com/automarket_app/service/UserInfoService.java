package com.automarket_app.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.automarket_app.CartActivity;
import com.automarket_app.CashActivity;
import com.automarket_app.VO.UserVO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserInfoService extends Service {
    private String api_url = "", login_userid = "";
    private SharedPreferences appData;
    private UserVO userVO;
    private String action_name="";
    public UserInfoService() {
    }

    class UserInfoRunnable implements Runnable {
        @Override
        public void run() {

            appData = getSharedPreferences("login_info", MODE_PRIVATE);
            login_userid = appData.getString("USERID", "");

            String url = api_url + "/api/user/info.do?uid=" + login_userid;
            String mykey = "";
            try {
                URL urlObj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                con.setRequestMethod("GET");
                //con.setRequestProperty("Authorization","");

                //기본적으로 stream은 bufferedReader형태로 생성
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                Intent i;
                //intent를 통해 activity에 전달
                if(action_name!=null && action_name.equals("cashform_call")){
                    i = new Intent(getApplicationContext(), CashActivity.class);
                }else {
                    i = new Intent(getApplicationContext(), CartActivity.class);

                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                i.putExtra("userResultData", sb.toString());
                i.setAction("userinfo");
                startActivity(i);


            } catch (Exception e) {
                Log.e("automarket_app", e.toString());
            }

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        //서비스 객체가 만들어지는 시점에 한번 호출
        //사용할 resource를 준비하는 과정
        super.onCreate();
        Log.i("automarket_app", "onCreate 호출");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("automarket_app", "onStartCommand 호출");

        action_name = intent.getAction();
        api_url = intent.getExtras().getString("api_url");

        UserInfoRunnable runnable = new UserInfoRunnable();
        Thread t = new Thread(runnable);
        t.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //서비스 객체가 메모리상에서 삭제될때 한번 호출
        //사용한 리소스를 정리하는 과정
        Log.i("automarket_app", "onDestroy 호출");
        super.onDestroy();
    }

}