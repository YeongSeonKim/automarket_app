package com.automarket_app.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.automarket_app.R;
import com.automarket_app.VO.OrderInfoVO;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class OrderInfoAdapter extends BaseAdapter {

    ArrayList<OrderInfoVO> orderInfo_list = new ArrayList<OrderInfoVO>();

    public OrderInfoAdapter() {
    }

    @Override
    public int getCount() {
        return orderInfo_list.size();
    }

    @Override
    public Object getItem(int position) {
        return orderInfo_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override      // 몇번째 그림을 그릴지
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        // data를 가져다가 그림을 그림

        final Context context = viewGroup.getContext();

        // 출력할 view 객체를 생성.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            // 생성된 view 객체에 XML Layout을 설정.
            view = inflater.inflate(R.layout.orderinfo_item, viewGroup, false);

            // 출력할 view Component의 reference 를 획득.
            TextView order_id = (TextView) view.findViewById(R.id.order_id);
            TextView order_receiptkey = (TextView)view.findViewById(R.id.order_receiptkey);
            TextView order_date = (TextView) view.findViewById(R.id.order_date);
            TextView order_total_price = (TextView) view.findViewById(R.id.order_total_price);

            // final ListView orderInfoList = (ListView)view.findViewById(R.id.orderInfoList);

            // 화면에 출력할 데이터를 가져와요!!
            final OrderInfoVO vo = orderInfo_list.get(position);

            order_id.setText(vo.getOrderid()); // 1000003
            order_receiptkey.setText(vo.getReceiptkey());
            order_date.setText(vo.getOrderdate()); //2019-10-02 15:38:29
                NumberFormat formatter = new DecimalFormat("#,###");
                String total_orderPrice = formatter.format(vo.getTotalprice());
            order_total_price.setText(total_orderPrice);

        }
        return view;
    }

    public void addList(OrderInfoVO vo) {
        orderInfo_list.add(vo);
    }
}