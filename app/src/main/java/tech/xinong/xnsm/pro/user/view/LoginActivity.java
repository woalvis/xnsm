package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.MainActivity;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.DeviceInfoUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText etPhoneNum;
    private EditText etPassword;
    private Button login;



    @Override
    protected int bindView() {
        return R.layout.activity_login;
    }

    @Override
    public void initWidget() {
        etPhoneNum = (EditText) this.findViewById(R.id.login_et_username);
        etPassword = (EditText) this.findViewById(R.id.login_et_password);
        login = (Button) this.findViewById(R.id.login_bt_login);

        login.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_bt_login:

                String phoneNum = etPhoneNum.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                login(phoneNum,DeviceInfoUtil.generateMD5(password));
                break;
            default:break;
        }

    }


    /**
     * 登录
     */
    private void login(final String name, final String password) {

        XinongHttpCommend.getInstence(mContext).login(name, password, new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                editor.putString("username",name);
                editor.putString("password",password);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
        });
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
