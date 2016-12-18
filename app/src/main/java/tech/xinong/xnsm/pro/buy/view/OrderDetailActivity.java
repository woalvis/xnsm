package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.OrderDetailModel;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.OrderProcessView;

import static tech.xinong.xnsm.R.id.order_pay_now_bt;

/**
 * 订单详情页
 */
@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {
    @ViewInject(R.id.order_seller_head_portrait)
    private ImageView ivSellerHeadPortrait;//卖家头像
    @ViewInject(R.id.order_seller_name)
    private TextView tvSellerName;//卖家姓名
    @ViewInject(R.id.order_product_pic)
    private ImageView ivProductPic;//货物图片
    @ViewInject(R.id.order_product_category)
    private TextView productCategory;//货物品类
    @ViewInject(R.id.order_product_description)
    private TextView productDescription;//货物描述
    @ViewInject(R.id.order_unit_price)
    private TextView unitPrice;//单价
    @ViewInject(R.id.order_amount)
    private TextView amount;//购买数量
    @ViewInject(R.id.order_goods_price)
    private TextView goodsPrice;//货物的价格
    @ViewInject(R.id.order_transport_cost)
    private TextView transportCost;//运输费用
    @ViewInject(R.id.total_price)
    private TextView totalPrice;//总共的价格
    @ViewInject(R.id.order_logistic_method)
    private TextView logistic_method;//运输方式
    @ViewInject(R.id.order_shipping_address)
    private TextView shippingAddress;//送货地址
    @ViewInject(R.id.order_buyer_info)
    private TextView buyerInfo;//买家信息
    @ViewInject(R.id.buyer_require)
    private TextView buyerRequire;//买家需求
    @ViewInject(R.id.order_create_time)
    private TextView createTime;//订单创建时间
    @ViewInject(order_pay_now_bt)
    private TextView payNow;//去支付的按钮
    @ViewInject(R.id.order_seller_order_process)
    private OrderProcessView orderProcess;
@ViewInject(R.id.tv_center)
    private TextView navigationTitle;


    @Override
    public void initWidget() {
        initNavigation();
    }

    private void initNavigation() {
        navigationTitle.setVisibility(View.VISIBLE);
        navigationTitle.setText("订单详情");
    }

    @Override
    public void initData() {

        String orderId = getIntent().getStringExtra("orderId");
        XinongHttpCommend.getInstence(mContext).getOrderDetailById(orderId, new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                OrderDetailModel orderDetail = JSON.parseObject(result, OrderDetailModel.class);
                tvSellerName.setText(orderDetail.getSeller().getFullName());
                productCategory.setText(orderDetail.getProduct().getName());
                productDescription.setText(orderDetail.getSpecDesc());
                unitPrice.setText(orderDetail.getUnitPrice() + "");
                amount.setText(orderDetail.getAmount() + "");
                goodsPrice.setText(orderDetail.getTotalPrice() + "");
                logistic_method.setText(orderDetail.getLogisticMethodTag());
                totalPrice.setText(orderDetail.getTotalPrice() + "");
                shippingAddress.setText(orderDetail.getAddress());
                transportCost.setText("0.0");
                buyerInfo.setText(orderDetail.getBuyer().getFullName());
                buyerRequire.setText(orderDetail.getBuyerRequire());
                createTime.setText(orderDetail.getCreateTime());
                orderProcess.setStatus(orderDetail.getStatus().getCode());
            }
        });

    }

    @OnClick({order_pay_now_bt})
    public void widgetClick(View view) {

        switch (view.getId()) {
            case R.id.order_pay_now_bt:
                Intent intent = new Intent(mContext, UploadActivity.class);
                startActivity(intent);
                break;
        }
    }

}
