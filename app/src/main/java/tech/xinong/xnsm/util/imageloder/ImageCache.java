package tech.xinong.xnsm.util.imageloder;

import android.graphics.Bitmap;

/**
 * Created by xiao on 2016/12/7.
 */

public interface ImageCache  {
    Bitmap get(String imageUrl);
    void put(String imageUrl,Bitmap bitmap);
}
