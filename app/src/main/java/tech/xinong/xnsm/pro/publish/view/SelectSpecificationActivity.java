package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.publish.model.SpecsModel;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_select_specification)
public class SelectSpecificationActivity extends BaseActivity {

    @ViewInject(R.id.spec_lv)
    private ListView lvSpec;

    private Map<String,List<String>> specsMap;

    public enum SpecificationCategory {
        VARIETY("品种"), SIZE("大小"), COLOR("颜色"), GRADE("等级"), MISC("其他");
        SpecificationCategory(String desc){
            this.desc = desc;
        }
        public String desc;
    }
    @Override
    public void initWidget() {

    }

    @Override
    public void initData() {
        specsMap = new LinkedHashMap<>();
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        XinongHttpCommend.getInstence(this).getAllSpecsByproductId(productId, new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                List<SpecsModel> specsModelList = JSON.parseArray(result,SpecsModel.class);
                List<String> specList = null;
                for (SpecsModel specsModel : specsModelList){
                    if (specsMap.get(specsModel.getCategory())==null){
                        specList = new ArrayList<>();
                        specList.add(specsModel.getItem());
                        specsMap.put(specsModel.getCategory(),specList);
                    }else {
                        specsMap.get(specsModel.getCategory()).add(specsModel.getItem());
                    }
                }

                lvSpec.setAdapter(new CommonAdapter<Map<String,List<String>>>() {
                });

            }
        });
    }
}
