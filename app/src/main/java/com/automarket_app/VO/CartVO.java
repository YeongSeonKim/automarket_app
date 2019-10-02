package com.automarket_app.VO;

import android.util.Log;

import com.automarket_app.util.Helper;

import java.util.Arrays;

public class CartVO {

    private String imgpath; // 상품이미지
    private String prodnm; // 상품명
    private int prodprice; // 상품가격
    private int prodcnt;   //  수량
    //private Bitmap imgBitmap;
    private byte[] thumbnailimg;

    public CartVO() {
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getProdnm() {
        return prodnm;
    }

    public void setProdnm(String prodnm) {
        this.prodnm = prodnm;
    }

    public int getProdprice() {
        return prodprice;
    }

    public void setProdprice(int prodprice) {
        this.prodprice = prodprice;
    }

    public int getProdcnt() {
        return prodcnt;
    }

    public void setProdcnt(int prodcnt) {
        this.prodcnt = prodcnt;
    }

    public byte[] getThumbnailimg() {
        return thumbnailimg;
    }

    public void setThumbnailimg(byte[] thumbnailimg) {
        this.thumbnailimg = thumbnailimg;
    }

    public  void byteFromURL(){
        byte[] d= null;
        try{
            this.thumbnailimg = Helper.recoverImageFromUrl(imgpath);

        }catch (Exception e){
            Log.e("automarket_app",e.toString());
        }
    }

    @Override
    public String toString() {
        return "CartVO{" +
                "imgpath='" + imgpath + '\'' +
                ", prodnm='" + prodnm + '\'' +
                ", prodprice=" + prodprice +
                ", prodcnt=" + prodcnt +
                ", thumbnailimg=" + Arrays.toString(thumbnailimg) +
                '}';
    }
}
