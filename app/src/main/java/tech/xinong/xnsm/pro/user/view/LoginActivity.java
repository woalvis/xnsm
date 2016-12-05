package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;

import okhttp3.Call;
import okhttp3.Response;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.MainActivity;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText etPhoneNum;
    private EditText etPassword;
    private Button login;


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
                login(phoneNum,password);
                break;
            default:break;
        }

    }


    /**
     * 登录
     */
    private void login(final String name, final String password) {

        XinongHttpCommend.getInstence(mContext).login(name, password, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                String token =response.header(HttpConstant.HTTP_HEADER_TOKEN);
                HttpHeaders headers = new HttpHeaders();
                headers.put(HttpConstant.HTTP_HEADER_TOKEN,token);

                /*登陆成功后将用户名密码，还有得到的token存入文件中*/
                editor.putString(XnsConstant.USER_NAME,name);
                editor.putString(XnsConstant.PASSWORD,password);
                editor.putString(XnsConstant.TOKEN,token);
                editor.commit();

                //将token放入到header
                OkGo.getInstance().addCommonHeaders(headers);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                Toast.makeText(mContext, "用户名或密码输入不正确，请重新输入", Toast.LENGTH_SHORT).show();
                etPassword.setText("");
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
