package tech.xinong.xnsm.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by xiao on 2017/11/30.
 */

public class PagerScrollView1 extends ScrollView {
    private GestureDetector mGestureDetector;

    private ListView listView;
    public PagerScrollView1(Context context) {
        super(context);
        init();
    }




    public PagerScrollView1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public PagerScrollView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
//        mGestureDetector = new GestureDetector(getContext(),
//                new YScrollDetector());
//        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {


        // TODO Auto-generated method stub
        if (listView != null && checkArea(listView, ev)) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
//        return super.onInterceptTouchEvent(ev)
//                && mGestureDetector.onTouchEvent(ev);
    }

    private class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            if (Math.abs(distanceY) >= Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }


    /**
     *  测试view是否在点击范围内
     * @param
     * @param v
     * @return
     */
    private boolean checkArea(View v, MotionEvent event){
        float x = event.getRawX();
        float y = event.getRawY();
        int[] locate = new int[2];
        v.getLocationOnScreen(locate);
        int l = locate[0];
        int r = l + v.getWidth();
        int t = locate[1];
        int b = t + v.getHeight();
        if (l < x && x < r && t < y && y < b) {
            return true;
        }
        return false;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}