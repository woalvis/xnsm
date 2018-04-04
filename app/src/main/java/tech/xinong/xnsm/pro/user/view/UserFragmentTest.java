
package tech.xinong.xnsm.pro.user.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
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
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.home.view.FollowActivity;
import tech.xinong.xnsm.pro.user.model.Customer;
import tech.xinong.xnsm.pro.wallet.MyWalletActivity;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;

/**
 * Created by xiao on 2017/10/19.
 */

public class UserFragmentTest extends BaseFragment implements View.OnClickListener {
    private TextView tv_setting;
    private SimpleDraweeView im_head;
    private LinearLayout user_my_publish_buy_layout;
    private LinearLayout user_my_follow_layout;
    private LinearLayout user_identification_layout;
//    private LinearLayout user_my_setting_layout;
    private LinearLayout user_my_wallet_layout;
    private LinearLayout user_online_layout;
    private TextView logout;//退出登录
    private LinearLayout user_my_order_layout;
    private SimpleDraweeView im_unlogin;
    private TextView tv_login;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private SimpleDraweeView user_bg;
    private Uri resultUri;
    private Map<String, Object> map;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String userName;

    private boolean isLogin;
    public static final int TAKE_PHOTO = 1;

    public static final int CROP_PHOTO = 2;


    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_user_test;
    }


    @Override
    public void initData() {

    }

    @Override
    protected void initContentView(View contentView) {
        map = new HashMap<>();
        sp = getActivity().getSharedPreferences(XnsConstant.SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        userName = sp.getString(XnsConstant.USER_NAME, "");
        Toolbar toolbar = contentView.findViewById(R.id.bar);
        user_my_publish_buy_layout = contentView.findViewById(R.id.user_my_publish_buy_layout);
        user_my_wallet_layout = contentView.findViewById(R.id.user_my_wallet_layout);
        im_unlogin = contentView.findViewById(R.id.im_unlogin);
        tv_login = contentView.findViewById(R.id.tv_login);
        user_online_layout = contentView.findViewById(R.id.user_online_layout);
        user_my_follow_layout = contentView.findViewById(R.id.user_my_follow_layout);
        user_identification_layout = contentView.findViewById(R.id.user_identification_layout);
//        user_my_setting_layout = contentView.findViewById(R.id.user_my_setting_layout);
        tv_login.setVisibility(View.GONE);
        user_bg = contentView.findViewById(R.id.user_bg);
        collapsingToolbarLayout = contentView.findViewById(R.id.collapsing_toolbar);
        if (!TextUtils.isEmpty(userName)){
            collapsingToolbarLayout.setTitle(userName);
        }

        tv_setting = contentView.findViewById(R.id.tv_setting);

        im_head = contentView.findViewById(R.id.im_head);

        logout = contentView.findViewById(R.id.logout);
        user_my_order_layout = contentView.findViewById(R.id.user_my_order_layout);
        logout.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        im_head.setOnClickListener(this);
        user_my_publish_buy_layout.setOnClickListener(this);
        user_my_order_layout.setOnClickListener(this);
        user_bg.setOnClickListener(this);
        im_unlogin.setOnClickListener(this);
        user_my_follow_layout.setOnClickListener(this);
        user_identification_layout.setOnClickListener(this);
//        user_my_setting_layout.setOnClickListener(this);
        user_my_wallet_layout.setOnClickListener(this);
        user_online_layout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting:
                break;
            case R.id.im_head:
                if (isLogin()){
                    skipActivity(EditMyInfoActivity.class);
                }else {
                    skipActivity(LoginActivity.class);
                }

                break;
            case R.id.user_my_publish_buy_layout:
                if (isLogin()){
                    skipActivity(MyPublishActivity.class);
                }else {
                    skipActivity(LoginActivity.class);
                }

                break;
            case R.id.logout:
                editor.putString(XnsConstant.USER_NAME, "");
                editor.putString(XnsConstant.PASSWORD, "");
                editor.putString(XnsConstant.HEADIMG, "");
                editor.putString(XnsConstant.CUSTOMERID, "");
                editor.putString(XnsConstant.LOGIN_NAME, "");
                editor.putString(XnsConstant.TOKEN,"");
                editor.putString(XnsConstant.COVER_IMG,"");
                editor.putString(XnsConstant.FAvID,"");
                editor.putString(XnsConstant.DISTRICT,"");
                editor.putString(XnsConstant.FULL_NAME,"");
                editor.putString(XnsConstant.STREET,"");
                editor.putString(XnsConstant.PROVINCE,"");
                editor.putString(XnsConstant.FAVNAME,"");
                HttpHeaders headers = new HttpHeaders();
                headers.put(HttpConstant.HTTP_HEADER_TOKEN, "");
                //将token放入到header
                OkGo.getInstance().addCommonHeaders(headers);
                editor.commit();
                skipActivity(LoginActivity.class);
                break;
            case R.id.user_my_order_layout:
                if (isLogin()){
                    skipActivity(MyOrdersActivity.class);
                }else {
                    skipActivity(LoginActivity.class);
                }

                break;

            case R.id.im_unlogin:
                if (isLogin()){
                    skipActivity(EditMyInfoActivity.class);
                }else {
                    skipActivity(LoginActivity.class);
                }

                break;
//            case R.id.user_my_setting_layout:
//                Customer customer = null;
//                customer.getHeadImg();
     //           break;
            case R.id.user_bg:

                if (isLogin()){
                    checkPermission();
                }else {
                    skipActivity(LoginActivity.class);
                }


                break;
                case R.id.user_my_follow_layout:
                    if (isLogin()){
                        skipActivity(FollowActivity.class);
                    }else {
                        skipActivity(LoginActivity.class);
                    }


                    break;

            case R.id.user_identification_layout:
//                skipActivity(PersonalVerifyActivity.class);
                if (isLogin()){
                    Intent intent = new Intent(mContext,VerificationActivity.class);
                    startActivity(intent);
                }else {
                    skipActivity(LoginActivity.class);
                }



                break;
            case R.id.user_my_wallet_layout:
                if (isLogin()){
                    Intent intentWallet = new Intent(mContext,MyWalletActivity.class);
                    startActivity(intentWallet);
                }else {
                    skipActivity(LoginActivity.class);
                }

                break;
            case R.id.user_online_layout:
                T.showShort(mContext,"开发中。。。。");
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == mContext.RESULT_OK) {
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == mContext.RESULT_OK) {
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
                        into(user_bg);
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == mContext.RESULT_OK) {
                    resultUri = UCrop.getOutput(data);
                    File file = roadImageView(resultUri, im_head);
                    updateUserBg(file);
//                    map.put("headImg",file);
//                    XinongHttpCommend.getInstance(mContext).updateUserInfo(customer.getId(), map,
//                            new AbsXnHttpCallback(mContext) {
//                                @Override
//                                public void onSuccess(String info, String result) {
//                                    T.showShort(mContext,"上传成功");
//                                }
//                            });
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();

        String userNameStr = sp.getString(XnsConstant.USER_NAME, "");

        if (!TextUtils.isEmpty(userNameStr)){
            mContext.updateUserInfo(new BaseActivity.OnUpdateUserInfoComplete() {
                @Override
                public void onComplete(Customer customer) {
                    String headImgStr = customer.getHeadImg();
                    String coverImgStr = customer.getCoverImg();
                    collapsingToolbarLayout.setTitle(customer.getFullName());
                    if (!TextUtils.isEmpty(headImgStr)) {
                        String url = ImageUtil.getImgUrl(headImgStr);
                        im_unlogin.setImageURI(url);
                    }else {
                        im_unlogin.setImageURI((new Uri.Builder()).scheme("res").path(String.valueOf(R.mipmap.avatar_img)).build());
                        im_unlogin.setBackgroundColor(mContext.getColorById(R.color.translate));
                    }
                    if (!TextUtils.isEmpty(coverImgStr)) {
                        String url = ImageUtil.getImgUrl(coverImgStr);
                        user_bg.setImageURI(url);
                    }else {
                        user_bg.setImageURI((new Uri.Builder()).scheme("res").path(String.valueOf(R.mipmap.user_bg)).build());
                    }
                }
            });

        }else {
            im_unlogin.setImageResource(R.mipmap.avatar_img);
            user_bg.setImageResource(R.mipmap.user_bg);
            collapsingToolbarLayout.setTitle("");
        }

    }

    private void updateUserBg(File file){
        map.put("coverImg",file);

        XinongHttpCommend.getInstance(mContext).updateUserInfo(mContext.mSharedPreferences.getString(XnsConstant.CUSTOMERID, ""), map,
                new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        T.showShort(mContext,"上传成功");
                        mContext.updateUserInfo(new BaseActivity.OnUpdateUserInfoComplete() {
                            @Override
                            public void onComplete(Customer customer) {
                                String headImgStr = customer.getHeadImg();
                                String coverImgStr = customer.getCoverImg();
                                if (!TextUtils.isEmpty(headImgStr)) {
                                    String url = ImageUtil.getImgUrl(headImgStr);
                                    im_unlogin.setImageURI(url);
                                }
                                if (!TextUtils.isEmpty(coverImgStr)) {
                                    String url = ImageUtil.getImgUrl(coverImgStr);
                                    user_bg.setImageURI(url);
                                }
                            }
                        });
                    }
                });
    }



    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
            int checkStoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(mContext,new String[]{Manifest.permission.CAMERA},222);
                if (checkStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                    return;
                }
                return;
            }else if (checkStoragePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(mContext,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},222);
                return;
            }else{
                openCamera();//调用具体方法
            }
        } else {
            openCamera();//调用具体方法
        }

    }


    private String getUserName(){
        return sp.getString(XnsConstant.USER_NAME, "");
    }

    private void openCamera(){

        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(this, TITLE);
        dialogChooseImage.show();

//        File outputImage = new File(mContext.getExternalCacheDir(), "user_bg.jpg");
//        try {
//            if (outputImage.exists()) {
//                outputImage.delete();
//            } else {
//                outputImage.createNewFile();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (Build.VERSION.SDK_INT >= 24) {
//            resultUri = FileProvider.getUriForFile(mContext,
//                    mContext.getPackageName() + ".fileprovider",
//                    outputImage);
//        } else {
//            resultUri = Uri.fromFile(outputImage);
//        }
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri);
//        startActivityForResult(intent, TAKE_PHOTO);
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
        options.setToolbarColor(ActivityCompat.getColor(getActivity(), R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(getActivity(), R.color.colorPrimaryDark));

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
//
//        UCrop.of(uri, destinationUri)
//                .withAspectRatio(1, 1)
//                .withMaxResultSize(1000, 1000)
//                .withOptions(options)
//                .start(getActivity());

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
        user_bg.setImageURI(uri);
        return (new File(RxPhotoTool.getImageAbsolutePath(getActivity(), uri)));
    }

}
