package com.automarket_app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.automarket_app.OrderActivity;
import com.automarket_app.VO.ProductVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductService extends Service {
    private String api_url;
    class ProductListRunnable implements Runnable{
        String categoryid;

        public ProductListRunnable(String categoryid){
            this.categoryid = categoryid;
        }
        @Override
        public void run() {

            String url = api_url+ "/api/prod/list.do?cid="+categoryid;
            String mykey = "";
            try{
                URL urlObj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                con.setRequestMethod("GET");
                //con.setRequestProperty("Authorization","");

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
                ArrayList<ProductVO> maplist = mapper.readValue(sb.toString(), new TypeReference<List<ProductVO>>() {});

                for(ProductVO vo: maplist){
                    vo.byteFromURL(api_url);
                }

                //intent를 통해 activity에 전달
                Intent i = new Intent(getApplicationContext(), OrderActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //전달되는 데이터는 직렬화가 가능한 형태로 구성되어야 함
                //컴포넌트간 객체전달 마셜링 작업필요
                i.putParcelableArrayListExtra("resultData",maplist);
                startActivity(i);

            }catch (Exception e){
                Log.e("automarket_app",e.toString());
            }
        }
    }
    public ProductService() {
    }

    @Override
    public void onCreate() {
        //서비스 객체가 만들어지는 시점에 한번 호출
        //사용할 resource를 준비하는 과정
        super.onCreate();
        Log.i("automarket_app","onCreate 호출");
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //onCreate()후에 자동적으로 호출되며 startService()에 의해서 호출
        //로직처리는 onStartCommand() 진행
        Log.i("automarket_app","onStartCommand 호출");
        //외부 네트워크 접속을 위한 thread를 생성
        String keyword = intent.getExtras().getString("categoryid");
        api_url = intent.getExtras().getString("api_url");
        ProductListRunnable runnable =new ProductListRunnable(keyword);
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
