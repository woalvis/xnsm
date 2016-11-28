package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import tech.xinong.xnsm.pro.publish.model.SpecsModel;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectSpecModel;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectSpecificationAdapter;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_select_specification)
public class SelectSpecificationActivity extends BaseActivity {

    @ViewInject(R.id.spec_lv)
    private ListView lvSpec;
    @ViewInject(R.id.select_spec_bt_submit)
    private Button submit;

    private Map<String,List<String>> specsMap;
    private List<SelectSpecModel> selectSpecModels;
    private SelectSpecificationAdapter adapter;

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
        selectSpecModels = new ArrayList<>();
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

                Iterator iter = specsMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String title = (String) entry.getKey();
                    List<String> specs = (List<String>) entry.getValue();
                    SelectSpecModel selectSpecModel = new SelectSpecModel();
                    selectSpecModel.setTitle(title);
                    selectSpecModel.setSpecs(specs);
                    selectSpecModels.add(selectSpecModel);
                }

                adapter = new SelectSpecificationAdapter(mContext,selectSpecModels);
                lvSpec.setAdapter(adapter);

            }
        });
    }


    @OnClick({R.id.select_spec_bt_submit})
    public void widgetClick(View view){
        switch (view.getId()){
            case R.id.select_spec_bt_submit:
                if (TextUtils.isEmpty(adapter.getResults())){
                    //Toast.makeText(mContext, "您还没有进行选择", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent myIntent = new Intent();
                myIntent.putExtra("result",adapter.getResults());
                setResult(RESULT_OK,myIntent);
                Toast.makeText(mContext, adapter.getResults(), Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

}
