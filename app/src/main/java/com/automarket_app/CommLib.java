package com.automarket_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommLib {
    //img url => byte[]
    public static byte[] recoverImageFromUrl(String urlText) {
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
}
