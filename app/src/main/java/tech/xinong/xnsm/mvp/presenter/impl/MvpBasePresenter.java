package tech.xinong.xnsm.mvp.presenter.impl;

import tech.xinong.xnsm.mvp.presenter.MvpPresenter;
import tech.xinong.xnsm.mvp.view.MvpView;

/**
 * Created by xiao on 2016/11/6.
 */

public abstract class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V>{
    private V view;

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public V getView(){
        return view;
    }
}
