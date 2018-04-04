package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.publish.view.PublishSellActivity;
import tech.xinong.xnsm.util.ioc.ContentView;

import static android.R.attr.id;

/**
 * 产品列表
 */
@ContentView(R.layout.activity_product_list)
public class ProductListActivity extends BaseActivity {

    private GridView gvProducts;
    private List<ProductDTO> products;
    private Intent mIntent;


    @Override
    public void initWidget() {

    }

    @Override
    public void initData() {
        mIntent = getIntent();
        CategoryModel categoryModel = (CategoryModel) mIntent.getSerializableExtra("category");

        if (categoryModel!=null){
            products = categoryModel.getProducts();
        }

        gvProducts = (GridView) this.findViewById(R.id.product_grid);
        gvProducts.setAdapter(new CommonAdapter<ProductDTO>(this,R.layout.item_border_text,products) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final ProductDTO item) {
                viewHolder.setTextForTextView(R.id.tv_show,item.getName());
                viewHolder.setOnClickListener(R.id.tv_show, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     showProgress("正在加载");
                     XinongHttpCommend.getInstance(mContext).getProduct(new AbsXnHttpCallback(mContext) {
                            @Override
                            public void onSuccess(String info, String result) {
                                cancelProgress();
                                Intent intent = null;
                                if (result.equals("[]")){
                                    intent = new Intent(mContext,PublishSellActivity.class);
                                    intent.putExtra("spec",item.getName());
                                    intent.putExtra("productId",item.getName());
                                    intent.putExtra("id",id);
                                    startActivity(intent);
                                }else {
                                    intent = new Intent(ProductListActivity.this, SpecActivity.class);
                                    intent.putExtra("result", result);
                                    intent.putExtra("productId", item.getName());
                                    intent.putExtra("id", item.getId());
                                    intent.putExtra("selectOp", mIntent.getSerializableExtra("selectOp"));
                                    startActivity(intent);
                                }
                            }
                        } ,item.getName());
                    }
                });
            }
        });
    }
}
