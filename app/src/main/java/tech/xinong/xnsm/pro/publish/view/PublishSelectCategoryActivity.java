package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.view.ProductListActivity;

public class PublishSelectCategoryActivity extends BaseActivity {


    private GridView publishGridCategory;
    private List<CategoryModel> categories;
    private CategoryModel.OP_SELECT opSelect;

    @Override
    protected int bindView() {
        return R.layout.activity_publish_select_category;
    }

    @Override
    public void initWidget() {
        publishGridCategory = (GridView) findViewById(R.id.publish_grid_category);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        opSelect = (CategoryModel.OP_SELECT) intent.getSerializableExtra("info");

        XinongHttpCommend.getInstence(mContext).getCategories(new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                categories = JSONArray.parseArray(result, CategoryModel.class);
                publishGridCategory.setAdapter(new CommonAdapter<CategoryModel>(mContext,android.R.layout.simple_list_item_1,categories) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder, int position, final CategoryModel item) {
                        viewHolder.setTextForTextView(android.R.id.text1,item.getName());
                        viewHolder.setOnClickListener(android.R.id.text1, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, ProductListActivity.class);
                                intent.putExtra("selectOp", opSelect);
                                intent.putExtra("category", item);
                                mContext.startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }
}
