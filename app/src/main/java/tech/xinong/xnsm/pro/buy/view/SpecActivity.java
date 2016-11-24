package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.SpecModel;

public class SpecActivity extends BaseActivity {

    private GridView gridSpec;
    private Intent mIntent;
    private CategoryModel.OP_SELECT opSelect;

    @Override
    protected int bindView() {
        return R.layout.activity_spec;
    }

    @Override
    public void initWidget() {

    }

    @Override
    public void initData() {
        mIntent = getIntent();

        opSelect = (CategoryModel.OP_SELECT) mIntent.getSerializableExtra("selectOp");
        String result = mIntent.getStringExtra("result");
        List<SpecModel> spes = JSON.parseArray(result,SpecModel.class);
        gridSpec = (GridView) this.findViewById(R.id.spec_grid);
        gridSpec.setAdapter(new CommonAdapter<SpecModel>(this,android.R.layout.simple_list_item_1,spes) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final SpecModel item) {
                viewHolder.setTextForTextView(android.R.id.text1,item.getItem());
                viewHolder.setOnClickListener(android.R.id.text1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        switch (opSelect){
                            case FIND_GOODS:
                                Intent intent = new Intent(SpecActivity.this,SelectActivity.class);
                                intent.putExtra("spec",item);
                                startActivity(intent);
                                break;
                            case PUBLISH_BUY:
                                Toast.makeText(SpecActivity.this, "buy", Toast.LENGTH_SHORT).show();
                                break;
                            case PUBLISH_SELL:
                                Toast.makeText(SpecActivity.this, "sell", Toast.LENGTH_SHORT).show();
                                break;
                            default:break;
                        }


                    }
                });
            }
        });
    }


}
