package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TabHost;

import com.automarket_app.VO.ProductVO;
import com.automarket_app.adapter.ProductAdapter;
import com.automarket_app.util.Helper;

import java.util.ArrayList;
import java.util.List;

 public class OrderActivity extends AppCompatActivity {
        //RecyclerView recyclerView;
        GridView gridView;
        List<ProductVO> productList=new ArrayList<ProductVO>();
        String api_url ="";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_order);

            api_url = Helper.getMetaData(this, "api_url");

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

            ///////////////////////////////////////////////////////////////////////////////////

            Intent i = new Intent();
            //명시적 Intent사용
            ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.service.ProductService");
            i.setComponent(cname);
            i.putExtra("categoryid","1");
            i.putExtra("api_url",api_url);
            startService(i);

            TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
            tabHost.setup();

            // 첫 번째 Tab. (탭 표시 텍스트:"과일"), (페이지 뷰:"tab1")
            tabHost.addTab(tabHost.newTabSpec("1").setContent(R.id.content1).setIndicator("과일"));


            // 두 번째 Tab. (탭 표시 텍스트:"채소"), (페이지 뷰:"tab2")
            tabHost.addTab(tabHost.newTabSpec("2").setContent(R.id.content2).setIndicator("채소"));


            // 세 번째 Tab. (탭 표시 텍스트:"쌀 / 잡곡"), (페이지 뷰:"tab3")
            tabHost.addTab(tabHost.newTabSpec("3").setContent(R.id.content3).setIndicator("쌀 / 잡곡"));


            // 네 번째 Tab. (탭 표시 텍스트:"정육"), (페이지 뷰:"tab4")
            tabHost.addTab(tabHost.newTabSpec("4").setContent(R.id.content3).setIndicator("정육"));


            // 다섯 번째 Tab. (탭 표시 텍스트:"해산물"), (페이지 뷰:"tab5")
            tabHost.addTab(tabHost.newTabSpec("5").setContent(R.id.content3).setIndicator("해산물"));


            // 여섯 번째 Tab. (탭 표시 텍스트:"유제품"), (페이지 뷰:"tab6")
            tabHost.addTab(tabHost.newTabSpec("6").setContent(R.id.content3).setIndicator("유제품"));


        }

     @Override
     protected void onNewIntent(Intent intent) {
         super.onNewIntent(intent);
         Log.i("automarket_app","데이터가 Activity에 도달");
         ArrayList<ProductVO> productList = intent.getParcelableArrayListExtra("resultData");

         gridView = (GridView) findViewById(R.id.griview);
         ProductAdapter adapter = new ProductAdapter();

//         productList.add(new ProductVO("1","과일","http://localhost:8082/automarket/upload/apple.jpg","1000011","사과",5000,10));
//         productList.add(new ProductVO("1","과일","http://localhost:8082/automarket/upload/be.jpg","1000022","배",10000,10));

         for(ProductVO vo : productList){
             adapter.addItem(vo);
         }


         gridView.setAdapter(adapter);

     }
    }
