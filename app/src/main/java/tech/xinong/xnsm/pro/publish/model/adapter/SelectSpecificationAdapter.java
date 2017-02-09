package tech.xinong.xnsm.pro.publish.model.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.publish.view.SelectSpecificationActivity;
import tech.xinong.xnsm.util.T;

/**
 * Created by xiao on 2016/11/27.
 */

public class SelectSpecificationAdapter extends BaseAdapter {

    private List<SelectSpecModel> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;
    private Map<String, String> results;
    private Map<String,String> ids;

    public SelectSpecificationAdapter(Context context, List<SelectSpecModel> datas) {
        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
        results = new HashMap<>();
        ids = new HashMap<>();
        for (SelectSpecModel selectSpecModel : mDatas) {
            for (SelectSpecificationActivity.SpecificationCategory category : SelectSpecificationActivity.SpecificationCategory.values())
            {
                if (selectSpecModel.getTitle().equals(category.name())){
                    results.put(category.desc, "");
                }
            }

        }
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
            viewHolder.specsTitle = (TextView) convertView.findViewById(R.id.specs_title);
            viewHolder.specsGrid = (GridView) convertView.findViewById(R.id.grid_specs);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String title = "";
        for (SelectSpecificationActivity.SpecificationCategory category : SelectSpecificationActivity.SpecificationCategory.values()) {
            if (getItem(position).getTitle().equals(category.name())) {
                title = category.desc;
            }
        }
        viewHolder.specsTitle.setText(title);

        viewHolder.specsGrid.setAdapter(new ArrayAdapter<String>(mContext, R.layout.item_border_text, getItem(position).getSpecs()));

        final String finalTitle = title;
        viewHolder.specsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                int a = parent.getChildCount();
                for (int i = 0; i < a; i++) {
                    TextView tv = (TextView) parent.getChildAt(i);
                    tv.setTextColor(mContext.getResources().getColor(R.color.black));
                }

                //Toast.makeText(mContext, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                results.put(finalTitle, ((TextView) view).getText().toString());
                //ids.put(finalTitle,mDatas.get(position).getId());
                ((TextView) parent.getChildAt(position)).setTextColor(mContext.getResources().getColor(R.color.green_85c43d));

            }
        });

        return convertView;
    }


    public String getResults() {
        StringBuffer stringBuffer = new StringBuffer();
        String resultStr = "";
        if (results != null) {
            for (String key : results.keySet()) {
                if (TextUtils.isEmpty(results.get(key))){
                    T.showShort(mContext, key+"不能为空");
                    return "";
                }
                stringBuffer.append(results.get(key) + ",");
            }
        }
        if (stringBuffer.toString().length()!=0) {
            resultStr = stringBuffer.toString().substring(0,stringBuffer.toString().length()-2);
        }
        return resultStr;
    }

    private class ViewHolder {
        public TextView specsTitle;
        public GridView specsGrid;
    }
}
