package tech.xinong.xnsm.util.imageloder.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileOutputStream;

import tech.xinong.xnsm.util.CloseUtil;
import tech.xinong.xnsm.util.imageloder.ImageCache;

/**
 * Created by xiao on 2016/12/7.
 */

public class DiskCache implements ImageCache {
    static String cacheDir = "sdcard/cache/";

    @Override
    public Bitmap get(String imageUrl) {
        return BitmapFactory.decodeFile(cacheDir+imageUrl);
    }

    @Override
    public void put(String imageUrl, Bitmap bitmap) {
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(cacheDir+imageUrl);
            //100等于不压缩
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            CloseUtil.close(fos);
        }
    }
}
