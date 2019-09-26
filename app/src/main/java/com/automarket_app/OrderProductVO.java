package com.automarket_app;

import java.util.List;

public class OrderProductVO {

    private List prodList;

    private  String CateId;
    private String CateNm;
    private String ImgPath;
    private String ProdNm;
    private int ProdPrice;
    private int ProdCnt;

    // alt + insert
    public OrderProductVO(){

    }

    public OrderProductVO(List prodList, String cateId, String cateNm, String imgPath, String prodNm, int prodPrice, int prodCnt) {
        this.prodList = prodList;
        CateId = cateId;
        CateNm = cateNm;
        ImgPath = imgPath;
        ProdNm = prodNm;
        ProdPrice = prodPrice;
        ProdCnt = prodCnt;
    }

    public List getProdList() {
        return prodList;
    }

    public void setProdList(List prodList) {
        this.prodList = prodList;
    }

    public String getCateId() {
        return CateId;
    }

    public void setCateId(String cateId) {
        CateId = cateId;
    }

    public String getCateNm() {
        return CateNm;
    }

    public void setCateNm(String cateNm) {
        CateNm = cateNm;
    }

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String imgPath) {
        ImgPath = imgPath;
    }

    public String getProdNm() {
        return ProdNm;
    }

    public void setProdNm(String prodNm) {
        ProdNm = prodNm;
    }

    public int getProdPrice() {
        return ProdPrice;
    }

    public void setProdPrice(int prodPrice) {
        ProdPrice = prodPrice;
    }

    public int getProdCnt() {
        return ProdCnt;
    }

    public void setProdCnt(int prodCnt) {
        ProdCnt = prodCnt;
    }

    @Override
    public String toString() {
        return "OrderProductVO{" +
                "prodList=" + prodList +
                ", CateId='" + CateId + '\'' +
                ", CateNm='" + CateNm + '\'' +
                ", ImgPath='" + ImgPath + '\'' +
                ", ProdNm='" + ProdNm + '\'' +
                ", ProdPrice=" + ProdPrice +
                ", ProdCnt=" + ProdCnt +
                '}';
    }
}
