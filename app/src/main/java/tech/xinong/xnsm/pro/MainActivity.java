package tech.xinong.xnsm.pro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.view.BuyFragment;
import tech.xinong.xnsm.pro.home.view.HomeFragment1;
import tech.xinong.xnsm.pro.sell.view.SellFragment;
import tech.xinong.xnsm.pro.user.view.UserFragmentTest;
import tech.xinong.xnsm.util.ActivityCollector;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;


@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener {
    //保存tab页的基本信息
    private List<TabItem> tabItemList;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTabItemList();
        initTabView();
    }

    @Override
    public void initData() {

    }


    private void initTabItemList() {

        this.tabItemList = new ArrayList<>();
        this.tabItemList.add(new TabItem(
                R.mipmap.tabbar_home,
                R.mipmap.tabbar_home_highlighted,
                R.string.home,
                HomeFragment1.class));
        this.tabItemList.add(new TabItem(
                R.mipmap.tabbar_buy,
                R.mipmap.tabbar_buy_highlighted,
                R.string.buy,
                BuyFragment.class));
        this.tabItemList.add(new TabItem(
                R.mipmap.tabbar_sell,
                R.mipmap.tabbar_sell_highlighted,
                R.string.sell,
                SellFragment.class));
        this.tabItemList.add(new TabItem(
                R.mipmap.tabbar_profile,
                R.mipmap.tabbar_profile_highlighted,
                R.string.user,
                UserFragmentTest.class));
    }

    /**
     * 初始化Tab，同时绑定tab
     */
    private void initTabView() {
        //管理我们的Fragment
        FragmentTabHost fragmentTabHost = findViewById(android.R.id.tabhost);
        //指定Fragment绑定的布局，就是说这些Fragment要放到哪里去
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        //删除分割线
        fragmentTabHost.getTabWidget().setDividerDrawable(null);

        for (int i = 0; i < tabItemList.size(); i++) {

            TabItem tabItem = tabItemList.get(i);
            //创建Tab
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(tabItem.getTabText())
                    .setIndicator(tabItem.getTabIndicator());
            //添加Fragment
            fragmentTabHost.addTab(tabSpec,tabItem.getFragmentClass(),tabItem.getBundle());
            //给我们的Tab设置背景
            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundColor(getResources().getColor(R.color.tabbar_bottom_bg));

            fragmentTabHost.setOnTabChangedListener(this);

            //设置默认选中的Tab
            if (i==0){
                tabItem.setChecked(true);
            }
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        //通过循环重置样式
        for (int i =0;i<tabItemList.size();i++){
            TabItem tabItem = tabItemList.get(i);
            if (tabItem.getTabText().equals(tabId)){
                tabItem.setChecked(true);
            }else {
                tabItem.setChecked(false);
            }
        }
    }

    public class TabItem {
        //正常显示的图片
        private int imageNormal;
        //选中的图片
        private int imagePress;
        //tab的名字
        private int tabTextRes;
        //tab对应的Fragment
        private Class<? extends Fragment> fragmentClass;
        //fragment对应的数据（例如下标）
        private Bundle bundle;
        private View viewIndicator;
        ImageView iv_tab;
        TextView tv_tab;

        public TabItem(int imageNormal, int imagePress, int tabTextRes, Class<? extends Fragment> fragmentClass) {
            this.imageNormal = imageNormal;
            this.imagePress = imagePress;
            this.tabTextRes = tabTextRes;
            this.fragmentClass = fragmentClass;
        }


        //重置tab的样式

        public int getImageNormal() {
            return imageNormal;
        }

        public int getImagePress() {
            return imagePress;
        }

        public int getTabTextRes() {
            return tabTextRes;
        }

        public String getTabText() {
            String str = "";
            if (tabTextRes > 0) {
                str = getResources().getString(getTabTextRes());
            }
            return str;
        }

        public Class<? extends Fragment> getFragmentClass() {
            return fragmentClass;
        }

        public Bundle getBundle() {
            if (bundle == null) {
                bundle = new Bundle();
                bundle.putInt("tabTextRes", tabTextRes);
            }
            return bundle;
        }

        //重置Tab样式
        public void setChecked(boolean isChecked){
            if (iv_tab != null){
                if (isChecked){
                    iv_tab.setImageResource(imagePress);
                }else {
                    iv_tab.setImageResource(imageNormal);
                }
            }
            if (tv_tab != null){
                if (isChecked){
                    tv_tab.setTextColor(getResources().getColor(R.color.colorPrimary));
                }else {
                    tv_tab.setTextColor(getResources().getColor(R.color.tabbar_text_normal_color));
                }
            }
        }

        public View getTabIndicator() {
            //创建tab视图
            if (viewIndicator == null) {
                int layoutId = 0;

                if (this.tabTextRes <= 0) {
                    layoutId = R.layout.tabbar_publish_indicator;
                } else {
                    layoutId = R.layout.tabbar_indicator;
                }
                viewIndicator = getLayoutInflater().inflate(layoutId, null);
                iv_tab = viewIndicator.findViewById(R.id.iv_tab);
                if (tabTextRes > 0) {
                    tv_tab = viewIndicator.findViewById(R.id.tv_tab);
                    tv_tab.setText(getTabText());
                }
                iv_tab.setImageResource(imageNormal);
            }
            return viewIndicator;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("xx","onStart");
    }






    private long mLastPressBack;

    /**
     * 捕捉back,实现二次点击回退键再推出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastPressBack > 2000) {
                T.showShort(this, "再按一次退出");
                mLastPressBack = currentTime;
            } else {
                ActivityCollector.finishAll();
                this.finish();
            }
        }
        return true;
    }
}
