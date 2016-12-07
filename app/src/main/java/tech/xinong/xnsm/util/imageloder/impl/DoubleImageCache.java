package tech.xinong.xnsm.util.imageloder.impl;

import android.graphics.Bitmap;

import tech.xinong.xnsm.util.imageloder.ImageCache;

/**
 * Created by xiao on 2016/12/7.
 */

public class DoubleImageCache implements ImageCache {
    private ImageCache mMemoryCache = new MemoryCache();
    private ImageCache mDiskCache = new DiskCache();


    @Override
    public Bitmap get(String imageUrl) {
        Bitmap bitmap = mMemoryCache.get(imageUrl);
        if (bitmap==null){
            bitmap = mDiskCache.get(imageUrl);
        }

        return bitmap;
    }

    @Override
    public void put(String imageUrl, Bitmap bitmap) {
        mMemoryCache.put(imageUrl,bitmap);
        mDiskCache.put(imageUrl, bitmap);
    }
}
