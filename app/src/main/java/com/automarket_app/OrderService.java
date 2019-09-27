//package com.automarket_app;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.Message;
//import android.util.Log;
//
//import com.automarket_app.VO.ProdDtailVO;
//import com.automarket_app.VO.ProdListVO;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Map;
//
//public class OrderService extends Service {
//
//    // class안에 class만들기
//    // => inner class의 형태로 Thread를 생성하기 위한 Runnable interface를 구현한 class를 정의.
//    // private : 외부에서 접근 불가능하게는 접근제한자
//    class ProdRunnable implements Runnable {
//
//        private String keyword;
//
//        ProdRunnable(String keyword) {
//            this.keyword = keyword;
//        }
//
//        @Override
//        public void run() {
//            // 구현내용
//
//            // keyword를 이용해서 web program에 접속한 후 결과를 받아온다!!
//            // 결과로 받아온 JSON 문자열을 이용해서 GridView에 출력해야 한다!!
//            // 외부THread이기떄문에 여기서는 GridView를 제어할 수 없다!!
//            // 그래서 Handler를 이용해서 UI Thread에 GridView에 사용할 데이터를 넘긴다.
//
//            // 네크워트 연결 , localhost를 사용하면 안되고 내가 접속할 실제 웹서버의 ip주소를 입력하면 된다.
//
//            // http://localhost:8080/automarket/api/prod/list.do?cid=1
//
//            String url = R.string.Server_URL + "/automarket/api/prod/list.do?cid=" + keyword;
//
//            // 네트워크로 연결되는 것들은 반드시 예외처리를 해줘야함
//
//            try {
//                URL urlObj = new URL(url); // 네트워크 접속 시도
//                HttpURLConnection con = (HttpURLConnection) urlObj.openConnection(); // 연결이 성공되면 연결객체를 들고옴
//                // Request 방식을 지정, 요청방식에 대해서 정의해줘야함
//                con.setRequestMethod("GET");
//                con.setRequestProperty("", ""); //key,value의 쌍으로
//                // 정상적으로 설정을 하면 API 호출이 성곡하고 결과를 받아 올 수 있다.
//                // 연결통로(stream)을 통해서 결과를 문자열로 얻어낸다!!
//                // 기본적인 Stream을 BufferedReader형태로 생성.
//                // 연결통로를 최종적으로 사용하기 편한 BufferedReader 사용
//                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                String line = null;
//                StringBuffer sb = new StringBuffer(); // 서버가 보내준 데이터를 합치기위해, 통로에있는데이터 싹가져오기위해
//
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//                // 데이터를 다 읽어들였으니 통로(Stream)를 닫는다.
//                br.close();
//
//                Log.i("ProdListLog", sb.toString());
//                // 데이터가 JSON 형태로 정상적으로 출력되면 외부 API 호출 성공!!
//                // Jackson library를 이용해서 JSON 데이터를 처리
//                // {documents : []}
//                ObjectMapper mapper = new ObjectMapper();
//                Map<String, Object> map = mapper.readValue(sb.toString(),
//                        new TypeReference<Map<String, Object>>() {
//
//                        });
//
//                Object obj = map.get("documents");
//                String resultJsonData = mapper.writeValueAsString(obj);
//                Log.i("ProdListLog", resultJsonData);
//                // 결과적으로 우리가 얻은 데이터의 최종 형태는
//                // [{책1권의 데이터},{책1권의 데이터},{책1권의 데이터}]
//                // 책 1권의 데이터를 객체화 => KAKAOBookVO class를 이용
//                // 책 여러권의 데이터는 ArrayList로 표현.
//                // 책 1권의 데이터는 key와 value의 쌍으로 표현되고 있다.
//
//                ArrayList<ProdDtailVO> myObject = mapper.readValue(resultJsonData,
//                        new TypeReference<ArrayList<ProdDtailVO>>() {
//
//                        });
//
////                // 정상적으로 객체화 되었는지를 확인.!
////                for (KAKAOBookVO book : myObject){
////                    Log.i("KAKAOBOOKLog", book.getTitle());
////                }
//
//
//                // 이미지데이터를 처리하기 위한 추가 코드
//                // 책 표지 데이터는 문자열 URL로 되어 있는데 해당 URL에 접속해서
//                // byte[]형태의 데이터로 추출해서 KAKAOBookVO에 Field를 추가해서
//                // byte[]을 저장.
//                for (ProdDtailVO prod : myObject) {
//                    Log.i("ProdListLog", prod.getProdNm());
//                }
//
//
//                // 정상적으로 객체화가 되었으면 intent에 해당 데이터를 붙여서
//                // Activity에게 전달해야 한다.
//                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                // Thread를 class안에서 정의해서 getApplicationContext()사용가능
//
//                // 만약 Activity가 메모리에 존재하면 새로 생성하지 않고 기존 Activity를 이용.
//                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                // 전달할 데이터를 intent에 붙인다!!
//                // parcelable interface를 구현한 객체를 붙이기 위해서
//                // method를 다른 method로 교체
//
//                // i.putExtra("resultData",myObject);
//                i.putParcelableArrayListExtra("resultData", myObject);
//
//                // Activity에게 데이터를 전달.
//                startActivity(i);
//
//            } catch (Exception e) {
//                Log.i("ProdListLog", e.toString());
//            }
//        }
//    }
//
//
//    public OrderService() {
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    @Override
//    public void onCreate() {
//        // 서비스 객체가 만들어지는 시점에 1번 호출
//        // 사용할 resource를 준비하는 과정
//        super.onCreate();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // onCreate()후에 자동적으로 호출되며
//        // startService()에 의해서 호출된다.!!
//        // 실제 로직처리는 onStartCommand()에서 진행
//        Log.i("ProdList","onStartCommand 호출됬어요!!!");
//        // 전달된 키워드를 이용해서 외부 네트워크 접속을 위한
//        // Thread를 하나 생성해야 한다.
//        String keyword = intent.getExtras().getString("searchKeyword");
//        // Thread를 만들기 위한 Runnable 객체부터 생성
//        ProdRunnable runnable = new ProdRunnable(keyword);
//        Thread t = new Thread(runnable);
//        t.start();
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        // 서비스 객체가 메모리상에서 삭제될 떄 1번 호출
//        // 사용한 resource를 정리하는 과정.
//        super.onDestroy();
//    }
//}
//
//
