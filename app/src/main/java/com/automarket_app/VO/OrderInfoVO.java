package com.automarket_app.VO;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

// 주문내역조회

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderInfoVO implements Parcelable {

    private String orderid;
    private String userid;
    private String orderdate;
    private String receiptkey;
    private String receiptflag;
    private String receiptaddr;
    private int receiptlati;
    private int receiptlong;
    private String carid;
    private int totalprice;
    private List<OrderDetailVO> orderdetail;

        // CREATOR 라고 불리는 static 상수를 반드시 정의
    public static final Parcelable.Creator<OrderInfoVO> CREATOR = new Parcelable.Creator<OrderInfoVO>() {
        @Override
        public OrderInfoVO createFromParcel(Parcel parcel) {
            // 마샬링된 데이터를 언마샬링(복원)할 때 사용되는 method
            return new OrderInfoVO(parcel);
        }

        @Override
        public OrderInfoVO[] newArray(int i) {
            return new OrderInfoVO[i];
        }
    };

    public OrderInfoVO() {
    }

    public OrderInfoVO(String orderid, String userid, String orderdate, String receiptkey, String receiptflag, String receiptaddr, int receiptlati, int receiptlong, String carid, int totalprice, List<OrderDetailVO> orderdetail) {
        this.orderid = orderid;
        this.userid = userid;
        this.orderdate = orderdate;
        this.receiptkey = receiptkey;
        this.receiptflag = receiptflag;
        this.receiptaddr = receiptaddr;
        this.receiptlati = receiptlati;
        this.receiptlong = receiptlong;
        this.carid = carid;
        this.totalprice = totalprice;
        this.orderdetail = orderdetail;
    }

    // 복원작업 할때 사용되는 생성자.
    // 복원시 제일 신경 써야되는 부분은 순서...
    // (마샬링 순서와 언마샬링 순서가 동일해야 한다.)
    protected OrderInfoVO(Parcel parcel){
        orderid = parcel.readString();
        userid = parcel.readString();
        orderdate = parcel.readString();
        receiptkey = parcel.readString();
        receiptflag = parcel.readString();
        receiptaddr = parcel.readString();
        receiptlati = parcel.readInt();
        receiptlong = parcel.readInt();
        carid = parcel.readString();
        totalprice = parcel.readInt();
        orderdetail = parcel.readArrayList(null);

    }

    // override method

    // 수정할 필요 없다!!
    @Override
    public int describeContents() {
        return 0;
    }

    // 마샬링하는 역할을 하는 method / 데이터 변환
    // 데이터를 변화하는 순서와 복원하는 순서가 반드시 같아야 한다.
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        try{
            // 순서 맞춰가면서 해야함!!
            parcel.writeString(orderid);
            parcel.writeString(userid);
            parcel.writeString(orderdate);
            parcel.writeString(receiptkey);
            parcel.writeString(receiptflag);
            parcel.writeString(receiptaddr);
            parcel.writeInt(receiptlati);
            parcel.writeInt(receiptlong);
            parcel.writeString(carid);
            parcel.writeInt(totalprice);
            parcel.writeList(orderdetail);

        }catch (Exception e){
            Log.i("automarket_app", e.toString());
        }
    }


    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getReceiptkey() {
        return receiptkey;
    }

    public void setReceiptkey(String receiptkey) {
        this.receiptkey = receiptkey;
    }

    public String getReceiptflag() {
        return receiptflag;
    }

    public void setReceiptflag(String receiptflag) {
        this.receiptflag = receiptflag;
    }

    public String getReceiptaddr() {
        return receiptaddr;
    }

    public void setReceiptaddr(String receiptaddr) {
        this.receiptaddr = receiptaddr;
    }

    public int getReceiptlati() {
        return receiptlati;
    }

    public void setReceiptlati(int receiptlati) {
        this.receiptlati = receiptlati;
    }

    public int getReceiptlong() {
        return receiptlong;
    }

    public void setReceiptlong(int receiptlong) {
        this.receiptlong = receiptlong;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public List<OrderDetailVO> getOrderdetail() {
        return orderdetail;
    }

    public void setOrderdetail(List<OrderDetailVO> orderdetail) {
        this.orderdetail = orderdetail;
    }

    @Override
    public String toString() {
        return "OrderInfoVO{" +
                "orderid='" + orderid + '\'' +
                ", userid='" + userid + '\'' +
                ", orderdate='" + orderdate + '\'' +
                ", receiptkey='" + receiptkey + '\'' +
                ", receiptflag='" + receiptflag + '\'' +
                ", receiptaddr='" + receiptaddr + '\'' +
                ", receiptlati=" + receiptlati +
                ", receiptlong=" + receiptlong +
                ", carid='" + carid + '\'' +
                ", totalprice=" + totalprice +
                ", orderdetail=" + orderdetail +
                '}';
    }
}

