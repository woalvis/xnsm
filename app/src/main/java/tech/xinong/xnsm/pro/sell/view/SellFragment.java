package tech.xinong.xnsm.pro.sell.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.Util.ProperTies;
import tech.xinong.xnsm.pro.base.application.XnsApplication;
import tech.xinong.xnsm.pro.base.model.OpMode;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;
import tech.xinong.xnsm.pro.publish.view.PublishBuyActivity;
import tech.xinong.xnsm.pro.publish.view.PublishSelectActivity;
import tech.xinong.xnsm.pro.sell.model.BuyerListingSum;
import tech.xinong.xnsm.pro.sell.model.SellListingsAdapter;
import tech.xinong.xnsm.pro.user.view.LoginActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;

/**
 * 我要卖页面
 * Created by Administrator on 2016/8/10.
 */
public class SellFragment extends BaseFragment<BuyPresenter,BaseView> {

    private BuyPresenter buyPresenter;
    private PullToRefreshListView productLv;
    private int SIZE = 10;
    private int currentPage;
    private List<BuyerListingSum> lists;
    private SellListingsAdapter adapter;
    private int totalPage = 0;

    //创建对象
    @Override
    public BuyPresenter bindPresenter() {
       buyPresenter = new BuyPresenter(getContext());

        return buyPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lists = new ArrayList<>();
    }


    @Override
    public void initData() {
        adapter = new SellListingsAdapter(mContext,R.layout.item_buy_show,lists);
        productLv.setMode(PullToRefreshBase.Mode.BOTH);
        productLv.setAdapter(adapter);
        productLv.setOnRefreshListener(new MyPullListener());
        currentPage = 0;
        getBuyerListings(currentPage,10,OpMode.INIT);
    }

    @Override
    protected void initContentView(View contentView) {
        setHasOptionsMenu(true);
        initNavigation(contentView);
        lists = new ArrayList<>();
        productLv = contentView.findViewById(R.id.lv_product);

    }

    private void getBuyerListings(int page, int size, final OpMode mode){
        XinongHttpCommend.getInstance(mContext).buyerListings(page, size, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                PageInfo pageInfo = JSONObject.parseObject(result,PageInfo.class);
                totalPage = pageInfo.getTotalPages();
                if (productLv!=null)
                productLv.onRefreshComplete();
                updateListings(pageInfo.getContent(),mode);
            }
        });
    }

    private void updateListings(String result, OpMode mode) {

        List<BuyerListingSum> tempLists = JSONObject.parseArray(result,BuyerListingSum.class);
        switch (mode){
            case INIT:
                lists = tempLists;
                break;
            case PULLUP:
                lists.addAll(tempLists);
                break;
            case PULLDOWN:
                lists = tempLists;
                break;
        }
        adapter.refresh(lists);
    }


    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_sell;
    }

    @Override
    public String setToolBarTitle() {

        return "我要卖";
    }


    class MyPullListener implements PullToRefreshBase.OnRefreshListener2<ListView>{

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            String label = DateUtils.formatDateTime(
                    XnsApplication.getAppContext(),
                    System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);

            // Update the LastUpdatedLabel
            refreshView.getLoadingLayoutProxy()
                    .setLastUpdatedLabel(label);
            getBuyerListings(0,10,OpMode.PULLDOWN);
        }

        @Override
        public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView) {
            if (currentPage+1 == totalPage){
                T.showShort(mContext,"已经到底了");
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.onRefreshComplete();
                    }
                }, 1000);
            }else {
                currentPage++;
                getBuyerListings(currentPage, SIZE, OpMode.PULLUP);
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_buy,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_publish:
                if (isLogin()){
                    Properties proper = ProperTies.getProperties(mContext);
                    String favs[] = proper.getProperty(XnsConstant.FAVS).split(",");
                    if (favs.length>0&&!favs[0].equals("")){
                        if (favs.length==1){
                            Intent intent = new Intent(mContext,PublishBuyActivity.class);
                            String productId = mContext.getConfigIds()[0];
                            String productName = mContext.getConfigNames()[0];
                            intent.putExtra("productId",
                                    productId);
                            intent.putExtra("productName",productName);
                            mContext.startActivity(intent);
                        }
                    }else {
                        PublishSelectActivity.skip(getActivity(),PublishSelectActivity.PUBLISH,0);
                    }
                }else {
                    twoButtonDialog("喜农市",
                            "您还没有登录账号，不能进行发布",
                            "去登陆",
                            "再看看",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    skipActivity(LoginActivity.class);
                                }
                            },
                            null);
                }




                break;
        }
        return true;
    }
}
