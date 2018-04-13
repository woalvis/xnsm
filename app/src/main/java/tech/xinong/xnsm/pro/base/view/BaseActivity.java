package tech.xinong.xnsm.pro.base.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.vondear.rxtools.view.dialog.RxDialogScaleView;

import java.io.File;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.Response;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.mvp.view.impl.MvpActivity;
import tech.xinong.xnsm.pro.base.Util.ProperTies;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.pro.user.model.Customer;
import tech.xinong.xnsm.util.ActivityCollector;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.InjectUtils;
import tech.xinong.xnsm.views.BaseDialog;
import tech.xinong.xnsm.views.BaseEditDialog;

/**
 * Created by xiao on 2016/11/7.
 */

public abstract class BaseActivity<p extends BasePresenter> extends MvpActivity<p> {

    public final String TAG = this.getClass().getSimpleName();
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor editor;
    public Context mContext;
    private int i = -1;
    public Toolbar toolbar;
    public ImageView back;
    private Dialog progressDialog;
    public Intent intent;
    public TextView title;
    private CountDownTimer timer;
    private String favString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences(XnsConstant.SP_NAME, MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        mContext = this;
        intent = getIntent();
        ActivityCollector.addActivity(this);
        InjectUtils.inject(this);
        initWidget();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public p bindPresenter() {
        return null;
    }


    /**
     * 初始化控件
     */
    public void initWidget() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar!=null) {
            toolbar.setTitle("");
            title = findViewById(R.id.tv_title);
            back = findViewById(R.id.back);
            title.setText(setToolBarTitle());
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.this.finish();
                }
            });
            setSupportActionBar(toolbar);
        }
    }

    public String setToolBarTitle(){
        return "";
    }

    public void setToolBarTitle(String title){
        this.title.setText(title);
    }

    /**
     * 初始化数据
     */
    public abstract void initData();


    public void skipActivity(Class<? extends Activity> cls) {
        Intent intent = new Intent(mContext, cls);
        mContext.startActivity(intent);
        this.finish();
    }

    public void skipActivity(Class<? extends Activity> cls,boolean isFinish) {
        Intent intent = new Intent(mContext, cls);
        mContext.startActivity(intent);
        if(isFinish){
            this.finish();
        }
    }



    public void setTextViewText(TextView tv, String text) {
        tv.setText(text);
    }


    public void widgetClick(View view) {
    }

    public void showProgress() {
        progressDialog = new Dialog(this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.loading);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);
        TextView msg = progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("卖力加载中");
        progressDialog.show();
    }


    public void showProgress(String text) {
        progressDialog = new Dialog(this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.loading);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);
        TextView msg = progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText(text);
        progressDialog.show();
    }

    public void cancelProgress() {
        if (progressDialog!=null)
        progressDialog.dismiss();
    }


    public void showSnackbar() {

    }


    public void twoButtonDialog(String title,
                                String contentText,
                                String confirmText,
                                String cancelText,
                                View.OnClickListener confirmListener,
                                View.OnClickListener cancelListener){



        BaseDialog.getInstance().setConfirmText(confirmText)
                .setTitle(title)
                .setCancelText(cancelText)
                .setCancelListener(cancelListener)
                .setContentText(contentText)
                .setConfirmListener(confirmListener).show(getSupportFragmentManager(),"base");






//        new CircleDialog.Builder(this)
//                .setCanceledOnTouchOutside(false)
//                .setCancelable(false)
//                .setTitle(title)
//                .setText(contentText)
//                .setTitleColor(getResources().getColor(R.color.primaryGreen))
//                .setTitleColor(getResources().getColor(R.color.primaryGreen))
//                .setTextColor(getResources().getColor(R.color.primaryGreen))
//                .setNegative(cancelText, cancelListener)
//                .setPositive(confirmText,confirmListener)
//                .show();


//           pDialog = new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
//                    .setTitleText(title)
//                    .setContentText(contentText)
//                    .setCancelText(cancelText)
//                    .setConfirmText(confirmText)
//                    .showCancelButton(true)
//                    .setCancelClickListener(cancelListener)
//                    .setConfirmClickListener(confirmListener);
//           pDialog.show();

    }
    public void showDialogButton(String title, String content,AdapterView.OnItemClickListener listener){
        final String[] items = content.split(",");
        new CircleDialog.Builder(this)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        //增加弹出动画
                        params.animStyle = R.style.dialogWindowAnim;
                    }
                })
                .setTitle(title)
                .setTitleColor(Color.BLUE)
                .setItems(items, listener)
                .setNegative("取消", null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        //取消按钮字体颜色
                        params.textColor = Color.RED;
                    }
                })
                .show();
    }
   public String getCustmerId(){
        return mSharedPreferences.getString(XnsConstant.CUSTOMERID,"");
    }

    public void updateUserInfo(final OnUpdateUserInfoComplete complete){
        XinongHttpCommend.getInstance(mContext).getCurrentInfo(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                Customer customer = JSONObject.parseObject(result,Customer.class);
                editor.putString(XnsConstant.CUSTOMERID,customer.getId());
                editor.putString(XnsConstant.LOGIN_NAME,customer.getLoginName());
                editor.putString(XnsConstant.HEADIMG,customer.getHeadImg());
                editor.putString(XnsConstant.COVER_IMG,customer.getCoverImg());
                editor.putString(XnsConstant.FULL_NAME,customer.getFullName());
                editor.putString(XnsConstant.DISTRICT,customer.getDistrict());
                editor.putString(XnsConstant.PROVINCE,customer.getProvince());
                editor.putString(XnsConstant.TAGS,customer.getTags());
                editor.putString(XnsConstant.STREET,customer.getStreet());
                editor.commit();
                complete.onComplete(customer);
            }
        });
    }


   public int getColorById(int colorId){
        return getResources().getColor(colorId);
   }
   public interface OnUpdateUserInfoComplete{
        void onComplete(Customer customer);
   }

   public String[] getFavNames(){
       String favnames = mSharedPreferences.getString(XnsConstant.FAVNAME,"");
       if (TextUtils.isEmpty(favnames)){
           return null;
       }else {
           return favnames.split(",");
       }
   }
    public String[] getConfigNames(){
        String favnames = mSharedPreferences.getString(XnsConstant.FAVS,"");
        if (TextUtils.isEmpty(favnames)){
            return null;
        }else {
            return favnames.split(",");
        }
    }

    public String[] getConfigIds(){
        String favnames = mSharedPreferences.getString("ids","");
        return favnames.split(",");

    }



   public boolean checkTextView(TextView textView){
       if (textView==null){
           return true;
       }
       return textView.getText().toString().trim().equals("")?true:false;
   }

   public void showImg(Bitmap bitmap){
       RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(mContext);
       rxDialogScaleView.setImageBitmap(bitmap);
       rxDialogScaleView.show();
   }

    public void showImg(String url){

        OkGo.get(ImageUtil.getImgUrl(url)).execute(new FileCallback() {
            @Override
            public void onSuccess(File file, Call call, Response response) {
                RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(mContext);
                rxDialogScaleView.setImagePath(file.getAbsolutePath());
                rxDialogScaleView.show();
            }
        });


    }



    public void setTimer(final TextView tv){
        timer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv.setText(millisUntilFinished/1000+"s后重新发送");
                tv.setBackgroundResource(R.drawable.tv_grey_bg);
                tv.setTextColor(mContext.getResources().getColor(R.color.black_404040));
            }

            @Override
            public void onFinish() {
                tv.setBackgroundResource(R.drawable.button_shape_orange);
                tv.setText("重新发送验证码");
                tv.setTextColor(mContext.getResources().getColor(R.color.white));
                tv.setPadding(10,10,10,10);
                tv.setClickable(true);
            }
        };
        timer.start();
    }

    public boolean isLogin(){
        return !TextUtils.isEmpty(mSharedPreferences.getString(XnsConstant.USER_NAME,""));
    }

    public String[] getFavs() {
        Properties proper = ProperTies.getProperties(this);
        favString = proper.getProperty(XnsConstant.FAVS);
        if (!TextUtils.isEmpty(favString))
            favString = proper.getProperty(XnsConstant.FAVS).replace("，", ",");
        if (TextUtils.isEmpty(favString)) {
            return null;
        } else {
            mSharedPreferences = getSharedPreferences(XnsConstant.SP_NAME, MODE_PRIVATE);
            editor = mSharedPreferences.edit();
            editor.putString(XnsConstant.FAVS, favString);
            editor.commit();
            return favString.split(",");
        }
    }


    public void editDialog(String title, String confirmText, BaseEditDialog.OnEditClickListener listener){
        BaseEditDialog.getInstance().setConfirmText(confirmText)
                .setTitle(title)
                .setConfirmListener(listener).show(getSupportFragmentManager(),"base");
    }
}