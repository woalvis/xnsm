package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.Order;
import tech.xinong.xnsm.pro.buy.model.OrderStatus;
import tech.xinong.xnsm.pro.buy.view.OrderDetailActivity;
import tech.xinong.xnsm.pro.user.model.adapter.MyPagerAdapter;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.views.BaseEditDialog;
import tech.xinong.xnsm.views.RefuseDialog;


/*
* 供应订单
* */
public class MySupplyOrderFragment extends BaseFragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Order> orderList;//所有的订单的集合
    private List<Order> payedOrderList;//代发货订单的集合
    private List<Order> modifiedOrderList;//待修改的订单的集合
    private List<Order> finishOrderList;//付款完成的订单集合
    private List<String> mDatas = new ArrayList<String>(Arrays.asList("全部", "待修改", "待发货", "退款/售后"));
    private List<View> mViewList;
    private Map<String, List<Order>> orderMap;
    private List<CommonAdapter> adapters;
    private EasyPopup mPopup;
    private int allPage;
    private int modifiedPage;
    private int payedPage;
    private int finishPage;
    private int size = 100;
    private int totalPageAll;
    private final int state_cancel = 1;//取消
    private final int state_update = 2;//修改
    private final int state_sent = 3;//发货
    private final int state_check_logistic = 4;//查看物流
    private final int state_del = 6;//删除
    private final int state_refund = 7;//查看退款
    private final int state_approved = 8;//同意退款
    private final int state_refuse = 9;//拒绝退款
    private final int state_sell_refund = 10;//卖家直接退款


    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_my_supply_order;
    }


    /**
     * 做一些数据初始化工作
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void initContentView(View contentView) {
        mTabLayout = contentView.findViewById(R.id.my_order_tabs);
        mViewPager = contentView.findViewById(R.id.my_order_vp_view);

    }

    @Override
    public void initData() {
        init();
    }


    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    private void initFromNet() {
        adapters = new ArrayList<>();
        orderMap = new HashMap<>();
        mViewList = new ArrayList<>();
        orderList = new ArrayList<>();
        payedOrderList = new ArrayList<>();
        modifiedOrderList = new ArrayList<>();
        finishOrderList = new ArrayList<>();
        final MyPagerAdapter mAdapter = new MyPagerAdapter(mContext, mViewList, mDatas);
        mAdapter.notifyDataSetChanged();

        //"全部", "待发货","待收款", "完成"
        orderMap.put("全部", orderList);
        orderMap.put("待修改", modifiedOrderList);
        orderMap.put("待发货", payedOrderList);
        orderMap.put("退款/售后", finishOrderList);

        for (String status : mDatas) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_lv, null);
            PullToRefreshListView listView = view.findViewById(R.id.lv);
            setListView(listView, orderMap.get(status));
            mViewList.add(view);
        }

        if (mViewPager.getAdapter() == null) {
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
            mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        }
//        showProgress();
//        XinongHttpCommend.getInstance(mContext).getSupplyOrders(new AbsXnHttpCallback(mContext) {
//            @Override
//            public void onSuccess(final String info, String result) {
//                cancelProgress();
//                PageInfo pageInfo = JSONObject.parseObject(result, PageInfo.class);
//                totalPageAll = pageInfo.getTotalPages();
//                if (totalPageAll <= 120) {
//                    orderList = JSON.parseArray(pageInfo.getContent(), Order.class);
//                    for (Order order : orderList) {
//                        switch (order.getStatus()) {
//                            case INITIATED://下单
//                                modifiedOrderList.add(order);
//                                break;
//                            case MODIFIED://修改
//                            case CONFIRMED://确认完成
//                            case PAYMENT_PROCESSING://付款处理中
//                                break;
//                            case PAID://已付款
//                                payedOrderList.add(order);
//                                break;
//                            case PAYMENT_FAILED://付款失败
//                            case SENT://已发货
//                            case RECEIVED://已收货
//                            case RECEIVE_MONEY:    //卖家已收款
//                            case CANCELED://已取消
//                            case CLOSED://关闭
//                                break;
//                            case REFUND_REQ://退款申请
//                            case REFUND://已退款
//                            case REFUND_FAILED:
//                            case REFUND_PROCESSING:
//                                finishOrderList.add(order);
//                                break;
//                            default:
//                                break;
//                        }
//
//                    }
//
//                } else {
//
//                }
//                //"全部", "待发货","待收款", "完成"
//                orderMap.put("全部", orderList);
//                orderMap.put("待修改", modifiedOrderList);
//                orderMap.put("待发货", payedOrderList);
//                orderMap.put("退款/售后", finishOrderList);
//
//                for (String status : mDatas) {
//                    View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_lv, null);
//                    PullToRefreshListView listView = view.findViewById(R.id.lv);
//                    setListView(listView, orderMap.get(status));
//                    mViewList.add(view);
//                }
//
//                final MyPagerAdapter mAdapter = new MyPagerAdapter(mContext, mViewList, mDatas);
////                mAdapter.notifyDataSetChanged();
//                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                    @Override
//                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                    }
//
//                    @Override
//                    public void onPageSelected(int position) {
//                        //T.showShort(mContext, mDatas.get(position));
//                    }
//
//                    @Override
//                    public void onPageScrollStateChanged(int state) {
//
//                    }
//                });
////                mViewPager.setAdapter(mAdapter);
//                if(mViewPager.getAdapter() == null) {
//                    mViewPager.setAdapter(mAdapter);
//                    mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
//                    mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
//                }
//
//            }
//        }, "", allPage, size);

    }

    private void setListView(final PullToRefreshListView listView, final List<Order> orders) {
        if (orders == null) {
            return;
        }
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        CommonAdapter adapter = new CommonAdapter<Order>(mContext, R.layout.item_my_orders, orders) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final Order item) {
                SimpleDraweeView product_pic = (SimpleDraweeView) viewHolder.getView(R.id.product_pic);
                TextView tv_freight = (TextView) viewHolder.getView(R.id.tv_freight);
                TextView tv_xn_fee = (TextView) viewHolder.getView(R.id.tv_xn_fee);
                tv_xn_fee.setVisibility(View.VISIBLE);
                viewHolder.setTextForTextView(R.id.tv_buyer, "买家：");
                product_pic.setImageURI(ImageUtil.getImgUrl(item.getCoverImg()));
                viewHolder.setTextForTextView(R.id.seller_name, item.getBuyerName());
                viewHolder.setTextForTextView(R.id.product_title, item.getTitle());
                viewHolder.setTextForTextView(R.id.unit_price, item.getUnitPrice() + "元");
                viewHolder.setTextForTextView(R.id.unit_num, item.getAmount().intValue() + "");
                viewHolder.setTextForTextView(R.id.product_total_price, item.getTotalPrice().subtract(item.getXnFees()) + "元");
                viewHolder.setTextForTextView(R.id.tv_xn_fee, "平台费用：-" + item.getXnFees() + "元");

                viewHolder.setOnClickListener(R.id.order_item_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderDetailActivity.skip( item.getId(),
                                item.getOrderNo(),
                                false,
                                mContext);
                    }
                });

//                viewHolder.setTextForTextView(R.id.tv_freight,"运费："+item.getFreight().toString()+"元");
                if (item.getFreeShipping()) {
                    tv_freight.setVisibility(View.VISIBLE);
                    tv_freight.setText("包邮");
                } else {
                    tv_freight.setText("不包邮");
                }

                viewHolder.setTextForTextView(R.id.tv_offer, "优惠：-" + item.getOffer().toString() + "元");
                TextView second = (TextView) viewHolder.getView(R.id.second);
                TextView third = (TextView) viewHolder.getView(R.id.third);
                TextView first = (TextView) viewHolder.getView(R.id.first);
                viewHolder.setTextForTextView(R.id.tv_order_no, "订单号：" + item.getOrderNo());

                 /*根据订单的状态改变按钮的显示及功能*/
                switch (item.getStatus()) {
                    case INITIATED:
                        viewHolder.setTextForTextView(R.id.order_state, "等待修改");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        second.setText("取消");
                        third.setText("修改订单");
                        second.setOnClickListener(new ButtonClickListener(state_cancel, item, this));
                        third.setOnClickListener(new ButtonClickListener(state_update, item, this));
                        break;
                    case MODIFIED:
                        viewHolder.setTextForTextView(R.id.order_state, "等待买家付款");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        second.setText("取消");
                        third.setText("修改订单");
                        second.setOnClickListener(new ButtonClickListener(state_cancel, item, this));
                        third.setOnClickListener(new ButtonClickListener(state_update, item, this));
                        break;
                    case CONFIRMED:
                        viewHolder.setTextForTextView(R.id.order_state, "确认完成");
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        first.setVisibility(View.GONE);
                        break;
                    case PAYMENT_PROCESSING:
                        viewHolder.setTextForTextView(R.id.order_state, "付款处理中");
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        first.setVisibility(View.GONE);
                        break;
                    case PAID:
                        viewHolder.setTextForTextView(R.id.order_state, "待发货");
                        if (item.getRefundCount() > 0) {
                            first.setVisibility(View.VISIBLE);
                            first.setText("查看退款");
                            first.setOnClickListener(new ButtonClickListener(state_refund, item, this));
                        } else {
                            first.setVisibility(View.GONE);
                        }
                        second.setVisibility(View.VISIBLE);
                        second.setText("退款");
                        second.setOnClickListener(new ButtonClickListener(state_sell_refund, item, this));
                        third.setVisibility(View.VISIBLE);
                        third.setText("发货");
                        third.setOnClickListener(new ButtonClickListener(state_sent, item, this));
                        break;
                    case SENT:
                        viewHolder.setTextForTextView(R.id.order_state, "已发货");
                        if (item.getRefundCount() > 0) {
                            second.setVisibility(View.VISIBLE);
                            second.setText("查看退款");
                            second.setOnClickListener(new ButtonClickListener(state_refund, item, this));
                        } else {
                            second.setVisibility(View.GONE);
                        }
                        third.setVisibility(View.VISIBLE);
                        first.setVisibility(View.GONE);
                        third.setText("查看物流");
                        third.setOnClickListener(new ButtonClickListener(state_check_logistic, item, this));
                        break;
                    case PAYMENT_FAILED:
                        viewHolder.setTextForTextView(R.id.order_state, "付款失败");
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        first.setVisibility(View.GONE);
                        break;
                    case RECEIVED:
                        viewHolder.setTextForTextView(R.id.order_state, "已收货");
                        if (item.getRefundCount() > 0) {
                            second.setVisibility(View.VISIBLE);
                            second.setText("查看退款");
                            second.setOnClickListener(new ButtonClickListener(state_refund, item, this));
                        } else {
                            second.setVisibility(View.GONE);
                        }
                        third.setVisibility(View.GONE);
                        first.setVisibility(View.GONE);
                        break;
                    case RECEIVE_MONEY:
                        viewHolder.setTextForTextView(R.id.order_state, "已收款");
                        second.setVisibility(View.GONE);
                        first.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("删除");
                        third.setOnClickListener(new ButtonClickListener(state_del, item, this));
                        break;
                    case CANCELED:
                        viewHolder.setTextForTextView(R.id.order_state, "已取消");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("删除");
                        third.setOnClickListener(new ButtonClickListener(state_del, item, this));
                        break;
                    case CLOSED:
                        viewHolder.setTextForTextView(R.id.order_state, "已关闭");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("删除");
                        third.setOnClickListener(new ButtonClickListener(state_del, item, this));
                        break;
                    case REFUND_REQ:
                        viewHolder.setTextForTextView(R.id.order_state, "退款申请");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        first.setText("同意退款");
                        second.setText("拒绝退款");
                        third.setText("查看退款详情");
                        first.setOnClickListener(new ButtonClickListener(state_approved, item, this));
                        second.setOnClickListener(new ButtonClickListener(state_refuse, item, this));
                        third.setOnClickListener(new ButtonClickListener(state_refund, item, this));
                        break;
                    case REFUND:
                        viewHolder.setTextForTextView(R.id.order_state, "已退款");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        second.setText("查看退款");
                        third.setText("删除");
                        second.setOnClickListener(new ButtonClickListener(state_refund, item, this));
                        third.setOnClickListener(new ButtonClickListener(state_del, item, this));
                        break;
                    case REFUND_FAILED:
                        viewHolder.setTextForTextView(R.id.order_state, "退款失败");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("查看退款");
                        third.setOnClickListener(new ButtonClickListener(state_refund, item, this));
                        break;
                    case REFUND_PROCESSING:
                        viewHolder.setTextForTextView(R.id.order_state, "退款处理中");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("查看退款");
                        third.setOnClickListener(new ButtonClickListener(state_refund, item, this));
                        break;
                    default:
                        break;
                }


            }
        };
        listView.setAdapter(adapter);
        adapters.add(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                OrderDetailActivity.skip(orders.get(position - 1).getId(),
//                        orders.get(position - 1).getOrderNo(), true, mContext);
////                Intent intent = new Intent(mContext, OrderDetailActivity.class);
////                intent.putExtra("orderId", orders.get(position-1).getId());
////                mContext.startActivity(intent);
//            }
//        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        listView.onRefreshComplete();
                        refresh();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                        listView.onRefreshComplete();
                    }
                }, 1000);
            }
        });

    }


    private void init() {

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式

        if (mDatas == null || mDatas.size() == 0) {
            for (String title : mDatas) {
                mTabLayout.addTab(mTabLayout.newTab().setText(title));
            }
        }

        initFromNet();

    }


    private void updateAll() {


        XinongHttpCommend.getInstance(mContext).getSupplyOrders(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(final String info, String result) {

            }
        }, "", allPage, size);
    }

    private void updateModified() {
        XinongHttpCommend.getInstance(mContext).getSupplyOrders(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(final String info, String result) {

            }
        }, "INITIATED,MODIFIED", modifiedPage, size);

    }

    private void updatePayed() {
        XinongHttpCommend.getInstance(mContext).getSupplyOrders(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(final String info, String result) {

            }
        }, "PAID", payedPage, size);
    }

    private void updateFinish() {
        XinongHttpCommend.getInstance(mContext).getSupplyOrders(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(final String info, String result) {

            }
        }, "REFUND,REFUND_REQ,RECEIVE_MONEY", payedPage, size);
    }

    public class CancelListener implements View.OnClickListener {
        private Order order;
        private CommonAdapter adapter;

        public CancelListener(Order order, CommonAdapter adapter) {
            this.order = order;
            this.adapter = adapter;
        }

        @Override
        public void onClick(View v) {
            XinongHttpCommend.getInstance(mContext).cancelOrderById(order.getId(), new AbsXnHttpCallback(mContext) {
                @Override
                public void onSuccess(String info, String result) {
                    T.showShort(mContext, "取消成功");
                    order.setStatus(OrderStatus.CANCELED);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }


    public class UpdateListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }


    public class ButtonClickListener implements View.OnClickListener {
        private Order item;
        private int state = 0;
        private CommonAdapter adapter;

        public ButtonClickListener(int state, Order order, CommonAdapter adapter) {
            this.state = state;
            this.item = order;
            this.adapter = adapter;
        }

        @Override
        public void onClick(View v) {
            switch (state) {
                case state_cancel:
                    XinongHttpCommend.getInstance(mContext).cancelOrderById(item.getId(), new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext, "取消成功");
                            item.setStatus(OrderStatus.CANCELED);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case state_update:
                    Intent intent = new Intent(mContext, UpdateOrderActivity.class);
                    String priceStr = item.getUnitPrice().multiply(item.getAmount()) + "";
                    intent.putExtra("price", priceStr);
                    intent.putExtra("orderId", item.getId());
                    startActivity(intent);
                    break;
                case state_sent:
                    Intent intentSent = new Intent(mContext, SentActivity.class);
                    intentSent.putExtra("orderNo", item.getOrderNo());
                    intentSent.putExtra("orderId", item.getId());
                    startActivity(intentSent);
                    mContext.finish();
                    break;
//                case state_refund_req:
//                    Intent intentRefund = new Intent(mContext,RefundReqActivity.class);
//                    intentRefund.putExtra("orderId",item.getId());
//                    intentRefund.putExtra("orderNo",item.getOrderNo());
//                    startActivity(intentRefund);
//                    break;
                case state_del:
                    XinongHttpCommend.getInstance(mContext).delOrder(item.getId(), new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext, "订单删除成功");
                            adapter.mData.remove(item);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case state_refund:
                    Intent intentRefund = new Intent(mContext, RefundReqActivity.class);
                    intentRefund.putExtra("orderId", item.getId());
                    intentRefund.putExtra("orderNo", item.getOrderNo());
                    intentRefund.putExtra("status", item.getStatus());
                    intentRefund.putExtra("isSell", true);
                    startActivity(intentRefund);
                    break;
                case state_check_logistic:
                    Intent intentCheck = new Intent(mContext, CheckLogisticActivity.class);
                    intentCheck.putExtra("orderId", item.getId());
                    intentCheck.putExtra("orderNo", item.getOrderNo());
                    startActivity(intentCheck);
                    break;
                case state_approved:
                    XinongHttpCommend.getInstance(mContext).approveRefund(
                            item.getId(), new AbsXnHttpCallback(mContext) {
                                @Override
                                public void onSuccess(String info, String result) {
                                    T.showShort(mContext, "已同意退款");
                                }
                            });
                    break;
                case state_refuse:
                    RefuseDialog.getInstance().setParam(mContext, item.getId()).show(getActivity().getSupportFragmentManager(), "DialogRefuse");
                    break;
                case state_sell_refund:
                    XinongHttpCommend.getInstance(mContext).feerate(new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            double fees = Double.parseDouble(result);

                            BaseEditDialog.getInstance().
                                    setConfirmText("确认")
                                    .setTitle("卖家退款")
                                    .setHintText("请输入退款理由")
                                    .setCancelText("取消")
                                    .setContentText("退款会产生" + fees * 100 + "%的手续费（由买家承担）")
                                    .setConfirmListener(new BaseEditDialog.OnEditClickListener() {
                                        @Override
                                        public void onClick(String text) {
                                            if (TextUtils.isEmpty(text)) {
                                                T.showShort(mContext, "退款理由不能为空");
                                                return;
                                            }
                                            XinongHttpCommend.getInstance(mContext).sellRefund(item.getId(),
                                                    text,
                                                    new AbsXnHttpCallback(mContext) {
                                                        @Override
                                                        public void onSuccess(String info, String result) {
                                                            T.showShort(mContext, "退款成功");
                                                            refresh();
                                                        }
                                                    });
                                        }
                                    }).show(getActivity().getSupportFragmentManager(), "base");
                        }
                    });


                    break;
            }
        }
    }


    private void refresh() {
        payedOrderList = new ArrayList<>();
        modifiedOrderList = new ArrayList<>();
        finishOrderList = new ArrayList<>();
        XinongHttpCommend.getInstance(mContext).getSupplyOrders(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(final String info, String result) {
                cancelProgress();
                PageInfo pageInfo = JSONObject.parseObject(result, PageInfo.class);
                totalPageAll = pageInfo.getTotalPages();
                if (totalPageAll <= 120) {
                    orderList = JSON.parseArray(pageInfo.getContent(), Order.class);
                    for (Order order : orderList) {
                        switch (order.getStatus()) {
                            case INITIATED://下单
                                modifiedOrderList.add(order);
                                break;
                            case MODIFIED://修改
                            case CONFIRMED://确认完成
                            case PAYMENT_PROCESSING://付款处理中
                                break;
                            case PAID://已付款
                                payedOrderList.add(order);
                                break;
                            case PAYMENT_FAILED://付款失败
                            case SENT://已发货
                            case RECEIVED://已收货
                            case RECEIVE_MONEY:    //卖家已收款
                            case CANCELED://已取消
                            case CLOSED://关闭
                                break;
                            case REFUND_REQ://退款申请
                            case REFUND://已退款
                            case REFUND_FAILED:
                            case REFUND_PROCESSING:
                                finishOrderList.add(order);
                                break;
                            default:
                                break;
                        }

                    }

                } else {

                }
                //"全部", "待发货","待收款", "完成"
                orderMap.put("全部", orderList);
                orderMap.put("待修改", modifiedOrderList);
                orderMap.put("待发货", payedOrderList);
                orderMap.put("退款/售后", finishOrderList);

                List<List<Order>> os = new ArrayList<>();
                os.add(orderList);
                os.add(modifiedOrderList);
                os.add(payedOrderList);
                os.add(finishOrderList);
                int i = 0;
                for (CommonAdapter adapter : adapters) {
                    adapter.refresh(os.get(i));
                    i++;
                }


            }
        }, "", allPage, size);

    }


}
