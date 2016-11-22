package tech.xinong.xnsm.http.framework.utils;

/**
 * Created by xiao on 2016/11/15.
 */

public class HttpConstant {

    public static final String HTTP_HEADER_TOKEN="X-ACCESS-TOKEN";


    public static final String PREFIX_HTTP = "http://";
    public static final String PREFIX_WS = "ws://";
    public static final String PORT = ":8082";

    public static final String IP ="101.201.147.46";
    public static final String HOST = PREFIX_HTTP+IP+PORT;


    /**
     * User模块
     */
    /*发送验证码*/
    public static final String URL_SEND_VERIFY = "/users/phone/verify";
    /*登录   用户名和密码*/
    public static final String URL_LOGIN = "/auth/token";
    /*登陆时候发的验证码 （没用）*/
    public static final String URL_LOGIN_VERIFY = "/users/phone/verifyLogin";
    /*注册  cellphone  verifyCode password*/
    public static final String URL_REGISTER = "/users/register";




    /*查询类别*/
    public static final String URL_CATEGORY = "/categorys";
    /**/
    public static final String URL_PRODUCT = "/products/%s/%s";
    /*获得地区*/
    public static final String URL_AREAS = "/areas";


    /**
     * 以下为listings
     */
    /*请求别人发布的信息*/
    public static final String URL_LISTINGS = "/listings";
    /*创建发布*/
    public static final String URL_LISTINGS_SELL = "/listings/sell";
    /*上传文件*/
    public static final String URL_UPLOAD = "/listingDocs/upload";
}
