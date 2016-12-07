package tech.xinong.xnsm.util.imageloder;

import android.graphics.Bitmap;

/**
 * Created by xiao on 2016/12/7.
 */

public interface ImageDownloader {
    Bitmap downloadImage(String imageUrl);
}
