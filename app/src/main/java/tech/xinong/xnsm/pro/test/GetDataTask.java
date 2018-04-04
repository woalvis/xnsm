package tech.xinong.xnsm.pro.test;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.LinkedList;

/**
 * Created by xiao on 2017/11/16.
 */

public class GetDataTask extends AsyncTask<Void,Void,Void>{

    private PullToRefreshListView mPullRefreshListView;
    private ArrayAdapter<String> mAdapter;
    private LinkedList<String> mListItems;

    public GetDataTask(PullToRefreshListView lv,
                       ArrayAdapter<String> adapter,
                       LinkedList<String> listItems){
        mPullRefreshListView = lv;
        mAdapter = adapter;
        mListItems = listItems;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO 自动生成的方法存根
        super.onPostExecute(result);
        //得到当前的模式
        PullToRefreshBase.Mode mode = mPullRefreshListView.getCurrentMode();
        if(mode == PullToRefreshBase.Mode.PULL_FROM_START) {
            mListItems.addFirst("这是刷新出来的数据");
        }
        else {
            mListItems.addLast("这是刷新出来的数据");
        }
        // 通知数据改变了
        mAdapter.notifyDataSetChanged();
        // 加载完成后停止刷新
        mPullRefreshListView.onRefreshComplete();

    }


}
