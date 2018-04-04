package tech.xinong.xnsm.pro.wallet;

import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.wallet.model.BankModel;
import tech.xinong.xnsm.pro.wallet.model.Banks;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_add_bank_acount)
public class AddBankAccountActivity extends BaseActivity {
    @ViewInject(R.id.tv_add)
    private TextView tv_add;
    @ViewInject(R.id.et_alipay_account)
    private EditText et_alipay_account;
    @ViewInject(R.id.tv_help)
    private TextView tv_help;
    @ViewInject(R.id.et_full_name)
    private EditText et_full_name;
    @ViewInject(R.id.pwd_verify_num)
    private EditText pwd_verify_num;
    @ViewInject(R.id.set_pwd_request_verify)
    private TextView set_pwd_request_verify;
    private CountDownTimer timer;
    private List<BankModel> bankModels;

    private boolean isAdd = true;
    private BankModel bank;
    @Override
    public void initData() {
        String phone = mSharedPreferences.getString("loginName"," ");
        String phoneStr = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        String text ="";
        if (isAdd){
            text = "本次操作需要验证手机号吗，请输入<font color='#FF0000'><bold>"+phoneStr
                    +"</bold></font>收到的短信验证码，添加成功后<font color='#FF0000'><bold>"
                    +"24小时"+"</bold></font>内无法进行提现操作";
        }else {
            text = "本次操作需要验证手机号吗，请输入<font color='#FF0000'><bold>"+phoneStr
                    +"</bold></font>收到的短信验证码，修改成功后<font color='#FF0000'><bold>"
                    +"24小时"+"</bold></font>内无法进行提现操作";
        }

        bank = (BankModel) getIntent().getSerializableExtra("bank");
        if (bank ==null){
            isAdd = true;
            tv_add.setText("确定添加");
        }else {
            isAdd = false;
            et_alipay_account.setText(bank.getAccountNumber());
            et_full_name.setText(bank.getAccountName());
            tv_add.setText("修改");
        }
        title.setText(setToolBarTitle());
        tv_help.setText(Html.fromHtml(text));
        set_pwd_request_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_pwd_request_verify.setClickable(false);
                timer.start();
                XinongHttpCommend.getInstance(mContext).banksVerify(
                        new AbsXnHttpCallback(mContext) {
                            @Override
                            public void onSuccess(String info, String result) {
                                T.showShort(mContext,"正在发送，请您耐心等待");
                            }
                        });
            }
        });
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()){
                    if (isAdd){
                        BankModel bankModel = new BankModel();
                        bankModel.setAccountNumber(et_alipay_account.getText().toString().trim());
                        bankModel.setAccountName(et_full_name.getText().toString().trim());
                        bankModel.setPrimary(true);
                        bankModel.setVerificationCode(pwd_verify_num.getText().toString().trim());
                        bankModel.setType(Banks.ALIPAY);
                        XinongHttpCommend.getInstance(mContext).addBanks(bankModel, new AbsXnHttpCallback(mContext) {
                            @Override
                            public void onSuccess(String info, String result) {
                                T.showShort(mContext,"账号添加成功");
                                finish();
                            }
                        });
                    }else {
                        bank.setAccountNumber(et_alipay_account.getText().toString().trim());
                        bank.setAccountName(et_full_name.getText().toString().trim());
                        bank.setVerificationCode(pwd_verify_num.getText().toString().trim());
                        XinongHttpCommend.getInstance(mContext).updateBank(bank, new AbsXnHttpCallback(mContext) {
                            @Override
                            public void onSuccess(String info, String result) {
                                T.showShort(mContext,"账号修改成功");
                                finish();
                            }
                        });
                    }

                }
            }
        });

        /**
          * 倒计时器，短信验证时间倒计时
                */

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

    private boolean check() {
        if (checkTextView(et_alipay_account)){
            T.showShort(mContext,"请填写账号");
           return false;
        }
        if (checkTextView(et_full_name)){
            T.showShort(mContext,"请填写真实姓名");
            return false;
        }
        if (checkTextView(pwd_verify_num)){
            T.showShort(mContext,"请填写验证码");
            return false;
        }
        return true;

    }


    @Override
    public String setToolBarTitle() {
        return isAdd?"添加支付宝账号":"修改支付宝账号";
    }
}
