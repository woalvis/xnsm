package tech.xinong.xnsm.pro.publish.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.util.T;

/**
 * Created by xiao on 2016/11/27.
 */

public class SelectSpecificationAdapter extends BaseAdapter {

    private List<SelectSpecModel> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;
    private Map<String, SpecificationConfigDTO> results;
    private Map<String,String> ids;
    private GridViewAdapter mAdapter;
    private int selectorPosition;

    public SelectSpecificationAdapter(Context context, List<SelectSpecModel> datas) {
        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
        results = new HashMap<>();
        ids = new HashMap<>();
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public SelectSpecModel getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_select_spec, parent, false);
            viewHolder.specsTitle = convertView.findViewById(R.id.specs_title);
            viewHolder.specsGrid = convertView.findViewById(R.id.grid_specs);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String title = mDatas.get(position).getTitle();

        viewHolder.specsTitle.setText(title);
        final GridViewAdapter mAdapter = new GridViewAdapter(mContext,getItem(position).getSpecs());
        viewHolder.specsGrid.setAdapter(mAdapter);
        final String finalTitle = title;
        final int pPosition = position;
        viewHolder.specsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //把点击的position传递到adapter里面去
                mAdapter.changeState(position);
                selectorPosition = position;
                results.put(getItem(pPosition).getTitle(),
                        getItem(pPosition).getSpecs().get(position));
            }
        });

        return convertView;
    }

    public void refresh(List<SelectSpecModel> datas){
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public String getResults() {
        StringBuffer stringBuffer = new StringBuffer();
        String resultStr = "";
        if (results != null) {
            for (String key : results.keySet()) {
                if (results.get(key)==null){
                    T.showShort(mContext, key+"不能为空");
                    return "";
                }
                stringBuffer.append(results.get(key).getItem() + ",");
            }
        }
        if (stringBuffer.toString().length()!=0) {
            resultStr = stringBuffer.toString().substring(0,stringBuffer.toString().length()-1);
        }
        return resultStr;
    }

    public String getIds(){
        StringBuffer stringBuffer = new StringBuffer();
        String resultStr = "";
        if (results!=null){
            for (String key : results.keySet()) {
                if (results.get(key)==null){
                    T.showShort(mContext, key+"不能为空");
                    return "";
                }
                stringBuffer.append(results.get(key).getId() + ",");
            }
        }

        if (stringBuffer.toString().length()!=0) {
            resultStr = stringBuffer.toString().substring(0,stringBuffer.toString().length()-1);
        }
        return resultStr;
    }

    private class ViewHolder {
        public TextView specsTitle;
        public GridView specsGrid;
    }
}
