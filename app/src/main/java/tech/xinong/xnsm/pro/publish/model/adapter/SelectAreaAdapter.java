package tech.xinong.xnsm.pro.publish.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.model.Area;

/**
 * Created by xiao on 2016/11/29.
 */

public class SelectAreaAdapter extends BaseAdapter {
    private List<Area> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public SelectAreaAdapter(Context context,List<Area> datas){
        this.mDatas = datas;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Area getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_lv_select_area,null);
        TextView tv = (TextView) view.findViewById(R.id.tv_item_select_area);
        tv.setText(getItem(position).getName());
        return view;
    }
}
