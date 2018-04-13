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
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.user.model.PublishStates;
import tech.xinong.xnsm.pro.user.model.adapter.MyPagerAdapter;
import tech.xinong.xnsm.pro.user.model.adapter.MyPublishSellAdapter;
import tech.xinong.xnsm.util.T;

/**
 * Created by xiao on 2017/12/15.
 * 我发布的采购
 */

public class MyPublishBuyFragment extends BaseFragment {

    private TabLayout my_publish_tabs;
    private List<SellerListingInfoDTO> listings;
    private List<SellerListingInfoDTO> initiatedListings;
    private List<SellerListingInfoDTO> rejectedListings;
    private ViewPager my_publish_vp_view;
    private PullToRefreshListView allLv;
    private PullToRefreshListView initiatedLv;
    private PullToRefreshListView rejectedLv;
    private MyPublishSellAdapter allAdapter;
    private MyPublishSellAdapter initAdapter;
    private MyPublishSellAdapter rejectedAdapter;
    private List<View> mViewList;//为ViewPager提供的视图组
    private Map<String, List<SellerListingInfoDTO>> publishMap;
    private List<String> mDatas = new ArrayList<>(Arrays.asList("全部", "等待审核", "审核未通过"));
    private int allPage = 0;
    private int initPage = 0;
    private int rePage = 0;
    private int size = 20;
    private View contentView;

    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_my_publish_buy;
    }

    @Override
    public void initData() {
        init();
    }

    private void init() {
        publishMap = new HashMap<>();
        listings = new ArrayList<>();
        initiatedListings = new ArrayList<>();
        rejectedListings = new ArrayList<>();
        mViewList = new ArrayList<>();

        View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_pull_lv, null);
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_pull_lv, null);
        View view2 = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_pull_lv, null);
        allLv = view.findViewById(R.id.lv);
        rejectedLv = view1.findViewById(R.id.lv);
        initiatedLv = view2.findViewById(R.id.lv);
        mViewList.add(allLv);
        mViewList.add(initiatedLv);
        mViewList.add(rejectedLv);
        MyPagerAdapter mAdapter = new MyPagerAdapter(mContext, mViewList, mDatas);

            my_publish_vp_view.setAdapter(mAdapter);
            for (String title : mDatas) {
                my_publish_tabs.addTab(my_publish_tabs.newTab().setText(title));
            }
            my_publish_tabs.setTabMode(TabLayout.MODE_FIXED);
            my_publish_tabs.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
            my_publish_tabs.setupWithViewPager(my_publish_vp_view);//将TabLayout和ViewPager关联起来。
        setAdapter();
        getListByState(null, OpMode.INIT, allLv, allPage);
        getListByState(PublishStates.REJECTED, OpMode.INIT, initiatedLv, initPage);
        getListByState(PublishStates.PENDING, OpMode.INIT, rejectedLv, rePage);
    }

    private void setLvListener() {
        allLv.setMode(PullToRefreshBase.Mode.BOTH);
        initiatedLv.setMode(PullToRefreshBase.Mode.BOTH);
        rejectedLv.setMode(PullToRefreshBase.Mode.BOTH);
        allLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                allPage = 0;
                getListByState(null, OpMode.PULLDOWN, refreshView, allPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                allPage++;
                getListByState(null, OpMode.PULLUP, refreshView, allPage);
            }
        });

        initiatedLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initPage = 0;
                getListByState(PublishStates.PENDING, OpMode.PULLDOWN, refreshView, initPage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                initPage++;
                getListByState(PublishStates.PENDING, OpMode.PULLUP, refreshView, initPage);
            }
        });

        rejectedLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                rePage = 0;
                getListByState(PublishStates.REJECTED, OpMode.PULLDOWN, refreshView, rePage);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                rePage++;
                getListByState(PublishStates.REJECTED, OpMode.PULLDOWN, refreshView, rePage);
            }
        });
    }

    private void setAdapter() {
        allAdapter = new MyPublishSellAdapter(mContext, R.layout.item_my_publish_sell, listings);
        initAdapter = new MyPublishSellAdapter(mContext, R.layout.item_my_publish_sell, initiatedListings);
        rejectedAdapter = new MyPublishSellAdapter(mContext, R.layout.item_my_publish_sell, rejectedListings);
        allLv.setAdapter(allAdapter);
        initiatedLv.setAdapter(initAdapter);
        rejectedLv.setAdapter(rejectedAdapter);
        setLvListener();
    }

    private void getListByState(final PublishStates states, final OpMode opMode, final PullToRefreshBase<ListView> listView, int page) {
        XinongHttpCommend.getInstance(mContext).sellerListingsMe(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                PageInfo pageInfo = JSONObject.parseObject(result, PageInfo.class);
                List<SellerListingInfoDTO> tempList = JSONObject.parseArray(pageInfo.getContent(), SellerListingInfoDTO.class);
                if (states == null) {
                    switch (opMode) {
                        case INIT:
                        case PULLDOWN:
                            listings = tempList;
                            break;
                        case PULLUP:
                            listings.addAll(tempList);
                            break;
                    }
                    allAdapter.refresh(listings);
                } else {
                    switch (states) {
                        case PENDING:
                            switch (opMode) {
                                case INIT:
                                case PULLDOWN:
                                    initiatedListings = tempList;
                                    break;
                                case PULLUP:
                                    initiatedListings.addAll(tempList);
                                    break;
                            }
                            initAdapter.refresh(initiatedListings);
                            break;
                        case REJECTED:
                            switch (opMode) {
                                case INIT:
                                case PULLDOWN:
                                    rejectedListings = tempList;
                                    break;
                                case PULLUP:
                                    rejectedListings.addAll(tempList);
                                    break;
                            }
                            rejectedListings = JSONObject.parseArray(pageInfo.getContent(), SellerListingInfoDTO.class);
                            rejectedAdapter.refresh(rejectedListings);
                            break;
                    }
                }
                listView.onRefreshComplete();

            }
        }, getCustomerId(), states, page, size);
    }


    @Override
    protected void initContentView(View contentView) {
        my_publish_tabs = contentView.findViewById(R.id.my_publish_tabs);
        my_publish_vp_view = contentView.findViewById(R.id.my_publish_vp_view);
       this.contentView = contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
