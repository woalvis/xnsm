package tech.xinong.xnsm.pro.base.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mylhyl.circledialog.CircleDialog;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.mvp.view.MvpView;
import tech.xinong.xnsm.mvp.view.impl.MvpFragment;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.util.L;
import tech.xinong.xnsm.util.XnsConstant;

/**
 * Created by xiao on 2016/11/7.
 */

public abstract class BaseFragment<P extends BasePresenter,V extends MvpView> extends MvpFragment<P,V> {

    public BaseActivity mContext;
    //我们自己的fragment需要缓存视图
    private View contentView;//缓存视图
    private boolean isInit;
    private Toolbar toolbar;
    private ImageView back;
    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor editor;
    private boolean hasBack = false;
    private View.OnClickListener backListener;
    public final String TAG = this.getClass().getSimpleName();

    @Override
    public P bindPresenter() {
        return null;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L.e(TAG,"onActivityCreated");
        mContext = (BaseActivity) getActivity();
        mSharedPreferences = mContext.getSharedPreferences(XnsConstant.SP_NAME, mContext.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        initData();
    }

    /**
     * 这个生命周期getActivity还是空的，所以所有关于mContext的操作都不要放在这里
     * 要放在initData里面
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        L.e(TAG,"onCreateView");
        if (contentView==null){
            contentView = inflater.inflate(bindLayoutId(),container,false);
            initContentView(contentView);
        }

        //判断Fragment对应的Activity是否存在这个视图
        ViewGroup parent = (ViewGroup) contentView.getParent();
        if (parent!=null){
            //如果存在，那么我们就把它干掉，重写添加，这样的方式我们就可以缓存视图
            parent.removeView(contentView);
        }
        initToolbar(contentView);
        return contentView;
    }

    protected  void initToolbar(View contentView){
        toolbar = contentView.findViewById(R.id.toolbar);
        if (toolbar!=null) {
            toolbar.setTitle("");
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            TextView title = contentView.findViewById(R.id.tv_title);
            back = contentView.findViewById(R.id.back);
            if(title!=null)
            title.setText(setToolBarTitle());
            ((BaseActivity)getActivity()).setSupportActionBar(toolbar);
            if (hasBack){
                back.setVisibility(View.VISIBLE);
            }else {
                back.setVisibility(View.GONE);
            }
            if (backListener!=null){
                back.setOnClickListener(backListener);
            }
        }
    }

    public void setHasBack(boolean hasBack){
        this.hasBack = hasBack;
    }

    public void setBackListener(View.OnClickListener backListener){
        this.backListener = backListener;
    }

    public String setToolBarTitle() {
        return "";
    }

    public View getContentView(){return contentView;}

    protected void resetContentView(View contentView){
        ViewGroup viewGroup = (ViewGroup) contentView;
        viewGroup.removeAllViews();
    }

    protected void loadLayout(int layoutId, View v){
        ViewGroup viewGroup = (ViewGroup) v;
        View view = LayoutInflater.from(getContext()).inflate(layoutId,viewGroup,false);
        //判断Fragment对应的Activity是否存在这个视图
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null){
            //如果存在,那么我就干掉,重写添加,这样的方式我们就可以缓存视图
            parent.removeView(view);
        }
        viewGroup.addView(view);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    protected abstract int bindLayoutId();


    @Override
    public V bindView() {
        return null;
    }

    public void initNavigation(View contentView){

    }

    public void initData() {

    }

    protected abstract void initContentView(View contentView);


    protected void skipActivity(Class<? extends Activity> cls){
        Intent intent = new Intent(getActivity(),cls);
        mContext.startActivity(intent);
    }


    public void showProgress(String title) {
        mContext.showProgress(title);
    }

    public void showProgress() {
        mContext.showProgress();
    }


    public void cancelProgress() {
        ((BaseActivity)mContext).cancelProgress();
    }

    public String[] getFavNames(){
       return mContext.getFavNames();
    }

    public String getCustomerId(){
        return mContext.mSharedPreferences.getString(XnsConstant.CUSTOMERID,"");
    }

    public boolean isLogin(){
        return !TextUtils.isEmpty(mSharedPreferences.getString(XnsConstant.USER_NAME,""));
    }

    public void twoButtonDialog(String title,
                                String contentText,
                                String confirmText,
                                String cancelText,
                                View.OnClickListener confirmListener,
                                View.OnClickListener cancelListener){

        new CircleDialog.Builder(mContext)
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .setTitle(title)
                .setText(contentText)
                .setNegative(cancelText, cancelListener)
                .setPositive(confirmText,confirmListener)
                .show();
    }
}

