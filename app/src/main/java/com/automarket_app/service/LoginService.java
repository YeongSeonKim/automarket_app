package com.automarket_app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.automarket_app.LoginActivity;
import com.automarket_app.VO.UserVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginService extends Service {

    private String api_url;

    class LoginRunnable implements Runnable {

    private String email;

        public LoginRunnable(String email) {
            this.email = email;
        }

        @Override
    public void run() {

        // http://localhost:8080/automarket/api/login.do?email=test07@gmail.com
        // String url = "http://localhost:8080/automarket/api/login.do?email=" + email + pwd ;
        String url = api_url + "/api/login.do?email=" + email ;

        try {
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("charset", "utf-8");

            Log.d("automarket_app_LOG","응답코드 : " + con.getResponseCode());
            Log.d("automarket_app_LOG","응답메세지 : " + con.getResponseMessage());

            //기본적으로 stream은 bufferedReader형태로 생성
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();

//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("email",email);

            //jackson library를 이용하여 json데이터 처리
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<UserVO> maplist = mapper.readValue(sb.toString(), new TypeReference<List<UserVO>>() {});

//            for(UserVO vo: maplist){
//
//            }

            //intent를 통해 activity에 전달
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //전달되는 데이터는 직렬화가 가능한 형태로 구성되어야 함
            //컴포넌트간 객체전달 마셜링 작업필요
            //parcelable interface를 구현한 객체를 붙이기 위해서 method를 putParcelableArrayListExtra로 교체
            //i.putExtra("resultData",myObject);
            i.putParcelableArrayListExtra("resultData",maplist);
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
        // onCreate()후에 자동적으로 호출되며
        // startService()에 의해서 호출된다.!!
        // 실제 로직처리는 onStartCommand()에서 진행
        Log.i("automarket_app","onStartCommand 호출");
        // 전달된 키워드를 이용해서 외부 네트워크 접속을 위한
        // Thread를 하나 생성해야 한다.
        String email = intent.getExtras().getString("email");
//        String pwd = intent.getExtras().getString("pwd");
        api_url = intent.getExtras().getString("api_url");
        // Thread를 만들기 위한 Runnable 객체부터 생성
        LoginRunnable runnable = new LoginRunnable(email);
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