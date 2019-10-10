package com.automarket_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.automarket_app.OrderPopupActivity;
import com.automarket_app.R;
import com.automarket_app.VO.ProductVO;

import org.w3c.dom.Text;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    LayoutInflater inflater;
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
        //View popupView=null;
        // 추력할 View 객체를 생성
        if(view == null){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 생성된 View의 객체에 XML layout을 설정
            view = inflater.inflate(R.layout.product_item, viewGroup, false);
        }
        // 출력할 View Component의 reference를 획득
        ImageView imageView = (ImageView)view.findViewById(R.id.imageview);
        TextView txtProdNm = (TextView)view.findViewById(R.id.txtProdNm);
        TextView txtProdPrice = (TextView)view.findViewById(R.id.txtProdPrice);

        final ProductVO vo = list.get(position);

        txtProdNm.setText(vo.getProdnm());
        NumberFormat formatter = new DecimalFormat("#,###");
        String prodPrice = formatter.format(vo.getProdprice());
        txtProdPrice.setText(prodPrice);

        if(vo.getThumbnailimg() !=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(vo.getThumbnailimg(), 0, vo.getThumbnailimg().length);
            imageView.setImageBitmap(bitmap);
        }

        /*final PopupWindow pw = new PopupWindow(
                inflater.inflate(R.layout.activity_order_popup, null, false),
                LinearLayout.LayoutParams.MATCH_PARENT,
                550,
                true);

        final View popupview = pw.getContentView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText et_selid = (EditText)popupview.findViewById(R.id.et_selid);
                TextView pop_txtProdNm = (TextView)popupview.findViewById(R.id.pop_txtProdNm);
                TextView pop_txtProdPrice = (TextView)popupview.findViewById(R.id.pop_txtProdPrice);
                ImageView pop_txtProdImg = (ImageView)popupview.findViewById(R.id.pop_txtProdImg);
                Button pop_btnMinus = (Button)popupview.findViewById(R.id.pop_btnMinus);
                Button pop_btnPlus = (Button)popupview.findViewById(R.id.pop_btnPlus);
                Button pop_btnAddCart = (Button)popupview.findViewById(R.id.pop_btnAddCart);
                final EditText pop_edtProdCnt = (EditText)popupview.findViewById(R.id.pop_edtProdCnt);

                et_selid.setText(vo.getProdid());
                pop_txtProdNm.setText(vo.getProdnm());
                pop_txtProdPrice.setText(String.valueOf(vo.getProdprice()));

                pop_btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("pop_btnMinus22");
                        Integer cnt = Integer.parseInt(pop_edtProdCnt.getText().toString());
                        cnt -= 1;
                        pop_edtProdCnt.setText(String.valueOf(cnt));
                    }
                });
                pop_btnPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("pop_btnPlus22");
                        Integer cnt = Integer.parseInt(pop_edtProdCnt.getText().toString());
                        cnt += 1;
                        pop_edtProdCnt.setText(String.valueOf(cnt));
                    }
                });
                pop_btnAddCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                if(vo.getThumbnailimg() !=null){
                    Bitmap bitmap = BitmapFactory.decodeByteArray(vo.getThumbnailimg(), 0, vo.getThumbnailimg().length);
                    pop_txtProdImg.setImageBitmap(bitmap);
                }

                if(!pw.isShowing()) {
                    pw.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
                else
                    pw.dismiss();

            }
        });*/

        return view;
    }

}
