package com.automarket_app.VO;

public class OrderDetailVO {
    private String prodid;
    private Integer ordercnt;

    public OrderDetailVO() {
    }

    public OrderDetailVO(String prodid, Integer ordercnt) {
        this.prodid = prodid;
        this.ordercnt = ordercnt;
    }

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public Integer getOrdercnt() {
        return ordercnt;
    }

    public void setOrdercnt(Integer ordercnt) {
        this.ordercnt = ordercnt;
    }
}
