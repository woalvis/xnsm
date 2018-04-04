package tech.xinong.xnsm.pro.wallet;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.wallet.model.BankModel;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.TimeUtil;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2018/1/25.
 */
@ContentView(R.layout.activity_my_wallet)
public class MyWalletActivity extends BaseActivity {
    @ViewInject(R.id.wallet_bill_layout)
    private LinearLayout wallet_bill_layout;
    @ViewInject(R.id.wallet_pwd_layout)
    private LinearLayout wallet_pwd_layout;
    @ViewInject(R.id.wallet_gathering_layout)
    private LinearLayout wallet_gathering_layout;
    @ViewInject(R.id.tv_balance)
    private TextView tv_balance;
    @ViewInject(R.id.tv_freeze)
    private TextView tv_freeze;
    @ViewInject(R.id.tv_cash)
    private TextView tv_cash;
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");

    private BigDecimal balance;
    private BigDecimal freezeAmount;
    private List<BankModel> bankModels;
    private boolean isHasAli;
    private boolean isHasPwd;


    @Override
    public void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        XinongHttpCommend.getInstance(mContext).wallets(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                JSONObject jb = JSON.parseObject(result);
                balance = jb.getBigDecimal("balance");
                freezeAmount = jb.getBigDecimal("freezeAmount");
                if (freezeAmount.compareTo(new BigDecimal(0)) > 0) {
                    tv_freeze.setVisibility(View.VISIBLE);
                    tv_freeze.setText("冻结金额：￥" + freezeAmount.doubleValue());
                }
                tv_balance.setText("￥" + balance.doubleValue());
            }
        });


        XinongHttpCommend.getInstance(mContext).getBanks(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                bankModels = JSON.parseArray(result, BankModel.class);
                isHasAli = bankModels.size() > 0 ? true : false;
            }
        });


        XinongHttpCommend.getInstance(mContext).hasPwd(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                if (result.equals("true")) {
                    isHasPwd = true;
                } else {
                    isHasPwd = false;
                }
            }
        });
    }

    @Override
    public String setToolBarTitle() {
        return "我的钱包";
    }


    @Override
    @OnClick({R.id.wallet_bill_layout, R.id.wallet_pwd_layout, R.id.wallet_gathering_layout,
            R.id.tv_cash})
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.wallet_bill_layout://收支记录
                if (isHasAli) {
                    Intent intent = new Intent(mContext, MyBillsActivity.class);
                    startActivity(intent);
                } else {
                    twoButtonDialog("喜农市",
                            "您还没有关联收款账号",
                            "现在就去",
                            "以后再说",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    skipActivity(AddBankAccountActivity.class);
                                }
                            }, null);
                }

                break;
            case R.id.wallet_pwd_layout:
                skipActivity(PayPwdActivity.class, false);
                break;
            case R.id.wallet_gathering_layout://
                skipActivity(BanksActivity.class, false);
                break;
            case R.id.tv_cash://提现

                if (isHasAli) {
                    if (isHasPwd) {
                        if (balance.compareTo(new BigDecimal((0)))<=0){
                            T.showShort(mContext,"账户余额为0");
                        }else {
                            XinongHttpCommend.getInstance(mContext).canWithdraw(new AbsXnHttpCallback(mContext) {
                                @Override
                                public void onSuccess(String info, String result) {
                                    JSONObject jb = JSONObject.parseObject(result);
                                    boolean canWithdraw = jb.getBoolean("canWithdraw");
                                    if (canWithdraw){
                                        skipActivity(WithdrawActivity.class, false);
                                    }else {
                                        long time = jb.getLong("time");
                                        T.showShort(mContext,"请于"+ TimeUtil.formatDuring(time)+"后再进行提现操作");
                                    }

                                }
                            });

                        }
                    }else {
                        twoButtonDialog("喜农市",
                                "您还没有设置钱包密码",
                                "现在就去",
                                "以后再说",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        skipActivity(PayPwdActivity.class, false);
                                    }
                                }, null);
                    }

                } else {
                    twoButtonDialog("喜农市",
                            "您还没有关联收款账号",
                            "现在就去",
                            "以后再说",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    skipActivity(AddBankAccountActivity.class, false);
                                }
                            }, null);
                }
                break;
        }
    }

}
