package tech.xinong.xnsm.pro.user.model;

import android.content.Context;

import com.lzy.okgo.callback.StringCallback;

import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.pro.base.model.BaseModel;


/**
 * Created by Administrator on 2016/9/6.
 */
public class LoginModel extends BaseModel {


    public LoginModel(Context context) {
        super(context);
    }

    public void login(String username, String password, StringCallback onHttpResultListener){
        XinongHttpCommend.getInstance(getContext()).login(username,password,onHttpResultListener);
    }
}
