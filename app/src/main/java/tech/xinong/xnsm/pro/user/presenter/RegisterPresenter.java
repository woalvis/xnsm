package tech.xinong.xnsm.pro.user.presenter;

import android.content.Context;

import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.pro.user.model.RegisterModel;
import tech.xinong.xnsm.pro.user.view.RegisterView;

/**
 * Created by xiao on 2017/1/10.
 */

public class RegisterPresenter extends BasePresenter<RegisterView> {
    private RegisterModel registerModel;
    public RegisterPresenter(Context context) {
        super(context);
        registerModel = new RegisterModel(context);
    }

    public void registerUser(String cellphone,String verificationCode){
        registerModel.registerUser(cellphone, verificationCode, new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                getView().onRegisterSuccess();
            }
        });
    }

}
