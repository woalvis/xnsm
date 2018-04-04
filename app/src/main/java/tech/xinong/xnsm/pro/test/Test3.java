package tech.xinong.xnsm.pro.test;

import android.support.v4.view.ViewPager;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2017/11/20.
 */
@ContentView(R.layout.test3)
public class Test3 extends BaseActivity{
    @ViewInject(R.id.my_order_vp_view)
    private ViewPager my_order_vp_view;

    @Override
    public void initWidget() {

    }

    @Override
    public void initData() {

    }
}
