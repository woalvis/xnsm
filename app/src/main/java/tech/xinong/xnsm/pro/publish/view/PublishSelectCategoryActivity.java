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
import tech.xinong.xnsm.util.ioc.ContentView;


@ContentView(R.layout.activity_publish_select_category)
public class PublishSelectCategoryActivity extends BaseActivity {

    private GridView publishGridCategory;
    private List<CategoryModel> categories;
    private CategoryModel.OP_SELECT opSelect;

    @Override
    public void initWidget() {
        publishGridCategory = findViewById(R.id.publish_grid_category);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        opSelect = (CategoryModel.OP_SELECT) intent.getSerializableExtra("info");
        showProgress();
        XinongHttpCommend.getInstance(mContext).getCategories(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                cancelProgress();
                categories = JSONArray.parseArray(result, CategoryModel.class);
                publishGridCategory.setAdapter(new CommonAdapter<CategoryModel>(mContext,R.layout.item_border_text,categories) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder, int position, final CategoryModel item) {
                        viewHolder.setTextForTextView(R.id.tv_show,item.getName());
                        viewHolder.setOnClickListener(R.id.tv_show, new View.OnClickListener() {
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
