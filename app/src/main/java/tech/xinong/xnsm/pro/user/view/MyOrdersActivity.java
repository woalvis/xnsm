package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.Order;
import tech.xinong.xnsm.pro.buy.model.OrderStatus;
import tech.xinong.xnsm.pro.buy.view.OrderDetailActivity;
import tech.xinong.xnsm.pro.user.model.adapter.MyPagerAdapter;
import tech.xinong.xnsm.util.imageloder.ImageLoader;
import tech.xinong.xnsm.util.imageloder.impl.DoubleImageCache;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_my_orders)
public class MyOrdersActivity extends BaseActivity {
    @ViewInject(R.id.my_order_lv)
    private ListView myOrderLv;
    @ViewInject(R.id.my_order_navigation)
    private LinearLayout navigation;
    @ViewInject(R.id.tv_center)
    private TextView tv_center;
    @ViewInject(R.id.my_order_tabs)
    private TabLayout mTabLayout;
    @ViewInject(R.id.my_order_vp_view)
    private ViewPager mViewPager;


    private List<Order> orderList;//所有的订单的集合
    private List<Order> unpaidOrderList;//未付款的订单的集合
    private List<Order> payedOrderList;//已付款的订单的集合
    private List<String> mDatas = new ArrayList<String>(Arrays.asList("全部", "未付款", "已付款"));
    private List<View> mViewList;
    private Map<String,List<Order>> orderMap;

    @Override
    public void initWidget() {
        orderMap = new HashMap<>();
        mViewList = new ArrayList<>();
        orderList = new ArrayList<>();
        unpaidOrderList = new ArrayList<>();
        payedOrderList = new ArrayList<>();
        initNavigation();
//        for (String title:mDatas){
//            mTabLayout.addTab(mTabLayout.newTab().setText(title));
//            View view = LayoutInflater.from(this).inflate(R.layout.viewpager_item_lv,null);
//            mViewList.add(view);
//        }

    }

    @Override
    public void initData() {


        XinongHttpCommend.getInstence(this).getAllOrders(new AbsXnHttpCallback() {
            @Override
            public void onSuccess(final String info, String result) {
                String content = JSON.parseObject(result).getString("content");
                orderList = JSON.parseArray(content, Order.class);
                for(Order order : orderList){
                    if(order.getStatus().getDesc().equals(OrderStatus.PAYED.getDesc())){
                        payedOrderList.add(order);
                    }else if (order.getStatus().getDesc().equals(OrderStatus.UNPAID.getDesc())){
                        unpaidOrderList.add(order);
                    }
                }

                orderMap.put("全部",orderList);
                orderMap.put("未付款",unpaidOrderList);
                orderMap.put("已付款",payedOrderList);

                for (String status : mDatas) {
                    View view  = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_lv, null);
                    ListView listView = (ListView) view.findViewById(R.id.lv);
                    setListView(listView,orderMap.get(status));
                    mViewList.add(view);
                }

                MyPagerAdapter mAdapter = new MyPagerAdapter(mContext,mViewList,mDatas);

                mViewPager.setAdapter(mAdapter);
                mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
                mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

            }
        });


    }


    private void initNavigation(){
        tv_center.setVisibility(View.VISIBLE);
        tv_center.setText("我的订单");
    }


    private void setListView(ListView listView,List<Order> orders){
        listView.setAdapter(new CommonAdapter<Order>(mContext, R.layout.item_my_orders, orders) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final Order item) {
                viewHolder.setTextForTextView(R.id.seller_name, item.getBuyer().getFullName());
                viewHolder.setTextForTextView(R.id.order_state, item.getStatus().getDesc());

                if (item.getSellerListing().getListingDocs().size()==0){
                    //加载默认图片
                    viewHolder.setImageForView(R.id.product_pic,R.mipmap.default_pic_bg);
                }else {
                    //viewHolder.setImageForView(R.id.product_pic,);//暂时没有图片
                    ImageLoader.getInstance().setImageCache(new DoubleImageCache())
                            .displayImage(item.getSellerListing().getListingDocs().get(0),
                                    (ImageView)viewHolder.getView(R.id.product_pic));
                }

                viewHolder.setTextForTextView(R.id.product_address, item.getAddress());
                viewHolder.setTextForTextView(R.id.product_desc, item.getSpecDesc());
                viewHolder.setTextForTextView(R.id.product_category, item.getProduct().getName());
                viewHolder.setTextForTextView(R.id.unit_price, item.getUnitPrice() + "元/" + item.getQuantityUnit().getName());
                viewHolder.setTextForTextView(R.id.product_total_price, item.getTotalPrice() + "元");
                viewHolder.setOnClickListener(R.id.cancel_order, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, item.getId()+"取消", Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolder.setOnClickListener(R.id.order_pay_now, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, item.getId()+"去付款", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        myOrderLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("orderId",orderList.get(position).getId());
                mContext.startActivity(intent);
            }
        });

    }


}
