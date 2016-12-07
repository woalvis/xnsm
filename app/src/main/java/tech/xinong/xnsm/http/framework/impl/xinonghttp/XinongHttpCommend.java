package tech.xinong.xnsm.http.framework.impl.xinonghttp;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import tech.xinong.xnsm.http.framework.IHttpCommand;
import tech.xinong.xnsm.http.framework.IRequestParam;
import tech.xinong.xnsm.http.framework.impl.RequestParam;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.XnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.buy.model.BuyOrderModel;
import tech.xinong.xnsm.pro.publish.model.PublishSellInfoModel;
import tech.xinong.xnsm.pro.user.view.LoginActivity;
import tech.xinong.xnsm.pro.user.view.RegisterActivity;

import static tech.xinong.xnsm.http.framework.utils.HttpConstant.URL_BUY_NOW;
import static tech.xinong.xnsm.http.framework.utils.HttpConstant.URL_GET_ALL_ORDERS;
import static tech.xinong.xnsm.http.framework.utils.HttpConstant.URL_GET_ORDER_BY_ID;
import static tech.xinong.xnsm.http.framework.utils.HttpConstant.URL_GET_PRO_BY_ID;

/**
 * Created by Dream on 16/6/11.
 */
public class XinongHttpCommend implements IHttpCommand<RequestParam> {
    private OkHttpClient mOkHttpClient;
    private Context mContext;
    public String token;
    private volatile static XinongHttpCommend instence;
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int LOGIN = 2;
    public static final int EXCEPTION = 3;


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
    public static XinongHttpCommend getInstence(Context context) {
        if (instence == null) {
            synchronized (XinongHttpCommend.class) {
                if (instence == null) {
                    instence = new XinongHttpCommend(context);
                }
            }
        }
        return instence;
    }

    @Override
    public String execute(String url, IRequestParam<RequestParam> requestParam) {
        return null;
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

    /**
     * 请求别人发布的信息
     */
    public void getListings(AbsXnHttpCallback callback) {

        StringCallback scb = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_LISTINGS))     // 请求方式和请求url
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(scb);
    }


    /**
     * 登录，需要用户名和密码，密码用MD5进行加密
     */
    public void login(String username, String password, StringCallback callback) {
      //  StringCallback scb = callback(callback);
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
        OkGo.get(String.format(getAbsoluteUrl(URL_GET_PRO_BY_ID), proId))
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
     *注册用户
     */
    public void registerUser(RegisterActivity.Register register, AbsXnHttpCallback callback) {
        StringCallback sbc = callback(callback);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_REGISTER))
                .upJson(JSON.toJSONString(register))
                .execute(sbc);
    }


     /*根据产品的ID得到所有的规格*/
    public void getAllSpecsByproductId(String id,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.get(String.format(getAbsoluteUrl(HttpConstant.URL_GET_ALLSPECS_BY_PRODUCTID),id))
            .execute(sbc);
    }

    /*得到所有的物流方式*/
    public void getAllLogisticMethods(AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(HttpConstant.URL_GET_LOGISTIC_METHODS))
                .execute(sbc);
    }


    /*发布卖货信息*/
    public void pulishSellInfo(PublishSellInfoModel publishSellInfoModel, AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        String jsonStr = JSON.toJSONString(publishSellInfoModel);
        OkGo.post(getAbsoluteUrl(HttpConstant.URL_LISTINGS_SELL))
                .upJson(jsonStr)
                .execute(sbc);
    }


    /*立即购买，创建订单*/
    public void buyNow(BuyOrderModel buyOrderModel,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        String jsonStr = JSON.toJSONString(buyOrderModel);
        OkGo.post(getAbsoluteUrl(URL_BUY_NOW))
                .upJson(jsonStr)
                .execute(sbc);
    }


    /*通过订单的id，得到订单的详情*/
    public void getOrderDetailById(String id,AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(String.format(URL_GET_ORDER_BY_ID,id)))
                .execute(sbc);

    }

     /*得到该用户的所有订单*/
    public void getAllOrders(AbsXnHttpCallback callback){
        StringCallback sbc = callback(callback);
        OkGo.get(getAbsoluteUrl(URL_GET_ALL_ORDERS))
                .tag(this)// 请求的 tag, 主要用于取消对应的请求
                .cacheKey("getCategories")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .execute(sbc);
    }


    /**
     * 普通的请求回调
     */
    public StringCallback callback(final XnHttpCallback callback) {
        StringCallback cb = new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (TextUtils.isEmpty(s)) {
                    Toast.makeText(mContext, "响应为空，请检查网络", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
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
               if (response!=null) {
                    if (response.code() == 401) {
                        Toast.makeText(mContext, "Token过期,请重新登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                        return;
                    }
                }
                callback.onHttpError(call, response, e);
                Toast.makeText(mContext, "网络请求错误，请检查网络", Toast.LENGTH_SHORT).show();
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
}
