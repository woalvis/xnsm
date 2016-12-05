package tech.xinong.xnsm.pro.user.view;

import android.widget.ListView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_my_order)
public class MyOrderActivity extends BaseActivity {
    @ViewInject(R.id.orders_lv_orders)
    private ListView lv_orders;
}
