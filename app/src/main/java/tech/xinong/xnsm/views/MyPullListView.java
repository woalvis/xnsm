package tech.xinong.xnsm.views;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by xiao on 2018/1/4.
 */

public class MyPullListView extends PullToRefreshListView {
    public MyPullListView(Context context) {
        super(context);
    }

    public MyPullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPullListView(Context context, Mode mode) {
        super(context, mode);
    }

    public MyPullListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
