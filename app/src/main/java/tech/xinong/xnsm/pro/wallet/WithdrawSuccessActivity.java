package tech.xinong.xnsm.pro.wallet;

import android.view.View;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_withdraw_success)
public class WithdrawSuccessActivity extends BaseActivity {
    @ViewInject(R.id.tv_amount)
    private TextView tv_amount;
    @ViewInject(R.id.tv_account)
    private TextView tv_account;
    @ViewInject(R.id.tv_i_know)
    private TextView tv_i_know;
    @Override
    public void initData() {
        String account = intent.getStringExtra("account");
        String amount = intent.getStringExtra("amount");
        tv_amount.setText(amount+"元");
        tv_account.setText(account);
        tv_i_know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public String setToolBarTitle() {
        return "提现成功";
    }
}
