package tech.xinong.xnsm.pro.user.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhoneNum;
    private EditText etRegisterVerifyNum;
    private Button reqVerify;
    private OkHttpClient mOkHttpClient;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initWidget();
        initOkHttpClient();
    }

    private void initWidget() {
        etPhoneNum = (EditText) this.findViewById(R.id.register_phone_num);
        reqVerify = (Button) this.findViewById(R.id.register_req_verify);
        etRegisterVerifyNum = (EditText) this.findViewById(R.id.register_verify_num);
        reqVerify.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_req_verify:
                String verify = etPhoneNum.getText().toString().trim();
                reqVerify(verify);
                break;
            case R.id.register_register:
                try {
                    register();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void register() throws IOException {
        String url = HttpConstant.HOSRT+HttpConstant.URL_REGISTER;
        Register register = new Register();
        register.setCellPhone(etPhoneNum.getText().toString().trim());
        register.setVerificationCode(etRegisterVerifyNum.getText().toString().trim());
        RequestBody formBody = new FormBody.Builder()
                .add("verificationCode ", register.getVerificationCode())
                .add("cellphone",register.getCellPhone())
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.i("wangshu", str);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    private void reqVerify(String verify) {
        String urlRegister = HttpConstant.HOSRT + HttpConstant.URL_SEND_VERIFY + "?cellphone=" + verify;
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


    public class Register {
        String cellPhone;
        String verificationCode;

        public Register() {
        }

        public String getCellPhone() {
            return cellPhone;
        }

        public void setCellPhone(String cellPhone) {
            this.cellPhone = cellPhone;
        }

        public String getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
        }
    }
}
