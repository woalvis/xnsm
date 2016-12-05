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
import tech.xinong.xnsm.pro.publish.view.PublishSellActivity;
import tech.xinong.xnsm.util.ioc.ContentView;

@ContentView( R.layout.activity_spec)
public class SpecActivity extends BaseActivity {

    private GridView gridSpec;
    private Intent mIntent;
    private CategoryModel.OP_SELECT opSelect;
    private String productId;
    private String id;


    @Override
    public void initWidget() {

    }

    @Override
    public void initData() {
        mIntent = getIntent();
        productId = mIntent.getStringExtra("productId");
        id = mIntent.getStringExtra("id");
        opSelect = (CategoryModel.OP_SELECT) mIntent.getSerializableExtra("selectOp");
        String result = mIntent.getStringExtra("result");
        List<SpecModel> spes = JSON.parseArray(result,SpecModel.class);
        gridSpec = (GridView) this.findViewById(R.id.spec_grid);
        gridSpec.setAdapter(new CommonAdapter<SpecModel>(this,R.layout.item_border_text,spes) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final SpecModel item) {
                viewHolder.setTextForTextView(R.id.tv_show,item.getItem());
                viewHolder.setOnClickListener(R.id.tv_show, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = null;
                        switch (opSelect){
                            case FIND_GOODS:
                                intent = new Intent(SpecActivity.this,SelectActivity.class);
                                intent.putExtra("spec",item);
                                intent.putExtra("productId",productId);
                                intent.putExtra("id",id);
                                startActivity(intent);
                                break;
                            case PUBLISH_BUY:
                                Toast.makeText(SpecActivity.this, "buy", Toast.LENGTH_SHORT).show();
                                break;
                            case PUBLISH_SELL:
                                intent = new Intent(SpecActivity.this,PublishSellActivity.class);
                                intent.putExtra("spec",item);
                                intent.putExtra("productId",productId);
                                intent.putExtra("id",id);
                                startActivity(intent);

                                break;
                            default:break;
                        }


                    }
                });
            }
        });
    }


}
