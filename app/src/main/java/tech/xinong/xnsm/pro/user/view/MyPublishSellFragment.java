package tech.xinong.xnsm.pro.user.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.OpMode;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.sell.model.BuyerListingSum;
import tech.xinong.xnsm.pro.user.model.ProxyOnRefreshListener;
import tech.xinong.xnsm.pro.user.model.PublishStates;
import tech.xinong.xnsm.pro.user.model.adapter.MyPagerAdapter;
import tech.xinong.xnsm.pro.user.model.adapter.MyPublishBuyAdapter;

/**
 * Created by xiao on 2017/12/15.
 * 我发布的供应
 */

public class MyPublishSellFragment extends BaseFragment {

    private TabLayout my_publish_tabs;
    private List<BuyerListingSum> listings;
    private List<BuyerListingSum> initiatedListings;
    private List<BuyerListingSum> rejectedListings;
    private ViewPager my_publish_vp_view;
    private PullToRefreshListView lsv;
    private List<View> mViewList;//为ViewPager提供的视图组
    private Map<String, List<BuyerListingSum>> publishMap;
    private List<String> mDatas = new ArrayList<>(Arrays.asList("全部", "等待审核", "审核未通过"));
    private List<ProxyOnRefreshListener> adapters;
    private PullToRefreshListView lvTemp;
    private PullToRefreshListView lvAll;
    private PullToRefreshListView lvInit;
    private PullToRefreshListView lvRejected;
    private MyPublishBuyAdapter allAdapter;
    private MyPublishBuyAdapter initiatedAdapter;
    private MyPublishBuyAdapter rejectedAdapter;
    private int allPage = 0;
    private int initPage = 0;
    private int rePage =0;
    private int size = 20;


    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_my_publish_sell;
    }


    @Override
    public void initData() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_pull_lv, null);
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_pull_lv, null);
        View view2 = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_pull_lv, null);
        lvAll =  view.findViewById(R.id.lv);
        lvInit =  view1.findViewById(R.id.lv);
        lvRejected =  view2.findViewById(R.id.lv);
        mViewList.add(lvAll);
        mViewList.add(lvInit);
        mViewList.add(lvRejected);
        MyPagerAdapter mAdapter = new MyPagerAdapter(mContext, mViewList, mDatas);
        my_publish_vp_view.setAdapter(mAdapter);
        my_publish_tabs.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        my_publish_tabs.setupWithViewPager(my_publish_vp_view);
        for(String title : mDatas){
            my_publish_tabs.addTab(my_publish_tabs.newTab().setText(title));
        }
        my_publish_tabs.setTabMode(TabLayout.MODE_FIXED);
        my_publish_tabs.setupWithViewPager(my_publish_vp_view);//将TabLayout和ViewPager关联起来。
        setAdapter();
        getListByState(null, OpMode.INIT,lvAll,allPage);
        getListByState(PublishStates.REJECTED,OpMode.INIT,lvInit,initPage);
        getListByState(PublishStates.PENDING,OpMode.INIT,lvRejected,rePage);
    }

    private void getListByState(final PublishStates states, final OpMode opMode, final PullToRefreshBase<ListView> listView, int page) {

        XinongHttpCommend.getInstance(mContext).buyerListingsMe(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                PageInfo pageInfo = JSONObject.parseObject(result,PageInfo.class);
                List<BuyerListingSum> tempList = JSONObject.parseArray(pageInfo.getContent(), BuyerListingSum.class);
                if (states == null){
                    switch (opMode){
                        case INIT:
                        case PULLDOWN:
                            listings = tempList;
                            break;
                        case PULLUP:
                            listings.addAll(tempList);
                            break;
                    }
                    allAdapter.refresh(listings);
                }else {
                    switch (states){
                        case PENDING:
                            switch (opMode){
                                case INIT:
                                case PULLDOWN:
                                    initiatedListings = tempList;
                                    break;
                                case PULLUP:
                                    initiatedListings.addAll(tempList);
                                    break;
                            }
                            initiatedAdapter.refresh(initiatedListings);
                            break;
                        case REJECTED:
                            switch (opMode){
                                case INIT:
                                case PULLDOWN:
                                    rejectedListings = tempList;
                                    break;
                                case PULLUP:
                                    rejectedListings.addAll(tempList);
                                    break;
                            }
                            rejectedAdapter.refresh(rejectedListings);
                            break;
                    }
                }
                listView.onRefreshComplete();

            }
        }, getCustomerId(),states,page,size);
    }

    private void setAdapter() {

        allAdapter = new MyPublishBuyAdapter(mContext,R.layout.item_my_publish_buy,listings);
        initiatedAdapter =  new MyPublishBuyAdapter(mContext,R.layout.item_my_publish_buy,initiatedListings);
        rejectedAdapter = new MyPublishBuyAdapter(mContext,R.layout.item_my_publish_buy,rejectedListings);
        lvAll.setAdapter(allAdapter);
        lvInit.setAdapter(initiatedAdapter);
        lvRejected.setAdapter(rejectedAdapter);
        setLvListener();
    }

    private void setLvListener() {
        lvAll.setMode(PullToRefreshBase.Mode.BOTH);
        lvInit.setMode(PullToRefreshBase.Mode.BOTH);
        lvRejected.setMode(PullToRefreshBase.Mode.BOTH);

        lvAll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                allPage = 0;
                getListByState(null,OpMode.PULLDOWN,refreshView,allPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                allPage++;
                getListByState(null,OpMode.PULLUP,refreshView,allPage);
            }
        });

        lvInit.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initPage = 0;
                getListByState(PublishStates.PENDING,OpMode.PULLDOWN,refreshView,initPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initPage++;
                getListByState(PublishStates.PENDING,OpMode.PULLUP,refreshView,initPage);
            }
        });

        lvRejected.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                rePage = 0;
                getListByState(PublishStates.PENDING,OpMode.PULLDOWN,refreshView,rePage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                rePage++;
                getListByState(PublishStates.PENDING,OpMode.PULLDOWN,refreshView,rePage);
            }
        });

    }


    @Override
    protected void initContentView(View contentView) {
        publishMap = new HashMap<>();
        listings = new ArrayList<>();
        adapters = new ArrayList<>();
        initiatedListings = new ArrayList<>();
        rejectedListings = new ArrayList<>();
        mViewList = new ArrayList<>();
        my_publish_tabs = contentView.findViewById(R.id.my_publish_tabs);
        my_publish_vp_view = contentView.findViewById(R.id.my_publish_vp_view);
    }
}
