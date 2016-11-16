package tech.xinong.xnsm.mvp.view.impl;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import tech.xinong.xnsm.mvp.presenter.MvpPresenter;
import tech.xinong.xnsm.mvp.view.MvpView;

/**
 * Created by xiao on 2016/11/6.
 */

public abstract class MvpFragment <P extends MvpPresenter,V extends MvpView> extends Fragment implements MvpView {

    private P presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.presenter = bindPresenter();
        if (this.presenter!=null) this.presenter.attachView(bindView());
    }

    public abstract P bindPresenter();

    public abstract V bindView();

    public P getPresenter(){
        return this.presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.presenter!=null){
            this.presenter.detachView();
            this.presenter = null;
        }
    }


}
