package tech.xinong.xnsm.pro.user.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.Order;
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
    @ViewInject(R.id.tv_right)
    private TextView tv_right;
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
    private boolean isSupply = false;
    private FragmentManager fragmentManager;
    private MyBuyOrderFragment myBuyOrderFragment;
    private MySupplyOrderFragment mySupplyOrderFragment;
    private long timeTemp = 0;

    @Override
    public void initWidget() {
        orderMap = new HashMap<>();
        mViewList = new ArrayList<>();
        orderList = new ArrayList<>();
        unpaidOrderList = new ArrayList<>();
        payedOrderList = new ArrayList<>();
        initNavigation();
    }

    @Override
    public void initData() {
        fragmentManager = getSupportFragmentManager();
        if (fragmentManager==null){
            fragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        tv_right.setText("采购订单");
        if (myBuyOrderFragment==null){
            myBuyOrderFragment = new MyBuyOrderFragment();
        }
        fragmentTransaction.replace(R.id.content_fragment,myBuyOrderFragment);
        fragmentTransaction.commit();
    }

    /**
     * 初始化标题栏
     */
    private void initNavigation(){
        tv_center.setVisibility(View.VISIBLE);
        tv_center.setText("我的订单");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("采购订单");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*为防止用户极端操作，按得太快了， 没有意义*/
                if (System.currentTimeMillis()-timeTemp<2000){
                    return;
                }
                timeTemp = System.currentTimeMillis();
                isSupply = !isSupply;
                if (fragmentManager==null){
                    fragmentManager = getSupportFragmentManager();
                }
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.setCustomAnimations(R.anim.acitivity_entry,R.anim.activity_exit);
                if (isSupply) {
                    tv_right.setText("供应订单");
                    if (mySupplyOrderFragment==null){
                        mySupplyOrderFragment = new MySupplyOrderFragment();
                    }
                    fragmentTransaction.replace(R.id.content_fragment,mySupplyOrderFragment);

                }else{

                    tv_right.setText("采购订单");
                    if (myBuyOrderFragment==null){
                        myBuyOrderFragment = new MyBuyOrderFragment();
                    }
                    fragmentTransaction.replace(R.id.content_fragment,myBuyOrderFragment);
                }

                fragmentTransaction.commit();
            }
        });
    }


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
//                        T.showShort(mContext, item.getId()+"去付款");
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


}
