package tech.xinong.xnsm.pro.base.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.xinong.xnsm.mvp.view.MvpView;
import tech.xinong.xnsm.mvp.view.impl.MvpFragment;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;

/**
 * Created by xiao on 2016/11/7.
 */

public abstract class BaseFragment<P extends BasePresenter,V extends MvpView> extends MvpFragment<P,V> {

    public Context mContext;

    @Override
    public P bindPresenter() {
        return null;
    }

    //我们自己的fragment需要缓存视图
    private View contentView;//缓存视图
    private boolean isInit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
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
        return contentView;
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
        if (!isInit){
            this.isInit = true;
            initData();
        }
    }

    protected abstract int bindLayoutId();


    @Override
    public V bindView() {
        return null;
    }

    private void initData() {

    }

    protected abstract void initContentView(View contentView);



}

