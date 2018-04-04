package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.Area;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectAreaAdapter;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_select_address)
public class SelectAddressActivity extends BaseActivity {
    @ViewInject(R.id.select_area_lv)
    private ListView lv;
    @ViewInject(R.id.select_area_tv_show)
    private TextView tvShow;
    private String ids;

    @Override
    public void initData() {
        showProgress();
        XinongHttpCommend.getInstance(mContext).getAreas(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                cancelProgress();
                List<Area> areas = JSON.parseArray(result, Area.class);
                showLv(areas);
            }
        });
    }

    private void showLv(final List<Area> areas) {
        lv.setAdapter(new SelectAreaAdapter(mContext, areas));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Area area = areas.get(position);
                T.showShort(mContext, area.getName());
                tvShow.append(area.getName()+"    ");
                ids += area.getId()+",";
                if (area.getChildren()==null||area.getChildren().size()==0){//没有子列表了
                    Intent myIntent = new Intent();
                    myIntent.putExtra("address",tvShow.getText().toString().trim());
                    myIntent.putExtra("ids",ids);
                    setResult(RESULT_OK,myIntent);
                    finish();
                }else {//还有子列表
                    showLv(area.getChildren());
                }
            }
        });
    }
}
