package tech.xinong.xnsm.pro.buy.view;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.OrderDetailModel;
import tech.xinong.xnsm.util.ioc.ContentView;

@ContentView(R.layout.activity_order_confirm)
public class OrderConfirmActivity extends BaseActivity {

    private OrderDetailModel orderDetail;

    @Override
    public void initData() {
        String orderId = intent.getStringExtra("orderId");
        if (TextUtils.isEmpty(orderId)){
            XinongHttpCommend.getInstance(mContext).getOrderDetailById(orderId, new AbsXnHttpCallback(mContext) {
                @Override
                public void onSuccess(String info, String result) {
                    orderDetail = JSON.parseObject(result, OrderDetailModel.class);

                }
            });
        }
    }
}
