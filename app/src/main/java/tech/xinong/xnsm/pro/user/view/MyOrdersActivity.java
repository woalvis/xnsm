package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.Order;
import tech.xinong.xnsm.pro.buy.view.OrderDetailActivity;
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
    private List<Order> orderList;


    @Override
    public void initData() {
        XinongHttpCommend.getInstence(this).getAllOrders(new AbsXnHttpCallback() {
            @Override
            public void onSuccess(final String info, String result) {
                String content = JSON.parseObject(result).getString("content");
                orderList = JSON.parseArray(content, Order.class);
                initNavigation();
                myOrderLv.setAdapter(new CommonAdapter<Order>(mContext, R.layout.item_my_orders, orderList) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder, int position, Order item) {
                        viewHolder.setTextForTextView(R.id.seller_name, item.getBuyer().getFullName());
                        viewHolder.setTextForTextView(R.id.order_state, item.getStatus().getDesc());

                        if (item.getSellerListing().getListingDocs().size()==0){
                            //加载默认图片
                            viewHolder.setImageForView(R.id.product_pic,R.mipmap.default_pic_bg);
                        }else {
                            //viewHolder.setImageForView(R.id.product_pic,);//暂时没有图片
                            ImageLoader imageLoader = new ImageLoader();
                            imageLoader.setImageCache(new DoubleImageCache())
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

                            }
                        });
                        viewHolder.setOnClickListener(R.id.order_pay_now, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

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
        });


    }


    private void initNavigation(){
        tv_center.setVisibility(View.VISIBLE);
        tv_center.setText("我的订单");
    }

}
