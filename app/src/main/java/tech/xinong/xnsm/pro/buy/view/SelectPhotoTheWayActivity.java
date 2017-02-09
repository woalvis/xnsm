package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_select_photo_the_way)
public class SelectPhotoTheWayActivity extends BaseActivity {
    @ViewInject(R.id.take_photo)
    private Button takePhoto;
    @ViewInject(R.id.select_media)
    private Button selectMedia;

    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;
    private List<File> files;

    private String filePath = Environment.getExternalStorageDirectory() + "/";


    @Override
    public void initData() {
        super.initData();
        files = new ArrayList<>();
    }

    @OnClick({R.id.take_photo,R.id.select_media})
    public void widgetClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.take_photo:
                //构建隐式Intent
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //调用系统相机
                startActivityForResult(intent, CAMERA_CODE);
                break;
            case R.id.select_media:
                //构建一个内容选择的Intent
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                //设置选择类型为图片类型
                intent.setType("image/*");
                //打开图片选择
                startActivityForResult(intent, GALLERY_CODE);
                break;
        }
    }

   private void upload() {
       if (files!=null&&files.size()>0)
       XinongHttpCommend.getInstance(this).upLoadFile(files, new AbsXnHttpCallback() {
           @Override
           public void onSuccess(String info, String result) {
               if (!TextUtils.isEmpty(info)&& HttpConstant.OK.equals(info)){
                   JSONArray jsonObject = JSON.parseArray(result);
                   showSnackBar("上传成功，请等待审核");
               }
           }
       });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CAMERA_CODE:
                //用户点击了取消
                if(data == null){
                    return;
                }else{
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        //获得拍的照片
                        Bitmap bm = extras.getParcelable("data");
                        //将Bitmap转化为uri
                        Uri uri = saveBitmap(bm, "temp");
                        //启动图像裁剪
                        startImageZoom(uri);
                        //File file = new File(uri);
                    }
                }
                break;
            case GALLERY_CODE:
                if (data == null){
                    return;
                }else{
                    //用户从图库选择图片后会返回所选图片的Uri
                    Uri uri;
                    //获取到用户所选图片的Uri
                    uri = data.getData();
                    //返回的Uri为content类型的Uri,不能进行复制等操作,需要转换为文件Uri
                    uri = convertUri(uri);
                    startImageZoom(uri);
                }
                break;
            case CROP_CODE:
                if (data == null){
                    return;
                }else{
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        //获取到裁剪后的图像
                        Bitmap bm = extras.getParcelable("data");
                        //mImageView.setImageBitmap(bm);
                        Intent intent = new Intent();
                        intent.putExtra("bitmap",bm);
                        setResult(RESULT_OK,intent);
                        this.finish();
                    }
                }
                break;
            default:
                break;
        }
    }


    /**
     * 将Bitmap写入SD卡中的一个文件中,并返回写入文件的Uri
     * @param bm
     * @param dirPath
     * @return
     */
    private Uri saveBitmap(Bitmap bm, String dirPath) {
        //新建文件夹用于存放裁剪后的图片
        File tmpDir = new File(filePath+dirPath);
        if (!tmpDir.exists()){
            tmpDir.mkdir();
        }

        //新建文件存储裁剪后的图片
        File img = new File(tmpDir.getAbsolutePath() + "/avator.png");
        try {
            //打开文件输出流
            FileOutputStream fos = new FileOutputStream(img);
            //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            //刷新输出流
            fos.flush();
            //关闭输出流
            fos.close();
            //返回File类型的Uri
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过Uri传递图像信息以供裁剪
     * @param uri
     */
    private void startImageZoom(Uri uri){
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_CODE);
    }

    private void showSnackBar(String msg){
        /**
         * 可以通过代码去改变Action的字体颜色：Snackbar setActionTextColor (int color)；
         * 设置Action行为事件，使用的方法是
         * public Snackbar setAction (CharSequence text, View.OnClickListener listener);
         * Action的字体颜色默认使用系
         */
//        final Snackbar snackbar = Snackbar.make(upload,msg,5000);
//        snackbar.show();
//        snackbar.setAction("取消",new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                snackbar.dismiss();
//            }
//        });
    }


    /**
     * 将content类型的Uri转化为文件类型的Uri
     * @param uri
     * @return
     */
    private Uri convertUri(Uri uri){
        InputStream is;
        try {
            //Uri ----> InputStream
            is = getContentResolver().openInputStream(uri);
            //InputStream ----> Bitmap
            Bitmap bm = BitmapFactory.decodeStream(is);
            //关闭流
            is.close();
            return saveBitmap(bm, "temp");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
