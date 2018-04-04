package tech.xinong.xnsm.http.framework.utils;

/**
 * Created by xiao on 2016/11/15.
 */

public class HttpConstant {

    public static final String HTTP_HEADER_TOKEN = "X-ACCESS-TOKEN";
    public static final String PREFIX_HTTP = "http://";
    public static final String PREFIX_WS = "ws://";
    public static final String PORT = ":8083";
    public static final String IP = "101.201.147.46";//test环境
   // public static final String IP = "192.168.33.12";//海超环境
 //   public static final String IP = "101.201.152.127";//intag环境

//    public static final String IP = "192.168.33.180";
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

    public static final String URL_NAME_TO_PRODUCT = "/products?q=names";
    /**
     * area-endpoint--------------------------------------------------------------------------------
     */
    /*获得地区*/
    public static final String URL_AREAS = "/public/areas";
    /**
     * buy-order-endpoint---------------------------------------------------------------------------
     */
    /*立即购买，创建订单 get请求是find   post请求是创建*/
    //public static final String URL_BUY_NOW = "/buyOrders";
    public static final String URL_BUY_NOW = "/orders";

    /*通过订单的id，得到订单的详情*/
    public static final String URL_GET_ORDER_BY_ID = "/orders/%s?query=details";
    /*得到该用户的所有采购订单*/
    public static final String URL_GET_ALL_ORDERS = "/orders?query=buyer";

    /*申请退款*/
    public static final  String URL_REFUND_REQ = "/orders/%s?action=refundReq";//{orderId}


    /*审批退款申请*/
    public static final String URL_ORDER_EXAMINE = "/orders/%s?action=approve";//{orderId}


    /*卖家直接退款*/
    public static final String URL_SELLER_REFUND = "/orders/%s?action=sellerRefund";//{orderId}



    /*停止采购*/
    public static final String URL_STOP_BUY = "/buyerListings/%s?action=stop";//
    /*得到该用户的所有供应订单*/
    public static final String URL_GET_ALL_SUPPLY_ORDERS = "/orders?query=seller";
    /*通过id取消订单*/
    public static final String URL_CANCEL_ORDER_BY_ID = "/orders/%s?action=cancel";
    /*上传付费凭证*/
    public static final String URL_UPLOAD_ORDER_TOPAY = "/buyOrders/%s?toPay";//%s 替代buyOrderId
    /*更改订单状体，发货或者收货*/
    public static final String URL_ORDER_TRANSFER = "/buyOrders/%s?productTransfer";//%s 替代buyOrderId

    /*卖家修改订单*/
    public static final String URL_ORDER_UPDATE = "/orders/%s?action=sellerModify";//{orderId}
    /*买家确认订单*/
    public static final String URL_ORDER_confirm = "/orders/%s?action=buyerConfirm";//{orderId}

    /*发货*/
    public static final String URL_SENT = "/orders/%s?action=sellerSend";

    /*收货*/
    public static final String URL_RECEIVE = "/orders/%s?action=buyerReceive";//orders
    /*查询物流信息*/
    public static final String URL_LOGISTICS = "/orders/%s/logistics";//orderId

    /*查询退款申请*/
    public static final String URL_REFUND_REQS = "/orders/%s/refundReqs";//{orderId}

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
    /*用户地址管理，增删改查*/
    public static final String URL_USER_ADDRESS = "/customers/%s/addresses";//用户ID



    /*用户删除地址 /customers/address/{addressId}*/
    public static final String URL_DELETE_ADDRESS = "/customers/address/%s";
    /*创建联系人create contact*/
    public static final String URL_CREATE_CONTACT = "/customers/contacts";
    /*删除联系人  /customers/contacts/{contactId}*/
    public static final String URL_DELETE_CONTACT = "/customers/contacts/%s";
    /*修改联系人  /customers/address/{addressId}*/
    public static final String URL_UPDATE_CONTACT = "/customers/contacts/%s";
    /*查找当前信息   findCurrentInfo*/
    public static final String URL_FIND_CONTACT = "/customers/currentInfo";
    /*查询自己发布的*/
    public static final String URL_SELLER_LISTINGS_ME = "/customers/%s/sellerListings";//customerId

    public static final String URL_BUYER_LISTINGS_ME = "/customers/%s/buyerListings";//customerId

    public final static String URL_PRODUCT_IMG = "/sellerListings/%s/product?q=coverImg";



    /**
     * 添加用户验证申请 post
     *
     * 修改用户个人认证信息 后面加/{reqId}  put
     *
     *获取用户验证申请详细信息 后面加/{reqId} get
     */
    public final static String URL_VERFICATION_PERSON = "/individualVerificationReqs";


    /**
     * 以下为listings--------------------------------------------------------------------------------
     */
    /*请求别人发布的信息*/
//   public static final String URL_LISTINGS = "/listings";
    public static final String URL_LISTINGS = "/sellerListings";
    /*查询某个人发布的*/
    public static final String URL_LISTINGS_BY_SELLER_ID = "/sellers/%s/sellerListings";//sellerId
    /*创建发布*/
    public static final String URL_LISTINGS_SELL = "/listings/sellerListing";
    /*创建采购*/
    public static final String URL_BUYER_LISTINGS = "/buyerListings";
    /*按产品和类别查找所有  /products/{product}/{spec}  findAllByProductAndCategory*/
    public static final String URL_PRODUCT = "/products/%s/%s";
    /* 通过产品的Id得到该产品所有的细节*/
    public static final String URL_GET_PRO_BY_ID = "/sellerListings/%s";

    /*得到所有的运输方式*/
    public static final String URL_GET_LOGISTIC_METHODS = "/logisticMethods";

    /*上传文件*/
    public static final String URL_UPLOAD = "/listingDocs/upload";


    /*根据图片的名称得到图片*/
    public static final String URL_SHOW_IMAGE = "/listings/%s/docs/%s";

    /*下架或者上架*/
    public static final String URL_PUBLISH_STATE_CHANGE = "/sellerListings/%s?action=shelves";

    /*根据ID查询报价*/
    public static final String URL_QUOTATIONS = "/buyerListings/%s/quotations";
    /*查询报价的货物图片（报价人的sellerlisting对应的图）*/
    public static final String URL_QUOTATION_DOCS = "/quotations/%s/sellerListing/docs";

    /*采购报价的人（某条报价是谁报的）*/
    public static final String URL_QUOTATION_OWNER = "/quotations/%s/owner";


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
    public static final String URL_GET_AUCTION_DETAIL_BY_ID = "/auctions/%s/details";//%s 替代auctionId


    /*拍卖报名*/
    public static final String URL_AUCTION_DEPOSITS = "/auctionDeposits";

    /*投标*/
    public static final String URL_AUCTION_BID = "/bids";

    /*支付尾款*/
    public static final String URL_AUCTION_RESIDUE_PAY = "/bids/%S/residuePay";//%S替代 bidId

    /*获得该人的所有关注项*/
    public static final String URL_FAVS = "/favs";

    /**
     * 首页--------------------------------------------------------------------------------
     */

    /*获得轮播页的信息*/
    public static final String URL_CAMPAIGNS = "/campaigns";
    /*获得广告 http://101.201.147.46:8083/ads/9B 加上id可获取广告明细*/
    public static final String URL_ADS = "/ads";

    /*为您推荐--推荐商品*/
    public static final String URL_RECOMMENDATIONS = "/recommendations";

    /*为您推荐--优质商家*/
    public static final String URL_RECOMMEND_SELLERS = "/sellers";

    /*商家发布的卖品*/
    public static final String URL_SELLER_LISTINGS = "/sellers/%s/sellerListings";
    /**
     * 个人--------------------------------------------------------------------------------
     */
    /*修改用户信息/上传头像等 需要在后面传入id*/
    public static final String URL_UPDATE_USERINFO = "/customers/";

    /*获得个人信息*/
    public static final String URL_CURRENTINFO = "/customers/currentInfo";

    /*获取用户验证申请详细信息*/

    public static final String URL_VERIFICATIONREQS = "/individualVerificationReqs";

    /*根据产品id获得所有规格*/
    public static final String URL_SPCESCONFIGS_BY_PRODUCTID = "/products/%s/specConfigs";
    /*根据产品id获得所有品类*/
    public static final String URL_SPCES_BY_PRODUCTID = "/products/%s/specs";

    public static final String URL_PROVIDE_SUPPORTS = "/provideSupports";

    public static final String URL_SEARCH_TEXT = "/productSpecs?searchText=";

    /*搜索框*/
    public static final String URL_SEARCH = "/productSpecs?query=pnameOrsname";


    /*支付宝*/
    public static final String URL_ALIPAY = "/alipay";

    public static final String URL_FEE_FATE = "/sysConfigs/xnFeeRate";

    /*查询自己的钱包*/
    public static final String URL_WALLETS = "/wallets?owner=me";

    /*查询钱包里面的收支记录*/
    public static final String URL_FINANCE_RECORDS = "/financeRecords?owner=me";
    /*是否设置了支付密码*/
    public static final String URL_HAS_PWD = "/wallets?query=hasPwd";
    /*设置支付密码*/
    public static final String URL_SET_PWD = "/wallets?action=setPwd";
    /*发送手机验证码*/
    public static final String URL_SET_PWD_CODE = "/wallets?action=sendVerificationCode";

    /*重置支付密码*/
    public static final String URL_UPDATE_PWD = "/wallets?action=resetPwd";

    /*提现*/
    public static final String URL_WITHDRAW = "/wallets?action=withdraw";

    /*银行账号*/
    public static final String URL_BANKS = "/banks";

    /*添加银行账号，请求验证码*/
    public static final String URL_BANKS_VERIFY = "/banks?registeredPhone=&&action=verificationCode";

    /*查询是否可以提现（修改提现号码锁定24小时）*/
    public static final String URL_CAN_WITHDRAW = "/wallets?query=canWithdraw";

    /*退款费率*/
    public static final String URL_REFUND_FEERATE = "/sysConfigs/refundFeeRate";


    /*----------------------行情------------------*/
    public static final String URL_PRICES_PRODUCT = "/prices?action=trend";//{productId}

    public static final String URL_PRICES_SPCE_OR_CITY = "/prices";//{specId}/{cityId}
}
