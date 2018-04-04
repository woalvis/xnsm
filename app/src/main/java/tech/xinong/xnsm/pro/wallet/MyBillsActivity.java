package tech.xinong.xnsm.pro.wallet;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.EndlessRecyclerOnScrollListener;
import tech.xinong.xnsm.pro.base.model.OpMode;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.wallet.model.FinanceRecordsModel;
import tech.xinong.xnsm.pro.wallet.model.MyDecoration;
import tech.xinong.xnsm.pro.wallet.model.RefreshAdapter;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_my_bills)
public class MyBillsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @ViewInject(R.id.rv_bill)
    private RecyclerView rv_bill;
    @ViewInject(R.id.swipe_layout)
    private SwipeRefreshLayout swipe_layout;
    @ViewInject(R.id.layout_no_data)
    private RelativeLayout no_data;
    private List<FinanceRecordsModel> mDatas;
    private RefreshAdapter mAdapter;
    private int size = 20;
    private int page = 0;
    private int totalPage = 0;

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //添加分隔线
        rv_bill.addItemDecoration(new MyDecoration(this, MyDecoration.VERTICAL_LIST));
        rv_bill.setHasFixedSize(true);
        rv_bill.setLayoutManager(linearLayoutManager);
        swipe_layout.setOnRefreshListener(this);
        financeRecords(OpMode.INIT);
        rv_bill.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreData();
            }
        });
    }


    private void financeRecords(final OpMode opMode){
        XinongHttpCommend.getInstance(mContext).financeRecords(page, size, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                PageInfo pageInfo = JSON.parseObject(result,PageInfo.class);
                totalPage =pageInfo.getTotalPages();
                String content = pageInfo.getContent();
                switch (opMode){
                    case INIT:
                    case PULLDOWN:

                        mDatas = JSON.parseArray(content,FinanceRecordsModel.class);
//                        mAdapter.notifyDataSetChanged();
                        mAdapter = new RefreshAdapter(mContext,mDatas);
                        rv_bill.setAdapter(mAdapter);
                        break;
                    case PULLUP:
                        mDatas.addAll(JSON.parseArray(content,FinanceRecordsModel.class));
                        mAdapter.notifyDataSetChanged();
                        break;
                }

                if (mDatas.size()==0){
                    swipe_layout.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                }else {
                    no_data.setVisibility(View.GONE);
                    swipe_layout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 0;
        financeRecords(OpMode.PULLDOWN);
        swipe_layout.setRefreshing(false);
    }


    private void loadMoreData(){
        page++;
        financeRecords(OpMode.PULLUP);
        swipe_layout.setRefreshing(false);
    }

    @Override
    public String setToolBarTitle() {
        return "我的账单";
    }
}
