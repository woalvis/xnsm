package tech.xinong.xnsm.pro.wallet;

import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_wallet_reset)
public class WalletResetActivity extends BaseActivity {
    @ViewInject(R.id.tv_help)
    private TextView tv_help;
    @ViewInject(R.id.tv_confirm)
    private TextView tv_confirm;
    @ViewInject(R.id.et_paypswd_re)
    private EditText et_paypswd_re;
    @ViewInject(R.id.et_paypswd)
    private EditText et_paypswd;
    @ViewInject(R.id.set_pwd_request_verify)
    private TextView set_pwd_request_verify;
    @ViewInject(R.id.pwd_verify_num)
    private EditText pwd_verify_num;
    private String code;
    private CountDownTimer timer;
    @Override
    public void initData() {
        String phone = mSharedPreferences.getString("loginName"," ");
        String phoneStr = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");;
        String text = "本次操作需要验证手机号吗，请输入<font color='#FF0000'><bold>"+phoneStr+"</bold></font>收到的短信验证码";
        tv_help.setText(Html.fromHtml(text));

        timer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                set_pwd_request_verify.setText(millisUntilFinished/1000+"s后重新发送");
                set_pwd_request_verify.setBackgroundResource(R.drawable.tv_grey_bg);
                set_pwd_request_verify.setTextColor(getColorById(R.color.black_404040));
            }

            @Override
            public void onFinish() {
                set_pwd_request_verify.setBackgroundResource(R.drawable.button_shape_orange);
                set_pwd_request_verify.setText("重新发送验证码");
                set_pwd_request_verify.setTextColor(getColorById(R.color.white));
                set_pwd_request_verify.setPadding(10,10,10,10);
                set_pwd_request_verify.setClickable(true);
            }
        };

    }


    @Override
    @OnClick({R.id.tv_confirm,R.id.set_pwd_request_verify})
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm:
                String pwd = et_paypswd.getText().toString();
                String pwdRe = et_paypswd_re.getText().toString();
                code = pwd_verify_num.getText().toString();
                if (TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwdRe)){
                    T.showShort(mContext,"请检查您的输入");
                }else if (!pwd.equals(pwdRe)){
                    T.showShort(mContext,"两次输入不一样，请重新输入");
                }else if (TextUtils.isEmpty(code)) {
                    T.showShort(mContext,"请输入验证码");
                }else{
                    XinongHttpCommend.getInstance(mContext).setPwd(pwd, code,new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext,"设置密码成功");
                            finish();
                        }
                    });
                }
                break;
            case R.id.set_pwd_request_verify:
                timer.start();
                set_pwd_request_verify.setClickable(false);
                XinongHttpCommend.getInstance(mContext).setPwdCode(new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        T.showShort(mContext,"请稍等，验证码正在发送中。。。");
                    }
                });
                break;
        }

    }

    @Override
    public String setToolBarTitle() {
        return "验证手机号码";
    }
}
