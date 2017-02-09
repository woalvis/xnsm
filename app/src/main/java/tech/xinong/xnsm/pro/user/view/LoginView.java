package tech.xinong.xnsm.pro.user.view;

import tech.xinong.xnsm.pro.base.view.BaseView;

/**
 * Created by Administrator on 2016/9/6.
 */
public interface LoginView extends BaseView<String> {
    void onLoginSuccess();

    void onLoginFiled();
}
