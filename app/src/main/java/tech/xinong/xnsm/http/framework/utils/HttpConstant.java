package tech.xinong.xnsm.http.framework.utils;

/**
 * Created by xiao on 2016/11/15.
 */

public class HttpConstant {

    public static final String PREFIX_HTTP = "http://";
    public static final String PREFIX_WS = "ws://";
    public static final String PORT = ":8082";

    public static final String IP ="101.201.147.46";
    public static final String HOST = PREFIX_HTTP+IP+PORT;

    /*发送验证码*/
    public static final String URL_SEND_VERIFY = "/users/phone/verify";
    /*登录*/
    public static final String URL_LOGIN = "/auth/token";
    /*登陆时候发的验证码*/
    public static final String URL_LOGIN_VERIFY = "/users/phone/verifyLogin";
    /*注册*/
    public static final String URL_REGISTER = "/users/register";
    /*查询类别*/
    public static final String URL_CATEGORY = "/categorys";
}
