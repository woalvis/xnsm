package tech.xinong.xnsm.pro.publish.model.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;

/**
 * Created by xiao on 2017/11/10.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<SpecificationConfigDTO> mList;
    private int selectorPosition = -1;

    public GridViewAdapter(Context context, List<SpecificationConfigDTO> mList) {
        this.mContext = context;
        this.mList = mList;

    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return mList != null ? position : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_publish_select_spec, null);
        RelativeLayout mRelativeLayout = convertView.findViewById(R.id.ll);
        TextView textView =  convertView.findViewById(R.id.tv);
        textView.setText(mList.get(position).getItem());
        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == position) {
            mRelativeLayout.setBackgroundResource(R.drawable.tv_green_bg);
            textView.setTextColor(Color.parseColor("#ffffff"));
        } else {
            //其他的恢复原来的状态
            mRelativeLayout.setBackgroundResource(R.drawable.tv_grey_bg);
            textView.setTextColor(Color.parseColor("#585858"));
        }
        return convertView;
    }


    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();
    }
}
