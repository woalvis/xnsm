package tech.xinong.xnsm.pro.publish.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by xiao on 2016/11/25.
 */

public class SpecsShowAdapter extends BaseAdapter {

    private LinkedHashMap<String,List<String>> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    public SpecsShowAdapter(LinkedHashMap<String,List<String>> datas, Context context){
        mContext = context;
        mDatas = datas;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private class ViewHolder{
        public TextView specsTitle;
        public GridView specsGrid;
    }
}
