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
    public static final String OK = "OK";


    /**
     * category-endpoint----------------------------------------------------------------------------
     */
    /*查询类别  get请求为查询所有的类别下面的product   post请求为保存以下类别*/
    public static final String URL_CATEGORY = "/categories";
    /*用过类别Id删除类别   /categories/{categoryId}*/
    public static final String URL_DELETE_CATEGORY = "/categories/{categoryId}";
    /*通过类别Id更改类别   /categories/{categoryId}*/
    public static final String URL_UPDATE_CATEGORY = "/categories/{categoryId}";
    /**
     * area-endpoint--------------------------------------------------------------------------------
     */
    /*获得地区*/
    public static final String URL_AREAS = "/areas";
    /**
     * buy-order-endpoint---------------------------------------------------------------------------
     */
    /*立即购买，创建订单 get请求是find   post请求是创建*/
    public static final String URL_BUY_NOW = "/buyOrders";
    /*通过订单的id，得到订单的详情*/
    public static final String URL_GET_ORDER_BY_ID = "/buyOrders/%s?details";
    /*得到该用户的所有采购订单*/
    public static final String URL_GET_ALL_ORDERS = "/buyOrders?buyer";
    /*得到该用户的所有供应订单*/
    public static final String URL_GET_ALL_SUPPLY_ORDERS = "/buyOrders?seller";
    /*通过id取消订单*/
    public static final String URL_CANCLE_ORDER_BY_ID = "";
    /*上传付费凭证*/
    public static final String URL_UPLOAD_ORDER_TOPAY = "/buyOrders/%s?toPay";//%s 替代buyOrderId
    /*更改订单状体，发货或者收货*/
    public static final String URL_ORDER_TRANSFER = "/buyOrders/%s?productTransfer";//%s 替代buyOrderId

    /**
     * change-log-endpoint--------------------------------------------------------------------------
     * /changeLog/{level}/{packageName}
     * 暂时不知道干嘛的
     */
    public static final String URL_CHANGE_LOG = "/changeLog/%s/%s";


    /**
     * User模块-------------------------------------------------------------------------------------
     */
    /*发送注册用的验证码*/
    public static final String URL_SEND_VERIFY = "/public/phone/code";
    /*登录   用户名和密码*/
    public static final String URL_LOGIN = "/auth/token";
    /*登陆时候发的验证码 （没用）*/
    public static final String URL_LOGIN_VERIFY = "/users/phone/verifyLogin";
    /*注册  cellphone  verifyCode password*/
    public static final String URL_REGISTER = "/public/registration";
    /*用户添加地址*/
    public static final String URL_ADD_ADDRESS = "/customers/address";
    /*用户删除地址 /customers/address/{addressId}*/
    public static final String URL_DELETE_ADDRESS = "/customers/address/%S";
    /*用户更新地址 /customers/address/{addressId}*/
    public static final String URL_UPDATE_ADDRESS = "/customers/address/%S";
    /*创建联系人create contact*/
    public static final String URL_CREATE_CONTACT = "/customers/contacts";
    /*删除联系人  /customers/contacts/{contactId}*/
    public static final String URL_DELETE_CONTACT = "/customers/contacts/%s";
    /*修改联系人  /customers/address/{addressId}*/
    public static final String URL_UPDATE_CONTACT = "/customers/contacts/%s";
    /*查找当前信息   findCurrentInfo*/
    public static final String URL_FIND_CONTACT = "/customers/currentInfo";


    /**
     * 以下为listings--------------------------------------------------------------------------------
     */
    /*请求别人发布的信息*/
    public static final String URL_LISTINGS = "/listings";
    /*创建发布*/
    public static final String URL_LISTINGS_SELL = "/listings/sellerListing";
    /*按产品和类别查找所有  /products/{product}/{spec}  findAllByProductAndCategory*/
    public static final String URL_PRODUCT = "/products/%s/%s";
    /* 通过产品的Id得到该产品所有的细节*/
    public static final String URL_GET_PRO_BY_ID = "/listings/%s";

    /*得到所有的运输方式*/
    public static final String URL_GET_LOGISTIC_METHODS = "/logisticMethods";

    /*根据产品的ID得到所有的规格*/
    public static final String URL_GET_ALLSPECS_BY_PRODUCTID = "/products/%S/allSpecs";


    /*上传文件*/
    public static final String URL_UPLOAD = "/listingDocs/upload";


    /*根据图片的名称得到图片*/
    public static final String URL_SHOW_IMAGE = "/listings/%s/docs/%s";


    /**
     * 以下为拍卖模块所需的URL--------------------------------------------------------------------------------
     */

    /*得到所有的拍卖信息*/
    public static final String URL_GET_AUCTIONS = "/auctions";

    /*根据Id删除一条拍卖信息*/
    public static final String URL_DEL_AUCTION_BY_ID = "/auctions/%s";//%s 替代auctionId

    /*根据Id返回该拍卖的授权文件*/
    public static final String URL_GET_ATTACHMENT_BY_ID = "/auctions/%s/attachment";//%s 替代auctionId

    /*根据id查看此条拍卖的详细信息*/
    public static final String URL_GET_AUCTION_DETAIL_BY_ID ="/auctions/%s/details";//%s 替代auctionId



}
