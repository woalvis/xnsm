package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.MainActivity;
import tech.xinong.xnsm.pro.base.view.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText etPhoneNum;
    private EditText etVerify;
    private Button loginSendVerify;
    private Button login;

    private String userPhoneNum;

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
                login();
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
        mOkHttpClient = new OkHttpClient();
        final String phone = etPhoneNum.getText().toString().trim();
        String urlRegister = HttpConstant.HOST + HttpConstant.URL_LOGIN_VERIFY + "?cellphone=" + phone;
        Request.Builder requestBuilder = new Request.Builder().url(urlRegister);
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.i("wangshu", "cache---" + str);
                } else {
                    response.body().string();
                    String str = response.networkResponse().toString();
                    Log.i("wangshu", "network---" + str);
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

    /**
     * 登录
     */
    private void login() {

        String phoneNum = etPhoneNum.getText().toString().trim();
        String verifyCode = etVerify.getText().toString().trim();
        String urlLogin = HttpConstant.HOST+HttpConstant.URL_LOGIN;
        mOkHttpClient=new OkHttpClient();
        Login login = new Login();
        login.setCellphone(phoneNum);
        login.setVerificationCode(verifyCode);
        String json = JSON.toJSONString(login);
        userPhoneNum = phoneNum;
        RequestBody formBody = RequestBody.create(RegisterActivity.JSON,json);

        Request request = new Request.Builder()
                .url(urlLogin)
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
                    Headers headers = response.headers();
                    final String token = headers.get("X-ACCESS-TOKEN");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(token)){
                                Toast.makeText(LoginActivity.this, "登陆不成功，请重试", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("phone",userPhoneNum);
                                LoginActivity.this.startActivity(intent);
                                LoginActivity.this.finish();
                            }
                        }
                    });

                    String str = response.networkResponse().toString();
                    Log.i("xzj", "network---" + str);
                }

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


    private class Login{
        private String cellphone;
        private String verificationCode;

        public String getCellphone() {
            return cellphone;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public String getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
        }
    }
}
