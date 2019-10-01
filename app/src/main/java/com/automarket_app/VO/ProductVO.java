package com.automarket_app.VO;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.automarket_app.CommLib;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

// 상품상세조회
public class ProductVO implements Parcelable {

    private  String cateid;
    //private String cateNm;
    private String imgpath;
    private String prodid;
    private String prodnm;
    private int prodprice;
    private int prodcnt;
    private byte[] thumbnailimg;
    public ProductVO() {
    }


    protected ProductVO(Parcel in) {
        cateid = in.readString();
        prodnm = in.readString();
        imgpath = in.readString();
        prodid = in.readString();
        prodnm = in.readString();
        prodprice = in.readInt();
        prodcnt = in.readInt();
        thumbnailimg = in.createByteArray();
    }

    public static final Creator<ProductVO> CREATOR = new Creator<ProductVO>() {
        @Override
        public ProductVO createFromParcel(Parcel in) {
            return new ProductVO(in);
        }

        @Override
        public ProductVO[] newArray(int size) {
            return new ProductVO[size];
        }
    };

    public  void byteFromURL(){
        byte[] d= null;
        try{
            this.thumbnailimg = recoverImageFromUrl(imgpath);

        }catch (Exception e){
            Log.e("automarket_app",e.toString());
        }
    }
    public byte[] recoverImageFromUrl(String urlText) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            URL url = new URL(urlText);
            InputStream inputStream = url.openStream();
            int n = 0;
            byte[] buffer = new byte[1024];
            while ((n = inputStream.read(buffer))!= -1) {
                output.write(buffer, 0, n);
            }
        }catch (Exception e){
            Log.e("automarket_app","recoverImageFromUrl>>"+e.toString());
        }
        return output.toByteArray();
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

    public String getCateid() {
        return cateid;
    }

    public void setCateid(String cateid) {
        this.cateid = cateid;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try{
            parcel.writeString(cateid);
            //parcel.writeString(catenm);
            parcel.writeString(imgpath);
            parcel.writeString(prodid);
            parcel.writeString(prodnm);
            parcel.writeInt(prodprice);
            parcel.writeInt(prodcnt);
            parcel.writeByteArray(thumbnailimg);
        }catch (Exception e){
            Log.e("automarket_app",e.toString());
        }
    }
}
