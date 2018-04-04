package tech.xinong.xnsm.pro.user.model;

import android.content.Context;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import tech.xinong.xnsm.util.T;

/**
 * Created by xiao on 2017/12/27.
 */

public class ProxyOnRefreshListener implements PullToRefreshBase.OnRefreshListener2{

    private Context mContext;
    private PullToRefreshBase.OnRefreshListener2 listener2;
    public ProxyOnRefreshListener(Context context,String status){
        mContext = context;
        switch (status){
            case "全部":
                listener2 = new AllListingsListener();
                break;
            case "等待审核":
                listener2 = new InitiatedListingsListener();
                break;
            case "审核未通过":
                listener2 = new RejectedListingsListener();
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        listener2.onPullDownToRefresh(refreshView);
        refreshView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        listener2.onPullUpToRefresh(refreshView);
        refreshView.onRefreshComplete();
    }



    private class AllListingsListener implements PullToRefreshBase.OnRefreshListener2{

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            T.showShort(mContext,"All---Down");
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            T.showShort(mContext,"All---up");
        }
    }
    private class InitiatedListingsListener implements PullToRefreshBase.OnRefreshListener2{

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            T.showShort(mContext,"Initiated---Down");
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            T.showShort(mContext,"Initiated---Down");
        }
    }
    private class RejectedListingsListener implements PullToRefreshBase.OnRefreshListener2{

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            T.showShort(mContext,"Rejected---Down");
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            T.showShort(mContext,"Rejected---Down");
        }
    }
}
