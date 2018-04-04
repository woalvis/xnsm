package tech.xinong.xnsm.pro.publish.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import tech.xinong.xnsm.pro.base.model.Area;

/**
 * Created by xiao on 2017/12/6.
 */

public class CityAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<Area> areas;

    public CityAdapter(Context context, List<Area> areas){
        mContext = context;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
