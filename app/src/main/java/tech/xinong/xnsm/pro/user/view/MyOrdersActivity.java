package tech.xinong.xnsm.pro.user.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.navigation.impl.DefaultNavigation;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

import static com.lzy.okgo.OkGo.getContext;


@ContentView(R.layout.activity_my_orders)
public class MyOrdersActivity extends BaseActivity {
@ViewInject(R.id.my_order_lv)
    private ListView myOrderLv;


    @Override
    public void initData() {

    }

    private void initNavigation(View contentView){
        DefaultNavigation.Builder builder = new DefaultNavigation.Builder(getContext(),(ViewGroup)contentView);
        builder.setCenterText(R.string.my_order).
                setLeftText(" ").create();


    }
}
