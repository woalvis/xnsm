package tech.xinong.xnsm.http.framework.utils;

/**
 * Created by xiao on 2016/11/15.
 */

public class HttpConstant {

    public static final String HTTP_HEADER_TOKEN = "X-ACCESS-TOKEN";


    public static final String PREFIX_HTTP = "http://";
    public static final String PREFIX_WS = "ws://";
    public static final String PORT = ":8082";

    public static final String IP = "101.201.147.46";
   // public static final String IP = "192.168.32.239";

    //public static final String IP ="192.168.12.254";

    public static final String HOST = PREFIX_HTTP + IP + PORT;


    /**
     * User模块
     */
    /*发送注册用的验证码*/
    public static final String URL_SEND_VERIFY = "/public/phone/code";
    /*登录   用户名和密码*/
    public static final String URL_LOGIN = "/auth/token";
    /*登陆时候发的验证码 （没用）*/
    public static final String URL_LOGIN_VERIFY = "/users/phone/verifyLogin";
    /*注册  cellphone  verifyCode password*/
    public static final String URL_REGISTER = "/public/registration";


    /*查询类别*/
    public static final String URL_CATEGORY = "/categories";
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
    public static final String URL_LISTINGS_SELL = "/listings/sellerListing";


    /* 通过产品的Id得到该产品所有的细节*/
    public static final String URL_GET_PRO_BY_ID = "/listings/%s";


    /*得到所有的运输方式*/
    public static final String URL_GET_LOGISTIC_METHODS = "/logisticMethods";

    /*根据产品的ID得到所有的规格*/
    public static final String URL_GET_ALLSPECS_BY_PRODUCTID = "/products/%S/allSpecs";
    /*立即购买，创建订单*/
    public static final String URL_BUY_NOW = "/buyOrders";
    /*通过订单的id，得到订单的详情*/
    public static final String URL_GET_ORDER_BY_ID = "/buyOrders/%s?details";
    /*得到该用户的所有订单*/
    public static final String URL_GET_ALL_ORDERS = "/buyOrders?buyer";

    /*上传文件*/
    public static final String URL_UPLOAD = "/listingDocs/upload";

}
