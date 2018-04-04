package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectSpecModel;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectSpecificationAdapter;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_select_specification)
public class SelectSpecificationActivity extends BaseActivity {

    @ViewInject(R.id.spec_lv)
    private ListView lvSpec;
    @ViewInject(R.id.select_spec_bt_submit)
    private Button submit;
    private Map<String, List<SpecificationConfigDTO>> specsMap;
    private List<SelectSpecModel> selectSpecModels;
    private SelectSpecificationAdapter adapter;
    private List<SpecificationConfigDTO> specsModelList;


    @Override
    public void initWidget() {

    }

    @Override
    public void initData() {
        selectSpecModels = new ArrayList<>();
        specsMap = new LinkedHashMap<>();
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        String[] spec_config = intent.getStringArrayExtra("spec_config");
        if (spec_config==null||spec_config.length<0) {
            showProgress();
            XinongHttpCommend.getInstance(this).getSpecConfigsByProductId(productId, new AbsXnHttpCallback(mContext) {
                @Override
                public void onSuccess(String info, String result) {
                    cancelProgress();
                    specsModelList = JSON.parseArray(result, SpecificationConfigDTO.class);
                    List<SpecificationConfigDTO> specList;
                    for (SpecificationConfigDTO specsModel : specsModelList) {
                        if (specsMap.get(specsModel.getName()) == null) {
                            specList = new ArrayList<>();
                            specList.add(specsModel);
                            specsMap.put(specsModel.getName(), specList);
                        } else {
                            specsMap.get(specsModel.getName()).add(specsModel);
                        }

                    }
                    int i = 0;
                    Iterator iter = specsMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String title = (String) entry.getKey();
                        List<SpecificationConfigDTO> specs = (List<SpecificationConfigDTO>) entry.getValue();
                        SelectSpecModel selectSpecModel = new SelectSpecModel();
                        selectSpecModel.setId(specsModelList.get(i).getId());
                        selectSpecModel.setTitle(title);
                        selectSpecModel.setSpecs(specs);
                        selectSpecModels.add(selectSpecModel);
                        i++;
                    }

                    adapter = new SelectSpecificationAdapter(mContext, selectSpecModels);
                    lvSpec.setAdapter(adapter);
                }
            });
        } else {

        }
        initNavigation();

    }

    private void initNavigation() {
        TextView tvTitle = findViewById(R.id.tv_center);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("选择货品规格");
    }


    @OnClick({R.id.select_spec_bt_submit})
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.select_spec_bt_submit:
                if (TextUtils.isEmpty(adapter.getResults())) {
                    Toast.makeText(mContext, "您还没有进行选择", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent myIntent = new Intent();
                myIntent.putExtra("result", adapter.getResults());
                String ids = adapter.getIds();
                myIntent.putExtra("ids", ids);
                setResult(RESULT_OK, myIntent);
                T.showShort(mContext, adapter.getResults());
                finish();
                break;
        }
    }

    private String getIds(String results) {
        String[] names = results.split(",");
        String ids = "";
        for (String name : names) {
            for (SpecificationConfigDTO specs : specsModelList) {
                if (name.equals(specs.getItem())) {
                    ids += specs.getId() + ",";
                }
            }

        }
        return ids.substring(0, ids.length() - 1);
    }

}
