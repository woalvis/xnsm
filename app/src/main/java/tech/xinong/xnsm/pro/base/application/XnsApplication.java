package tech.xinong.xnsm.pro.base.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.vondear.rxtools.RxTool;

import org.litepal.LitePal;

import okhttp3.OkHttpClient;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.util.L;
import tech.xinong.xnsm.util.XnsConstant;

/**
 * Created by xiao on 2016/11/17.
 */
public class XnsApplication extends Application {

    public String token;
    private static Context mContext;
    public static XnsApplication mInstance;
    private OkHttpClient okHttpClient;
    public SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    private static int timeOut = 20000;

    @Override
    public void onCreate() {

//       //  设置Thread Exception Handler
//        UnCatchExceptionHandler catchException = new UnCatchExceptionHandler(this);
//        Thread.setDefaultUncaughtExceptionHandler(catchException);
//        //在这里为应用设置异常处理程序，然后我们的程序才能捕获未处理的异常
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);


        /**
         * 初始化Log工具类
         * true为打印日志用作开发调试
         * 发布版本时候应该改为false不用日志输出了
         */
        L.isDebug = true;
        mContext = this;
        mInstance = this;
        /**
         * 初始化Fresco
         */
        okHttpClient = new OkHttpClient();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(mContext, okHttpClient)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this,config);
        initOkHttp();
        RxTool.init(this);
        LitePal.initialize(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void initOkHttp() {
        mSharedPreferences = getSharedPreferences(XnsConstant.SP_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        token = mSharedPreferences.getString(XnsConstant.TOKEN, "");
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpConstant.HTTP_HEADER_TOKEN, token);
        //-----------------------------------------------------------------------------------//
        //必须调用初始化
        OkGo.init(this);
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                    .debug("OkGo")

                    //如果使用默认的 60秒,以下三行也不需要传
//                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)  //全局的连接超时时间
//                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)     //全局的读取超时时间
//                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
                    .setConnectTimeout(timeOut)  //全局的连接超时时间
                    .setReadTimeOut(timeOut)     //全局的读取超时时间
                    .setWriteTimeOut(timeOut)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //如果不想让框架管理cookie,以下不需要
//                .setCookieStore(new MemoryCookieStore())                //cookie使用内存缓存（app退出后，cookie消失）
                    .setCookieStore(new PersistentCookieStore())          //cookie持久化存储，如果cookie不过期，则一直有效

                    //可以设置https的证书,以下几种方案根据需要自己设置,不需要不用设置
//                    .setCertificates()                                  //方法一：信任所有证书
//                    .setCertificates(getAssets().open("srca.cer"))      //方法二：也可以自己设置https证书
//                    .setCertificates(getAssets().open("aaaa.bks"), "123456", getAssets().open("srca.cer"))//方法三：传入bks证书,密码,和cer证书,支持双向加密

                    //可以添加全局拦截器,不会用的千万不要传,错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

                    //这两行同上,不需要就不要传
                    .addCommonHeaders(headers);                                          //设置全局公共参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 当系统处于资源匮乏状态时，具有良好行为得应用程序可以释放额外的内存。
     * 这个方法一般只会在后台进程已经终止，但是前台应用程序仍然缺少内存时调用。
     * 可以重写这个处理程序来清空缓存或者释放不必要的资源
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 作为，onLowMemory的一个特定于应用程序的替代选择，在Android4.0(API level 13)中引入。
     * 当运行时决定当前应用程序应该尝试减少其内存开销时（通常在它进入后台时）调用。
     * 它包含一个level参数，用于提供请求的上下文。
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    /**
     * 与Activity不同，在配置改变时，应用程序对象不会被终止和重启。
     * 如果应用程序使用的值依赖于特定的配置，则重写这个方法来重新加载这些值，
     * 或者在应用程序级别处理配置改变。
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public static Context getAppContext(){
        return mContext;
    }
}
