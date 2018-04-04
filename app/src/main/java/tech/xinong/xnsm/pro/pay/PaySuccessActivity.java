package tech.xinong.xnsm.pro.pay;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.MainActivity;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.OrderDetailModel;
import tech.xinong.xnsm.pro.user.view.MyOrdersActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_pay_success)
public class PaySuccessActivity extends BaseActivity{
    @ViewInject(R.id.tv_account_of_payment)
    private TextView tv_account_of_payment;//支付金额
    @ViewInject(R.id.tv_order_id)
    private TextView tv_order_id;//订单编号
    @ViewInject(R.id.tv_goods_name)
    private TextView tv_goods_name;
    @ViewInject(R.id.tv_view_order)
    private TextView tv_view_order;//查看订单
    @ViewInject(R.id.tv_back_home_index)
    private TextView tv_back_home_index;//返回主页
    private String orderId;

    @Override
    public void initData() {
        orderId = intent.getStringExtra("orderId");
        XinongHttpCommend.getInstance(mContext).getOrderDetailById(orderId, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                OrderDetailModel orderDetailModel = JSON.parseObject(result, OrderDetailModel.class);
                tv_account_of_payment.setText(orderDetailModel.getTotalPrice()+"元");
                tv_order_id.setText(orderDetailModel.getOrderNo()+"");
                tv_goods_name.setText(orderDetailModel.getTitle());
            }
        });


    }


    @Override
    @OnClick({R.id.tv_view_order,R.id.tv_back_home_index})
    public void widgetClick(View view) {
        switch (view.getId()){

            case R.id.tv_view_order:
                Intent intent = new Intent(mContext, MyOrdersActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_back_home_index:
                Intent intentHome = new Intent(mContext, MainActivity.class);
                startActivity(intentHome);
                finish();
                break;
        }
    }
}
