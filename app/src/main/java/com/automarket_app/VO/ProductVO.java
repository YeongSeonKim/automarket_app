package com.automarket_app.VO;

import android.graphics.Bitmap;
import android.util.Log;

import com.automarket_app.CommLib;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

// 상품상세조회
public class ProductVO {

    private  String cateId;
    private String cateNm;
    private String imgPath;
    private String prodId;
    private String prodNm;
    private int prodPrice;
    private int prodCnt;
    //private Bitmap imgBitmap;
    private byte[] thumbnailimg;
    public ProductVO() {
    }

    public ProductVO(String cateId, String cateNm, String imgPath, String prodId, String prodNm, int prodPrice, int prodCnt) {
        this.cateId = cateId;
        this.cateNm = cateNm;
        this.imgPath = imgPath;
        this.prodId = prodId;
        this.prodNm = prodNm;
        this.prodPrice = prodPrice;
        this.prodCnt = prodCnt;
    }

    public  void byteFromURL(){
        byte[] d= null;
        try{
            this.thumbnailimg = CommLib.recoverImageFromUrl(imgPath);

        }catch (Exception e){
            Log.e("automarket_app",e.toString());
        }
    }

    public byte[] getImgByte() {
        return thumbnailimg;
    }

    public byte[] getThumbnailimg() {
        return thumbnailimg;
    }

    public void setThumbnailimg(byte[] thumbnailimg) {
        this.thumbnailimg = thumbnailimg;
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
        return "ProductVO{" +
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
