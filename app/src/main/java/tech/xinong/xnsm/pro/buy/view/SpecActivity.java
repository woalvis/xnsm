package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.util.ioc.ContentView;

@ContentView( R.layout.activity_spec)
public class SpecActivity extends BaseActivity {

    private GridView gridSpec;
    private Intent mIntent;
    private CategoryModel.OP_SELECT opSelect;
    private String productId;
    private List<SpecificationConfigDTO> spes;


    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void initData() {
        mIntent = getIntent();
        productId = mIntent.getStringExtra("productId");
        opSelect = (CategoryModel.OP_SELECT) mIntent.getSerializableExtra("selectOp");
        gridSpec = this.findViewById(R.id.spec_grid);
        initSpec();
    }


    private void initSpec(){
        XinongHttpCommend.getInstance(mContext).getSpecByProductId(productId,
                new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                spes = JSON.parseArray(result,SpecificationConfigDTO.class);
                gridSpec.setAdapter(new CommonAdapter<SpecificationConfigDTO>(mContext,R.layout.item_border_text,spes) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder, int position, final SpecificationConfigDTO item) {
                        viewHolder.setTextForTextView(R.id.tv_show,item.getName());
                        viewHolder.setOnClickListener(R.id.tv_show, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent myIntent = new Intent();
                                myIntent.putExtra("id",item.getId());
                                myIntent.putExtra("spec",item.getName());
                                setResult(RESULT_OK,myIntent);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public String setToolBarTitle() {
        return "选择品类";
    }

}
