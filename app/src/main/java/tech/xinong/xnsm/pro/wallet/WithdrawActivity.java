package tech.xinong.xnsm.pro.wallet;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.wallet.model.BankModel;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.PasswordInputView;


/**/
@ContentView(R.layout.activity_withdraw)
public class WithdrawActivity extends BaseActivity {
    @ViewInject(R.id.tv_alipay)
    private TextView tv_alipay;
    @ViewInject(R.id.et_amount)
    private EditText et_amount;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;
    private BankModel bank;
    @ViewInject(R.id.set_pwd_request_verify)
    private TextView set_pwd_request_verify;
    @ViewInject(R.id.et_paypswd_re)
    private PasswordInputView et_paypswd_re;
    @ViewInject(R.id.pwd_verify_num)
    private EditText pwd_verify_num;
    @ViewInject(R.id.tv_help)
    private TextView tv_help;


    @Override
    public void initData() {
        String text = "提现工作预计<font color='#FF0000'><bold>"
                +"24小时"+"</bold></font>内到账，单笔最多5万";
        tv_help.setText(Html.fromHtml(text));

        XinongHttpCommend.getInstance(mContext).getBanks(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                List<BankModel> bankModels = JSON.parseArray(result, BankModel.class);
                if (bankModels.size() > 0) {
                    for (BankModel bankModel:bankModels){
                        if (bankModel.getPrimary()){
                            bank = bankModel;
                        }
                    }

                    tv_alipay.setText(bank.getAccountNumber());
                    tv_name.setText(bank.getAccountName());
                }
            }
        });

    }


    @Override
    @OnClick({R.id.set_pwd_request_verify,R.id.tv_submit})
    public void widgetClick(View view) {
       switch (view.getId()){
           case R.id.set_pwd_request_verify:
               set_pwd_request_verify.setClickable(false);
               setTimer(set_pwd_request_verify);
               XinongHttpCommend.getInstance(mContext).sendVerify(new AbsXnHttpCallback(mContext) {
                   @Override
                   public void onSuccess(String info, String result) {
                       T.showShort(mContext,"请稍等，验证码正在发送中。。。");
                   }
               });
               break;
           case R.id.tv_submit:
               if (check()) {
                   showProgress();
                   XinongHttpCommend.getInstance(mContext).withdraw(
                           new BigDecimal(et_amount.getText().toString().trim()),
                           bank.getId(),
                           pwd_verify_num.getText().toString().trim(),
                           et_paypswd_re.getText().toString(),
                           new AbsXnHttpCallback(mContext) {
                               @Override
                               public void onSuccess(String info, String result) {
//                                   T.showShort(mContext, "提款成功提交");
                                   Intent intent = new Intent(mContext,WithdrawSuccessActivity.class);
                                   intent.putExtra("account",bank.getAccountNumber());
                                   intent.putExtra("amount",et_amount.getText().toString().trim());
                                   mContext.startActivity(intent);
                                   finish();
                               }
                           });
               }
               break;
       }


    }


    private boolean check(){
        if (checkTextView(et_amount)){
            T.showShort(mContext,"请输入提款金额");
            return false;
        }else {
            int amount = Integer.parseInt(et_amount.getText().toString().trim());
            if (amount>50000){
                T.showShort(mContext,"单笔提款金额不能超过50，000元");
                return false;
            }
        }

        if (checkTextView(et_paypswd_re)){
            T.showShort(mContext,"请输入钱包密码");
            return false;
        }

        if (checkTextView(pwd_verify_num)){
            T.showShort(mContext,"请输入验证码");
            return false;
        }


        return true;
    }

    @Override
    public String setToolBarTitle() {
        return "提现";
    }
}
