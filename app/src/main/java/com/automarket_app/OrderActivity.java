package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TabHost;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);
        ImageButton btnCart = (ImageButton)findViewById(R.id.btnCart);

        // btnBack
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // btnCart
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost) ;
        tabHost.setup() ;


        // 첫 번째 Tab. (탭 표시 텍스트:"과일"), (페이지 뷰:"tab1")
        TabHost.TabSpec tab1 = tabHost.newTabSpec("1").setContent(R.id.tab1).setIndicator("과일") ;
        tabHost.addTab(tab1)  ;

        // 두 번째 Tab. (탭 표시 텍스트:"채소"), (페이지 뷰:"tab2")
        TabHost.TabSpec tab2 = tabHost.newTabSpec("2").setContent(R.id.tab2).setIndicator("채소") ;
        tabHost.addTab(tab2) ;

        // 세 번째 Tab. (탭 표시 텍스트:"쌀 / 잡곡"), (페이지 뷰:"tab3")
        TabHost.TabSpec tab3 = tabHost.newTabSpec("3").setContent(R.id.tab3).setIndicator("쌀 / 잡곡") ;
        tabHost.addTab(tab3) ;

        // 네 번째 Tab. (탭 표시 텍스트:"정육"), (페이지 뷰:"tab4")
        TabHost.TabSpec tab4 = tabHost.newTabSpec("4").setContent(R.id.tab4).setIndicator("정육") ;
        tabHost.addTab(tab4) ;


        // 다섯 번째 Tab. (탭 표시 텍스트:"해산물"), (페이지 뷰:"tab5")
        TabHost.TabSpec tab5 = tabHost.newTabSpec("5").setContent(R.id.tab5).setIndicator("해산물") ;
        tabHost.addTab(tab5) ;

        // 여섯 번째 Tab. (탭 표시 텍스트:"유제품"), (페이지 뷰:"tab6")
        TabHost.TabSpec tab6 = tabHost.newTabSpec("6").setContent(R.id.tab6).setIndicator("유제품") ;
        tabHost.addTab(tab6) ;

        GridView gridView = (GridView)findViewById(R.id.tab1GridView);


    }
}
