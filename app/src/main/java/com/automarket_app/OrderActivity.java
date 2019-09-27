package com.automarket_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

//class ProdRunnable implements Runnable {
//
//    private String keyword;
//    private Handler handler;
//
//    ProdRunnable(Handler handler, String keyword) {
//        this.handler = handler;
//        this.keyword = keyword;
//    }
//
//    @Override
//    public void run() {
//        // 구현내용
//
//        // keyword를 이용해서 web program에 접속한 후 결과를 받아온다!!
//        // 결과로 받아온 JSON 문자열을 이용해서 GridView에 출력해야 한다!!
//        // 외부THread이기떄문에 여기서는 GridView를 제어할 수 없다!!
//        // 그래서 Handler를 이용해서 UI Thread에 GridView에 사용할 데이터를 넘긴다.
//
//        // 네크워트 연결 , localhost를 사용하면 안되고 내가 접속할 실제 웹서버의 ip주소를 입력하면 된다.
//
//        // http://localhost:8080/automarket/api/prod/list.do?cid=1
//
//        String url = R.string.Server_URL + "/automarket/api/prod/list.do?cid=" + keyword;
//
//
//        // 네트워크로 연결되는 것들은 반드시 예외처리를 해줘야함
//        try {
//            URL urlObj = new URL(url); // 주소를 자바 객체로 만들어줄꺼임 -> open connection 사용가능
//            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
//            //연결객체를 만든 다음에 데이터를 읽어드릴수  있는 Stream(통로)을 뽑아낼수 있다.
//            // network연결이 성공 한 후 데이터를 읽어들이기 위한 데이터 연결 통로 Stream을 생성한다.
//            BufferedReader br = new BufferedReader(
//                    new InputStreamReader(con.getInputStream())); // Stream을 더좋은걸로 바꿔주는거
//
//            // 데이터 저장 용도로 만듬
//            String input = "";
//            StringBuffer sb = new StringBuffer();
//
//            // 서버가 보내준 데이터를 읽어서 input에 넣는데 null이 아닐때까지 모두 읽으라는 의미
//            while ((input = br.readLine()) != null) {
//                sb.append(input);
//            }
//            // Log창에 찍어보기
//            Log.i("DATA",sb.toString());
//
//            // 얻어온 결과 JSON 문자열을 Jackson Library를 이용해서
//            // Java 객체 형태(String[])로 변형해서 List에 입력할수 있게끔
//            ObjectMapper mapper = new ObjectMapper();
//            // Jackson Library를 이용하여 JSON문자열을 String[] 형태로 변환
//            String[] resultArr = mapper.readValue(sb.toString(), String[].class); // 배열로 변환시켜주기.
//            // 원래형태 <-> JSON
//
//            // 데이터를 합치기 위해서 , 바구니라고 생각하면됨
//            Bundle bundle = new Bundle();
//            bundle.putStringArray("PRODARRAY", resultArr); // key, value 쌍으로
//
//            // 번들을 보내기 위해서는 메세지가 필요함
//            Message msg = new Message();
//            msg.setData(bundle);
//
//            // 위에서 만든 메세지 객체를 보냄 , Activtiy에 있는 handler가 받음
//
//            handler.sendMessage(msg);
//
//
//        } catch (Exception e) {
//            Log.i("DATAerror", e.toString());
//        }
//    }
//}
    public class OrderActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_order);

            ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
            ImageButton btnCart = (ImageButton) findViewById(R.id.btnCart);

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

            // btnCart
            btnCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    //finish();
                }
            });
                    //test
                    ///////////////////////////////////////////////////////////////////////////////////

            ListView listView1 = (ListView)findViewById(R.id.listView1);


        // TabHost

            TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
            tabHost.setup();

            // 첫 번째 Tab. (탭 표시 텍스트:"과일"), (페이지 뷰:"tab1")
            tabHost.addTab(tabHost.newTabSpec("1").setContent(R.id.content1).setIndicator("과일"));
//            TabHost.TabSpec tab1 = tabHost.newTabSpec("1").setContent(R.id.content1).setIndicator("과일");
//            tabHost.addTab(tab1);

            // tab1 gridView
//            final GridView gridView = (GridView) findViewById(R.id.tab1GridView);

            // 두 번째 Tab. (탭 표시 텍스트:"채소"), (페이지 뷰:"tab2")
            tabHost.addTab(tabHost.newTabSpec("2").setContent(R.id.content2).setIndicator("채소"));
//            TabHost.TabSpec tab2 = tabHost.newTabSpec("2").setContent(R.id.content2).setIndicator("채소");
//            tabHost.addTab(tab2);

            // 세 번째 Tab. (탭 표시 텍스트:"쌀 / 잡곡"), (페이지 뷰:"tab3")
            tabHost.addTab(tabHost.newTabSpec("3").setContent(R.id.content3).setIndicator("쌀 / 잡곡"));
//            TabHost.TabSpec tab3 = tabHost.newTabSpec("3").setContent(R.id.content3).setIndicator("쌀 / 잡곡");
//            tabHost.addTab(tab3);

            // 네 번째 Tab. (탭 표시 텍스트:"정육"), (페이지 뷰:"tab4")
            tabHost.addTab(tabHost.newTabSpec("4").setContent(R.id.content3).setIndicator("정육"));
//            TabHost.TabSpec tab4 = tabHost.newTabSpec("4").setContent(R.id.content4).setIndicator("정육");
//            tabHost.addTab(tab4);

            // 다섯 번째 Tab. (탭 표시 텍스트:"해산물"), (페이지 뷰:"tab5")
            tabHost.addTab(tabHost.newTabSpec("5").setContent(R.id.content3).setIndicator("해산물"));
//            TabHost.TabSpec tab5 = tabHost.newTabSpec("5").setContent(R.id.content5).setIndicator("해산물");
//            tabHost.addTab(tab5);

            // 여섯 번째 Tab. (탭 표시 텍스트:"유제품"), (페이지 뷰:"tab6")
            tabHost.addTab(tabHost.newTabSpec("6").setContent(R.id.content3).setIndicator("유제품"));
//            TabHost.TabSpec tab6 = tabHost.newTabSpec("6").setContent(R.id.content6).setIndicator("유제품");
//            tabHost.addTab(tab6);
        }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
