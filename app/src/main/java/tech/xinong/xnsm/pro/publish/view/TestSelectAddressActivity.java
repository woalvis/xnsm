package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.Area;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.publish.model.adapter.AreaAdapter;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_address)
public class TestSelectAddressActivity extends BaseActivity implements AreaAdapter.OnItemClickListener{
    @ViewInject(R.id.tv_address_show)
    private TextView tv_address_show;
    @ViewInject(R.id.rv_area)
    private RecyclerView rv_area;
    private List<Area> areaList;
    private AreaAdapter adapter;
    private String areaStr="";
    private String ids="";

    @Override
    public void initData() {
        areaList = new ArrayList<>();
        showProgress();
        XinongHttpCommend.getInstance(mContext).getAreas(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                cancelProgress();
                areaList = JSON.parseArray(result, Area.class).get(0).getChildren();
                showLv();
            }
        });
    }

    private void showLv() {
        adapter = new AreaAdapter(areaList);
        rv_area.setLayoutManager(new GridLayoutManager(mContext, 4,LinearLayoutManager.VERTICAL,
                false));
        rv_area.setAdapter(adapter);
        adapter.setListener(this);
    }

    @Override
    public String setToolBarTitle() {
        return "选择地址";
    }

    @Override
    public void onClick(AreaAdapter.AreaHolder holder, int position) {
        ids += areaList.get(position).getId()+",";
        if (areaList.get(position).getChildren().size()==0){
            Intent intent = new Intent();
            areaStr += areaList.get(position).getName()+" ";
            intent.putExtra("address",areaStr);
            intent.putExtra("ids",ids);
            setResult(RESULT_OK,intent);
            finish();
        }else {
            areaStr += areaList.get(position).getName()+" ";
            tv_address_show.setText(areaStr);
            adapter.reFresh(areaList.get(position).getChildren());
            areaList = areaList.get(position).getChildren();
        }
    }
}
