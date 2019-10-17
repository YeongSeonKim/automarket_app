package com.automarket_app.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.automarket_app.OrderCarActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarMoveService extends Service {
    Socket socket;
    BufferedReader br ;
    PrintWriter out;
    ExecutorService executorService = Executors.newCachedThreadPool();

    public CarMoveService() {
    }
    private void printMsg(String msg) {
//        if(msg.contains("/10000101/")){
//            Log.i("TcpCarService","차량선정:"+msg);
//            Intent i = new Intent(getApplicationContext(), OrderCarActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            i.setAction("car_move");
//            i.putExtra("carResultData",msg);
//            startActivity(i);
//
//        }
        Log.i("TcpCarService",msg);
    }
    //서버로부터 들어오는 메시지를 계속 받는부분
    class CarMoveRunnable implements Runnable {
        private BufferedReader br;
        public String tcp_server_ip;
        public int tcp_server_port;
        private SharedPreferences appData;
        String login_userid="",carid="";

        public CarMoveRunnable(BufferedReader br) {
            super();
            this.br = br;
        }
        public CarMoveRunnable(BufferedReader br,String tcp_server_ip,Integer tcp_server_port
                ,String carid) {
            super();
            this.br = br;
            this.tcp_server_ip = tcp_server_ip;
            this.tcp_server_port = tcp_server_port;
            this.carid = carid;
        }
        public void sendmsg(String msg){
            out.println(msg);
            out.flush();
        }
        @Override
        public void run() {
            String line = "";
            try {
                appData = getSharedPreferences("login_info", MODE_PRIVATE);
                login_userid= appData.getString("USERID","");

                socket = new Socket(tcp_server_ip,tcp_server_port);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                printMsg("서버 접속 성공");
                String msg = "";

                //tcp server에 차량등록
                sendmsg(String.format("U/10000100/%s",login_userid));
                Thread.sleep(1000);
                //인근차량 요청
                msg = String.format("U/10000102/%s/%s",login_userid,carid);

                sendmsg(msg);
                sendmsg("/EXIT/");

                while((line=br.readLine())!=null) {
                    printMsg(line);
                }
                printMsg("서버 연결해제");


            }catch(Exception e) {
                e.getStackTrace();
            }
        }

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
        try {
            String tcp_server_ip = intent.getExtras().getString("tcp_server_ip");
            Integer tcp_server_port = intent.getExtras().getInt("tcp_server_port");
            String carid = intent.getExtras().getString("carid");

            CarMoveRunnable runnable = new CarMoveRunnable(br,tcp_server_ip,tcp_server_port,carid);
            executorService.execute(runnable);
        }catch(Exception e) {
            System.out.println(e);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("automarket_app","onDestroy 호출");

        printMsg("접속 종료");
    }
}
