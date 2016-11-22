package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.SpecModel;

public class SpecActivity extends BaseActivity {

    private GridView gridSpec;


    @Override
    protected int bindView() {
        return R.layout.activity_spec;
    }

    @Override
    public void initWidget() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        List<SpecModel> spes = JSON.parseArray(result,SpecModel.class);
        gridSpec = (GridView) this.findViewById(R.id.spec_grid);
        gridSpec.setAdapter(new CommonAdapter<SpecModel>(this,android.R.layout.simple_list_item_1,spes) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final SpecModel item) {
                viewHolder.setTextForTextView(android.R.id.text1,item.getItem());
                viewHolder.setOnClickListener(android.R.id.text1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SpecActivity.this,SelectActivity.class);
                        intent.putExtra("spec",item);
                        startActivity(intent);
                    }
                });
            }
        });
    }


}
