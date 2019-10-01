package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.automarket_app.VO.CartVO;
import com.automarket_app.adapter.CartAdapter;
import com.automarket_app.database.MySqliteHelper;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    List<CartVO> cartList = new ArrayList<CartVO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        MySqliteHelper helper = new MySqliteHelper(CartActivity.this);
        //helper를 통해서 database에 대한 Handle을 얻어 올 수 있다.
        db = helper.getWritableDatabase(); // 액티비티의 필드로 올림

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);

        // btnBack
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                //finish();
            }
        });

        // 주문하기에서 물건을 담은 장바구니 리스트
        CartAdapter Cartadapter = new CartAdapter();

        ListView cart_list = (ListView)findViewById(R.id.cart_list);
        cart_list.setAdapter(Cartadapter);

        cartList.add(new CartVO());

//        // Database handle을 이용해서 Database 처리를 할 수 있다.
//        // rawQuery() : select 계열의 SQL문을 실행할 때 사용된다.!
//        // Cursor : Cursor의 역할은 JDBC의 ResultSet의 역할을 수행
//        Cursor c = db.rawQuery("SELECT * FROM cart ", null);
//        String result = "" ;
//        while (c.moveToNext()) { // moveToNext() :  rs.next()와 같은역할
//            result += c.getString(0);
//            result += ", ";
//            result += c.getInt(1);
//            result += "\n";
//        }
//        Log.i("result", result);

        // db에 있는 데이터를 다 얻어오면 listView에 뿌려주기



        // 캐시충전
        Button btnAddCash_Page = (Button)findViewById(R.id.btnAddCash_Page);
        btnAddCash_Page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CashActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });

        // 주문하기, 다이어로그 이용해서 차량선택창 띄우기
        Button btnOrder = (Button)findViewById(R.id.btnOrder);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                AlertDialog.Builder dialog = new AlertDialog.Builder(CartActivity.this);
                dialog.setTitle("차량선택");

                dialog.setPositiveButton("주문하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 다른 Activity를 호출하는 code
                        Intent intent = new Intent(getApplicationContext(), CartActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                        Toast.makeText(getApplicationContext(),"주문이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 취소버튼을 눌렀을때 수행할 코드 작성
                    }
                });


                dialog.show();
            }
        });


    }
}
