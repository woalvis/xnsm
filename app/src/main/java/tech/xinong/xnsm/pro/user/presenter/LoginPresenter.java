package tech.xinong.xnsm.pro.user.presenter;

import android.content.Context;
import tech.xinong.xnsm.http.framework.utils.HttpUtils;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.pro.user.model.LoginModel;


/**
 * Created by Administrator on 2016/9/6.
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    private LoginModel loginModel;
    public LoginPresenter(Context context) {
        super(context);
        loginModel = new LoginModel(context);
    }

    public void login(String username,String password){
        loginModel.login(username, password, new HttpUtils.OnHttpResultListener() {
            @Override
            public void onResult(String result) {
                getView().onResult(result);
            }
        });
    }
}
