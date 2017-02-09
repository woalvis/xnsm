package tech.xinong.xnsm.util.imageloder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tech.xinong.xnsm.util.imageloder.impl.DoubleImageCache;
import tech.xinong.xnsm.util.imageloder.impl.HttpUrlImageDownloader;

/**
 * Created by xiao on 2016/12/7.
 * 图片加载，缓存工具类，饿汉模式实现单例，保证线程安全
 */

public class ImageLoader {
    /**
     * 图片缓存类，给一个默认的实现，可以调用setImageCache更改缓存策略
     */
    private ImageCache imageCache = new DoubleImageCache();

    private static ImageLoader instance = new ImageLoader();

    private ExecutorService mEService;

    private ImageLoader(){
        getThreadPool();
    }
    public static ImageLoader getInstance(){
        return instance;
    }

    private DownloadSuccessListener listener;
    public static final int MYHANDLER_WHAT = 0X101;


    private void getThreadPool() {
        mEService = Executors.newFixedThreadPool(2);
    }

    /**
     * 图片下载类，给一个默认的实现，可以调用setImageCache更改缓存策略
     */
    private ImageDownloader imageDownloader = new HttpUrlImageDownloader();

    /**
     * 注入缓存实现，用户可以自行选择缓存策略，也可以自己去实现
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
        mEService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = imageDownloader.downloadImage(imageUrl);
                if (bitmap==null){
                    return;
                }
                if (imageView.getTag().equals(imageUrl)){
                    try {
                        if (listener!=null){
                            listener.onDownloadSuccess(bitmap,imageView);
                        }

//                        MyHandler myHandler = new MyHandler(bitmap,imageView);
//                        myHandler.sendEmptyMessage(MYHANDLER_WHAT);

//                        int height  = bitmap.getHeight();
//                        int width = bitmap.getWidth();
//                        L.e("bitmap.getHeight()---->",""+bitmap.getHeight());
//                        L.e("bitmap.getWidth()----->",""+bitmap.getWidth());
//                       imageView.setImageBitmap(bitmap);
                    }catch (Throwable e){
                        e.printStackTrace();
                    }

                }

                imageCache.put(imageUrl,bitmap);
            }
        });
    }

    public void displayImage(final String imageUrl,final ImageView imageView){
        Bitmap bitmap = imageCache.get(imageUrl);
        if (bitmap!=null){
            if (listener!=null){
                listener.onDownloadSuccess(bitmap,imageView);
            }
            return;
        }
        //图片没有缓存，提交到线程池中下载图片
        submitLoadRequest(imageUrl, imageView);
    }



    public Bitmap loadImage(final String path,final int width,final int height,final OnCallBackListener listener){
        Bitmap bitmap= getFormCache(path);
        final Handler handle=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Bitmap bitmap=(Bitmap) msg.obj;
                if(listener!=null){
                    listener.setOnCallBackListener(bitmap,path);
                }
                super.handleMessage(msg);
            }
        };
        if(bitmap==null){
            mEService.execute(new Thread(){
                @Override
                public void run() {
                    Bitmap bitmap=getThumbImage(path, width, height);
                    Message msg=new Message();
                    msg.obj=bitmap;
                    handle.sendMessage(msg);
                    addtoCache(path,bitmap);
                }
            });
            return null;
        }else{
            return bitmap;
        }

    }


    public interface OnCallBackListener{
        public void setOnCallBackListener(Bitmap bitmap,String url);
    }

    private Bitmap getFormCache(String path) {
        return imageCache.get(path);
    }


    private Bitmap getThumbImage(String path, int width, int height) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, option);
        option.inSampleSize = getScale(option, width, height);
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, option);

    }

    private int getScale(BitmapFactory.Options options, int viewWidth, int viewHeight) {
        int inSampleSize = 1;
        if (viewWidth == 0 || viewWidth == 0) {
            return inSampleSize;
        }
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        if (bitmapWidth > viewWidth || bitmapHeight > viewWidth) {
            int widthScale = Math
                    .round((float) bitmapWidth / (float) viewWidth);
            int heightScale = Math.round((float) bitmapHeight
                    / (float) viewWidth);
            inSampleSize = widthScale < heightScale ? widthScale : heightScale;
        }
        return inSampleSize;
    }


    private void addtoCache(String path, Bitmap bitmap) {
        if (bitmap != null) {
            imageCache.put(path, bitmap);
        }
    }

    public interface DownloadSuccessListener{
        void onDownloadSuccess(Bitmap bitmap,ImageView imageView);
    }

    public ImageLoader setLis(DownloadSuccessListener listener){
        this.listener = listener;
        return this;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    private class MyHandler extends Handler{

        private Bitmap bitmap;
        private ImageView imageView;
        public MyHandler(Bitmap bitmap,ImageView imageView){
            this.bitmap = bitmap;
            this.imageView = imageView;
        }
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==MYHANDLER_WHAT){
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
