package tech.xinong.xnsm.util.imageloder.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.HttpURLConnection;
import java.net.URL;

import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.application.XnsApplication;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.imageloder.ImageDownloader;

/**
 * Created by xiao on 2016/12/28.
 */

public class HttpUrlImageDownloader implements ImageDownloader {

    private String token;
    public  HttpUrlImageDownloader(){
        SharedPreferences sp = XnsApplication.getAppContext().getSharedPreferences(XnsConstant.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        token = sp.getString(XnsConstant.TOKEN, "");
    }
    @Override
    public Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try{
            URL url = new URL(imageUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty(HttpConstant.HTTP_HEADER_TOKEN, token);
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }
}
