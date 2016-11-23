package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.ProductModel;

public class ProductListActivity extends BaseActivity {

    private GridView gvProducts;
    private List<ProductModel> products;


    @Override
    protected int bindView() {
        return R.layout.activity_product_list;
    }

    @Override
    public void initWidget() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        CategoryModel categoryModel = (CategoryModel) intent.getSerializableExtra("category");
        if (categoryModel!=null){
            products = categoryModel.getProducts();
            Toast.makeText(this, products.toString(), Toast.LENGTH_SHORT).show();
        }

        gvProducts = (GridView) this.findViewById(R.id.product_grid);
        gvProducts.setAdapter(new CommonAdapter<ProductModel>(this,android.R.layout.simple_list_item_1,products) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final ProductModel item) {
                viewHolder.setTextForTextView(android.R.id.text1,item.getName());
                viewHolder.setOnClickListener(android.R.id.text1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     XinongHttpCommend.getInstence(mContext).getProduct(new AbsXnHttpCallback() {
                            @Override
                            public void onSuccess(String info, String result) {
                                Intent intent = new Intent(ProductListActivity.this,SpecActivity.class);
                                intent.putExtra("result",result);
                                startActivity(intent);
                            }
                        } ,item.getName());
                    }
                });
            }
        });
    }
}
