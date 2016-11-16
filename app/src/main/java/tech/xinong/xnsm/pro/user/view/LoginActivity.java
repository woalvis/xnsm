package tech.xinong.xnsm.pro.user.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.view.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText etPhoneNum;
    private EditText etVerify;
    private Button loginSendVerify;
    private Button login;

    private OkHttpClient mOkHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidget();
    }

    private void initWidget() {
        etPhoneNum = (EditText) this.findViewById(R.id.login_et_phone_num);
        etVerify = (EditText) this.findViewById(R.id.login_et_verify_num);
        loginSendVerify = (Button) this.findViewById(R.id.login_send_verify);
        login = (Button) this.findViewById(R.id.login_login);
        loginSendVerify.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_login:
               // login();
                break;
            case R.id.login_send_verify:
                sendVerify();
                
                break;
            default:break;
        }

    }


    /**
     * 向服务器请求验证码
     */
    private void sendVerify() {

    }

    /**
     * 登录
     */
    private void login(String username,String password) {
        mOkHttpClient=new OkHttpClient();
    //    Request.Builder requestBuilder = new Request.Builder().url(HttpConstant.HOSRT+HttpConstant.URL_LOGIN);
        //可以省略，默认是GET请求
   //     requestBuilder.method("GET",null);
//        Request request = requestBuilder.build();
//         RequestBody formBody = new FormEncodingBuilder()
//            .add("platform", "android")
//            .add("name", "bug")
//            .add("subject", "XXXXXXXXXXXXXXX")
//            .build();



        RequestBody formBody = new FormBody.Builder()
                .add("username ", username)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url(HttpConstant.HOSRT+HttpConstant.URL_LOGIN)
                .post(formBody)
                .build();
        Call mcall= mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.i("xzj", "cache---" + str);
                } else {
                    response.body().string();
                    String str = response.networkResponse().toString();
                    Log.i("xzj", "network---" + str);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getAsynHttp() {

    }



    private void initOkHttpClient() {
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
    }
}
