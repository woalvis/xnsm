package tech.xinong.xnsm.pro.pay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;

@ContentView(R.layout.activity_bank_pay)
public class BankPayActivity extends BaseActivity {
    @ViewInject(R.id.tv_bank_pay)
    private TextView tv_bank_pay;
    @ViewInject(R.id.tv_content)
    private TextView tv_content;
    @ViewInject(R.id.tv_pay_count)
    private TextView tv_pay_count;
    @ViewInject(R.id.tv_order_no)
    private TextView tv_order_no;
    @ViewInject(R.id.im_pay)
    private ImageView im_pay;
    private String orderId;
    private String orderNo;
    private String orderTitle;
    private BigDecimal totalPrice;
    private Uri resultUri;
    private Map<String,Object> map;

    @Override
    public void initData() {
        orderId = intent.getStringExtra("orderId");
        orderNo = intent.getStringExtra("orderNo");
        orderTitle = intent.getStringExtra("orderTitle");
        totalPrice = (BigDecimal)intent.getSerializableExtra("totalPrice");
        tv_content.setText(orderTitle);
        tv_order_no.setText(orderNo);
        tv_pay_count.setText(totalPrice.doubleValue()+"元");
        tv_bank_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启相机或者图库选择
                checkPermission();
            }
        });
    }

    @Override
    public String setToolBarTitle() {
        return "银行卡付款";
    }


    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
            int checkStoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(BankPayActivity.this,new String[]{Manifest.permission.CAMERA},222);
                if (checkStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BankPayActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                    return;
                }
                return;
            }else if (checkStoragePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(BankPayActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},222);
                return;
            }else{
                openCamera();//调用具体方法
            }
        } else {
            openCamera();//调用具体方法
        }

    }

    private void openCamera() {

        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(this, TITLE);
        dialogChooseImage.show();
    }


    private void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(mContext.getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(mContext, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(mContext, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        Intent intent = new Intent(mContext,UCropActivity.class);
        Bundle bundle = new Bundle();
        bundle.putAll(options.getOptionBundle());

        bundle.putParcelable(UCrop.EXTRA_INPUT_URI, uri);
        bundle.putParcelable(UCrop.EXTRA_OUTPUT_URI, destinationUri);
        bundle.putFloat(UCrop.EXTRA_ASPECT_RATIO_X, 1);
        bundle.putFloat(UCrop.EXTRA_ASPECT_RATIO_Y, 1);
        bundle.putFloat(UCrop.EXTRA_MAX_SIZE_X, 1000);
        bundle.putFloat(UCrop.EXTRA_MAX_SIZE_Y, 1000);

        intent.putExtras(bundle);
        startActivityForResult(intent,69);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == this.RESULT_OK) {
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == this.RESULT_OK) {
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
                        placeholder(R.mipmap.user_bg).
                        priority(Priority.LOW).
                        error(R.mipmap.user_bg).
                        fallback(R.mipmap.user_bg).
                        into(im_pay);
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == this.RESULT_OK) {
                    resultUri = UCrop.getOutput(data);
                    File file = roadImageView(resultUri, im_pay);
                    updateUserBg(file);
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
    }


    private void updateUserBg(File file){
        map.put("coverImg",file);

        XinongHttpCommend.getInstance(mContext).updateUserInfo(mSharedPreferences.getString(XnsConstant.CUSTOMERID, ""), map,
                new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        T.showShort(mContext,"上传成功");
                    }
                });
    }

    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, ImageView imageView) {
        im_pay.setImageURI(uri);
        return (new File(RxPhotoTool.getImageAbsolutePath(mContext, uri)));
    }

}
