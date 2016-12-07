package tech.xinong.xnsm.util.imageloder.impl;

import android.graphics.Bitmap;
import android.util.LruCache;

import tech.xinong.xnsm.util.imageloder.ImageCache;

/**
 * Created by xiao on 2016/12/7.
 */

public class MemoryCache implements ImageCache {
    private LruCache<String,Bitmap> mMemoryCache;

    /**
     * 构造函数，进行初始化
     */
    public MemoryCache(){
        initImageCache();
    }

    private void initImageCache() {
        //计算可使用的最大内存
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        //取四分之一作为可用的内存缓存
        final int cacheSize = maxMemory/4;

        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };
    }

    @Override
    public Bitmap get(String imageUrl) {
        return mMemoryCache.get(imageUrl);
    }

    @Override
    public void put(String imageUrl, Bitmap bitmap) {
        mMemoryCache.put(imageUrl, bitmap);
    }
}
