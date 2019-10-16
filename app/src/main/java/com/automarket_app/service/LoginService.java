package com.automarket_app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.automarket_app.LoginActivity;
import com.automarket_app.RegisterActivity;
import com.automarket_app.VO.UserVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginService extends Service {
    private String api_url;
    private UserVO userVO;

    class LoginRunnable implements Runnable {
        private UserVO userVO;

        public LoginRunnable() {
        }

        public LoginRunnable(UserVO userVO) {
            this.userVO = userVO;
        }

        @Override
        public void run() {

            // // http://localhost:8080/automarket/api/login.do
            //  String url = "// http://localhost:8080/automarket/api/login.do";
            String url = api_url + "/api/login.do";

            try{
                URL urlObj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                con.setRequestMethod("POST"); // post로 넘기기
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("charset", "utf-8");

                OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(userVO);
                out.write(json);
                out.flush();
                out.close();

                int responseCode=con.getResponseCode();
                StringBuffer sb=null;
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader( new InputStreamReader(con.getInputStream()));
                    sb = new StringBuffer("");
                    String line="";
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                }
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //i.putParcelableArrayListExtra("resultData",maplist);

                startActivity(i);


            }catch (Exception e){
                Log.e("automarket_app",e.toString());
            }
        }
    }

    public LoginService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("automarket_app", "onBind 호출");
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
        Log.i("automarket_app","onStartCommand 호출");

        String order_vo = intent.getExtras().getString("user_vo");
        api_url = intent.getExtras().getString("api_url");

        ObjectMapper mapper = new ObjectMapper();
        try {
            userVO = mapper.readValue(order_vo, new TypeReference<UserVO>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoginService.LoginRunnable runnable =new LoginService.LoginRunnable(userVO);
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
