package com.automarket_app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.automarket_app.OrderCarActivity;
import com.automarket_app.VO.OrderVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class OrderService extends Service {
    public OrderService() {
    }
    private String api_url;
    private OrderVO orderVO;
    class OrderSaveRunnable implements Runnable{
        private OrderVO orderVO;

        public OrderSaveRunnable(OrderVO orderVO) {
            this.orderVO = orderVO;
        }

        public OrderSaveRunnable(){

        }
        @Override
        public void run() {

            String url = api_url+ "/api/order.do";
            String mykey = "";
            try{
                URL urlObj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("charset", "utf-8");

                OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(orderVO);
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
                Intent i = new Intent(getApplicationContext(), OrderCarActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //i.putParcelableArrayListExtra("resultData",maplist);
                i.setAction("order");
                i.putExtra("orderResultData",sb.toString());
                startActivity(i);


            }catch (Exception e){
                Log.e("automarket_app",e.toString());
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("automarket_app","onCreate 호출");
    }
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("automarket_app","onStartCommand 호출");

        String order_vo = intent.getExtras().getString("order_vo");
        api_url = intent.getExtras().getString("api_url");

        ObjectMapper mapper = new ObjectMapper();
        try {
            orderVO = mapper.readValue(order_vo, new TypeReference<OrderVO>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        OrderSaveRunnable runnable =new OrderSaveRunnable(orderVO);
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
