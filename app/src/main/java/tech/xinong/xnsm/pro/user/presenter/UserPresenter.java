package tech.xinong.xnsm.pro.user.presenter;

import android.content.Context;

import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.pro.user.model.LoginModel;
import tech.xinong.xnsm.pro.user.view.LoginView;

/**
 * Created by xiao on 2016/12/23.
 */

public class UserPresenter extends BasePresenter<LoginView> {
    private LoginModel loginModel;
    public UserPresenter(Context context) {
        super(context);
        loginModel = new LoginModel(context);
    }

    public void register(){

    }
}
