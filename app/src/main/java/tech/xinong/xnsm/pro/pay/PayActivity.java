package tech.xinong.xnsm.pro.pay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;

import java.math.BigDecimal;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2018/1/4.
 */
@ContentView(R.layout.activity_pay)
public class PayActivity extends BaseActivity {
    @ViewInject(R.id.pay)
    private Button pay;
    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;
    @ViewInject(R.id.tv_order_no)
    private TextView tv_order_no;
    @ViewInject(R.id.rb_alipay)
    private RadioButton rb_alipay;
    @ViewInject(R.id.rb_bank_pay)
    private RadioButton rb_bank_pay;
    public static int SDK_PAY_FLAG = 1001;
    private String orderNo;
    private String orderId;
    private String orderInfo;
    private BigDecimal totalPrice;
    private String orderTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        orderId = intent.getStringExtra("orderId");
        orderNo = intent.getStringExtra("orderNo");
        orderTitle = intent.getStringExtra("orderTitle");
        totalPrice = (BigDecimal) intent.getSerializableExtra("totalPrice");
        tv_order_no.setText(orderNo);
        tv_total_price.setText(totalPrice.doubleValue() + "元");
    }

    @OnClick({R.id.pay})
    @Override
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.pay:
                if (rb_alipay.isChecked()) {
                    XinongHttpCommend.getInstance(mContext).pay(new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            orderInfo = result;

                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(PayActivity.this);
                                    Map<String, String> result = alipay.payV2(orderInfo, true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        }
                    }, orderNo);
                } else if (rb_bank_pay.isChecked()) {
                    Intent bankIntent = new Intent(mContext, BankPayActivity.class);
                    bankIntent.putExtra("orderId", orderId);
                    bankIntent.putExtra("orderNo", orderNo);
                    bankIntent.putExtra("orderTitle", orderTitle);
                    bankIntent.putExtra("totalPrice", totalPrice);
                    startActivity(bankIntent);
                }

                break;
        }
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
//            Toast.makeText(PayActivity.this, payResult.getResult(), Toast.LENGTH_LONG).show();
            if (payResult.getResultStatus().equals("9000")) {
                Intent intent = new Intent(mContext, PaySuccessActivity.class);
                intent.putExtra("orderId", orderId);
                startActivity(intent);
                finish();
            } else {
                T.showShort(mContext, payResult.getMemo());
            }

        }
    };

    public static void skip(Context context,
                            String orderId,
                            String orderNo,
                            String orderTitle,
                            BigDecimal totalPrice){
        Intent intent = new Intent(context,PayActivity.class);
        intent.putExtra("orderId",orderId);
        intent.putExtra("orderNo",orderNo);
        intent.putExtra("orderTitle",orderTitle);
        intent.putExtra("totalPrice",totalPrice);
        context.startActivity(intent);
    }


    @Override
    public String setToolBarTitle() {
        return "支付页面";
    }
}
