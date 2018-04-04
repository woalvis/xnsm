package tech.xinong.xnsm.pro.user.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2017/12/7.
 */
@ContentView(R.layout.activity_my_publish_sell)
public class MyPublishActivity extends BaseActivity{

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
    private boolean isBuy = true;
    private FragmentManager fragmentManager;
    private MyPublishBuyFragment myPublishBuyFragment;
    private MyPublishSellFragment myPublishSellFragment;
    private long timeTemp = 0;
    @ViewInject(R.id.tv_buy_list)
    private TextView tv_buy_list;
    @ViewInject(R.id.tv_sell_list)
    private TextView tv_sell_list;
    private FragmentTransaction fragmentTransaction;

    private List<Fragment> fragments;
    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void initData() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (myPublishBuyFragment==null){
            myPublishBuyFragment = new MyPublishBuyFragment();
        }
        if (myPublishSellFragment==null){
            myPublishSellFragment = new MyPublishSellFragment();
        }
        fragmentTransaction.add(R.id.content_fragment,myPublishSellFragment,"sell");
        fragmentTransaction.add(R.id.content_fragment,myPublishBuyFragment,"buy");
        fragmentTransaction.hide(myPublishSellFragment);
        fragmentTransaction.commitAllowingStateLoss();


    }

    @OnClick({R.id.tv_buy_list,R.id.tv_sell_list})
    public void widgetClick(View view){
        switch (view.getId()){
            case R.id.tv_buy_list:
                if (!isBuy){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.activity_entry,R.anim.activity_exit);
                    tv_buy_list.setBackgroundColor(getColorById(R.color.colorPrimary));
                    tv_buy_list.setTextColor(getColorById(R.color.white));
                    tv_sell_list.setTextColor(getColorById(R.color.text_color));
                    tv_sell_list.setBackgroundColor(getColorById(R.color.white));
                    isBuy = true;
                    fragmentTransaction.hide(myPublishSellFragment);
                    fragmentTransaction.show(myPublishBuyFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }
                break;
            case R.id.tv_sell_list:
                if (isBuy){
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.activity_entry,R.anim.activity_exit);
                    tv_sell_list.setBackgroundColor(getColorById(R.color.colorPrimary));
                    tv_sell_list.setTextColor(getColorById(R.color.white));
                    tv_buy_list.setTextColor(getColorById(R.color.text_color));
                    tv_buy_list.setBackgroundColor(getColorById(R.color.white));
                    isBuy = false;
                    fragmentTransaction.hide(myPublishBuyFragment);
                    fragmentTransaction.show(myPublishSellFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }
                break;
        }

    }



    @Override
    public String setToolBarTitle() {
        return "我的发布";
    }
}
