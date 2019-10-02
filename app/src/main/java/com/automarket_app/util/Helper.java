package com.automarket_app.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class Helper {

    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("automarket_app", "Unable to load meta-data: " + e.getMessage());
        }
        return null;
    }
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
