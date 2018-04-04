package tech.xinong.xnsm.pro.user.model.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xiao on 2016/12/12.
 */

public class MyPagerAdapter extends PagerAdapter {

    private List<View> mViewList;
    private List<String> mTitleList;
    private Context mContext;
    private LayoutInflater mInflate;

    public MyPagerAdapter(Context context, List<View> viewList, List<String> titleList){

        mContext = context;
        mTitleList = titleList;
        mViewList = viewList;
        mInflate = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup parent = (ViewGroup) mViewList.get(position).getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(mViewList.get(position));//添加页卡
        return mViewList.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));//删除页卡
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);//页卡标题
    }



}
