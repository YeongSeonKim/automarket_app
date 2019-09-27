package com.automarket_app.VO;

// 상품상세조회
public class ProdDtailVO {

    private  String cateId;
    private String cateNm;
    private String imgPath;
    private String prodId;
    private String prodNm;
    private int prodPrice;
    private int prodCnt;

    public ProdDtailVO() {
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getCateNm() {
        return cateNm;
    }

    public void setCateNm(String cateNm) {
        this.cateNm = cateNm;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdNm() {
        return prodNm;
    }

    public void setProdNm(String prodNm) {
        this.prodNm = prodNm;
    }

    public int getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(int prodPrice) {
        this.prodPrice = prodPrice;
    }

    public int getProdCnt() {
        return prodCnt;
    }

    public void setProdCnt(int prodCnt) {
        this.prodCnt = prodCnt;
    }

    @Override
    public String toString() {
        return "ProdDtailVO{" +
                "cateId='" + cateId + '\'' +
                ", cateNm='" + cateNm + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", prodId='" + prodId + '\'' +
                ", prodNm='" + prodNm + '\'' +
                ", prodPrice=" + prodPrice +
                ", prodCnt=" + prodCnt +
                '}';
    }
}
