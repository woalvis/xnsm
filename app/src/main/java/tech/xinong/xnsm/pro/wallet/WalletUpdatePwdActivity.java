package tech.xinong.xnsm.pro.wallet;

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
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_wallet_update_pwd)
public class WalletUpdatePwdActivity extends BaseActivity {
//    @ViewInject(R.id.et_paypswd)
//    private PasswordInputView et_paypswd;
//    @ViewInject(R.id.et_paypswd_re)
//    private PasswordInputView et_paypswd_re;
    @ViewInject(R.id.et_old_pwd)
    private EditText et_old_pwd;
    @ViewInject(R.id.et_new_pwd)
    private EditText et_new_pwd;
    @ViewInject(R.id.tv_confirm)
    private TextView tv_confirm;
    @Override
    public void initData() {
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_old_pwd.getText().toString();
                String pwdRe = et_new_pwd.getText().toString();

                if (TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwdRe)){
                    if (pwd.length()==6){

                    }else {
                        T.showShort(mContext,"请检查您的输入");
                    }

                }else if (pwd.equals(pwdRe)){
                    T.showShort(mContext,"两次输入一样，请重新输入");
                }else{
                    XinongHttpCommend.getInstance(mContext).updatePwd(pwd, pwdRe,new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext,"修改密码成功");
                            finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    public String setToolBarTitle() {
        return "修改密码";
    }
}
