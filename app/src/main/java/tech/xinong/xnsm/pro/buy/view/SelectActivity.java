package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.Area;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.SpecModel;


/**
 * 选择具体品类的界面，包含之前传过来的参数
 */
public class SelectActivity extends BaseActivity implements View.OnClickListener {

    private TextView selectArea;
    private TextView areaShow;
    private TextView selectCategory;
    private TextView categoryShow;
    private TextView selectSpec;
    private TextView specShow;
    private TextView selectOrder;
    private TextView orderShow;
    /*地区数据map*/
    private Map<Area, List<Area>> areasMap;
    private List<Area> parentList;
    private List<Area> subList;

    private View selectLayoutBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int bindView() {
        return R.layout.activity_select;
    }

    @Override
    public void initWidget() {
        selectArea = (TextView) this.findViewById(R.id.select_area);
        areaShow = (TextView) this.findViewById(R.id.select_area_show);
        selectCategory = (TextView) this.findViewById(R.id.select_category);
        categoryShow = (TextView) this.findViewById(R.id.select_category_show);
        selectSpec = (TextView) this.findViewById(R.id.select_spec);
        specShow = (TextView) this.findViewById(R.id.select_spec_show);
        selectOrder = (TextView) this.findViewById(R.id.select_order);
        orderShow = (TextView) this.findViewById(R.id.select_order_show);
        selectLayoutBottom = this.findViewById(R.id.select_layout_bottom);


        selectArea.setOnClickListener(this);
        selectCategory.setOnClickListener(this);
        selectSpec.setOnClickListener(this);
        selectOrder.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        SpecModel spce = (SpecModel) intent.getSerializableExtra("spec");
        Toast.makeText(this, spce.toString(), Toast.LENGTH_SHORT).show();
        //展示之前选择的品类
        categoryShow.setText(spce.getProduct());
        //展示之前选择品种
        specShow.setText(spce.getItem());
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
        XinongHttpCommend.getInstence(mContext).getAreas(new AbsXnHttpCallback() {

            @Override
            public void onSuccess(String info, String result) {
                List<Area> areas = JSON.parseArray(result, Area.class);
                areasMap = getAreaMap(areas);
                showAreaPopup();
            }
        });
    }


    private void selectCategory() {

    }


    /**
     * 把从服务器拿到的地区信息组装成能用的数据结构
     *
     * @param areas
     * @return
     */
    private Map<Area, List<Area>> getAreaMap(List<Area> areas) {
        Map<Area, List<Area>> areaMap = new LinkedHashMap<>();
        List<Area> areaParentList = new ArrayList<>();
        List<Area> areaSubList = new ArrayList<>();
        String rootId = "";

        for (Area area : areas) {
            if (TextUtils.isEmpty(area.getParentStr())) {
                rootId = area.getId();
            }
        }


        for (Area area : areas) {
            if (TextUtils.isEmpty(area.getParentStr()) || rootId.equals(area.getParentStr())) {
                areaParentList.add(area);
            } else {
                areaSubList.add(area);
            }
        }

        for (Area area : areaParentList) {
            String parentId = area.getId();
            List<Area> areaSubs = new ArrayList<>();
            for (Area areaSub : areaSubList) {
                if (areaSub.getParentStr().equals(parentId)) {
                    areaSubs.add(areaSub);
                    //areaSubList.remove(areaSub);
                }

            }
            areaMap.put(area, areaSubs);
        }

        parentList = new ArrayList<>();
        for (Map.Entry<Area, List<Area>> entry : areaMap.entrySet()) {
            parentList.add(entry.getKey());
        }
        return areaMap;
    }


    /**
     * 选择地区的弹出框
     */
    private void showAreaPopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_select, null);
        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        ListView parentLv = (ListView) view.findViewById(R.id.popup_lv_parent);
        final ListView subLv = (ListView) view.findViewById(R.id.popup_lv_sub);
        parentLv.setAdapter(new CommonAdapter<Area>(this, android.R.layout.simple_list_item_1, parentList) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final Area item) {
                viewHolder.setTextForTextView(android.R.id.text1, item.getName());
                viewHolder.setOnClickListener(android.R.id.text1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (areasMap.get(item).size() != 0) {
                            Area areaAll = new Area();
                            areaAll.setName("全部");
                            areaAll.setParentStr(areasMap.get(item).get(0).getParentStr());
                            areasMap.get(item).add(0, areaAll);
                            subLv.setVisibility(View.VISIBLE);

                            subLv.setAdapter(new CommonAdapter<Area>(SelectActivity.this, android.R.layout.simple_list_item_1, areasMap.get(item)) {
                                @Override
                                protected void fillItemData(CommonViewHolder viewHolder, int position, final Area item) {
                                    viewHolder.setTextForTextView(android.R.id.text1, item.getName());

                                    viewHolder.setOnClickListener(android.R.id.text1, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            popupWindow.dismiss();

                                              /*设置选择区域按钮可点击*/
                                            selectArea.setClickable(true);
                                            if (item.getName().equals("全部")) {

                                                for (Area area : parentList) {
                                                    if (item.getParentStr().equals(area.getId())) {
                                                        areaShow.setText(area.getName());
                                                    }
                                                }

                                            } else {
                                                areaShow.setText(item.getName());
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
  /*设置选择区域按钮可点击*/
                            selectArea.setClickable(true);

                            popupWindow.dismiss();
                            areaShow.setText(item.getName());


                        }

                    }
                });
            }
        });

        popupWindow.setContentView(view);

        popupWindow.showAsDropDown(selectLayoutBottom);

    }
}
