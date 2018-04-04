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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.user.model.VerificationReq;
import tech.xinong.xnsm.pro.user.model.VerificationReqModel;
import tech.xinong.xnsm.pro.user.model.VerificationReqStatus;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.RegexpUtils;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;

/**
 * Created by xiao on 2018/1/19.
 */
@ContentView(R.layout.activity_personal_verify)
public class PersonalVerifyActivity extends BaseActivity {
    @ViewInject(R.id.id_card_back)
    private SimpleDraweeView id_card_back;
    @ViewInject(R.id.id_card_front)
    private SimpleDraweeView id_card_front;
    @ViewInject(R.id.id_card_hand)
    private SimpleDraweeView id_card_hand;
    @ViewInject(R.id.et_id_card)
    private EditText et_id_card;
    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;
    @ViewInject(R.id.et_real_name)
    private EditText et_real_name;


    private static final int REQ_BACK = 1001;
    private static final int REQ_FRONT = 1002;
    private static final int REQ_HAND = 1003;
    private int opCode = 0;
    private Uri resultUri;
    private Map<String, File> files;
    private File fileBack;
    private File fileFront;
    private File fileHand;
    private VerificationReqModel verification;


    @Override
    public void initData() {

        verification= (VerificationReqModel) intent.getSerializableExtra("req");
        if (verification.getStatus() != VerificationReqStatus.UNCRETIFIED) {
            id_card_back.setImageURI(ImageUtil.getImgUrl(verification.getIdCardNegative()));
            id_card_front.setImageURI(ImageUtil.getImgUrl(verification.getIdCardPositive()));
            id_card_hand.setImageURI(ImageUtil.getImgUrl(verification.getHandHeldIdCard()));
            et_real_name.setText(verification.getRealName());
            et_id_card.setText(verification.getIdCardNo());
        }
        files = new HashMap<>();
        Resources r = mContext.getResources();
        resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.mipmap.avatar_img) + "/"
                + r.getResourceTypeName(R.mipmap.avatar_img) + "/"
                + r.getResourceEntryName(R.mipmap.avatar_img));
    }

    @Override
    public String setToolBarTitle() {
        return "个人认证";
    }


    @Override
    @OnClick({R.id.id_card_front, R.id.id_card_back, R.id.id_card_hand, R.id.tv_submit})
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.id_card_front:
                checkPermission(REQ_FRONT);
                opCode = REQ_FRONT;
                break;
            case R.id.id_card_back:
                checkPermission(REQ_BACK);
                opCode = REQ_BACK;
                break;
            case R.id.id_card_hand:
                checkPermission(REQ_HAND);
                opCode = REQ_HAND;
                break;
            case R.id.tv_submit:
                if (verification.getStatus() == VerificationReqStatus.UNCRETIFIED) {
                  if (check()) {
                        VerificationReq verificationReq = new VerificationReq();
                        verificationReq.setHandHeldIdCard(fileHand);
                        verificationReq.setIdCardPositive(fileBack);
                        verificationReq.setIdCardNegative(fileFront);
                        verificationReq.setIdCardNo(et_id_card.getText().toString().trim());
                        verificationReq.setRealName(et_real_name.getText().toString().trim());
                        showProgress("正在努力上传");
                        XinongHttpCommend.getInstance(mContext).verificationPerson(verificationReq,
                                new AbsXnHttpCallback(mContext) {
                                    @Override
                                    public void onSuccess(String info, String result) {
                                        cancelProgress();
                                        T.showShort(mContext, "上传成功，请等待审核");
                                    }
                                });
                    }
                }else {
                    if (checkEt()){
                        if (checkTextView(et_real_name)) {
                            T.showShort(mContext, "请填写真实姓名");
                            return;
                        }else {
                            VerificationReq verificationReq = new VerificationReq();
                            if (fileHand!=null&&fileHand.length()>0)
                                verificationReq.setHandHeldIdCard(fileHand);
                            if (fileBack!=null&&fileBack.length()>0)
                                verificationReq.setIdCardPositive(fileBack);
                            if (fileFront!=null&&fileFront.length()>0)
                                verificationReq.setIdCardNegative(fileFront);
                            verificationReq.setRealName(et_real_name.getText().toString().trim());
                            verificationReq.setIdCardNo(et_id_card.getText().toString().trim());

                            XinongHttpCommend.getInstance(mContext).updateVerification(
                                    verification.getId(), verificationReq, new AbsXnHttpCallback(mContext) {
                                        @Override
                                        public void onSuccess(String info, String result) {
                                            T.showShort(mContext,"提交修改成功");
                                        }
                                    }
                            );
                        }
                    }
                }

                break;
        }
    }

    private boolean check() {
        if (fileBack == null || fileBack.length() <= 0) {
            T.showShort(mContext, "请选择身份证人像页");
            return false;
        }
        if (fileFront == null || fileFront.length() <= 0) {
            T.showShort(mContext, "请选择身份证国徽页");
            return false;
        }
        if (fileBack == null || fileBack.length() <= 0) {
            T.showShort(mContext, "请选择手持身份证照片");
            return false;
        }

        if (!checkEt()) {
            return false;
        }
        if (checkTextView(et_real_name)) {
            T.showShort(mContext, "请填写真实姓名");
            return false;
        }
        return true;
    }


    private void checkPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
            int checkStoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(PersonalVerifyActivity.this,new String[]{Manifest.permission.CAMERA},222);
                if (checkStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PersonalVerifyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                    return;
                }
                return;
            }else if (checkStoragePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(PersonalVerifyActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},222);
                return;
            } else {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                    initUCrop(data.getData());

                    break;
                case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                    break;

                case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                    resultUri = UCrop.getOutput(data);

                    switch (opCode) {
                        case REQ_BACK:
                            fileBack = roadImageView(resultUri, id_card_back);
                            files.put("fileBack", fileBack);
                            break;
                        case REQ_FRONT:
                            fileFront = roadImageView(resultUri, id_card_front);
                            files.put("fileFront", fileFront);
                            break;
                        case REQ_HAND:
                            fileHand = roadImageView(resultUri, id_card_hand);
                            files.put("fileHand", fileHand);
                            break;
                    }

                    break;
                case REQ_BACK:
                    initUCrop(data.getData());
                    break;
                case REQ_FRONT:
                    initUCrop(data.getData());
                    break;
                case REQ_HAND:
                    initUCrop(data.getData());
                    break;
            }
        }
    }


    private void initUCrop(Uri uri) {
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

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }


    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, SimpleDraweeView imageView) {
        imageView.setImageURI(uri);
        return (new File(RxPhotoTool.getImageAbsolutePath(this, uri)));
    }


    private boolean checkEt() {
        String etString = et_id_card.getText().toString().trim();
        if (TextUtils.isEmpty(etString)) {
            Toast.makeText(mContext, "请填写身份证号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!RegexpUtils.pIdCard.matcher(etString).matches()) {
            Toast.makeText(mContext, "请填写正确的身份证号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
