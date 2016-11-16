package tech.xinong.xnsm.pro.base.presenter;

import android.content.Context;

import tech.xinong.xnsm.mvp.presenter.impl.MvpBasePresenter;
import tech.xinong.xnsm.mvp.view.MvpView;

/**
 * 自己项目的P层
 * Created by xiao on 2016/11/7.
 */

public class BasePresenter<V extends MvpView> extends MvpBasePresenter<V> {
    private Context context;
    public BasePresenter(Context context){
        this.context = context;
    }

    public Context getContext(){
        return context;
    }

    public interface OnUIThreadListener<T>{
        void onResult(T result,String errorMessage);
    }
}
