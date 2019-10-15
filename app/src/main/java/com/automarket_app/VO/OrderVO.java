package com.automarket_app.VO;

import java.util.List;

// 주문하기
public class OrderVO {

    private String userid;
    private int totalprice;
    private String carid;
    private String receiptaddr;
    private double receiptlati;
    private double receiptlong;
    private List<OrderDetailVO> orderdetail;

    public OrderVO() {
    }

    public OrderVO(String userid, int totalprice, String carid, String receiptaddr, double receiptlati, double receiptlong, List<OrderDetailVO> orderdetail) {
        this.userid = userid;
        this.totalprice = totalprice;
        this.carid = carid;
        this.receiptaddr = receiptaddr;
        this.receiptlati = receiptlati;
        this.receiptlong = receiptlong;
        this.orderdetail = orderdetail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getReceiptaddr() {
        return receiptaddr;
    }

    public void setReceiptaddr(String receiptaddr) {
        this.receiptaddr = receiptaddr;
    }

    public double getReceiptlati() {
        return receiptlati;
    }

    public void setReceiptlati(double receiptlati) {
        this.receiptlati = receiptlati;
    }

    public double getReceiptlong() {
        return receiptlong;
    }

    public void setReceiptlong(double receiptlong) {
        this.receiptlong = receiptlong;
    }

    public List<OrderDetailVO> getOrderdetail() {
        return orderdetail;
    }

    public void setOrderdetail(List<OrderDetailVO> orderdetail) {
        this.orderdetail = orderdetail;
    }
}

