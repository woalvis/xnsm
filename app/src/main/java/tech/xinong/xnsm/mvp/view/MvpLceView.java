package tech.xinong.xnsm.mvp.view;

/**
 * 请求网络需要等待
 * Created by xiao on 2016/11/6.
 */

public interface MvpLceView<M> extends MvpView{
    //用于显示进度条
    void showLoading(boolean isPullToRefresh);
    //显示视图
    void showContent();
    //处理异常接口方法
    void showError(Exception e,boolean isPullToRefresh);
    //绑定数据
    void showData(M data);
}
