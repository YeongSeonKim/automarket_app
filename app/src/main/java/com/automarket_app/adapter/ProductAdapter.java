package com.automarket_app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automarket_app.R;
import com.automarket_app.VO.ProductVO;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    ArrayList<ProductVO> list = new ArrayList<ProductVO>();
    URL url;
    public void addItem(ProductVO vo) {
        list.add(vo);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        // 추력할 View 객체를 생성
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 생성된 View의 객체에 XML layout을 설정
            view = inflater.inflate(R.layout.product_item, viewGroup, false);
        }
        // 출력할 View Component의 reference를 획득
        ImageView imageView = (ImageView)view.findViewById(R.id.imageview);
        TextView txtProdNm = (TextView)view.findViewById(R.id.txtProdNm);
        TextView txtProdPrice = (TextView)view.findViewById(R.id.txtProdPrice);

        ProductVO vo = list.get(position);

        txtProdNm.setText(vo.getProdnm());
        NumberFormat formatter = new DecimalFormat("#,###");
        String prodPrice = formatter.format(vo.getProdprice());
        txtProdPrice.setText(prodPrice);

        if(vo.getThumbnailimg() !=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(vo.getThumbnailimg(), 0, vo.getThumbnailimg().length);
            imageView.setImageBitmap(bitmap);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "You Clicked "+list.get(position).getProdnm(), Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }

}
