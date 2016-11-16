package tech.xinong.xnsm.mvp.presenter;

import tech.xinong.xnsm.mvp.view.MvpView;

/**
 * P层
 * Created by xiao on 2016/11/6.
 */

public interface MvpPresenter<V extends MvpView>{
    //绑定view
    void attachView(V view);

    //解除绑定view
    void detachView();
}
