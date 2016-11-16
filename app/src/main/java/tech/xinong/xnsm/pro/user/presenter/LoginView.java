package tech.xinong.xnsm.pro.user.presenter;

import tech.xinong.xnsm.pro.base.view.BaseView;

/**
 * Created by Administrator on 2016/9/6.
 */
public interface LoginView extends BaseView<String> {
    @Override
    void onResult(String data);
}
