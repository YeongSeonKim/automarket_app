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
import java.util.Map;

public class LoginService extends Service {

    // http://localhost:8080/automarket/api/login.do?email=test07@gmail.com

    class LoginRunnable implements Runnable {

    private String useremail;
    private String password;

        public LoginRunnable(String useremail, String password) {
            this.useremail = useremail;
            this.password = password;
        }

        @Override
    public void run() {

        // String url = "http://localhost:8080/automarket/api/login.do?email=" + useremail + password ;
        String url = "http://70.12.115.56:8080/automarket/api/login.do?email=" + useremail + password ;

        try{
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("GET");
            //con.setRequestProperty("Authorization","KakaoAK "+mykey);

            //기본적으로 stream은 bufferedReader형태로 생성
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
            br.close();

            //jackson library를 이용하여 json데이터 처리
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(sb.toString(), new TypeReference<Map<String,Object>>() {});
            Object obj = map.get("documents");
            String resultJsonData = mapper.writeValueAsString(obj);

            Log.i("automarket_app","resultJsonData>>"+resultJsonData);
            ArrayList<UserVO> myObject = mapper.readValue(resultJsonData,new TypeReference<ArrayList<UserVO>>(){});

//            for(UserVO vo: myObject){
//                vo.byteFromURL();
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
            //i.putParcelableArrayListExtra("resultData",myObject);
            startActivity(i);

        }catch (Exception e){
            Log.e("automarket_app",e.toString());
        }
    }
}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}