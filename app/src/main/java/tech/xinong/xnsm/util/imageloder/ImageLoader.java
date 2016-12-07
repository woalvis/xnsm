package tech.xinong.xnsm.util.imageloder;

import android.graphics.Bitmap;
import android.widget.ImageView;

import tech.xinong.xnsm.util.imageloder.impl.MemoryCache;
import tech.xinong.xnsm.util.imageloder.impl.OkgoImageDownloader;

/**
 * Created by xiao on 2016/12/7.
 */

public class ImageLoader {
    /**
     * 图片缓存类，给一个默认的实现，可以调用setImageCache更改缓存策略
     */
    private ImageCache imageCache = new MemoryCache();



    /**
     * 图片下载类，给一个默认的实现，可以调用setImageCache更改缓存策略
     */
    private ImageDownloader imageDownloader = new OkgoImageDownloader();

    /**
     * 注入缓存实现，用户可以自行选择缓存策略，也可以自己去实线
     * @param imageCache
     */
    public ImageLoader setImageCache(ImageCache imageCache) {
        this.imageCache = imageCache;
        return this;
    }

    public void setImageDownloader(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
    }

    private void submitLoadRequest(final String imageUrl,
                                   final ImageView imageView){
        imageView.setTag(imageUrl);

    }

    public void displayImage(final String imageUrl,final ImageView imageView){
        Bitmap bitmap = imageCache.get(imageUrl);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        //图片没有缓存，提交到线程池中下载图片
        submitLoadRequest(imageUrl, imageView);
    }


}
