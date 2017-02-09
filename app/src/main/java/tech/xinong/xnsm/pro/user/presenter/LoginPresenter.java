package tech.xinong.xnsm.pro.user.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;

import okhttp3.Call;
import okhttp3.Response;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.pro.user.model.LoginModel;
import tech.xinong.xnsm.pro.user.view.LoginView;
import tech.xinong.xnsm.util.XnsConstant;


/**
 * Created by xiao on 2016/9/6.
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    private LoginModel loginModel;
    public LoginPresenter(Context context) {
        super(context);
        loginModel = new LoginModel(context);
    }

    public void login(final String username, final String password, final SharedPreferences.Editor editor){
        loginModel.login(username, password, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                String token = response.header(HttpConstant.HTTP_HEADER_TOKEN);
                HttpHeaders headers = new HttpHeaders();
                headers.put(HttpConstant.HTTP_HEADER_TOKEN, token);
                //将token放入到header
                OkGo.getInstance().addCommonHeaders(headers);
                /*登陆成功后将用户名密码，还有得到的token存入文件中*/
                editor.putString(XnsConstant.USER_NAME, username);
                editor.putString(XnsConstant.PASSWORD, password);
                editor.putString(XnsConstant.TOKEN, token);
                editor.commit();
                getView().onLoginSuccess();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                getView().onLoginFiled();
            }
        });
    }
}
