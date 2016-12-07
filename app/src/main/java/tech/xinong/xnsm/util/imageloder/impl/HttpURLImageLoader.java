package tech.xinong.xnsm.util.imageloder.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tech.xinong.xnsm.util.imageloder.ImageDownloader;

/**
 * Created by xiao on 2016/12/7.
 */

public class HttpURLImageLoader implements ImageDownloader {
    /**
     *线程池，线程数量为CPU的数量
     */
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    @Override
    public Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

        return bitmap;
    }
}
