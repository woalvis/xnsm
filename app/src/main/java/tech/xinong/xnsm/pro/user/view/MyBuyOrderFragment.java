package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.Order;
import tech.xinong.xnsm.pro.buy.view.OrderDetailActivity;
import tech.xinong.xnsm.pro.user.model.adapter.MyPagerAdapter;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.imageloder.ImageLoader;
import tech.xinong.xnsm.util.imageloder.impl.DoubleImageCache;

/**
 * Created by xiao on 2017/2/7.
 */

public class MyBuyOrderFragment extends BaseFragment {

    private TabLayout mTabLayout;//导航控件
    private ViewPager mViewPager;//ViewPager控件
    private List<Order> orderList;//所有的订单的集合
    private List<Order> unpaidOrderList;//未付款的订单的集合
    private List<Order> paymentInOrderList;//付款处理中的
    private List<Order> payedOrderList;//已付款的订单的集合---买家待收货
    private List<Order> finishOrderList;//付款完成的订单集合
    private List<String> mDatas = new ArrayList<>(Arrays.asList("全部", "未付款", "处理中", "待收货", "完成"));//选项卡
    private List<View> mViewList;//为ViewPager提供的视图组
    private Map<String, List<Order>> orderMap;

    /**
     * 加载布局
     * @return
     */
    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_my_supply_order;
    }

    /**
     * 做一些数据初始化工作
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderMap = new HashMap<>();
        mViewList = new ArrayList<>();
        orderList = new ArrayList<>();
        unpaidOrderList = new ArrayList<>();
        payedOrderList = new ArrayList<>();
        paymentInOrderList = new ArrayList<>();
        finishOrderList = new ArrayList<>();
    }

    /**
     * 初始化控件
     * @param contentView
     */
    @Override
    protected void initContentView(View contentView) {
        mTabLayout = (TabLayout) contentView.findViewById(R.id.my_order_tabs);
        mViewPager = (ViewPager) contentView.findViewById(R.id.my_order_vp_view);
        init();
    }


    private void setListView(ListView listView, final List<Order> orders) {

//        View listEmptyView = View.inflate(mContext, R.layout.layout_empty, (ViewGroup) listView.getParent());
//        listView.setEmptyView(listEmptyView);
        listView.setAdapter(new CommonAdapter<Order>(mContext, R.layout.item_my_orders, orders) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final Order item) {
                viewHolder.setTextForTextView(R.id.seller_name, item.getBuyer().getFullName());
                viewHolder.setTextForTextView(R.id.order_state, item.getStatus().getDesc());

//                if (item.getSellerListing().getListingDocs().size()==0){
                if (1 == 1) {
                    //加载默认图片
                    viewHolder.setImageForView(R.id.product_pic, R.mipmap.default_pic_bg);
                } else {
                    //viewHolder.setImageForView(R.id.product_pic,);//暂时没有图片
                    ImageLoader.getInstance().setImageCache(new DoubleImageCache())
                            .displayImage(item.getSellerListing().getListingDocs().get(0).getId(),
                                    (ImageView) viewHolder.getView(R.id.product_pic));
                }

                viewHolder.setTextForTextView(R.id.product_address, item.getAddress());
                viewHolder.setTextForTextView(R.id.product_desc, item.getSpecDesc());
                viewHolder.setTextForTextView(R.id.product_category, item.getProduct().getName());
                viewHolder.setTextForTextView(R.id.unit_price, item.getUnitPrice() + "元/" + item.getQuantityUnit().getName());
                viewHolder.setTextForTextView(R.id.product_total_price, item.getTotalPrice() + "元");
                Button btCancel = (Button) viewHolder.getView(R.id.cancel_order);
                Button btPay = (Button) viewHolder.getView(R.id.order_pay_now);
                viewHolder.setOnClickListener(R.id.cancel_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.showShort(mContext, item.getId() + "取消");

                    }
                });
                viewHolder.setOnClickListener(R.id.order_pay_now, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        T.showShort(mContext, item.getId() + "去付款");
                    }
                });


                /*根据订单的状态改变按钮的显示及功能*/
                switch (item.getStatus()) {
                    case UNPAID:
                        btPay.setText("去付款");
                        btCancel.setVisibility(View.VISIBLE);
                        btPay.setVisibility(View.VISIBLE);
                        break;
                    case PAYMENT_IN:
                        btCancel.setVisibility(View.GONE);
                        btPay.setVisibility(View.GONE);
                        break;
                    case PAYED:
                    case SHIP_GOODS:
                        btCancel.setVisibility(View.GONE);
                        btPay.setVisibility(View.VISIBLE);
                        btPay.setText("确认收货");

                        break;
                    case RECEIVE_GOODS:
                    case RECEIVE_MONEY:

                        break;
                    default:
                        break;
                }

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("orderId", orders.get(position).getId());
                mContext.startActivity(intent);
            }
        });

    }


    private void init() {

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        for (String title : mDatas) {
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
        }

        XinongHttpCommend.getInstance(mContext).getAllOrders(new AbsXnHttpCallback() {
            @Override
            public void onSuccess(final String info, String result) {
                String content = JSON.parseObject(result).getString("content");
                orderList = JSON.parseArray(content, Order.class);
                for (Order order : orderList) {
                    switch (order.getStatus()) {
                        case UNPAID:
                            unpaidOrderList.add(order);
                            break;
                        case PAYMENT_IN:
                            paymentInOrderList.add(order);
                            break;
                        case PAYED:
                        case SHIP_GOODS:
                            payedOrderList.add(order);
                            break;
                        case RECEIVE_GOODS:
                        case RECEIVE_MONEY:
                            finishOrderList.add(order);
                            break;
                        default:
                            break;

                    }

                }
                //"全部", "未付款", "处理中","待收货","完成"
                orderMap.put("全部", orderList);
                orderMap.put("未付款", unpaidOrderList);
                orderMap.put("处理中", paymentInOrderList);
                orderMap.put("待收货", payedOrderList);
                orderMap.put("完成", finishOrderList);

                for (String status : mDatas) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_lv, null);
                    ListView listView = (ListView) view.findViewById(R.id.lv);
                    setListView(listView, orderMap.get(status));
                    mViewList.add(view);
                }

                MyPagerAdapter mAdapter = new MyPagerAdapter(mContext, mViewList, mDatas);

                mViewPager.setAdapter(mAdapter);
                mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
                mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

            }
        });

    }
}
