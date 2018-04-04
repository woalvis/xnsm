package tech.xinong.xnsm.mvp.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import tech.xinong.xnsm.mvp.presenter.MvpPresenter;
import tech.xinong.xnsm.mvp.view.MvpView;

/**
 * Created by xiao on 2016/11/6.
 */
public abstract class MvpActivity<P extends MvpPresenter> extends AppCompatActivity implements MvpView{
    private P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = bindPresenter();
        if (presenter !=null){
            presenter.attachView(this);
        }
    }

    public abstract P bindPresenter();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.presenter!=null){
            this.presenter.detachView();
            this.presenter = null;
        }
    }

    public P getPresenter(){
        return this.presenter;
    }

//
//    public void setOnClickListener(View.OnClickListener listener, int... ids) {
//        if (ids == null) {
//            return;
//        }
//        for (int id : ids) {
//            get(id).setOnClickListener(listener);
//        }
//    }
//
//    public <T extends View> T get(int id) {
//        return (T) bindView(id);
//    }
//
//    public <T extends View> T bindView(int id) {
//        T view = (T) mViews.get(id);
//        if (view == null) {
//            view = (T) rootView.findViewById(id);
//            mViews.put(id, view);
//        }
//        return view;
//    }
}
