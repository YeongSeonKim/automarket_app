package com.automarket_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.automarket_app.VO.ProdListVO;
import com.automarket_app.VO.ProductVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

 public class OrderActivity extends AppCompatActivity {
        //RecyclerView recyclerView;
        GridView gridView;
        List<ProductVO> productList=new ArrayList<ProductVO>();
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

            ///////////////////////////////////////////////////////////////////////////////////


            gridView = (GridView) findViewById(R.id.griview);
            ProductAdapter adapter = new ProductAdapter();

            productList.add(new ProductVO("1","과일","http://localhost:8082/automarket/upload/apple.jpg","1000011","사과",5000,10));
            productList.add(new ProductVO("1","과일","http://localhost:8082/automarket/upload/be.jpg","1000022","배",10000,10));

           // Bitmap imgmap;
            for(ProductVO vo : productList){
//                try {
//                    imgmap = CommLib.getbmpfromURL(vo.getImgPath());
//                    vo.setImgBitmap(imgmap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                vo.byteFromURL();
                adapter.addItem(vo);
            }


            gridView.setAdapter(adapter);



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
    }
