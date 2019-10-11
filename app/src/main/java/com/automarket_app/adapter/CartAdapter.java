package com.automarket_app.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.automarket_app.OrderPopupActivity;
import com.automarket_app.R;
import com.automarket_app.VO.CartVO;
import com.automarket_app.VO.ProductVO;
import com.automarket_app.database.MySqliteHelper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    ArrayList<CartVO> cart_list = new ArrayList<CartVO>();
    private SQLiteDatabase db;
    //TextView parent_tv;
    private Handler handler;
    public CartAdapter(){}
    public CartAdapter(Handler handler,SQLiteDatabase db){
        this.handler = handler;
        this.db = db;
        //this.parent_tv = textView;
    }

    public void addItem(CartVO vo) {
        cart_list.add(vo);
    }
    @Override
    public int getCount() {
        return cart_list.size();
    }

    @Override
    public Object getItem(int position) {
        return cart_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position,View view, final ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();
        View viewparent=null;
        // 출력할 view 객체를 생성.
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            // 생성된 view 객체에 XML Layout을 설정.
            view = inflater.inflate(R.layout.cart_item, viewGroup, false);
        }

        ImageView cart_prodImg = (ImageView)view.findViewById(R.id.cart_prodImg);
        TextView cart_prodNm = (TextView)view.findViewById(R.id.cart_prodNm);
        TextView cart_prodPrice = (TextView)view.findViewById(R.id.cart_prodPrice);
        final TextView cart_prodCnt = (TextView)view.findViewById(R.id.cart_prodCnt);

        Button btn_cart_plus = (Button)view.findViewById(R.id.btn_cart_plus);
        Button btn_cart_minus = (Button)view.findViewById(R.id.btn_cart_minus);
        ImageButton btn_cart_delete = (ImageButton)view.findViewById(R.id.btn_cart_delete);
        final ListView lv_cartlist = (ListView)view.findViewById(R.id.lv_cartlist);

        final CartVO vo = cart_list.get(position);

        if(vo.getThumbnailimg() !=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(vo.getThumbnailimg(), 0, vo.getThumbnailimg().length);
            cart_prodImg.setImageBitmap(bitmap);
        }

        cart_prodNm.setText(vo.getProdnm());
        final NumberFormat formatter = new DecimalFormat("#,###");
        String prodPrice = formatter.format(vo.getProdprice());
        cart_prodPrice.setText(prodPrice);
        cart_prodCnt.setText(String.valueOf(vo.getProdcnt()));

        btn_cart_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer prod_total = 0;
                Integer cnt = Integer.parseInt(cart_prodCnt.getText().toString());
                cnt += 1;
                cart_prodCnt.setText(String.valueOf(cnt));
                vo.setProdcnt(cnt);
                db.execSQL("UPDATE cart SET prodcnt=?,prodprice=? where prodid=?"
                        ,new Object[]{cnt,vo.getProdprice(),vo.getProdid()});

                Integer cart_sum = prod_sum();
                String sumformat = formatter.format(cart_sum);
                //parent_tv.setText(sumformat);
                msg_handle(sumformat);
            }
        });
        btn_cart_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer cnt = Integer.parseInt(cart_prodCnt.getText().toString());
                if(cnt>1){
                    cnt -= 1;
                    cart_prodCnt.setText(String.valueOf(cnt));
                    vo.setProdcnt(cnt);
                    db.execSQL("UPDATE cart SET prodcnt=?,prodprice=? where prodid=?"
                            ,new Object[]{cnt,vo.getProdprice(),vo.getProdid()});

                    Integer cart_sum = prod_sum();
                    String sumformat = formatter.format(cart_sum);
                    //parent_tv.setText(sumformat);
                    msg_handle(sumformat);
                }
            }
        });
        btn_cart_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.execSQL("DELETE FROM cart where prodid=?"
                        ,new Object[]{vo.getProdid()});

                cart_list.remove(position);
                notifyDataSetChanged();

                Integer cart_sum = prod_sum();
                String sumformat = formatter.format(cart_sum);
                //parent_tv.setText(sumformat);
                msg_handle(sumformat);
            }
        });

        return view;
    }
    public Integer prod_sum(){
        Cursor c = db.rawQuery("SELECT * FROM cart ", null);
        String result = "" ;
        CartVO vo = new CartVO();
        Integer cart_sum=0,prod_total=0;
        while (c.moveToNext()) {
            prod_total = c.getInt(3) * c.getInt(1);
            cart_sum += prod_total;
        }
        return  cart_sum;
    }
    public void msg_handle(String send_msg){
        Bundle bundle =new Bundle();
        bundle.putString("PROD_SUM",send_msg);

        Message msg = new Message();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
