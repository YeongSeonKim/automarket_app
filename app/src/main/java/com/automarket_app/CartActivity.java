package com.automarket_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.automarket_app.VO.CartVO;
import com.automarket_app.adapter.CartAdapter;
import com.automarket_app.database.MySqliteHelper;
import com.automarket_app.util.Helper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private CartAdapter cartAdapter;
    private SQLiteDatabase db;
    List<CartVO> cartList = new ArrayList<CartVO>();
    String api_url="";
    public CartActivity() {
    }

    class CartDataRunnable implements Runnable {
        private CartAdapter cartAdapter;
        private Handler handler;
        TextView tvTotal;
        CartDataRunnable(){}
        CartDataRunnable(CartAdapter cartAdapter,Handler handler){
            this.cartAdapter = cartAdapter;
            this.handler = handler;
        }
        @Override
        public void run() {

            Cursor c = db.rawQuery("SELECT * FROM cart ", null);
            String result = "" ;
            CartVO vo = new CartVO();
            Integer cart_sum=0,prod_total=0;
            while (c.moveToNext()) {
                System.out.println("cart >> "+ c.getColumnName(0));

                vo = new CartVO();
                vo.setProdid(c.getString(0));
                vo.setProdcnt(c.getInt(1));
                vo.setProdnm(c.getString(2));
                vo.setProdprice(c.getInt(3));
                vo.setImgpath(c.getString(4));

                vo.byteFromURL(api_url);
                cartAdapter.addItem(vo);
                prod_total = c.getInt(3) * c.getInt(1);
                cart_sum += prod_total;
            }

            NumberFormat formatter = new DecimalFormat("#,###");
            String sumformat = formatter.format(cart_sum);

            Bundle bundle =new Bundle();
            bundle.putString("PROD_SUM",sumformat);
            Message msg = new Message();
            msg.setData(bundle);
            this.handler.sendMessage(msg);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        api_url = Helper.getMetaData(this, "api_url");
        final TextView tvTotal;
        MySqliteHelper helper = new MySqliteHelper(CartActivity.this);
        //helper를 통해서 database에 대한 Handle을 얻어 올 수 있다.
        db = helper.getWritableDatabase(); // 액티비티의 필드로 올림

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBack);

        // btnBack
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //final TextView
        tvTotal=(TextView)findViewById(R.id.cart_total_price);
        final ListView lv_cartlist = (ListView)findViewById(R.id.lv_cartlist);

        final Handler handler = new Handler(){
            //handler에게 message가 전달되면 아래 method가 callback됨
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String result = bundle.getString("PROD_SUM");
                tvTotal.setText(result);

                lv_cartlist.setAdapter(cartAdapter);
            }
        };

        cartAdapter = new CartAdapter(handler,db);

        //데이터 바인딩
        CartDataRunnable runnable = new CartDataRunnable(cartAdapter,handler);
        Thread t = new Thread(runnable);
        t.start();

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
