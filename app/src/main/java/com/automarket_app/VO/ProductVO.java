package com.automarket_app.VO;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.automarket_app.util.Helper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductVO implements Parcelable {

    private  String cateid;
    //private String cateNm;
    private String imgpath;
    private String prodid;
    private String prodnm;
    private int prodprice;
    private int prodcnt;
    private int costprice;
    private byte[] thumbnailimg;
    private int discount;
    public ProductVO() {
    }


    protected ProductVO(Parcel in) {
        cateid = in.readString();
        imgpath = in.readString();
        prodid = in.readString();
        prodnm = in.readString();
        prodprice = in.readInt();
        prodcnt = in.readInt();
        costprice = in.readInt();
        discount = in.readInt();
        thumbnailimg = in.createByteArray();
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try{
            parcel.writeString(cateid);
            parcel.writeString(imgpath);
            parcel.writeString(prodid);
            parcel.writeString(prodnm);
            parcel.writeInt(prodprice);
            parcel.writeInt(prodcnt);
            parcel.writeInt(costprice);
            parcel.writeInt(discount);
            parcel.writeByteArray(thumbnailimg);
        }catch (Exception e){
            Log.e("automarket_app",e.toString());
        }
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

    public static Creator<ProductVO> getCREATOR() {
        return CREATOR;
    }

    public  void byteFromURL(String apiurl){
        byte[] d= null;
        try{
            if(imgpath!=null){
                Log.i("automarket_app","imgpath: "+ apiurl+ imgpath);
                this.thumbnailimg = Helper.recoverImageFromUrl(apiurl+ imgpath);
            }
            //this.thumbnailimg = recoverImageFromUrl(apiurl+ "/upload/apple.jpg");

        }catch (Exception e){
            Log.e("automarket_app",e.toString());
        }
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getCostprice() {
        return costprice;
    }

    public void setCostprice(int costprice) {
        this.costprice = costprice;
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


}
