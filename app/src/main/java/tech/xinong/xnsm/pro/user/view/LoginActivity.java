package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.MainActivity;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.user.presenter.LoginPresenter;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity<LoginPresenter> implements View.OnClickListener,LoginView {
    private EditText etPhoneNum;
    private EditText etPassword;
    private Button login;
    /*导航栏标题显示*/
    @ViewInject(R.id.tv_center)
    private TextView tvCenter;
    /*导航栏右侧文字*/
    @ViewInject(R.id.tv_right)
    private TextView tvRight;
    /*新型EditText的布局-----用于输入用户名*/
    @ViewInject(R.id.text_input_username)
    private TextInputLayout textInputUsername;
    /*新型EditText的布局-----用于输入密码*/
    @ViewInject(R.id.text_input_password)
    private TextInputLayout textInputPassword;
    /*导航栏的布局*/
    @ViewInject(R.id.my_order_navigation)
    private LinearLayout navigationLayout;


    @Override
    public void initWidget() {
        initNavigation();
        login = (Button) this.findViewById(R.id.login_bt_login);
        login.setOnClickListener(this);
        textInputUsername.setHint(getResources().getString(R.string.phone_num_hint));
        etPhoneNum = textInputUsername.getEditText();
        textInputPassword.setHint(getResources().getString(R.string.input_password));
        etPassword = textInputPassword.getEditText();
    }

    @Override
    public void initData() {

    }

    @Override
    public LoginPresenter bindPresenter() {
        return new LoginPresenter(mContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt_login:
                String phoneNum = etPhoneNum.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                login(phoneNum, password);
                break;
            default:
                break;
        }

    }


    private void initNavigation() {
        tvCenter.setVisibility(View.VISIBLE);
        tvCenter.setText("登录");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("注册");
        tvRight.setTextColor(Color.WHITE);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(RegisterActivity.class);
            }
        });
    }


    /**
     * 登录
     */
    private void login(final String name, final String password) {
        getPresenter().login(name,password,editor);
    }


    @Override
    public void onLoginSuccess() {
        T.showShort(mContext, "登陆成功");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        LoginActivity.this.startActivity(intent);
        LoginActivity.this.finish();
    }

    @Override
    public void onLoginFiled() {
        T.showShort(mContext, "用户名或密码输入不正确，请重新输入");
        etPassword.setText("");
    }

}
