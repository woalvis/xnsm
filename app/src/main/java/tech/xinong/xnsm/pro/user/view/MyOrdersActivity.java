package tech.xinong.xnsm.pro.user.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.Order;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_my_orders)
public class MyOrdersActivity extends BaseActivity {
    @ViewInject(R.id.my_order_lv)
    private ListView myOrderLv;
    @ViewInject(R.id.tv_center)
    private TextView tv_center;
    @ViewInject(R.id.tv_right)
    private TextView tv_right;
    @ViewInject(R.id.my_order_tabs)
    private TabLayout mTabLayout;
    @ViewInject(R.id.my_order_vp_view)
    private ViewPager mViewPager;
    @ViewInject(R.id.tv_buy_list)
    private TextView tv_buy_list;
    @ViewInject(R.id.tv_sell_list)
    private TextView tv_sell_list;

    private List<Order> orderList;//所有的订单的集合
    private List<Order> unpaidOrderList;//未付款的订单的集合
    private List<Order> payedOrderList;//已付款的订单的集合
    private List<View> mViewList;
    private Map<String,List<Order>> orderMap;
    private boolean isSupply = true;
    private MyBuyOrderFragment myBuyOrderFragment;
    private MySupplyOrderFragment mySupplyOrderFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private long timeTemp = 0;
    private boolean isSell;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        orderMap = new HashMap<>();
        mViewList = new ArrayList<>();
        orderList = new ArrayList<>();
        unpaidOrderList = new ArrayList<>();
        payedOrderList = new ArrayList<>();
    }

    @Override
    public void initData() {
        if (fragmentManager==null){
            fragmentManager = getSupportFragmentManager();
        }
        fragmentTransaction = fragmentManager.beginTransaction();

        if (myBuyOrderFragment==null){
            myBuyOrderFragment = new MyBuyOrderFragment();
        }
        if (mySupplyOrderFragment==null){
            mySupplyOrderFragment = new MySupplyOrderFragment();
        }
        fragmentTransaction.add(R.id.content_fragment,myBuyOrderFragment,"sell");
        fragmentTransaction.add(R.id.content_fragment,mySupplyOrderFragment,"buy");
//        if (isSell){
            fragmentTransaction.hide(mySupplyOrderFragment);
//        }else{
//
//        }
        fragmentTransaction.commitAllowingStateLoss();
    }


    @OnClick({R.id.tv_buy_list,R.id.tv_sell_list})
    public void widgetClick(View view){
        switch (view.getId()){
            case R.id.tv_buy_list:
                if (!isSupply){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.activity_entry,R.anim.activity_exit);
                    tv_buy_list.setBackgroundColor(getColorById(R.color.colorPrimary));
                    tv_buy_list.setTextColor(getColorById(R.color.white));
                    tv_sell_list.setTextColor(getColorById(R.color.text_color));
                    tv_sell_list.setBackgroundColor(getColorById(R.color.white));
                    isSupply = true;
                    fragmentTransaction.hide(mySupplyOrderFragment);
                    fragmentTransaction.show(myBuyOrderFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }
                break;
            case R.id.tv_sell_list:
                if (isSupply){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.activity_entry,R.anim.activity_exit);
                    tv_sell_list.setBackgroundColor(getColorById(R.color.colorPrimary));
                    tv_sell_list.setTextColor(getColorById(R.color.white));
                    tv_buy_list.setTextColor(getColorById(R.color.text_color));
                    tv_buy_list.setBackgroundColor(getColorById(R.color.white));
                    isSupply = false;
                    fragmentTransaction.hide(myBuyOrderFragment);
                    fragmentTransaction.show(mySupplyOrderFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }
                break;
        }
    }


//    /**
//     * 初始化标题栏
//     */
//    private void initNavigation(){
//        tv_center.setVisibility(View.VISIBLE);
//        tv_center.setText("我的订单");
//        tv_right.setVisibility(View.VISIBLE);
//        tv_right.setText("采购订单");
//        tv_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*为防止用户极端操作，按得太快了， 没有意义*/
//                if (System.currentTimeMillis()-timeTemp<1000){
//                    return;
//                }
//                timeTemp = System.currentTimeMillis();
//                isSupply = !isSupply;
//                if (fragmentManager==null){
//                    fragmentManager = getSupportFragmentManager();
//                }
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                fragmentTransaction.setCustomAnimations(R.anim.activity_entry,R.anim.activity_exit);
//                if (isSupply) {
//                    tv_right.setText("供应订单");
//                    if (mySupplyOrderFragment==null){
//                        mySupplyOrderFragment = new MySupplyOrderFragment();
//                    }
//                    fragmentTransaction.replace(R.id.content_fragment,mySupplyOrderFragment);
//
//                }else{
//
//                    tv_right.setText("采购订单");
//                    if (myBuyOrderFragment==null){
//                        myBuyOrderFragment = new MyBuyOrderFragment();
//                    }
//                    fragmentTransaction.replace(R.id.content_fragment,myBuyOrderFragment);
//                }
//
//                fragmentTransaction.commit();
//            }
//        });
//    }


//    private void setListView(ListView listView,List<Order> orders){
//        listView.setAdapter(new CommonAdapter<Order>(mContext, R.layout.item_my_orders, orders) {
//            @Override
//            protected void fillItemData(CommonViewHolder viewHolder, int position, final Order item) {
//                viewHolder.setTextForTextView(R.id.seller_name, item.getBuyer().getFullName());
//                viewHolder.setTextForTextView(R.id.order_state, item.getStatus().getDesc());
//
////                if (item.getSellerListing().getListingDocs().size()==0){
//                if (1==1){
//                    //加载默认图片
//                    viewHolder.setImageForView(R.id.product_pic,R.mipmap.default_pic_bg);
//                }else {
//                    //viewHolder.setImageForView(R.id.product_pic,);//暂时没有图片
//                    ImageLoader.getInstance().setImageCache(new DoubleImageCache())
//                            .displayImage(item.getSellerListing().getListingDocs().get(0).getId(),
//                                    (ImageView)viewHolder.getView(R.id.product_pic));
//                }
//
//                viewHolder.setTextForTextView(R.id.product_address, item.getAddress());
//                viewHolder.setTextForTextView(R.id.product_desc, item.getSpecDesc());
//                viewHolder.setTextForTextView(R.id.product_category, item.getProduct().getName());
//                viewHolder.setTextForTextView(R.id.unit_price, item.getUnitPrice() + "元/" + item.getQuantityUnit().getName());
//                viewHolder.setTextForTextView(R.id.product_total_price, item.getTotalPrice() + "元");
//                viewHolder.setOnClickListener(R.id.cancel_order, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        T.showShort(mContext, item.getId()+"取消");
//
//                    }
//                });
//                viewHolder.setOnClickListener(R.id.order_pay_now, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        T.showShort(mContext, item.getId()+"付款");
//                    }
//                });
//            }
//        });
//
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(mContext, OrderDetailActivity.class);
//                intent.putExtra("orderId",orderList.get(position).getId());
//                mContext.startActivity(intent);
//            }
//        });
//
//    }


    @Override
    public String setToolBarTitle() {
        return "我的订单";
    }
}
