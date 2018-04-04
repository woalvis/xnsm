package tech.xinong.xnsm.pro.publish.model.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.model.Area;

/**
 * Created by xiao on 2017/12/5.
 */

public class ProvinceAdapter extends BaseAdapter {

    private List<Area> areas;
    private LayoutInflater inflater;
    private Context mContext;
    private int defItem = 0;

    public ProvinceAdapter(Context context, List<Area> areas) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.areas = areas;
        Area a = new Area();
        a.setName("");
        areas.add(a);
    }

    @Override
    public int getCount() {
        return areas.size();
    }

    @Override
    public Area getItem(int i) {
        return areas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public void setDefItem(int defItem) {
        this.defItem = defItem;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = inflater.inflate(R.layout.item_text, viewGroup, false);
        TextView tv = view1.findViewById(R.id.item_tv);
        tv.setText(areas.get(i).getName());
        if (defItem == i) {
            tv.setTextColor(mContext.getResources().getColor(R.color.green_btn_color));
            tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.select_bg));
        } else {
            tv.setTextColor(Color.parseColor("#767676"));
            tv.setBackgroundDrawable(null);
        }

        return view1;
    }
}
