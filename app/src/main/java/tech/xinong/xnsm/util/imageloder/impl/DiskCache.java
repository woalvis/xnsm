package tech.xinong.xnsm.util.imageloder.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;

import tech.xinong.xnsm.util.CloseUtil;
import tech.xinong.xnsm.util.SDCardUtils;
import tech.xinong.xnsm.util.imageloder.ImageCache;

/**
 * Created by xiao on 2016/12/7.
 */

public class DiskCache implements ImageCache {
    static String cacheDir = SDCardUtils.getRootDirectoryPath()+ File.separator+"cache"+File.separator;

    @Override
    public Bitmap get(String imageUrl) {
        return BitmapFactory.decodeFile(cacheDir+imageUrl);
    }

    @Override
    public void put(String imageUrl, Bitmap bitmap) {
        FileOutputStream fos = null;
        String imageUrlTemp = imageUrl.replaceAll("/","#");
        try{
            fos = new FileOutputStream(cacheDir+imageUrlTemp);
            //100等于不压缩
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            CloseUtil.close(fos);
        }
    }
}
