package tech.xinong.xnsm.pro.wallet;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.PasswordInputView;

@ContentView(R.layout.activity_pay_pwd)
public class PayPwdActivity extends BaseActivity {
    @ViewInject(R.id.et_paypswd_re)
    private PasswordInputView et_paypswd_re;
    @ViewInject(R.id.et_paypswd)
    private PasswordInputView et_paypswd;
    @ViewInject(R.id.tv_confirm)
    private TextView tv_confirm;
    @ViewInject(R.id.no_pwd_ll)
    private LinearLayout no_pwd_ll;
    @ViewInject(R.id.has_pwd_ll)
    private LinearLayout has_pwd_ll;
    @ViewInject(R.id.set_pwd_request_verify)
    private TextView set_pwd_request_verify;
    @ViewInject(R.id.pwd_verify_num)
    private EditText pwd_verify_num;
    @ViewInject(R.id.rl_update_pwd)
    private RelativeLayout rl_update_pwd;
    @ViewInject(R.id.rl_reset)
    private RelativeLayout rl_reset;
    private String code;
    private CountDownTimer timer;


    @Override
    public void initData() {
        showProgress();
        XinongHttpCommend.getInstance(mContext).hasPwd(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                if (result.equals("true")){
                    no_pwd_ll.setVisibility(View.GONE);
                    has_pwd_ll.setVisibility(View.VISIBLE);
                }else {
                    no_pwd_ll.setVisibility(View.VISIBLE);
                    has_pwd_ll.setVisibility(View.GONE);
                }
            }
        });

    }


    @Override
    @OnClick({R.id.tv_confirm,R.id.set_pwd_request_verify,R.id.rl_update_pwd,R.id.rl_reset})
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

                set_pwd_request_verify.setClickable(false);
                setTimer(set_pwd_request_verify);
                XinongHttpCommend.getInstance(mContext).setPwdCode(new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        T.showShort(mContext,"请稍等，验证码正在发送中。。。");
                    }
                });
                break;
            case R.id.rl_update_pwd:
                updatePwd();
                break;
            case R.id.rl_reset:
                reset();
                break;
        }
    }

    /*重置密码*/
    private void reset() {
        Intent intent = new Intent(mContext,WalletResetActivity.class);
        startActivity(intent);
    }

    /*修改密码*/
    private void updatePwd() {
        Intent intent = new Intent(mContext,WalletUpdatePwdActivity.class);
        startActivity(intent);
    }



    @Override
    public String setToolBarTitle() {
        return "钱包密码";
    }
}
