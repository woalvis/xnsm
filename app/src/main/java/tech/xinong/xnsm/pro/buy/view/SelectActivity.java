package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.Area;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.SpecModel;
import tech.xinong.xnsm.pro.buy.model.adapter.SelectAreaAdapter;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

import static tech.xinong.xnsm.R.id.select_area_show;


/**
 * 选择具体品类的界面，包含之前传过来的参数
 */
@ContentView(R.layout.activity_select)
public class SelectActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.select_area)
    private TextView selectArea;
    @ViewInject(select_area_show)
    private TextView areaShow;
    @ViewInject(R.id.select_category)
    private TextView selectCategory;
    @ViewInject(R.id.select_category_show)
    private TextView categoryShow;
    @ViewInject(R.id.select_spec)
    private TextView selectSpec;
    @ViewInject(R.id.select_spec_show)
    private TextView specShow;
    @ViewInject(R.id.select_order)
    private TextView selectOrder;
    @ViewInject(R.id.select_order_show)
    private TextView orderShow;
    @ViewInject(R.id.select_layout_bottom)
    private View selectLayoutBottom;
    private LinearLayout linearLayoutPopup;
    private PopupWindow popupWindow;
    @Override
    public void initWidget() {
        selectArea.setOnClickListener(this);
        selectCategory.setOnClickListener(this);
        selectSpec.setOnClickListener(this);
        selectOrder.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        SpecModel spec = (SpecModel) intent.getSerializableExtra("spec");
        //展示之前选择的品类
        categoryShow.setText(spec.getProduct());
        //展示之前选择品种
        specShow.setText(spec.getItem());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_area:
                selectArea();
                break;
        }
    }

    /**
     * 选择区域
     */
    private void selectArea() {
         /*设置选择区域按钮不可点击,防止用户极端操作*/
        selectArea.setClickable(false);
        XinongHttpCommend.getInstance(mContext).getAreas(new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                List<Area> areas = JSON.parseArray(result, Area.class);
                resetAreas(areas);
                showAreaPopup(areas);
            }
        });
    }


    private void resetAreas(List<Area> areas){
        for (Area area : areas){
            if (area.getChildren()==null||area.getChildren().size()==0){

            }else {
                Area tempArea = new Area();
                tempArea.setId(area.getId());
                tempArea.setCode(area.getCode());
                tempArea.setChildren(null);
                tempArea.setName("全部");
                tempArea.setLevel(-1);
                tempArea.setHint(area.getName());
                area.getChildren().add(0,tempArea);
                resetAreas(area.getChildren());
            }
        }
    }

    /**
     * 选择地区的弹出框
     */
    private void showAreaPopup(final List<Area> areas) {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_select, null);
        linearLayoutPopup = (LinearLayout) view.findViewById(R.id.popup_layout);
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        ListView parentLv = (ListView) view.findViewById(R.id.popup_lv_parent);
        for (Area area : areas){
            area.setLevel(0);
        }

        parentLv.setAdapter(new SelectAreaAdapter(
                mContext,areas));


        parentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (areas.get(position).getChildren().size()!=0){
                    setSubArea(areas.get(position));

                }else {
                    T.showShort(SelectActivity.this, areas.get(position).getName());
                }
            }
        });
        popupWindow.setContentView(view);
        popupWindow.showAsDropDown(selectLayoutBottom);

    }




    private void setSubArea(final Area item){
        for (Area area : item.getChildren()){
            if (!area.getName().equals("全部"))
            area.setLevel(item.getLevel()+1);
        }

        int childCount = linearLayoutPopup.getChildCount();
        for (int i = childCount-1;i>item.getLevel();i--) {
            try {
                linearLayoutPopup.removeViewAt(i);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        final ListView subListView = new ListView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        subListView.setLayoutParams(params);
        linearLayoutPopup.addView(subListView);

        subListView.setAdapter(new tech.xinong.xnsm.pro.buy.model.adapter.SelectAreaAdapter(
                mContext,item.getChildren()));

        subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if (item.getChildren().get(position).getLevel()<0){
                   popupWindow.dismiss();
                   areaShow.setText(item.getChildren().get(position).getHint());
                   selectArea.setClickable(true);
                   return;
               }

                if (item.getChildren().get(position).getChildren().size()!=0){
                    setSubArea(item.getChildren().get(position));
                }else {

                    popupWindow.dismiss();
                    areaShow.setText(item.getChildren().get(position).getName());
                    selectArea.setClickable(true);
                }
            }
        });

    }

}
