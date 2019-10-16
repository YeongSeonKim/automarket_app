package com.automarket_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.Toast;

import com.automarket_app.VO.ProductVO;
import com.automarket_app.adapter.ProductAdapter;
import com.automarket_app.util.Helper;

import java.util.ArrayList;
import java.util.List;

 public class OrderActivity extends AppCompatActivity {
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
                    finish();
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
            //첫 로드시 데이터 바인딩
            Intent i = new Intent();
            ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.service.ProductService");
            i.setComponent(cname);
            i.putExtra("categoryid","1");
            i.putExtra("api_url",api_url);
            startService(i);

            TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
            tabHost.setup();

            // Tab 정의
            tabHost.addTab(tabHost.newTabSpec("1").setContent(R.id.content1).setIndicator("과일"));
            tabHost.addTab(tabHost.newTabSpec("2").setContent(R.id.content2).setIndicator("채소"));
            tabHost.addTab(tabHost.newTabSpec("3").setContent(R.id.content3).setIndicator("정육"));
            tabHost.addTab(tabHost.newTabSpec("4").setContent(R.id.content3).setIndicator("쌀 / 잡곡"));
            tabHost.addTab(tabHost.newTabSpec("5").setContent(R.id.content3).setIndicator("해산물"));
            tabHost.addTab(tabHost.newTabSpec("6").setContent(R.id.content3).setIndicator("유제품"));

            tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String s) {
                    //System.out.println(s);

                    Intent i = new Intent();
                    //명시적 Intent사용
                    ComponentName cname = new ComponentName("com.automarket_app","com.automarket_app.service.ProductService");
                    i.setComponent(cname);
                    i.putExtra("categoryid",s);
                    i.putExtra("api_url",api_url);
                    startService(i);
                }
            });


        }

     @Override
     protected void onNewIntent(Intent intent) {
         super.onNewIntent(intent);
         Log.i("automarket_app","데이터가 Activity에 도달");
         final ArrayList<ProductVO> productList = intent.getParcelableArrayListExtra("resultData");

         gridView = (GridView) findViewById(R.id.griview);
         ProductAdapter adapter = new ProductAdapter();

         if(productList==null)
             return;

         for(ProductVO vo : productList){
             adapter.addItem(vo);
         }

         gridView.setAdapter(adapter);

         gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View v,
                                     int position, long id) {

                 // DO something
                //System.out.println("gridview click!!!");
                 Intent intent = new Intent();
                 ComponentName cname=new ComponentName("com.automarket_app","com.automarket_app.OrderPopupActivity");
                 intent.setComponent(cname);
                 //intent.putExtra("sendMsg",productList.get(position).getProdid());
                 intent.putExtra("selData",productList.get(position));
                 startActivity(intent);

             }
         });

     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

     }
 }
