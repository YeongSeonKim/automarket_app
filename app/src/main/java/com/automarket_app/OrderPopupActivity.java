package com.automarket_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.automarket_app.VO.ProductVO;
import com.automarket_app.database.MySqliteHelper;

public class OrderPopupActivity extends AppCompatActivity {
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_popup);

        Intent i = getIntent();
        final ProductVO vo = (ProductVO)i.getExtras().get("selData");

        ImageButton btnClose = (ImageButton)findViewById(R.id.btnClose);
        TextView pop_txtProdNm = (TextView)findViewById(R.id.pop_txtProdNm);
        TextView pop_txtProdPrice = (TextView)findViewById(R.id.pop_txtProdPrice);
        ImageView pop_txtProdImg = (ImageView)findViewById(R.id.pop_txtProdImg);
        Button pop_btnMinus = (Button)findViewById(R.id.pop_btnMinus);
        Button pop_btnPlus = (Button)findViewById(R.id.pop_btnPlus);
        Button pop_btnAddCart = (Button)findViewById(R.id.pop_btnAddCart);
        final EditText pop_edtProdCnt = (EditText)findViewById(R.id.pop_edtProdCnt);

        pop_txtProdNm.setText(vo.getProdnm());
        pop_txtProdPrice.setText(String.valueOf(vo.getProdprice()));

        if(vo.getThumbnailimg() !=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(vo.getThumbnailimg(), 0, vo.getThumbnailimg().length);
            pop_txtProdImg.setImageBitmap(bitmap);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pop_btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer cnt = Integer.parseInt(pop_edtProdCnt.getText().toString());
                if(cnt>1){
                    cnt -= 1;
                    pop_edtProdCnt.setText(String.valueOf(cnt));
                    vo.setProdcnt(cnt);
                }
            }
        });
        pop_btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer cnt = Integer.parseInt(pop_edtProdCnt.getText().toString());
                cnt += 1;
                pop_edtProdCnt.setText(String.valueOf(cnt));
                vo.setProdcnt(cnt);
            }
        });
        pop_btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //장바구니 담기
                MySqliteHelper helper =new MySqliteHelper(OrderPopupActivity.this);
                //helper를 통해서 database에 대한 Handle을 얻어올수 있음
                db = helper.getWritableDatabase();


                Cursor c = db.rawQuery("select * from cart where prodid=? ", new String[]{vo.getProdid()});

                if(c.getCount()==0){
                    db.execSQL("INSERT INTO cart VALUES (?,?,?,?,?) ",
                            new Object[]{vo.getProdid(),pop_edtProdCnt.getText().toString(),vo.getProdnm(),vo.getProdprice(),vo.getImgpath()});
                }else{
                    db.execSQL("UPDATE cart SET prodcnt=?,prodprice=? where prodid=?"
                            ,new Object[]{pop_edtProdCnt.getText().toString(),vo.getProdprice(),vo.getProdid()});
                }

                while(c.moveToNext()){
                  System.out.println(c.getString(0)+"/"+c.getString(1));
                }

                Toast.makeText(OrderPopupActivity.this,"장바구니에 담겼습니다.",Toast.LENGTH_LONG).show();

            }
        });


    }
}
