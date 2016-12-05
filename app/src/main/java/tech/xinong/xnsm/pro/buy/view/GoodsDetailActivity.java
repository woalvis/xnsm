package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.SpecModel;
import tech.xinong.xnsm.pro.publish.model.PublishSellInfoModel;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_goods_detail)
public class GoodsDetailActivity extends BaseActivity {

    /*产品描述*/
    @ViewInject(R.id.product_specification)
    private TextView productDescription;
    /*总量*/
    @ViewInject(R.id.product_total_quantity)
    private TextView totalQuantity;
    @ViewInject(R.id.product_address)
    private TextView address;
    @ViewInject(R.id.product_unit_price)
    private TextView unitPrice;
    @ViewInject(R.id.product_unit)
    private TextView productUnit;
    @ViewInject(R.id.product_min_quantity)
    private TextView minQuantity;
    @ViewInject(R.id.product_grid_specification_configs)
    private GridView gridSpecificationConfigs;
    @ViewInject(R.id.product_notes)
    private TextView notes;
    @ViewInject(R.id.product_logistic_method_tags)
    private LinearLayout logisticMethodLayout;
    @ViewInject(R.id.buy_now)
    private Button btBuyNow;

    private Map<String, String> weightUnit;


    @Override
    public void initData() {
        init();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        XinongHttpCommend.getInstence(this).getProductListings(id, new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                PublishSellInfoModel  publishSellInfoModel = JSON.parseObject(result, PublishSellInfoModel.class);
                initPage(publishSellInfoModel);
            }
        });
    }




    private void init() {
        weightUnit = new HashMap<>();
        weightUnit.put("JIN", "斤");
        weightUnit.put("KG", "公斤");
        weightUnit.put("TON", "吨");
    }


    private void initPage(final PublishSellInfoModel publishSellInfoModel) {
        String unit = weightUnit.get(publishSellInfoModel.getQuantityUnit());
        productDescription.setText(publishSellInfoModel.getSpecification());
        String totalQuantityStr = String.valueOf(publishSellInfoModel.getTotalQuantity());
        totalQuantityStr+=weightUnit.get(publishSellInfoModel.getQuantityUnit());
        if (publishSellInfoModel.getInStock()) {
            totalQuantityStr+="现货";
        } else {
            totalQuantityStr+="期货";
        }
        totalQuantity.setText(totalQuantityStr);
        address.setText(publishSellInfoModel.getAddress());
        unitPrice.setText(String.valueOf(publishSellInfoModel.getUnitPrice()));
        productUnit.setText(weightUnit.get(publishSellInfoModel.getQuantityUnit()));
        minQuantity.setText(publishSellInfoModel.getMinQuantity()+unit+"起");
        List<SpecModel> specShows = publishSellInfoModel.getSpecificationConfigs();
        SpecModel specModelTemp = null;
        for (SpecModel specModel : specShows){

            if (specModel.getCategory().equals("VARIETY")){
                specModelTemp = specModel;
            }
        }
        specShows.remove(specModelTemp);
        gridSpecificationConfigs.setAdapter(new CommonAdapter<SpecModel>(mContext,R.layout.item_grid_specification_configs,specShows) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, SpecModel item) {

                    viewHolder.setTextForTextView(R.id.item_grid_spec_name, SpecificationCategory.get(item.getCategory()));
                    viewHolder.setTextForTextView(R.id.item_grid_spec_value,item.getItem());

            }
        });
        notes.setText(publishSellInfoModel.getNotes());


        for (String logisticMethodTag : publishSellInfoModel.getLogisticMethodTags().split(",")){
            TextView tv = new TextView(this);
            tv.setText(logisticMethodTag);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 20;
            logisticMethodLayout.addView(tv,params);
        }

        btBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,BuyNowActivity.class);
                intent.putExtra("item",publishSellInfoModel);
                startActivity(intent);
            }
        });
    }


    public enum SpecificationCategory {
        VARIETY("品种"), SIZE("大小"), COLOR("颜色"), GRADE("等级"), MISC("其他");
        SpecificationCategory(String desc){
            this.desc = desc;
        }
        public String desc;
        public static String get(String name){

            switch (name){
                case "VARIETY":
                    return VARIETY.desc;
                case "SIZE":
                    return SIZE.desc;
                case "COLOR":
                    return COLOR.desc;
                case "GRADE":
                    return GRADE.desc;
                case "MISC":
                    return MISC.desc;
                default:
                    return "";
            }
        }
    }
}
