package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.user.presenter.RegisterPresenter;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


/**
 * 注册界面，首先用手机号得到注册码，再通过注册码和自己填写的密码手机号完成注册
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements View.OnClickListener,RegisterView {
    @ViewInject(R.id.register_phone_num)
    private EditText etPhoneNum;                     //手机号码编辑栏
    @ViewInject(R.id.register_verify_num)
    private EditText etRegisterVerifyNum;            //注册码编辑栏
    @ViewInject(R.id.register_request_verify)
    private TextView reqVerify;                      //请求注册码文本控件
    @ViewInject(R.id.register_register)
    private Button registerUser;                     //点击此按钮发送手机号注册码和密码完成注册
    @ViewInject(R.id.register_password_layout)
    private LinearLayout passwordLayout;             //密码编辑布局
    @ViewInject(R.id.register_text_input_password)
    private TextInputLayout inputPassword;           //
    @ViewInject(R.id.register_text_input_confirm)
    private TextInputLayout inputConfirmPassword;
    @ViewInject(R.id.tv_login)
    private TextView tv_login;
    private OkHttpClient mOkHttpClient;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    @ViewInject(R.id.tv_center)
    private TextView centerTv;

    private CountDownTimer timer;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOkHttpClient();
    }


    /**
     * 初始化控件
     */
    @Override
    public void initWidget() {

        initNavigation();

        reqVerify.setOnClickListener(this);
        registerUser.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        etPassword = inputPassword.getEditText();
        etPasswordConfirm = inputConfirmPassword.getEditText();


        etPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwordStr = etPassword.getText().toString();
                if (TextUtils.isEmpty(passwordStr)){
                    return;
                }else {
                    if (passwordStr.equals(s.toString())){
                        inputConfirmPassword.setErrorEnabled(false);
                    }else {
                        inputConfirmPassword.setErrorEnabled(true);
                        inputConfirmPassword.setError("两次输入的不一致");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * 倒计时器，短信验证时间倒计时
         */
        timer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                reqVerify.setText(millisUntilFinished/1000+"s后重新发送");
            }

            @Override
            public void onFinish() {
                reqVerify.setBackgroundResource(R.drawable.button_shape_orange);
                reqVerify.setText("重新发送验证码");
                reqVerify.setPadding(10,10,10,10);
                reqVerify.setClickable(true);
            }
        };
    }

    @Override
    public void initData() {

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
            case R.id.register_request_verify:
                String verify = etPhoneNum.getText().toString().trim();
                reqVerify(verify);
                break;
            case R.id.register_register:
                register();
                break;
            case R.id.tv_login:
                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    /**
     * 注册
     * @throws IOException
     */
    private void register() {
        getPresenter().registerUser(etPhoneNum.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                etRegisterVerifyNum.getText().toString().trim());
//
//        XinongHttpCommend.getInstance(this).registerUser(register, new AbsXnHttpCallback() {
//            @Override
//            public void onSuccess(String info, String result) {
//                T.showShort(RegisterActivity.this,info);
//            }
//        });
    }


    /**
     * 发送验证码
     * @param cellPhone
     */
    private void reqVerify(String cellPhone) {
        XinongHttpCommend.getInstance(this).registerVerify(cellPhone, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                passwordLayout.setVisibility(View.VISIBLE);
                T.showShort(getApplicationContext(), "请求成功");
                timer.start();
            }
        });
    }


    @Override
    public void onRegisterSuccess() {
        T.showShort(this,"注册成功");
        skipActivity(LoginActivity.class);
    }

    private void initNavigation(){
        centerTv.setVisibility(View.VISIBLE);
        centerTv.setText(getResources().getString(R.string.register));
    }

    @Override
    public RegisterPresenter bindPresenter() {
        return new RegisterPresenter(this);
    }
}
