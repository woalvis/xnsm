package tech.xinong.xnsm.mvp.view.impl;


import tech.xinong.xnsm.mvp.view.MvpLceView;

/**
 * 实现类
 * 适配器模式
 * 请求网络，需要等待
 * Created by Administrator on 2016/8/9.
 */
public abstract class MvpBaseLceView<M> implements MvpLceView<M> {
    @Override
    public void showContent() {

    }

    @Override
    public void showData(M data) {

    }

    @Override
    public void showError(Exception e, boolean isPullToRefresh) {

    }

    @Override
    public void showLoading(boolean isPullToRefresh) {

    }
}
