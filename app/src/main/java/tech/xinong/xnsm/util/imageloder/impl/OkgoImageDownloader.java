package tech.xinong.xnsm.util.imageloder.impl;

import android.graphics.Bitmap;

import com.lzy.okgo.callback.BitmapCallback;

import okhttp3.Call;
import okhttp3.Response;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.pro.base.application.XnsApplication;
import tech.xinong.xnsm.util.imageloder.ImageDownloader;

/**
 * Created by xiao on 2016/12/7.
 */

public class OkgoImageDownloader implements ImageDownloader {


    @Override
    public Bitmap downloadImage(String imageUrl) {
        final Bitmap[] bitmapTemp = {null};
        XinongHttpCommend.getInstance(XnsApplication.getAppContext()).showPic(imageUrl, new BitmapCallback() {
            @Override
            public void onSuccess(Bitmap bitmap, Call call, Response response) {
                bitmapTemp[0] = bitmap;
            }
        });
      return null;
    }

}
