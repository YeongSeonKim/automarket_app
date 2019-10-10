package com.automarket_app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.automarket_app.R;
import com.automarket_app.VO.CartVO;
import com.automarket_app.VO.ProductVO;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
   ArrayList<CartVO> cart_list = new ArrayList<CartVO>();

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
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();

        // 출력할 view 객체를 생성.
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            // 생성된 view 객체에 XML Layout을 설정.
            view = inflater.inflate(R.layout.cart_item, viewGroup, false);
        }

        ImageView cart_prodImg = (ImageView)view.findViewById(R.id.cart_prodImg);
        TextView cart_prodNm = (TextView)view.findViewById(R.id.cart_prodNm);
        TextView cart_prodPrice = (TextView)view.findViewById(R.id.cart_prodPrice);
        TextView cart_prodCnt = (TextView)view.findViewById(R.id.cart_prodCnt);

        Button btn_cart_plus = (Button)view.findViewById(R.id.btn_cart_plus);
        Button btn_cart_minus = (Button)view.findViewById(R.id.btn_cart_minus);
        Button btn_cart_delete = (Button)view.findViewById(R.id.btn_cart_delete);

        CartVO vo = cart_list.get(position);

        if(vo.getThumbnailimg() !=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(vo.getThumbnailimg(), 0, vo.getThumbnailimg().length);
            cart_prodImg.setImageBitmap(bitmap);
        }

        cart_prodNm.setText(vo.getProdnm());
        NumberFormat formatter = new DecimalFormat("#,###");
        String prodPrice = formatter.format(vo.getProdprice());
        cart_prodPrice.setText(prodPrice);
        cart_prodCnt.setText(String.valueOf(vo.getProdcnt()));

        btn_cart_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_cart_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_cart_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }


}
