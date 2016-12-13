package tech.xinong.xnsm.pro.base.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import tech.xinong.xnsm.mvp.view.impl.MvpActivity;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.util.ActivityCollector;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.InjectUtils;

/**
 * Created by xiao on 2016/11/7.
 */

public abstract class BaseActivity <p extends BasePresenter> extends MvpActivity<p> {

    public final String TAG = this.getClass().getSimpleName();
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor editor;
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences(XnsConstant.SP_NAME, MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        mContext = this;
        ActivityCollector.addActivity(this);
        InjectUtils.inject(this);
        initWidget();
        initData();

    }


    @Override
    public p bindPresenter() {
        return null;
    }

    /**
     * 初始化控件
     */
    public void initWidget(){

    }


    /**
     * 初始化数据
     */
    public void initData(){

    }


    public void skipActivity(Class<? extends Activity> cls){
        Intent intent = new Intent(mContext,cls);
        mContext.startActivity(intent);
        this.finish();
    }


    public void setTextViewText(TextView tv,String text){
        tv.setText(text);
    }

}
