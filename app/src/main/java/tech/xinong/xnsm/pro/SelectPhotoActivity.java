package tech.xinong.xnsm.pro;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;

/**
 * Created by xiao on 2017/11/3.
 */
@ContentView(R.layout.activity_select_photo)
public class SelectPhotoActivity extends BaseActivity implements View.OnClickListener{

    //用于展示选择的图片
    private ImageView mImageView;
    private Button tv_cancel;

    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;

    private String localTempImgDir = "local_temp";
    private String localTempImgFileName = "temp_img";
    private String localTempImgFileName2 = "temp_img2";

    private Uri uri1;
    private boolean isCamera;
    private static final int GET_IMAGE_VIA_CAMERA = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void initData() {

    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.photo_show);
        tv_cancel = (Button) findViewById(R.id.tv_cancel);
        Button chooseCamera = (Button) findViewById(R.id.choose_camera);
        chooseCamera.setOnClickListener(this);
        Button chooseGallery = (Button) findViewById(R.id.choose_gallery);
        chooseGallery.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_camera:
                //拍照选择
                testCamera();
                break;
            case R.id.choose_gallery:
                //从相册选取
                chooseFromGallery();
                break;
            case R.id.tv_cancel:
                cancel();
            default:
                break;
        }

    }



    private void cancel() {
        //Toast.makeText(this, "我被点击了", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 拍照选择图片
     */
    private void chooseFromCamera() {
        //构建隐式Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //调用系统相机
        startActivityForResult(intent, CAMERA_CODE);
    }


    /**
     * 拍照选择原图
     */
    private void testCamera(){
        //先验证手机是否有sdcard
        String status= Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED))
        {
            try {
                File dir=new File(Environment.getExternalStorageDirectory() + "/"+localTempImgDir);
                if(!dir.exists())dir.mkdirs();
                Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File file = null;
                file =new File(dir, localTempImgFileName);//localTempImgDir和localTempImageFileName是自己定义的名字
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                try {
                    startActivityForResult(intent, GET_IMAGE_VIA_CAMERA);
                }catch (Exception e){
                    T.showShort(mContext,"启动相机失败");
                    e.printStackTrace();
                    Log.e("aaaaa",e.toString());
                }
            } catch (ActivityNotFoundException e) {
// TODO Auto-generated catch block
                Toast.makeText(SelectPhotoActivity.this, "没有找到储存目录",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(SelectPhotoActivity.this, "没有储存卡",Toast.LENGTH_LONG).show();
        }

    }

    /**
     *
     */
    private void onActivityRe(){
        File f=new File(Environment.getExternalStorageDirectory()
                +"/"+localTempImgDir+"/"+localTempImgFileName);
        try {
            uri1 = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(),
                    f.getAbsolutePath(), null, null));
            //u就是拍摄获得的原始图片的uri，剩下的你想干神马坏事请便……
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 从相册选择图片
     */
    private void chooseFromGallery() {
        //构建一个内容选择的Intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //设置选择类型为图片类型
        intent.setType("image/*");
        //打开图片选择
        startActivityForResult(intent, GALLERY_CODE);
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
                    isCamera = true;
                    if (extras != null){
                        onActivityRe();
                        setResultByUriAndBitmap();
                    }
                }
                break;
            case GALLERY_CODE:
                if (data == null){
                    return;
                }else{
                    isCamera = false;
                    setResultByUriAndBitmap(data.getData());
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
                        // mImageView.setImageBitmap(bm);
                    }
                }
                break;
            case 1001:
                onActivityRe();
                setResultByUriAndBitmap();
                break;
            default:
                break;
        }
    }

    private void setResultByUriAndBitmap() {
        Intent i = new Intent();
        i.putExtra("uri",uri1);
        i.putExtra("isCamera",isCamera);
        setResult(RESULT_OK,i);
        finish();
    }
    private void  setResultByUriAndBitmap(Uri uri){
        Intent i = new Intent();
        i.putExtra("uri",uri);
        i.putExtra("isCamera",isCamera);
        setResult(RESULT_OK,i);
        finish();
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

    /**
     * 将Bitmap写入SD卡中的一个文件中,并返回写入文件的Uri
     * @param bm
     * @param dirPath
     * @return
     */
    private Uri saveBitmap(Bitmap bm, String dirPath) {
        //新建文件夹用于存放裁剪后的图片
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/" + dirPath);
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

    private void displayImage(String imagePath){

    }


}

