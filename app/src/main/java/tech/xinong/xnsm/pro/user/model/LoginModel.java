package tech.xinong.xnsm.pro.user.model;

import android.content.Context;

import tech.xinong.xnsm.http.framework.impl.RequestParam;
import tech.xinong.xnsm.http.framework.impl.system.SystemHttpCommand;
import tech.xinong.xnsm.http.framework.utils.HttpTask;
import tech.xinong.xnsm.http.framework.utils.HttpUtils;
import tech.xinong.xnsm.pro.base.model.BaseModel;


/**
 * Created by Administrator on 2016/9/6.
 */
public class LoginModel extends BaseModel {


    public LoginModel(Context context) {
        super(context);
    }

    private String getServerUrl(){
        return "http://192.168.146.1:8080/LoginServer/servlet/LoginServlet/";
    }

    public void login(String username, String password, HttpUtils.OnHttpResultListener onHttpResultListener){
        RequestParam requestParam = new RequestParam();
        requestParam.put("username",username);
        requestParam.put("password",password);
        HttpTask httpTask = new HttpTask(
                getServerUrl(),
                requestParam,
                new SystemHttpCommand(),
                onHttpResultListener);
        httpTask.execute();
    }
}
