package tech.xinong.xnsm.pro.buy.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.OrderDetailModel;
import tech.xinong.xnsm.pro.home.view.SellerDetailActivity;
import tech.xinong.xnsm.pro.pay.PayActivity;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.OrderProcessView;


/**
 * 订单详情页
 */
@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {
    @ViewInject(R.id.order_seller_name)
    private TextView tvSellerName;         //卖家姓名
    @ViewInject(R.id.order_product_pic)
    private SimpleDraweeView ivProductPic;        //货物图片
    @ViewInject(R.id.order_product_category)
    private TextView productCategory;      //货物品类
    @ViewInject(R.id.order_product_description)
    private TextView productDescription;   //货物描述
    @ViewInject(R.id.order_unit_price)
    private TextView unitPrice;            //单价
    @ViewInject(R.id.order_amount)
    private TextView amount;               //购买数量
    @ViewInject(R.id.order_goods_price)
    private TextView goodsPrice;           //货物的价格
    @ViewInject(R.id.order_transport_cost)
    private TextView transportCost;        //运输费用
    @ViewInject(R.id.total_price)
    private TextView totalPrice;           //总共的价格
    @ViewInject(R.id.order_logistic_method)
    private TagFlowLayout logistic_method;      //运输方式
    @ViewInject(R.id.order_shipping_address)
    private TextView shippingAddress;      //送货地址
    @ViewInject(R.id.order_buyer_info)
    private TextView buyerInfo;            //买家信息
    @ViewInject(R.id.buyer_require)
    private TextView buyerRequire;         //买家需求
    @ViewInject(R.id.order_create_time)
    private TextView createTime;           //订单创建时间
    @ViewInject(R.id.order_pay_now_bt)
    private TextView payNow;               //去支付的按钮
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.order_seller_order_process)
    private OrderProcessView orderProcess;
    @ViewInject(R.id.order_offer)
    private TextView order_offer;
    @ViewInject(R.id.rl_state)
    private RelativeLayout rl_state;
    @ViewInject(R.id.tv_state)
    private TextView tv_state;
    @ViewInject(R.id.order_no)
    private TextView order_no;//订单编号
    @ViewInject(R.id.ll_seller)
    private LinearLayout ll_seller;
    @ViewInject(R.id.ll_product)
    private LinearLayout ll_product;

    private String orderId;
    private String orderNo;
    private boolean isSeller;
    private OrderDetailModel orderDetail;

    @Override
    public void initData() {
        showProgress();
        orderId = getIntent().getStringExtra("orderId");
        orderNo = getIntent().getStringExtra("orderNo");
        isSeller = getIntent().getBooleanExtra("isSeller",false);
        order_no.setText(orderNo);
        if (isSeller){
            payNow.setVisibility(View.GONE);
        }
        XinongHttpCommend.getInstance(mContext).getOrderDetailById(orderId, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                cancelProgress();
                orderDetail = JSON.parseObject(result, OrderDetailModel.class);
                tvSellerName.setText(orderDetail.getSellerName());
                productCategory.setText(orderDetail.getTitle());
                ivProductPic.setImageURI(ImageUtil.getImgUrl(orderDetail.getCoverImg()));
                unitPrice.setText(orderDetail.getUnitPrice() + "");
                amount.setText(orderDetail.getAmount() + "");
                goodsPrice.setText(orderDetail.getTotalPrice() + "");
//                logistic_method.setText(orderDetail.getProvideSupport());
                if (TextUtils.isEmpty(orderDetail.getProvideSupport())){

                }else {
                    List<String> tags = new ArrayList<>();
                    for (String tag : orderDetail.getProvideSupport().split(",")){
                        tags.add(tag);
                    }
                    logistic_method.setAdapter(new TagAdapter<String>(tags){
                        @Override
                        public View getView(FlowLayout parent, int position, String o) {
                            TextView tvTag = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag,parent,false);
                            tvTag.setText(o);
                            return tvTag;
                        }
                    });
                }

//                totalPrice.setText(orderDetail.getTotalPrice() + "");
                shippingAddress.setText(orderDetail.getReceiverAddr());
//                transportCost.setText(orderDetail.getFreight().doubleValue() + "元");
                transportCost.setText(orderDetail.getFreeShipping()?"包邮":"不包邮");
                buyerInfo.setText(orderDetail.getBuyerName());
                buyerRequire.setText(orderDetail.getBuyerMsg());
                createTime.setText(orderDetail.getCreateTime());
                order_offer.setText(orderDetail.getOffer().doubleValue() + "元");
                tv_phone.setText(orderDetail.getReceiverPhone());


                switch (orderDetail.getStatus()) {
                    case INITIATED://下单
                    case MODIFIED://修改
                    case CONFIRMED://确认完成
                        break;
                    case PAYMENT_PROCESSING://付款处理中
                    case PAID://已付款
                    case SENT://已发货
                    case RECEIVED://已收货
                    case RECEIVE_MONEY:    //卖家已收款
                        orderProcess.setStatus(orderDetail.getStatus().getCode());
                        break;
                    case CANCELED://已取消
                        rl_state.setBackgroundColor(getColorById(R.color.gray1));
                        tv_state.setText("已取消");
                        break;
                    case CLOSED://关闭
                        rl_state.setBackgroundColor(getColorById(R.color.gray1));
                        tv_state.setText("已关闭");
                        break;
                    case REFUND_REQ://退款申请
                        rl_state.setBackgroundColor(getColorById(R.color.gray1));
                        tv_state.setText("退款中");
                        break;
                    case PAYMENT_FAILED://付款失败
                        rl_state.setBackgroundColor(getColorById(R.color.gray1));
                        tv_state.setText("付款失败");
                        break;
                    case REFUND://已退款
                        rl_state.setBackgroundColor(getColorById(R.color.gray1));
                        tv_state.setText("已退款");
                        break;
                    case REFUND_PROCESSING:
                        rl_state.setBackgroundColor(getColorById(R.color.gray1));
                        tv_state.setText("退款处理中");
                        break;
                    case REFUND_FAILED:
                        rl_state.setBackgroundColor(getColorById(R.color.gray1));
                        tv_state.setText("退款失败");
                        break;
                    default:
                        break;
                }

                if (orderDetail.getStatus().getCode() > 1) {
                    payNow.setVisibility(View.GONE);
                }

                if (orderDetail.getStatus().getCode() > 6) {
                    rl_state.setVisibility(View.VISIBLE);
                    orderProcess.setVisibility(View.GONE);
                } else {
                    rl_state.setVisibility(View.GONE);
                    orderProcess.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @OnClick({R.id.order_pay_now_bt,R.id.ll_seller,R.id.ll_product})
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.order_pay_now_bt:
                PayActivity.skip(mContext,orderId,orderNo,orderDetail.getTitle().split(" ")[0],orderDetail.getTotalPrice());
//                Intent intent = new Intent(mContext, PayActivity.class);
//                intent.putExtra("orderId", orderId);
//                intent.putExtra("orderNo", orderNo);
//                intent.putExtra("totalPrice", orderDetail.getTotalPrice());
//                intent.putExtra("title",orderDetail.getTitle().split(" ")[0]);
//                startActivity(intent);
                break;
            case R.id.ll_seller:
                Intent intentSeller = new Intent(mContext, SellerDetailActivity.class);
                intentSeller.putExtra("sellerId",orderDetail.getSeller().getId());
                startActivity(intentSeller);
                break;
            case R.id.ll_product:
                Intent intentProduct = new Intent(mContext, GoodsDetailActivity.class);
                intentProduct.putExtra("id",orderDetail.getSellerListing().getId());
                startActivity(intentProduct);
                break;
        }
    }

    public static void skip(String orderId, String orderNo,boolean isSeller,Context context) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("orderId", orderId);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("isSeller",isSeller);
        context.startActivity(intent);
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public String setToolBarTitle() {
        return "订单详情";
    }
}
 