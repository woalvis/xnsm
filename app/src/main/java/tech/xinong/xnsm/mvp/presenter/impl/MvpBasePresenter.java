package tech.xinong.xnsm.mvp.presenter.impl;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import tech.xinong.xnsm.mvp.presenter.MvpPresenter;
import tech.xinong.xnsm.mvp.view.MvpView;

/**
 * 抽象类 统一管理View层绑定和解除绑定
 *
 * Created by xiao on 2016/11/6.
 */

public abstract class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V>{

    private WeakReference<Context> weakContext;
    private WeakReference<V> weakView;
    private V proxyView;




    public MvpBasePresenter(Context context){
        this.weakContext = new WeakReference<Context>(context);
    }


    public Context getContext(){
        return weakContext.get();
    }


    /**
     * 用于检查View是否为空对象
     *
     * @return
     */
    public boolean isAttachView() {
        if (this.weakView != null && this.weakView.get() != null) {
            return true;
        }
        return false;
    }


    @Override
    public void attachView(V view) {


        this.proxyView = view;
//        this.weakView = new WeakReference<V>(view);
//        MvpViewInvocationHandler mvpViewInvocationHandler = new MvpViewInvocationHandler(
//                this.weakView.get());
//        //动态代理
//        proxyView = (V) Proxy.newProxyInstance(
//                view.getClass().getClassLoader(),
//                view.getClass().getInterfaces(),
//                mvpViewInvocationHandler
//        );
    }

    @Override
    public void detachView() {
       if (this.weakView!=null){
           weakView.clear();
           this.weakView = null;
       }
    }

    public V getView(){
        return proxyView;
    }


    private class MvpViewInvocationHandler implements InvocationHandler{

        private MvpView mvpView;

        public MvpViewInvocationHandler(MvpView mvpView){
            this.mvpView = mvpView;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isAttachView()){
                return method.invoke(mvpView,args);
            }

            return null;
        }
    }
}
