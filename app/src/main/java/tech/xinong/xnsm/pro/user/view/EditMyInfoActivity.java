package tech.xinong.xnsm.pro.user.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.user.model.Customer;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;


@ContentView(R.layout.activity_edit_my_info)
public class EditMyInfoActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.user_tel)
    private TextView user_tel;//登录名，电话号码
    @ViewInject(R.id.et_username)
    private EditText et_username;//用户全名
    @ViewInject(R.id.edit_profile_layout)
    private LinearLayout edit_profile_layout;
    @ViewInject(R.id.im_head)
    private SimpleDraweeView im_head;//头像
    @ViewInject(R.id.bt_save)
    private TextView bt_save;
    @ViewInject(R.id.layout_address)
    private LinearLayout layout_address;

    private Customer customer;
    private File headerFile;
    private String userName;
    private Uri resultUri;
    private Map<String,Object>map;



    @Override
    public String setToolBarTitle() {
        return "个人信息";
    }


    private void saveCustomer() {
        Map<String,Object> params = isChange();
        XinongHttpCommend.getInstance(mContext).updateUserInfo(
                customer.getId(),
                params,
                new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        T.showShort(mContext,"保存成功");
                    }
                });
    }


    @Override
    public void initData() {
        bt_save.setOnClickListener(this);
        im_head.setOnClickListener(this);
        layout_address.setOnClickListener(this);
        map = new HashMap<>();
        Resources r = mContext.getResources();
        resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.mipmap.avatar_img) + "/"
                + r.getResourceTypeName(R.mipmap.avatar_img) + "/"
                + r.getResourceEntryName(R.mipmap.avatar_img));


        XinongHttpCommend.getInstance(mContext).currentInfo(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                customer = JSON.parseObject(result,Customer.class);
                et_username.setHint("请填写您的全名");
                user_tel.setText(customer.getLoginName());

                if(!TextUtils.isEmpty(customer.getFullName())){
                    et_username.setText(customer.getFullName());
                    userName = customer.getFullName();
                }
                if (customer.getHeadImg()!=null){
                    im_head.setImageURI(ImageUtil.getImgUrl(customer.getHeadImg()));
                }
            }
        });
    }



    private Map<String,Object> isChange(){
        Map<String,Object> parmas = new HashMap<>();

        if (TextUtils.isEmpty(customer.getFullName())){
            if (!TextUtils.isEmpty(et_username.getText().toString()))
            parmas.put("fullName",et_username.getText().toString());
        }else {
            if(!customer.getFullName().equals(et_username.getText().toString()))
            parmas.put("fullName",et_username.getText().toString());
        }
        return parmas;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_address:
                skipActivity(EditMyAddressActivity.class,false);
                break;
            case R.id.bt_save:
                saveCustomer();
                break;
            case R.id.im_head:
                checkPermission();
                break;
        }
    }

    private void changeHeader() {
        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(this, TITLE);
        dialogChooseImage.show();
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
            int checkStoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EditMyInfoActivity.this,new String[]{Manifest.permission.CAMERA},222);
                if (checkStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditMyInfoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                    return;
                }
                return;
            }else if (checkStoragePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(EditMyInfoActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},222);
                return;
            } else{
                changeHeader();//调用具体方法
            }
        } else {
            changeHeader();//调用具体方法
        }

    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
//                    RxPhotoTool.cropImage(ActivityUser.this, );// 裁剪图片
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                   /* data.getExtras().get("data");*/
//                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }

                break;
            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
                Glide.with(mContext).
                        load(RxPhotoTool.cropImageUri).
                        diskCacheStrategy(DiskCacheStrategy.RESULT).
                        bitmapTransform(new CropCircleTransformation(mContext)).
                        thumbnail(0.5f).
                        placeholder(R.mipmap.avatar_img).
                        priority(Priority.LOW).
                        error(R.mipmap.avatar_img).
                        fallback(R.mipmap.avatar_img).
                        into(im_head);
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    resultUri = UCrop.getOutput(data);
                    File file = roadImageView(resultUri, im_head);
                    map.put("headImg",file);
                    XinongHttpCommend.getInstance(mContext).updateUserInfo(customer.getId(), map,
                            new AbsXnHttpCallback(mContext) {
                                @Override
                                public void onSuccess(String info, String result) {
                                    T.showShort(mContext,"上传成功");
                                }
                            });
                    RxSPTool.putContent(mContext, "AVATAR", resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR://UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);


//
//        if (resultCode == RESULT_OK) {
//
//            Uri uri = data.getParcelableExtra("uri");
//
//            if (requestCode == 1001) {
//                im_head.setImageURI(uri);
//                headerFile = getFileByUri(mContext,uri);
//                if (headerFile!=null){
//                    final Map<String,Object> map = new HashMap<>();
//                    map.put("headImg",headerFile);
//                    L.e(headerFile.getName()+":"+headerFile.length());
//                    XinongHttpCommend.getInstance(mContext).updateUserInfo(customer.getId(), headerFile,
//                            new AbsXnHttpCallback(mContext) {
//                                @Override
//                                public void onSuccess(String info, String result) {
//                                    T.showShort(mContext,"上传成功");
//                                }
//                            });
//                }
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
    }



    private void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
        //options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }


    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, ImageView imageView) {
//        Glide.with(mContext).
//                load(uri).
//                diskCacheStrategy(DiskCacheStrategy.RESULT).
//                bitmapTransform(new CropCircleTransformation(mContext)).
//                thumbnail(0.5f).
//                placeholder(im_head).
//                priority(Priority.LOW).
//                error(R.drawable.circle_elves_ball).
//                fallback(R.drawable.circle_elves_ball).
//                into(imageView);
        im_head.setImageURI(uri);
        return (new File(RxPhotoTool.getImageAbsolutePath(this, uri)));
    }



}
