package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
import tech.xinong.xnsm.pro.pay.PayActivity;
import tech.xinong.xnsm.pro.pay.PayResult;
import tech.xinong.xnsm.pro.user.model.adapter.MyPagerAdapter;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.views.ReqPayDialog;

/**
 * Created by xiao on 2017/2/7.
 * 采购订单
 */

public class MyBuyOrderFragment extends BaseFragment {

    private TabLayout mTabLayout;//导航控件
    private ViewPager mViewPager;//ViewPager控件
    private List<Order> orderList;//所有的订单的集合
    private List<Order> unpaidOrderList;//未付款的订单的集合
    private List<Order> payedOrderList;//已付款的订单的集合---买家待收货
    private List<Order> refundOrderList;//付款完成的订单集合
    private List<CommonAdapter> adapters;
//    private List<String> mDatas = new ArrayList<>(Arrays.asList("全部", "未付款", "处理中", "待收货", "完成"));//选项卡
    private List<String> mDatas = new ArrayList<>(Arrays.asList("全部", "待支付", "待收货", "退款/售后"));//选项卡
    private List<View> mViewList;//为ViewPager提供的视图组
    private Map<String, List<Order>> orderMap;
    private View contentView;
    private int pageAll;
    private int totalPageAll;
    private int size = 20;
    public static int SDK_PAY_FLAG = 1001;
    private MyPagerAdapter mAdapter;

    private static final int state_cancel = 1;     //取消
   private static final int state_confirm = 2;     //确认
   private static final int state_pay = 3;        //付款
   private static final int state_req_pay = 4;    //申请退款
   private static final int state_received = 5;   //收货
   private static final int state_del = 6;        //删除
   private static final int state_refund = 8;     //查看退款
   private static final int state_check_logistic = 9;//查看物流

    /**
     * 加载布局
     * @return
     */
    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_my_supply_order;
    }



    /**
     * 初始化控件
     * @param contentView
     */
    @Override
    protected void initContentView(View contentView) {
        this.contentView = contentView;
    }




    @Override
    public void initData() {
        initFromNet();
    }


    @Override
    public void onStart() {
        super.onStart();
        refresh();

    }

    private void initFromNet() {
        init(contentView);
        adapters= new ArrayList<>();
        mViewList = new ArrayList<>();
        orderMap = new HashMap<>();
        mViewList = new ArrayList<>();
        orderList = new ArrayList<>();
        unpaidOrderList = new ArrayList<>();
        payedOrderList = new ArrayList<>();
        refundOrderList = new ArrayList<>();
        orderMap.put("全部", orderList);
        orderMap.put("待支付", unpaidOrderList);
        orderMap.put("待收货", payedOrderList);
        orderMap.put("退款/售后", refundOrderList);

        int listViewPosition = 0;
        for (String status : mDatas) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_lv, null);
            PullToRefreshListView listView = view.findViewById(R.id.lv);
            setListView(listView, orderMap.get(status),contentView,listViewPosition);
            listViewPosition++;
            mViewList.add(view);
        }

        mAdapter = new MyPagerAdapter(mContext, mViewList, mDatas);
//                mAdapter.notifyDataSetChanged();
        // mViewPager.setAdapter(mAdapter);
        if(mViewPager.getAdapter() == null) {
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
            mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        }
    }

    private void setListView(final PullToRefreshListView listView, final List<Order> orders, final View contentView, final int listViewPosition) {
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        if (totalPageAll<=100){
            listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    refreshView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                            switch (listViewPosition){
                                case 0:
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                            }
                            listView.onRefreshComplete();
                        }
                    }, 1000);
                }

                @Override
                public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView) {
                   // T.showShort(mContext,"已经到底了");
                    refreshView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.onRefreshComplete();
                        }
                    }, 1000);
                }
            });
        }

        CommonAdapter adapter = new CommonAdapter<Order>(mContext, R.layout.item_my_orders, orders) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, final int position, final Order item) {
                final SimpleDraweeView coverImg = (SimpleDraweeView) viewHolder.getView(R.id.product_pic);
                String coverImgUrl = item.getCoverImg();
                TextView tv_xn_fee = (TextView) viewHolder.getView(R.id.tv_xn_fee);
                TextView first = (TextView) viewHolder.getView(R.id.first);
                TextView second = (TextView) viewHolder.getView(R.id.second);
                TextView third = (TextView) viewHolder.getView(R.id.third);
                TextView tv_freight = (TextView) viewHolder.getView(R.id.tv_freight);
                TextView tv_offer = (TextView) viewHolder.getView(R.id.tv_offer);
                viewHolder.setOnClickListener(R.id.order_item_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderDetailActivity.skip( item.getId(),
                                item.getOrderNo(),
                        false,
                        mContext);
                    }
                });
                tv_xn_fee.setVisibility(View.GONE);
                viewHolder.setTextForTextView(R.id.tv_order_no,"订单号："+item.getOrderNo());
                if (TextUtils.isEmpty(coverImgUrl)){
//                    XinongHttpCommend.getInstance(mContext).getProductImg(new AbsXnHttpCallback(mContext) {
//                        @Override
//                        public void onSuccess(String info, String result) {
//                            coverImg.setImageURI(ImageUtil.getProductImg(result));
//                        }
//                    },item.get);
                }else {
                    coverImg.setImageURI(ImageUtil.getImgUrl(coverImgUrl));
                }
                viewHolder.setTextForTextView(R.id.product_title,item.getTitle());
                viewHolder.setTextForTextView(R.id.seller_name, item.getBuyerName());
                viewHolder.setTextForTextView(R.id.unit_price, item.getUnitPrice() + "元" );
                viewHolder.setTextForTextView(R.id.unit_num,"*"+item.getAmount().intValue()+"");
                viewHolder.setTextForTextView(R.id.order_state, item.getStatus().getDisplayName());
                viewHolder.setTextForTextView(R.id.product_total_price, item.getTotalPrice() + "元");
                if (item.getFreeShipping()){
                    tv_freight.setVisibility(View.VISIBLE);
                    tv_freight.setText("包邮" );
                }else {
                    tv_freight.setText("不包邮" );
                }

                if (item.getFreight()!=null){
                    tv_offer.setVisibility(View.VISIBLE);
                    tv_offer.setText("优惠：-"+item.getOffer().toString()+"元" );
                }else {
                    tv_offer.setVisibility(View.GONE);
                }


                /*根据订单的状态改变按钮的显示及功能*/
                switch (item.getStatus()) {
                    case INITIATED://下单
                        viewHolder.setTextForTextView(R.id.order_state, "待付款");
                        third.setVisibility(View.VISIBLE);
                        third.setText("取消订单");
                        second.setVisibility(View.VISIBLE);
                        second.setText("支付");
                        first.setVisibility(View.GONE);
                        third.setOnClickListener(new OneListener(state_cancel,item,this));
                        second.setOnClickListener(new OneListener(state_pay,item,this));
                        break;
                    case MODIFIED://取消，确认
                        viewHolder.setTextForTextView(R.id.order_state, "待付款");
                        first.setVisibility(View.GONE);
                        second.setText("取消订单");
                        third.setText("支付");
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        second.setOnClickListener(new OneListener(state_cancel,item,this));
                        third.setOnClickListener(new OneListener(state_pay,item,this));
                        break;
                    case CONFIRMED:
                        viewHolder.setTextForTextView(R.id.order_state, "确认完成");
                        first.setVisibility(View.GONE);
                        third.setText("取消订单");
                        second.setText("支付");
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        third.setOnClickListener(new OneListener(state_cancel,item,this));
                        second.setOnClickListener(new OneListener(state_pay,item,this));
                        break;
                    case PAYMENT_PROCESSING://付款处理中
                        viewHolder.setTextForTextView(R.id.order_state, "付款处理中");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        break;
                    case PAID://已付款  申请退款
                        viewHolder.setTextForTextView(R.id.order_state, "已付款");
                        first.setVisibility(View.GONE);
                        if (item.getRefundCount()>0){
                            second.setVisibility(View.VISIBLE);
                            second.setText("查看退款");
                            second.setOnClickListener(new OneListener(state_refund,item,this));
                        }else {
                            second.setVisibility(View.GONE);
                        }
                        third.setVisibility(View.GONE);
                        third.setText("申请退款");
                        third.setOnClickListener(new OneListener(state_req_pay,item,this));
                        break;
                    case PAYMENT_FAILED://付款失败
                        viewHolder.setTextForTextView(R.id.order_state, "付款失败");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.GONE);
                        break;
                    case SENT://已发货
                        viewHolder.setTextForTextView(R.id.order_state, "已发货");
                        first.setVisibility(View.VISIBLE);
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        first.setText("查看物流");
                        second.setText("确认收货");
                        if(item.getRefundCount()==0){

                            third.setVisibility(View.VISIBLE);
                            third.setText("申请退款");
                            third.setOnClickListener(new OneListener(state_req_pay,item,this));
                        }else {
                            third.setVisibility(View.VISIBLE);
                            third.setText("查看退款");
                            third.setOnClickListener(new OneListener(state_refund,item,this));
                        }

                        first.setOnClickListener(new OneListener(state_check_logistic,item,this));
                        second.setOnClickListener(new OneListener(state_received,item,this));

                        break;
                    case RECEIVED://已收货
                        viewHolder.setTextForTextView(R.id.order_state, "已收货");
                        if (item.getRefundCount()>0){
                            first.setVisibility(View.VISIBLE);
                            first.setText("查看退款");
                            first.setOnClickListener(new OneListener(state_refund,item,this));
                            second.setVisibility(View.GONE);
                        }else {
                            first.setVisibility(View.GONE);
                            second.setText("申请退款");
                        }
                        third.setVisibility(View.VISIBLE);
                        third.setText("查看物流");
                        second.setOnClickListener(new OneListener(state_req_pay,item,this));
                        third.setOnClickListener(new OneListener(state_check_logistic,item,this));
                        break;
                    case RECEIVE_MONEY:    //卖家已收款
                        viewHolder.setTextForTextView(R.id.order_state, "交易完成");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("删除");
                        third.setOnClickListener(new OneListener(state_del,item,this));
                        break;
                    case CANCELED://已取消
                        viewHolder.setTextForTextView(R.id.order_state, "已取消");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("删除");
                        third.setOnClickListener(new OneListener(state_del,item,this));
                        break;
                    case CLOSED://关闭
                        viewHolder.setTextForTextView(R.id.order_state, "已关闭");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("删除");
                        third.setOnClickListener(new OneListener(state_del,item,this));
                        break;
                    case REFUND_REQ://退款申请
                        viewHolder.setTextForTextView(R.id.order_state, "退款处理中");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("查看退款");
                        third.setOnClickListener(new OneListener(state_refund,item,this));
                        break;
                    case REFUND://已退款
                        viewHolder.setTextForTextView(R.id.order_state, "已退款");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.VISIBLE);
                        third.setVisibility(View.VISIBLE);
                        second.setText("查看退款");
                        third.setText("删除");
                        second.setOnClickListener(new OneListener(state_refund,item,this));
                        third.setOnClickListener(new OneListener(state_del,item,this));
                        break;
                    case  REFUND_PROCESSING:
                        viewHolder.setTextForTextView(R.id.order_state, "退款处理中");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("查看退款");
                        third.setOnClickListener(new OneListener(state_refund,item,this));
                        break;
                    case REFUND_FAILED:
                        viewHolder.setTextForTextView(R.id.order_state, "退款失败");
                        first.setVisibility(View.GONE);
                        second.setVisibility(View.GONE);
                        third.setVisibility(View.VISIBLE);
                        third.setText("查看退款");
                        third.setOnClickListener(new OneListener(state_refund,item,this));
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
//                OrderDetailActivity.skip( orders.get(position-1).getId(),
//                        orders.get(position-1).getOrderNo(),
//                        false,
//                        mContext);
//            }
//        });

    }


    private void init(final View contentView) {

        /**
         * 因为要重置所以要清空一下原来的数据
         */

        mTabLayout = null;
        mViewPager = null;
        mTabLayout = contentView.findViewById(R.id.my_order_tabs);
        mViewPager = contentView.findViewById(R.id.my_order_vp_view);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        /*如果已经有数据了，那么说明TabLayout已经初始化完成，那么就不要再往里加了*/

        if (mTabLayout.getTabCount()==0){
            for (String title : mDatas) {
                mTabLayout.addTab(mTabLayout.newTab().setText(title));
            }
        }

//        if (mDatas == null || mDatas.size() == 0) {
//
//        }else {
//            for (String title : mDatas) {
//                mTabLayout.addTab(mTabLayout.newTab().setText(title));
//            }
//        }


    }

    public class OneListener implements View.OnClickListener{
        private int state;
        private Order item;
        private CommonAdapter parent;

        public OneListener(int state,Order order,CommonAdapter adapter){
            this.state = state;
            item = order;
            parent = adapter;
        }
        @Override
        public void onClick(View v) {
            switch (state){
                case state_cancel:
                    XinongHttpCommend.getInstance(mContext).cancelOrderById(item.getId(), new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext,"取消成功");
                            item.setStatus(OrderStatus.CANCELED);
                            parent.notifyDataSetChanged();
                        }
                    });
                    break;

                case state_confirm:

                    break;
                case state_pay:
                    PayActivity.skip(mContext,item.getId(),item.getOrderNo(),
                            item.getTitle().split(" ")[0],item.getTotalPrice());
                    break;
                case state_req_pay://申请退款
                    XinongHttpCommend.getInstance(mContext).feerate(new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            double fees = Double.parseDouble(result);

                            ReqPayDialog.getInstance().
                                    setParam(mContext,item.getId(),fees).
                                    show(getActivity().getSupportFragmentManager(),
                                            "DialogLogout");


//                            twoButtonDialog("退款", "退款会产生"+fees*100+"%的手续费（由买家承担）",
//                                    "确定退款", "再想想",
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//                                        }
//                                    },null);
                        }
                    });
                    break;
                case state_received:
                    XinongHttpCommend.getInstance(mContext).receive(item.getId(), new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext,"收货成功");
                            item.setStatus(OrderStatus.RECEIVED);
                            parent.notifyDataSetChanged();
                        }
                    });

                    break;
                case state_del:
                    XinongHttpCommend.getInstance(mContext).delOrder(item.getId(), new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext,"订单删除成功");
                            parent.mData.remove(item);
                            parent.notifyDataSetChanged();
                        }
                    });

                    break;
//                case state_refund_req:
//                    Intent intentRefundReq = new Intent(mContext,RefundReqActivity.class);
//                    intentRefundReq.putExtra("orderId",item.getId());
//                    intentRefundReq.putExtra("orderNo",item.getOrderNo());
//
//                    startActivity(intentRefundReq);
//                    break;
                case state_refund: //查看退款
                    Intent intentRefund = new Intent(mContext,RefundReqActivity.class);
                    intentRefund.putExtra("orderId",item.getId());
                    intentRefund.putExtra("orderNo",item.getOrderNo());
                    intentRefund.putExtra("status",item.getStatus());
                    startActivity(intentRefund);
                    break;
                case state_check_logistic:
                    Intent intentCheck = new Intent(mContext,CheckLogisticActivity.class);
                    intentCheck.putExtra("orderId",item.getId());
                    intentCheck.putExtra("orderNo",item.getOrderNo());
                    startActivity(intentCheck);
                    break;
            }
        }
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            Toast.makeText(mContext, payResult.getResult(), Toast.LENGTH_LONG).show();
        };
    };



    private void refresh(){
        unpaidOrderList.clear();
        payedOrderList.clear();
        refundOrderList.clear();

        XinongHttpCommend.getInstance(mContext).getAllOrders(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(final String info, String result) {
                PageInfo pageInfo = JSONObject.parseObject(result,PageInfo.class);
                totalPageAll = pageInfo.getTotalPages();
                if (totalPageAll<=100){
                    orderList = JSON.parseArray(pageInfo.getContent(), Order.class);
                    for (Order order : orderList) {
                        switch (order.getStatus()) {
                            case INITIATED://下单  取消
                                break;
                            case MODIFIED://修改   取消，确认
                            case CONFIRMED://确认完成  付款
                                unpaidOrderList.add(order);
                                break;
                            case PAYMENT_PROCESSING://付款处理中  无
                                break;
                            case PAID://已付款  申请退款
                                payedOrderList.add(order);
                                break;
                            case PAYMENT_FAILED://付款失败  无
                            case SENT://已发货  收货，申请退款
                                payedOrderList.add(order);
                                break;
                            case RECEIVED://已收货
                            case RECEIVE_MONEY:    //卖家已收款
                            case CANCELED://已取消 删除
                            case CLOSED://关闭  删除
                                break;
                            case REFUND_REQ://退款申请  查看退款申请
                            case REFUND://已退款  删除，查看退款
                            case REFUND_PROCESSING:
                                refundOrderList.add(order);
                                break;
                            default:
                                break;
                        }
                    }
                    //"全部", "待支付", "待收货", "退款/售后"
                    orderMap.put("全部", orderList);
                    orderMap.put("待支付", unpaidOrderList);
                    orderMap.put("待收货", payedOrderList);
                    orderMap.put("退款/售后", refundOrderList);
                    List<List<Order>> os = new ArrayList<>();
                    os.add(orderList);
                    os.add(unpaidOrderList);
                    os.add(payedOrderList);
                    os.add(refundOrderList);
                    int i = 0;
                    for (CommonAdapter adapter : adapters){
                        adapter.refresh(os.get(i));
                        i++;
                    }

                }else {
                    //订单多余20需要逐个做分页
                }
            }
        },pageAll,size);

    }

}
