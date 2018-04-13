package tech.xinong.xnsm.http.framework.impl.xinonghttp;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import tech.xinong.xnsm.http.framework.IHttpCommand;
import tech.xinong.xnsm.http.framework.IRequestParam;
import tech.xinong.xnsm.http.framework.impl.RequestParam;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.XnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.BuyOrderModel;
import tech.xinong.xnsm.pro.buy.model.ListingDocDTO;
import tech.xinong.xnsm.pro.buy.model.OrderStatus;
import tech.xinong.xnsm.pro.publish.model.ListingDocFile;
import tech.xinong.xnsm.pro.publish.model.ListingSellCreator;
import tech.xinong.xnsm.pro.publish.model.PublishBuyInfoModel;
import tech.xinong.xnsm.pro.sell.model.QuotationModel;
import tech.xinong.xnsm.pro.user.model.Address;
import tech.xinong.xnsm.pro.user.model.PublishStates;
import tech.xinong.xnsm.pro.user.model.RegisterModel;
import tech.xinong.xnsm.pro.user.model.SentCreator;
import tech.xinong.xnsm.pro.user.model.VerificationReq;
import tech.xinong.xnsm.pro.user.view.LoginActivity;
import tech.xinong.xnsm.pro.wallet.model.BankModel;
import tech.xinong.xnsm.util.DeviceInfoUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;

/**
 * 网络请求封装类
 * Created by Dream on 16/6/11.
 */
public class XinongHttpCommend implements IHttpCommand<RequestParam> {
    private String favsId;
    private String favsName;
    private OkHttpClient mOkHttpClient;
    private static Context mContext;
    public String token;
    private volatile static XinongHttpCommend instance;
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int LOGIN = 2;
    public static final int EXCEPTION = 3;
    public static final int NOT_FOUNT = 4;
    public String url = "";


    private XinongHttpCommend(Context context) {
        mContext = context;
        mOkHttpClient = new OkHttpClient();
    }


    /**
     * 单例模式，保证线程安全
     *
     * @param context
     * @return
     */
    public static XinongHttpCommend getInstance(Context context) {
        if (instance == null) {
            synchronized (XinongHttpCommend.class) {
                if (instance == null) {
                    instance = new XinongHttpCommend(context);
                }
            }
        }
        mContext = context;

        if (mContext!=null&&!DeviceInfoUtil.isNetworkConnected(mContext)){
            T.showShort(mContext,"网络没有连接");
        }
        return instance;
    }

    @Override
    public String execute(String url, IRequestParam<RequestParam> requestParam) {
        return null;
    }


    public void checkUpdate(String url, StringCallback callback) {
        OkGo.get(url).execute(callback);
    }

    private void favsRequest(String url, AbsXnHttpCallback callback,boolean isId) {
        favsId = ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.IDS, "");
        favsId += ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.FAvID, "");
        favsName = ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.FAVS, "");
        favsName += ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.FAVNAME, "");
        GetRequest request = OkGo.get(getAbsoluteUrl(url));
        if (isId){
            if (!TextUtils.isEmpty(favsId)) {
                if (favsId.endsWith(",")||favsId.endsWith(","))
                request.params("productIds", favsId.substring(0,favsId.length()-1));
                else
                 request.params("productIds", favsId.substring(0,favsId.length()));
            }
        }else {
            if (!TextUtils.isEmpty(favsName)) {
                if (favsName.endsWith(",")||favsName.endsWith("，"))
                request.params("productNames", favsName.substring(0,favsId.length()-1));
                else
                    request.params("productNames", favsName.substring(0,favsId.length()));
            }
        }
        request.execute(callback(callback));
    }

    private void favsRequest(String url, int page, int size, AbsXnHttpCallback callback) {
        favsId = ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.IDS, "");
        if (!TextUtils.isEmpty(favsId)) {
            favsId += ",";
        }
        favsId += ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.FAvID, "");
        if (favsId.trim().endsWith(",")) {
            favsId = favsId.substring(0, favsId.length() - 1);
        }
        GetRequest request = OkGo.get(getAbsoluteUrl(url));
        if (!TextUtils.isEmpty(favsId)) {
            request.params("productIds", favsId);
        }
        request.params("page", page).params("size", size);
        request.execute(callback(callback));
    }

    /**
     * 得到所有的品类
     *
     * @param callback
     */
    public void getCategories(AbsXnHttpCallback callback) {
        StringCallback scb = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_CATEGORY))     // 请求方式和请求url
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .cacheKey("getCategories")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(scb);
    }

    /**
     * 得到某个品类下面的所有的产品
     *
     * @param callback
     */
    public void getProduct(AbsXnHttpCallback callback, String name) {
        StringCallback scb = callback(callback);
        OkGo.get(getAbsoluteUrl(String.format(HttpConstant.URL_PRODUCT, name, "VARIETY")))     // 请求方式和请求url
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .cacheKey("getCategories")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(scb);
    }

    /**
     * 得到区域
     */
    public void getAreas(AbsXnHttpCallback callback) {
        StringCallback scb = callback(callback);

        OkGo.get(getAbsoluteUrl(HttpConstant.URL_AREAS))     // 请求方式和请求url
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .cacheKey("getAreas")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(scb);

    }



/*--------------------------主页-----------------------------*/

    public void nameToProduct(String name, AbsCallback callback) {
//        getRequest(callback, HttpConstant.URL_NAME_TO_PRODUCT + name);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_NAME_TO_PRODUCT))
                .params("names", name)
                .execute(callback);
    }

    /**
     * 请求别人发布的信息
     */
    public void getListings(AbsXnHttpCallback callback, int size, int page) {
        favsRequest(HttpConstant.URL_LISTINGS, page, size, callback);
    }

    public void filterListings(AbsXnHttpCallback callback, String... args) {
        StringCallback scb = callback(callback);
        String ids = "";
        for (String id : args) {
            ids += id + ",";
        }
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_LISTINGS))     // 请求方式和请求url
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("productIds", ids.substring(0, ids.length() - 1))
                .execute(scb);
    }

    public void filterListings(AbsXnHttpCallback callback, String productIds, String productSpecId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("productIds", productIds);
        paramMap.put("productSpecId", productSpecId);
        filterListings(callback, paramMap);
    }

    /**
     * productIds	String数组
     * productSpecId	String
     * maxPrice	BigDecimal
     * minPrice	BigDecimal
     * provinceId	String
     * cityId	String
     * districtId	String
     * title	String
     * sort	String
     */
    public void filterListings(AbsXnHttpCallback callback, Map<String, String> param) {
        StringCallback sbc = callback(callback);
        GetRequest getRequest = OkGo.get(getAbsoluteUrl(HttpConstant.URL_LISTINGS));
        Iterator<Map.Entry<String, String>> paramIterator = param.entrySet().iterator();

        while (paramIterator.hasNext()) {
            Map.Entry<String, String> entry = paramIterator.next();
            getRequest.params(entry.getKey(), entry.getValue());
        }
        getRequest.execute(sbc);
    }

    public void filterSellListings(AbsXnHttpCallback callback, Map<String, String> param) {
        StringCallback sbc = callback(callback);
        GetRequest getRequest = OkGo.get(getAbsoluteUrl(HttpConstant.URL_BUYER_LISTINGS));
        Iterator<Map.Entry<String, String>> paramIterator = param.entrySet().iterator();

        while (paramIterator.hasNext()) {
            Map.Entry<String, String> entry = paramIterator.next();
            getRequest.params(entry.getKey(), entry.getValue());
        }
        getRequest.execute(sbc);
    }


    /*获取轮播图列表*/
    public void campaigns(String[] productNames, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        StringBuilder sb = new StringBuilder();
        String configStr = ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.FAVS, "");
        String parma = "";
        if (productNames == null || productNames.length == 0) {

        } else {
            for (String productName : productNames) {
                sb.append(productName + ",");
            }
            parma = sb.substring(0, sb.length() - 1);
        }
        if (!TextUtils.isEmpty(configStr)){
            parma += ","+configStr;
        }

        OkGo.get(getAbsoluteUrl(HttpConstant.URL_CAMPAIGNS))
                .params("productNames", parma)
                .execute(sbc);
    }


    /*请求推荐商家列表*/
    public void sells(AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        favsId = ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.IDS, "");
        favsId += ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.FAvID, "");
        if (TextUtils.isEmpty(favsId)){
            OkGo.get(getAbsoluteUrl(HttpConstant.URL_RECOMMEND_SELLERS))
                    .execute(sbc);
        }else {
            OkGo.get(getAbsoluteUrl(HttpConstant.URL_RECOMMEND_SELLERS))
                    .params("prodectIds",favsId)
                    .execute(sbc);
        }

    }

    /*商家详情*/
    public void sellerById(String id, AbsXnHttpCallback callback) {
        favsId = ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.IDS, "");
        favsId += ((BaseActivity) mContext).mSharedPreferences.getString(XnsConstant.FAvID, "");

        StringCallback sbc = callback(callback);
        if (TextUtils.isEmpty(favsId)) {
            OkGo.get(getAbsoluteUrl(HttpConstant.URL_RECOMMEND_SELLERS + "/" + id))
                    .execute(sbc);
        }else {
            OkGo.get(getAbsoluteUrl(HttpConstant.URL_RECOMMEND_SELLERS + "/" + id))
                    .params("productIds",favsId)
                    .execute(sbc);
        }

    }

    /*商家发布的卖品*/
    public void listingsBySellerId(String id, int page, int size, AbsXnHttpCallback callback) {
        getRequest(callback, String.format(HttpConstant.URL_SELLER_LISTINGS, id), page, size);
    }


    /**
     * 登录，需要用户名和密码，密码用MD5进行加密
     */
    public void login(String username, String password, StringCallback callback) {
        LoginBean loginBean = new LoginBean();
        loginBean.setUsername(username);
        loginBean.setPassword(password);

        String jsonString = JSON.toJSONString(loginBean);
        OkGo.post(HttpConstant.HOST + HttpConstant.URL_LOGIN)
                .upJson(jsonString)
                .execute(callback);
    }


    /**
     * 通过产品的Id得到该产品所有的细节
     */
    public void getProductListings(String proId, AbsXnHttpCallback callback) {
        StringCallback scb = callback(callback);
        OkGo.get(String.format(getAbsoluteUrl(HttpConstant.URL_GET_PRO_BY_ID), proId))
                .execute(scb);
    }

    /**
     * 通过产品的Id得到该发布的采购所有的细节
     */
    public void getBuyListings(String proId, AbsXnHttpCallback callback) {
        StringCallback scb = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_BUYER_LISTINGS + "/" + proId))
                .execute(scb);
    }


    /**
     * 发送注册用验证码
     *
     * @param cellphone
     */
    public void registerVerify(String cellphone, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_SEND_VERIFY))
                .params("cellphone", cellphone)
                .execute(sbc);

    }

    /**
     * 注册用户
     */
    public void registerUser(RegisterModel.Register register, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_REGISTER))
                .upJson(JSON.toJSONString(register))
                .execute(sbc);
    }

    /**
     * 得到当前用户信息
     */
    public void getCurrentInfo(AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_FIND_CONTACT))
                .execute(sbc);
    }

    public void getCurrentInfo(StringCallback callback) {

        OkGo.get(getAbsoluteUrl(HttpConstant.URL_FIND_CONTACT))
                .execute(callback);
    }

    /*获取用户验证申请详细信息*/
    public void verificationreqs(AbsXnHttpCallback callback){
        getRequest(callback,HttpConstant. URL_VERIFICATIONREQS);
    }


    /*得到所有的物流方式*/
    public void getAllLogisticMethods(AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_GET_LOGISTIC_METHODS))
                .execute(sbc);
    }


    /*发布卖货信息*/
    public void publishSellInfo(ListingSellCreator publishSellInfoModel, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);

        List<ListingDocFile> files = publishSellInfoModel.getDocs();
        PostRequest request = OkGo.post(getAbsoluteUrl(HttpConstant.URL_LISTINGS));
        request.params("product.id", publishSellInfoModel.getProduct().getId());
        request.params("productSpec.id", publishSellInfoModel.getProductSpec().getId());
        if (publishSellInfoModel.getSpecificationConfigs() != null) {
            for (int i = 0; i < publishSellInfoModel.getSpecificationConfigs().size(); i++) {
                request.params("specificationConfigs[" + i + "].id",
                        publishSellInfoModel.getSpecificationConfigs().get(i).getId());
            }
        }
        request.params("freeShipping",publishSellInfoModel.getFreeShipping());
        request.params("unitPrice", publishSellInfoModel.getUnitPrice().toString());
        request.params("weightUnit", publishSellInfoModel.getWeightUnit().getName());
        request.params("minQuantity", publishSellInfoModel.getMinQuantity().toString());
        request.params("termBeginDate", publishSellInfoModel.getTermBeginDate().toString());
        request.params("termEndDate", publishSellInfoModel.getTermEndDate().toString());
        request.params("totalQuantity", publishSellInfoModel.getTotalQuantity().toString());
        request.params("province.id", publishSellInfoModel.getProvince().getId());
        if (publishSellInfoModel.getCity() != null)
            request.params("city.id", publishSellInfoModel.getCity().getId());
        if (publishSellInfoModel.getDistrict() != null)
            request.params("district.id", publishSellInfoModel.getDistrict().getId());
        request.params("street", publishSellInfoModel.getStreet());
        request.params("provideSupport", publishSellInfoModel.getProvideSupport());
        if (!TextUtils.isEmpty(publishSellInfoModel.getNotes()))
        request.params("notes", publishSellInfoModel.getNotes());
        request.params("title", publishSellInfoModel.getTitle());
        request.params("originCity.id", publishSellInfoModel.getOriginCity());
        request.params("originProvince.id", publishSellInfoModel.getOriginProvince());
        for (int i = 0; i < files.size(); i++) {
            List<File> files1 = new ArrayList<>();
            files1.add(files.get(i).getFile());
            request.addFileParams("docs[" + i + "].file", files1);
        }
        request.execute(sbc);
    }


    /*立即购买，创建订单*/
    public void buyNow(BuyOrderModel buyOrderModel, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        String jsonStr = JSON.toJSONString(buyOrderModel);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_BUY_NOW))
                .upJson(jsonStr)
                .execute(sbc);
    }


    /*立即购买，创建订单*/
    public void buyNowFromQuote(BuyOrderModel buyOrderModel, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        String jsonStr = JSON.toJSONString(buyOrderModel);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_BUY_NOW)+"?from=quote")
                .upJson(jsonStr)
                .execute(sbc);
    }



    /*删除订单*/
    public void delOrder(String id,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.delete(getAbsoluteUrl(HttpConstant.URL_BUY_NOW)+"/"+id)
                .execute(sbc);

    }


    /*通过订单的id，得到订单的详情*/
    public void getOrderDetailById(String id, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(String.format(HttpConstant.URL_GET_ORDER_BY_ID, id)))
                .execute(sbc);

    }
        /*取消订单*/
    public void cancelOrderById(String orderId, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_CANCEL_ORDER_BY_ID, orderId)))
                .execute(sbc);
    }

    /*申请退款*/
    public void refundReq(String id,String reason,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_REFUND_REQ,id)))
                .params("refundReason",reason)
                .execute(sbc);
    }

    public void refundReq(String orderId,AbsXnHttpCallback callback){
        getRequest(callback,String.format(HttpConstant.URL_REFUND_REQS,orderId));
    }

    /*同意退款*/
    public void approveRefund(String orderId,AbsXnHttpCallback callback){
        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_ORDER_EXAMINE,orderId)))
                .params("agree",true)
               .execute(callback(callback));
    }

    /*拒绝退款*/
    public void refuseRefund(String orderId,String msg,AbsXnHttpCallback callback){
        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_ORDER_EXAMINE,orderId)))
                .params("agree",false)
                .params("approveMsg",msg)
                .execute(callback(callback));
    }

    /*卖家直接退款*/
    public void sellRefund(String orderId,String msg,AbsXnHttpCallback callback){
        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_SELLER_REFUND,orderId)))
                .params("refundReason",msg)
                .execute(callback(callback));

    }


    /*买家确认订单*/
    public void confirmOrder(String orderId, AbsXnHttpCallback callback){
        putRequest(callback,String.format(HttpConstant.URL_ORDER_confirm,orderId));
    }

    /*修改订单*/
   public void updateOrder(String orderId,String offer,String freight,AbsXnHttpCallback callback){//freight运费  offer优惠
       StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_ORDER_UPDATE,orderId)))
                .params("offer",offer)
                .params("freight",freight)
                .execute(sbc);
   }

    /*得到该用户的所有采购订单*/
    public void getAllOrders(AbsXnHttpCallback callback, int page, int size) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_GET_ALL_ORDERS)+"&sort=updateTime,DESC&sort=orderNo,DESC")
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .cacheKey("getCategories")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(sbc);
    }

    /*停止采购*/
    public void stopBuy(String id, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_STOP_BUY, id)))
                .execute(sbc);
    }

    /*得到该用户的所有供应订单*/
    public void getSupplyOrders(AbsXnHttpCallback callback, String status, int page, int size) {
        StringCallback sbc = callback(callback);

        GetRequest getRequest = OkGo.get(getAbsoluteUrl(HttpConstant.URL_GET_ALL_SUPPLY_ORDERS)+"&sort=updateTime,DESC&sort=orderNo,DESC")
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .cacheKey("getCategories")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .params("page", page)
                .params("size", size);
        if (!TextUtils.isEmpty(status)) {
            getRequest.params("status", status);
        }
        getRequest.execute(sbc);
    }

    /*发货或者收货*/
    public void orderTransfer(String orderId, OrderStatus status, AbsXnHttpCallback callback) {

    }


    private void get(String url, Map<String, String> params, AbsXnHttpCallback callback) {

    }

    /*搜索标题*/
    public void searchTitle(String title, AbsXnHttpCallback callback) {
        favsRequest(HttpConstant.URL_LISTINGS +"?title="+ title, callback,true);
    }


    public void search(String content,AbsXnHttpCallback callback){
        favsRequest(HttpConstant.URL_SEARCH+"&searchText="+content,callback,true);
    }

    /*搜索，只有一个输入需字符串*/
    public void searchTextByProductId(String productId,AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_LISTINGS))
                .params("productIds", productId)
                .execute(sbc);
    }





    /*搜索，只有一个输入需字符串*/
    public void searchTextByspecId(String specId, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_LISTINGS))
                .params("productSpecId", specId)
                .execute(sbc);
    }

    public void getSellerListing(AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_LISTINGS))
                .execute(sbc);
    }

    /*获得推荐商品列表*/
    public void recommendations(AbsXnHttpCallback callback, int size, int page) {
        favsRequest(HttpConstant.URL_RECOMMENDATIONS, page, size, callback);
    }


    /*添加地址*/
    public void addAddress(Address address, String userId, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        String jsonStr = JSON.toJSONString(address);
        OkGo.post(getAbsoluteUrl(String.format(HttpConstant.URL_USER_ADDRESS, userId)))
                .upJson(jsonStr)
                .execute(sbc);
    }


    /*查询用户收货地址*/
    public void address(String userId, AbsXnHttpCallback callback) {
        getRequest(callback, String.format(HttpConstant.URL_USER_ADDRESS, userId));
    }

    /*修改地址*/
    public void updateAddress(String userId, String addressId, boolean isPrimary, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        Address address = new Address();
        address.setId(addressId);
        address.setPrimary(isPrimary);
        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_USER_ADDRESS, userId) + "/" + addressId))
                .upJson(JSON.toJSONString(address))
                .execute(sbc);
    }


    public void updateAddress(String userId, Address address, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        address.setPrimary(null);
        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_USER_ADDRESS, userId) + "/" + address.getId()))
                .upJson(JSON.toJSONString(address))
                .execute(sbc);
    }

    /*删除地址*/
    public void delAddress(String customerId, String addressId, AbsXnHttpCallback callback) {
        deleteRequest(callback, String.format(HttpConstant.URL_USER_ADDRESS, customerId) + "/" + addressId);
    }

    /*上传文件*/
    public void upLoadFile(List<File> files, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_UPLOAD))
                .addFileParams("files", files)
                .execute(sbc);
    }

    /*修改供货发布*/
    public void updateSell(ListingSellCreator publishSellInfoModel, String id, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        List<ListingDocFile> files = publishSellInfoModel.getDocs();
        List<ListingDocDTO> updateFiles = publishSellInfoModel.getUpdateDocs();
        PutRequest request = OkGo.put(getAbsoluteUrl(HttpConstant.URL_LISTINGS) + "/" + id);
//        request.params("product.id", publishSellInfoModel.getProduct().getId());
//        request.params("productSpec.id", publishSellInfoModel.getProductSpec().getId());
        if (publishSellInfoModel.getSpecificationConfigs() != null) {
            for (int i = 0; i < publishSellInfoModel.getSpecificationConfigs().size(); i++) {
                request.params("specificationConfigs[" + i + "].id",
                        publishSellInfoModel.getSpecificationConfigs().get(i).getId());
            }
        }
        if (publishSellInfoModel.getFreeShipping()!=null){
            request.params("freeShipping", publishSellInfoModel.getFreeShipping());
        }
        if (publishSellInfoModel.getUnitPrice() != null)
            request.params("unitPrice", publishSellInfoModel.getUnitPrice().toString());
        if (publishSellInfoModel.getWeightUnit() != null)
            request.params("weightUnit", publishSellInfoModel.getWeightUnit().getName());
        if (publishSellInfoModel.getMinQuantity() != null)
            request.params("minQuantity", publishSellInfoModel.getMinQuantity().toString());
        if (publishSellInfoModel.getTermBeginDate() != null)
            request.params("termBeginDate", publishSellInfoModel.getTermBeginDate().toString());
        if (publishSellInfoModel.getTermEndDate() != null)
            request.params("termEndDate", publishSellInfoModel.getTermEndDate().toString());
        if (publishSellInfoModel.getTotalQuantity() != null)
            request.params("totalQuantity", publishSellInfoModel.getTotalQuantity().toString());
        if (publishSellInfoModel.getProvince() != null)
            request.params("province.id", publishSellInfoModel.getProvince().getId());
        if (publishSellInfoModel.getCity() != null)
            request.params("city.id", publishSellInfoModel.getCity().getId());
        if (publishSellInfoModel.getDistrict() != null)
            request.params("district.id", publishSellInfoModel.getDistrict().getId());
        if (publishSellInfoModel.getStreet() != null)
            request.params("street", publishSellInfoModel.getStreet());
        if (publishSellInfoModel.getProvideSupport() != null)
            request.params("provideSupport", publishSellInfoModel.getProvideSupport());
        if (publishSellInfoModel.getNotes() != null)
            request.params("notes", publishSellInfoModel.getNotes());
        if (publishSellInfoModel.getTitle() != null)
            request.params("title", publishSellInfoModel.getTitle());
        if (publishSellInfoModel.getOriginCity() != null)
            request.params("originCity.id", publishSellInfoModel.getOriginCity());
        if (publishSellInfoModel.getOriginProvince() != null)
            request.params("originProvince.id", publishSellInfoModel.getOriginProvince());
        if (files != null && files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                List<File> files1 = new ArrayList<>();
                files1.add(files.get(i).getFile());
                if (files.get(i).getFile() == null) {
                    request.params("docs[" + i + "].file", files.get(i).getId());
                } else {
                    request.addFileParams("docs[" + i + "].file", files1);
                }
            }
        }
        if (updateFiles.size() > 0) {
            for (int i = files.size(); i < updateFiles.size() + files.size(); i++) {
                request.params("docs[" + (i + files.size()) + "].id", updateFiles.get(i-files.size()).getId());
            }
        }
        request.execute(sbc);
    }


    /*修改供应信息*/
    public void updatePublishBuy(String id,PublishBuyInfoModel model,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(HttpConstant.URL_BUYER_LISTINGS)+"/"+id)
                .upJson(JSON.toJSONString(model)).execute(sbc);
    }


    /*显示图片*/
    public void showPic(String imageUrl, BitmapCallback callback) {
        // StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(imageUrl))
                .execute(callback);
    }


    /*得到所有的拍卖信息*/
    public void getAuctions(AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_GET_AUCTIONS))
                .execute(sbc);
    }


    /*查询自己发布的*/
    public void sellerListingsMe(AbsXnHttpCallback callback, String customerId, PublishStates states, int page, int size) {
        StringCallback sbc = callback(callback);
        if (states == null)
            OkGo.get(getAbsoluteUrl(String.format(HttpConstant.URL_SELLER_LISTINGS_ME, customerId)))
                    .params("page", page).params("size", size)
                    .execute(sbc);
        else {
            OkGo.get(getAbsoluteUrl(String.format(HttpConstant.URL_SELLER_LISTINGS_ME, customerId)))
                    .params("page", page).params("size", size).params("status", states.name())
                    .execute(sbc);
        }
    }


    /*查询某个人发布的*/
    public void sellerListingsBySellerId(AbsXnHttpCallback callback, String sellerId, int page, int size) {
//        StringCallback sbc = callback(callback);
//        OkGo.get(getAbsoluteUrl(String.format(HttpConstant.URL_LISTINGS_BY_SELLER_ID, sellerId)))
//                .execute(sbc);
//
        favsRequest(String.format(HttpConstant.URL_LISTINGS_BY_SELLER_ID, sellerId), page, size, callback);
    }

    /*查询自己发布的相关spec*/
    public void sellerListingMeSpec(AbsXnHttpCallback callback, String customerId, String specId) {
        Map<String, String> params = new HashMap<>();
        params.put("sellerId", customerId);
        params.put("specId", specId);
        getRequestPutParam(callback, HttpConstant.URL_LISTINGS, params);
    }


    public void buyerListingsMe(AbsXnHttpCallback callback, String customerId) {
        getRequest(callback, String.format(HttpConstant.URL_BUYER_LISTINGS_ME, customerId));
    }

    public void buyerListingsMe(AbsXnHttpCallback callback, String customerId, PublishStates status, int page, int size) {
        if (status != null)
            getRequest(callback, String.format(HttpConstant.URL_BUYER_LISTINGS_ME, customerId) + "?status=" +
                    status, page, size);
        else
            getRequest(callback, String.format(HttpConstant.URL_BUYER_LISTINGS_ME, customerId), page, size);
    }

    public void getAuctionDetailById(String auctionId, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(String.format(HttpConstant.URL_GET_AUCTION_DETAIL_BY_ID, auctionId)))
                .execute(sbc);
    }


    /*提交报价*/
    public void postQuotation(AbsXnHttpCallback callback, QuotationModel model) {
        OkGo.post(getAbsoluteUrl(String.format(HttpConstant.URL_QUOTATIONS, model.getBuyerListingId())))
                .upJson(JSON.toJSONString(model))
                .execute(callback(callback));
    }

    /*发货*/
    public void sent(String orderId, SentCreator sentCreator, AbsXnHttpCallback callback){
        PutRequest request = OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_SENT,orderId)));
        request.params("mode",sentCreator.getMode())
                .params("number",sentCreator.getNumber())
                .params("company",sentCreator.getCompany());
        request.addFileParams("files", sentCreator.getFiles());
        request.execute(callback(callback));

    }

    /*收货*/
    public void receive(String orderId,AbsXnHttpCallback callback){
        putRequest(callback,String.format(HttpConstant.URL_RECEIVE,orderId));
    }

    /*查询物流信息*/
    public void logistics(String orderId,AbsXnHttpCallback callback){
        OkGo.get(getAbsoluteUrl(String.format(HttpConstant.URL_LOGISTICS,orderId)))
                .execute(callback(callback));
    }


    /*采购报价的人（某条报价是谁报的）*/
    public void quotationOwner(String id, AbsXnHttpCallback callback) {
        getRequest(callback, String.format(HttpConstant.URL_QUOTATION_OWNER, id));
    }

    /*查询报价的货物图片（报价人的sellerlisting对应的图）*/
    public void quotationDocs(String id, AbsXnHttpCallback callback) {
        getRequest(callback, String.format(HttpConstant.URL_QUOTATION_DOCS, id));
    }


    /*根据ID查询报价*/
    public void quotationsById(String id, AbsXnHttpCallback callback, int page, int size) {
        getRequest(callback, String.format(HttpConstant.URL_QUOTATIONS, id), page, size);
    }

    /**
     * 上传付费凭证
     */
    public void uploadBuyOrderTopay(String buyOrderId, File file, AbsXnHttpCallback callback) {
//        List<File> files = new ArrayList<>();
//        files.add(file);

        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_UPLOAD_ORDER_TOPAY, buyOrderId)))
                .params("payFile", file)
                .execute(callback(callback));
    }


    /**
     * 更改订单状态，仅限于收货和发货
     * 请求参数：
     * status表示订单状态(发货Status.SHIP_GOODS，收货Status.RECEIVE_GOODS)
     */
    public void orderProductTransfer(String orderId, OrderStatus orderStatus, AbsXnHttpCallback callback) {
        String orderStatusStr = orderStatus.toString();

        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_ORDER_TRANSFER, orderId)))
                .params("status", orderStatus.toString())
                .execute(callback(callback));
    }


    /**
     * 拍卖报名
     * 请求参数：
     * auctionId拍卖ID
     * depositFile押金文件
     */
    public void auctionDeposits(String auctionId, File depositFile, AbsXnHttpCallback callback) {
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_AUCTION_DEPOSITS))
                .params("auctionId", auctionId)
                .params("depositFile", depositFile)
                .execute(callback(callback));

    }

    /**
     * 请求参数：JSON格式：
     * auctionId拍卖ID
     * price出价
     * 返回状态0表示成功。
     */
    public void auctionBid(String auctionId, double price, AbsXnHttpCallback callback) {
        AuctionBid auctionBid = new AuctionBid(auctionId, price);
        String jsonStr = JSON.toJSONString(auctionBid);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_AUCTION_BID))
                .upJson(jsonStr)
                .execute(callback(callback));
    }

    /**
     * 请求参数：residuePaymentFile尾款文件
     * 返回状态0表示成功。
     *
     * @param residuePaymentFile
     * @param callback
     */
    public void URL_AUCTION_RESIDUE_PAY(File residuePaymentFile, AbsXnHttpCallback callback) {

        OkGo.put(getAbsoluteUrl(HttpConstant.URL_AUCTION_RESIDUE_PAY))
                .params("residuePaymentFile", residuePaymentFile)
                .execute(callback(callback));
    }

    /*得到用户关注的产品*/
    public void favs(AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_FAVS))
                .execute(sbc);
    }

    public void favs(StringCallback callback) {
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_FAVS))
                .execute(callback);
    }

    /*添加一个关注*/
    public void addFavs(String productId, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_FAVS))
                .params("productId", productId)
                .execute(sbc);
    }

    /*删除一个关注*/
    public void deleteFav(String productId, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.delete(getAbsoluteUrl(HttpConstant.URL_FAVS) + "?productId=" + productId)
                .execute(sbc);
    }

    /*首页广告*/
    public void ads(AbsXnHttpCallback callback) {
//        StringCallback sbc = callback(callback);
//        OkGo.get(getAbsoluteUrl(HttpConstant.URL_ADS))
//                .execute(sbc);
        favsRequest(HttpConstant.URL_ADS, callback,false);
    }

    /*首页一条广告明细*/
    public void adsById(String adId, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_ADS) + "/" + adId)
                .execute(sbc);
    }

    /*得到当前用户的信息*/
    public void currentInfo(AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_CURRENTINFO))
                .execute(sbc);
    }

    /*修改用户信息/上传头像等*/
    public void updateUserInfo(String customerId, Map<String, Object> updateInfo, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        PutRequest okGo = OkGo.put(getAbsoluteUrl(HttpConstant.URL_UPDATE_USERINFO + customerId));
        Iterator<Map.Entry<String, Object>> it = updateInfo.entrySet().iterator();
        okGo.params("id", customerId);
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            if (entry.getKey().equals("coverImg") || entry.getKey().equals("headImg")) {
                File file = (File) entry.getValue();
                List<File> files = new ArrayList<>();
                files.add(file);
                okGo.addFileParams(entry.getKey(), files);
            } else {
                okGo.params(entry.getKey(), (String) entry.getValue());
            }
        }
        okGo.execute(sbc);
    }


    /*添加用户验证申请*/
    public void verificationPerson(VerificationReq verificationReq,AbsXnHttpCallback callback){
        PostRequest request = OkGo.post(getAbsoluteUrl(HttpConstant.URL_VERFICATION_PERSON));
        request.params("idCardPositive",verificationReq.getIdCardNegative());
        request.params("idCardNegative",verificationReq.getIdCardPositive());
        request.params("handHeldIdCard",verificationReq.getHandHeldIdCard());
        request.params("realName",verificationReq.getRealName());
        request.params("idCardNo",verificationReq.getIdCardNo());
        request.execute(callback(callback));
    }


    /*修改用户验证*/
    public void updateVerification(String reqId,VerificationReq verificationReq,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        PutRequest request = OkGo.put(getAbsoluteUrl(HttpConstant.URL_VERFICATION_PERSON)+"/"+reqId);
        if (verificationReq.getIdCardPositive()!=null)
        request.params("idCardPositive",verificationReq.getIdCardNegative());
        if (verificationReq.getIdCardNegative()!=null)
        request.params("idCardNegative",verificationReq.getIdCardPositive());
        if (verificationReq.getHandHeldIdCard()!=null)
        request.params("handHeldIdCard",verificationReq.getHandHeldIdCard());
        if (verificationReq.getRealName()!=null)
        request.params("realName",verificationReq.getRealName());
        if (verificationReq.getIdCardNo()!=null)
        request.params("idCardNo",verificationReq.getIdCardNo());
        request.execute(callback(callback));
    }



    /*获得所有的提供支持*/
    public void providerSupports(AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_PROVIDE_SUPPORTS))
                .execute(sbc);
    }


    public void getSpecByProductId(String productId, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(String.format(HttpConstant.URL_SPCES_BY_PRODUCTID, productId)))
                .execute(sbc);

    }


    public void getSpecConfigsByProductId(String productId, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(String.format(HttpConstant.URL_SPCESCONFIGS_BY_PRODUCTID, productId)))
                .execute(sbc);
    }


    /*我要卖列表*/
    public void buyerListings(int page, int size, AbsXnHttpCallback callback) {
        favsRequest(HttpConstant.URL_BUYER_LISTINGS, page, size, callback);
    }

    public void buyerListingById(String id, AbsXnHttpCallback callback) {
        getRequest(callback, HttpConstant.URL_BUYER_LISTINGS + "/" + id);
    }

/**
 * -----------------------钱包---------------------
 * */

    /*支付宝*/
    public void pay(AbsXnHttpCallback callback, String orderNo) {
        StringCallback sbc = callback(callback);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_ALIPAY))
                .params("orderNo", orderNo)
                .execute(sbc);
    }

    /*钱包*/
    public void wallets(AbsXnHttpCallback callback){
        getRequest(callback,HttpConstant.URL_WALLETS);
    }


    /*查询钱包里面的收支记录*/
    public void financeRecords(int page,int size,AbsXnHttpCallback callback){
        getRequest(callback,HttpConstant.URL_FINANCE_RECORDS,page,size);
    }

    /*是否设置了支付密码*/
    public void hasPwd(AbsXnHttpCallback callback){
        getRequest(callback,HttpConstant.URL_HAS_PWD);
    }

    /*设置支付密码*/
    public void setPwd(String pwd,String code,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(HttpConstant.URL_SET_PWD))
                .params("password",pwd)
                .params("verificationCode",code)
                .execute(sbc);
    }

    /*设置密码时候发送验证码*/
    public void setPwdCode(AbsXnHttpCallback callback){
        getRequest(callback,HttpConstant.URL_SET_PWD_CODE);
    }

    public void sendVerify(AbsXnHttpCallback callback){
        getRequest(callback,HttpConstant.URL_SET_PWD_CODE);
    }

    public void updatePwd(String pwd,String newPwd,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(HttpConstant.URL_UPDATE_PWD))
                .params("password",pwd)
                .params("newPassword",newPwd)
                .execute(sbc);
    }

    /*提现*/
    public void withdraw(BigDecimal amount,String accountId,String verificationCode,String paymentPwd,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(HttpConstant.URL_WITHDRAW))
                .params("amount",amount.doubleValue())
                .params("accountId",accountId)
                .params("verificationCode",verificationCode)
                .params("paymentPwd",paymentPwd)
                .execute(sbc);
    }


    /*查询是否可以提现*/
    public void canWithdraw(AbsXnHttpCallback callback){
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_CAN_WITHDRAW))
                .execute(callback(callback));
    }

    /*添加银行账号*/
    public void addBanks(BankModel bankModel,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_BANKS))
                .upJson(JSON.toJSONString(bankModel))
                .execute(sbc);
    }

    /*修改银行账号*/
    public void updateBank(BankModel bankModel,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(HttpConstant.URL_BANKS)+"/"+bankModel.getId())
                .upJson(JSON.toJSONString(bankModel))
                .execute(sbc);
    }


    public void getBanks(AbsXnHttpCallback callback){
        getRequest(callback,HttpConstant.URL_BANKS);
    }

    /*添加银行账号时候发送的验证码*/
    public void banksVerify(AbsXnHttpCallback callback){
        getRequest(callback,HttpConstant.URL_BANKS_VERIFY);
    }

    /*退款费率*/
    public void feerate(AbsXnHttpCallback callback){
        getRequest(callback,HttpConstant.URL_REFUND_FEERATE);
    }




    public void getRequest(AbsXnHttpCallback callback, String url) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(url))
                .execute(sbc);
    }

    public void putRequest(AbsXnHttpCallback callback, String url){
        StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(url))
                .execute(sbc);
    }


    public void getRequest(AbsXnHttpCallback callback, String url, int page, int size) {
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(url))
                .params("page", page)
                .params("size", size)
                .execute(sbc);
    }

    private void postRequest(AbsXnHttpCallback callback, String url, Object obj) {
        StringCallback sbc = callback(callback);
        OkGo.post(getAbsoluteUrl(url))
                .upJson(JSON.toJSONString(obj))
                .execute(sbc);
    }


    public void deleteRequest(AbsXnHttpCallback callback, String url) {
        StringCallback sbc = callback(callback);
        OkGo.delete(getAbsoluteUrl(url))
                .execute(sbc);
    }

    /*发布采购*/
    public void publishBuy(PublishBuyInfoModel model, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_BUYER_LISTINGS))
                .upJson(JSON.toJSONString(model))
                .execute(sbc);
    }

    /*查询采购*/
    public void publishBuy(AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_BUYER_LISTINGS))
                .execute(sbc);
    }


    /*删除采购*/
    public void delPublishBuy(String id,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.delete(getAbsoluteUrl(HttpConstant.URL_BUYER_LISTINGS)+"/"+id)
                .execute(sbc);
    }


    public void publishStateChange(AbsXnHttpCallback callback, PublishStates state, String id) {
        StringCallback sbc = callback(callback);
        OkGo.put(getAbsoluteUrl(String.format(HttpConstant.URL_PUBLISH_STATE_CHANGE, id)))
                .params("status", state.name())
                .execute(sbc);
    }


    /*删除一个发布的供货*/
    public void publishDeleteById(AbsXnHttpCallback callback, String id) {
        deleteRequest(callback, String.format(HttpConstant.URL_GET_PRO_BY_ID, id));
    }

    /*查询喜农服务费率*/
    public void feeFate(AbsXnHttpCallback callback) {
        getRequest(callback, HttpConstant.URL_FEE_FATE);
    }


    /**
     *-----------------------行情-----------------------
     */
    public void pricesProduct(String productId,AbsXnHttpCallback callback){
        getRequest(callback,HttpConstant.URL_PRICES_PRODUCT+"&productId="+productId);
    }


    public void priceSpceOrCity(String specId,String cityId,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_PRICES_SPCE_OR_CITY)+"/"+specId+"/"+cityId)

                .execute(sbc);
    }


    /**
     * 普通的请求回调
     */
    public StringCallback callback(final XnHttpCallback callback) {
        StringCallback cb = new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {

                ((BaseActivity) mContext).cancelProgress();

                if (TextUtils.isEmpty(s)) {
                    T.showShort(mContext, "响应为空，请检查网络");
                } else {
                    JSONObject jsonObject = JSON.parseObject(s);
                    int code = jsonObject.getInteger("c");
                    String info = jsonObject.getString("i");
                    String result = jsonObject.getString("r");
                    switch (code) {
                        case XinongHttpCommend.SUCCESS://成功
                            callback.onSuccess(info, result);
                            break;
                        case XinongHttpCommend.LOGIN:
                        case XinongHttpCommend.EXCEPTION:
                        case XinongHttpCommend.ERROR://出错
                            if (response != null) {
                                if (response.code() == 401) {
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    mContext.startActivity(intent);
                                    ((BaseActivity) mContext).finish();
                                    return;
                                } else if (response.code() == 404) {
                                    T.showShort(mContext, "没有找到接口---NOT FOUND");
                                }
                            }
                            T.showShort(mContext, info);
                            if (callback != null)
                                callback.onError(info);
                            break;
                        default:
                            new IllegalArgumentException("没有指定的该状态码");
                    }

                }
            }


            /** 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程 */
            @Override
            public void onError(Call call, Response response, Exception e) {
                ((BaseActivity) mContext).cancelProgress();
                if (response != null) {
                    if (response.code() == 401) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                        //((BaseActivity) mContext).finish();
                        return;
                    } else if (response.code() == 404) {
                        T.showShort(mContext, "没有找到接口---NOT FOUND");
                    }
                }
                callback.onHttpError(call, response, e);
                T.showShort(mContext, "网络请求错误，请检查网络");

            }

        };

        return cb;
    }


    /**
     * 得到完整的URL，拼接主机
     *
     * @param url
     * @return
     */
    public String getAbsoluteUrl(String url) {
        return HttpConstant.HOST + url;
    }

    public void getProductImg(AbsXnHttpCallback callback, String id) {
        getRequest(callback, String.format(HttpConstant.URL_PRODUCT_IMG, id));
    }



    private void getRequestPutParam(AbsXnHttpCallback callback, String url, Map<String, String> params) {
        StringCallback sbc = callback(callback);
        url += "?";
        for (String key : params.keySet()) {
            url += key + "=" + params.get(key) + "&";
        }
        GetRequest request = OkGo.get(getAbsoluteUrl(url.substring(0, url.length() - 1)));
        request.execute(sbc);
    }


    /**
     * 登陆用包装类
     */
    private class LoginBean {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


    /**
     * 拍卖报名包装类
     */
    private class AuctionBid {

        public AuctionBid(String auctionId, double price) {
            this.auctionId = auctionId;
            this.price = price;
        }

        private String auctionId;//拍卖ID
        private double price;//出价

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getAuctionId() {
            return auctionId;
        }

        public void setAuctionId(String auctionId) {
            this.auctionId = auctionId;
        }
    }
}
