package tech.xinong.xnsm.pro.test;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Arrays;
import java.util.LinkedList;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;

/**
 * Created by xiao on 2017/10/11.
 */

@ContentView(R.layout.test_view_layout1)
public class TestActivity extends BaseActivity {
    private Button button1, button2, button3;
    private ImageView animationIV;
    private ImageView animationIV2;
    private ImageView animationIV3;
    private AnimationDrawable AniDraw, AniDraw2, AniDraw3;
    //一个可以下拉刷新的listView对象
    private PullToRefreshListView mPullRefreshListView;
    //普通的listview对象
    private ListView actualListView;
    //添加一个链表数组，来存放string数组，这样就可以动态增加string数组中的内容了
    private LinkedList<String> mListItems;
    //给listview添加一个普通的适配器
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        button1 = (Button) findViewById(android.R.id.button1);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (AniDraw.isRunning()) {
//                    AniDraw.stop();
//                } else {
//                    AniDraw.start();
//                }
//
//            }
//        });
//        button2 = (Button) findViewById(android.R.id.button2);
//        button2.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (AniDraw2.isRunning()) {
//                    AniDraw2.stop();
//                } else {
//                    AniDraw2.start();
//                }
//            }
//        });
//        button3 = (Button) findViewById(android.R.id.button3);
//        button3.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (AniDraw3.isRunning()) {
//                    AniDraw3.stop();
//                } else {
//                    AniDraw3.start();
//                }
//            }
//        });
//
//        animationIV = (ImageView) findViewById(R.id.animationIV);
//        /**
//         * 这里设置的是setBackgroundResource，那么你获取的时候通过getBackground
//         */
//        animationIV.setBackgroundResource(R.drawable.loading_anim);
//        AniDraw = (AnimationDrawable) animationIV.getBackground();
//        /**
//         * 在xml里面通过src来设置跟在代码里面使用setImageResource获取的时候通过getDrawable
//         * 例如：animationIV2.setImageResource(R.anim.load_animation_2);是一样的
//         */
//        animationIV2 = (ImageView) findViewById(R.id.animationIV2);
//        AniDraw2 = (AnimationDrawable) animationIV2.getDrawable();
//        animationIV3 = (ImageView) findViewById(R.id.animationIV3);
//        animationIV3.setImageResource(R.drawable.loading_anim);
//        AniDraw3 = (AnimationDrawable) animationIV3.getDrawable();
    }


    @Override
    public void initData() {
        initPTRListView();
        initListView();
        mPullRefreshListView.setRefreshing(true);
    }

    /**
     * 设置下拉刷新的listview的动作
     */
    private void initPTRListView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        //设置拉动监听器
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //设置下拉时显示的日期和时间
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // 更新显示的label
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                // 开始执行异步任务，传入适配器来进行数据改变
                new GetDataTask(mPullRefreshListView, mAdapter, mListItems).execute();
            }
        });

        // 添加滑动到底部的监听器
        mPullRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                Toast.makeText(getApplication(), "已经到底了", Toast.LENGTH_SHORT).show();
            }
        });

        //mPullRefreshListView.isScrollingWhileRefreshingEnabled();//看刷新时是否允许滑动
        //在刷新时允许继续滑动
        mPullRefreshListView.setScrollingWhileRefreshingEnabled(true);
        //mPullRefreshListView.getMode();//得到模式
        //上下都可以刷新的模式。这里有两个选择：Mode.PULL_FROM_START，Mode.BOTH，PULL_FROM_END
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

        /**
         * 设置反馈音效
         */
//        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(this);
//        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
//        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
//        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
//        mPullRefreshListView.setOnPullEventListener(soundListener);
    }


    /**
     * 设置listview的适配器
     */
    private void initListView() {
        //通过getRefreshableView()来得到一个listview对象
        actualListView = mPullRefreshListView.getRefreshableView();

        String []data = new String[] {"android","ios","wp","java","c++","c#"};
        mListItems = new LinkedList<String>();
        //把string数组中的string添加到链表中
        mListItems.addAll(Arrays.asList(data));

        mAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, mListItems);
        actualListView.setAdapter(mAdapter);
    }

}