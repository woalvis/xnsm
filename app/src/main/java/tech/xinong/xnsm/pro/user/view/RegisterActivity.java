package tech.xinong.xnsm.pro.user.view;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;


/**
 * 注册界面，首先用手机号得到注册码，再通过注册码和自己填写的密码手机号完成注册
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etPhoneNum;//手机号码编辑栏
    private EditText etRegisterVerifyNum;//注册码编辑栏
    private Button reqVerify;//发送注册码按钮
    private Button registerUser;//点击此按钮发送手机号注册码和密码完成注册
    private LinearLayout passwordLayout;//密码编辑布局

    private TextInputLayout inputPassword;
    private TextInputLayout inputConfirmPassword;

    private OkHttpClient mOkHttpClient;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initWidget();
        initOkHttpClient();
    }


    /**
     * 初始化控件
     */
    private void initWidget() {
        etPhoneNum = (EditText) this.findViewById(R.id.register_phone_num);
        reqVerify = (Button) this.findViewById(R.id.register_req_verify);
        etRegisterVerifyNum = (EditText) this.findViewById(R.id.register_verify_num);
        registerUser = (Button) this.findViewById(R.id.register_register);
        passwordLayout = (LinearLayout) this.findViewById(R.id.register_password_layout);


        reqVerify.setOnClickListener(this);
        registerUser.setOnClickListener(this);
    }


    /**
     * 初始化OkHttp
     */
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


    /**
     * 控件点击事件回调
     * @param v
     */
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


    /**
     * 注册
     * @throws IOException
     */
    private void register() throws IOException {

        Register register = new Register();
        register.setCellphone(etPhoneNum.getText().toString().trim());
        register.setVerificationCode(etRegisterVerifyNum.getText().toString().trim());
        register.setPassword("123456");
        register.setConfirmPassword("123456");

        XinongHttpCommend.getInstence(this).registerUser(register, new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                Toast.makeText(RegisterActivity.this,info,Toast.LENGTH_SHORT).show();
            }
        });




//        String url = HttpConstant.HOST+HttpConstant.URL_REGISTER;
//        Register register = new Register();
//        register.setCellphone(etPhoneNum.getText().toString().trim());
//        register.setVerificationCode(etRegisterVerifyNum.getText().toString().trim());
//
//        String json = com.alibaba.fastjson.JSON.toJSONString(register);
//        Log.i("xx",json);
//        RequestBody formBodyT = RequestBody.create(JSON,json);
//
//
//        Request request = new Request.Builder()
//                .url(url)
//                .post(formBodyT)
//                .build();
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("XX","出错了");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//               if (!TextUtils.isEmpty(response.toString())){
//                   String str = response.body().string();
//                   JSONObject result = JSONObject.parseObject(str);
//                   int c = result.getInteger("c");
//                   Log.i("wangshu", str);
//                   final String info = JSONObject.parseObject(str).getString("i");
//                   if (c==0){
//                       runOnUiThread(new Runnable() {
//                           @Override
//                           public void run() {
//                              // login();
//
//                               Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
//                           }
//                       });
//                       RegisterActivity.this.finish();
//                   }else {
//
//                       runOnUiThread(new Runnable() {
//                           @Override
//                           public void run() {
//                               Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
//                           }
//                       });
//
//                   }
//               }
//            }
//
//        });
    }


    /**
     * 发送验证码
     * @param cellPhone
     */
    private void reqVerify(String cellPhone) {
        XinongHttpCommend.getInstence(this).registerVerify(cellPhone, new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                passwordLayout.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 注册信息包装类
     */
    public class Register {
        String cellphone;
        String verificationCode;
        String password;
        String confirmPassword;

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Register() {
        }

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
