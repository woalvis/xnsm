package tech.xinong.xnsm.pro.publish.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.zhy.view.flowlayout.TagAdapter;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.sql.PriceFav;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.home.view.ProductQuotesActivity;
import tech.xinong.xnsm.pro.publish.model.adapter.CategoryAdapter;
import tech.xinong.xnsm.pro.publish.model.adapter.ProductAdapter;
import tech.xinong.xnsm.util.ArrayUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2017/10/25.
 */

@ContentView(R.layout.activity_publish_select)
public class PublishSelectActivity extends BaseActivity implements CategoryAdapter.OnItemClickListener{
    @ViewInject(R.id.rv_category)
    private RecyclerView rv_category;
    @ViewInject(R.id.rv_product)
    private RecyclerView rv_product;
    private List<CategoryModel> categories;
    private ProductAdapter.OnItemClickListener listener;
    private int categoryPosition;
    private ProductAdapter productAdapter;
    //操作模式，根据不同的参数实例化不同的点击事件
    private int op;
    public static final int PUBLISH = 1;
    public static final int FOLLOW = 2;
    public static final int SELL = 3;
    public static final int SEARCH = 4;
    public static final int QUOTES = 5;
    public int defaultPosition;
    private TagAdapter<ProductDTO> tagAdapter;
    private List<ProductDTO> productModels;
    private String[] tags;
    private String favsStr;
    private String favsNames;

    @Override
    public void initData() {
        Intent intent = getIntent();
        Connector.getDatabase();
        favsStr = mSharedPreferences.getString(XnsConstant.FAvID,"");
        favsNames = mSharedPreferences.getString(XnsConstant.FAVNAME,"");
        if (intent!=null){
            op = intent.getIntExtra("op",0);
            defaultPosition = intent.getIntExtra("defaultPosition",0);
//            if (intent.getStringArrayExtra("tags")!=null&&intent.getStringArrayExtra("tags").length>0){
////                tags = intent.getStringArrayExtra("tags");
//                tags = mSharedPreferences.getString(XnsConstant.FAVNAME,"").split(",");
//            }
            String tagsStr = mSharedPreferences.getString(XnsConstant.FAVNAME,"");

            if (!TextUtils.isEmpty(tagsStr)){
                tags = tagsStr.split(",");
            }
            categoryPosition = defaultPosition;
            switch (op){
                case PUBLISH:
                    listener = new PublishListener();
                    break;
                case FOLLOW:
                    listener = new FollowListener();
                    break;
                case SELL:
                    listener = new SellListener();
                    break;
                case SEARCH:
                    listener = new SearchListener();
                    break;
                case  QUOTES:
                    listener = new QuotesListener();
                    break;
            }
        }

        XinongHttpCommend.getInstance(mContext).getCategories(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                categories = JSONArray.parseArray(result, CategoryModel.class);
                CategoryAdapter adapter = new CategoryAdapter(categories);
                rv_category.setAdapter(adapter);
                rv_category.setLayoutManager(new LinearLayoutManager(
                        mContext,
                        LinearLayoutManager.VERTICAL,
                        false));
                adapter.setListener(PublishSelectActivity.this);
                adapter.setDefSelect(defaultPosition);
                productAdapter = new ProductAdapter(categories.get(defaultPosition).getProducts());
                rv_product.setAdapter(productAdapter);
                rv_product.setLayoutManager(new GridLayoutManager(
                        mContext,3,
                        LinearLayoutManager.VERTICAL,
                        false));
                productAdapter.setListener(listener);
                productModels = categories.get(defaultPosition).getProducts();
                //setProductAdapter();
            }
        });
    }

    @Override
    public String setToolBarTitle() {
        return getResources().getString(R.string.select_product);
    }


    public void setListener(ProductAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(CategoryAdapter.CategoryViewHolder item, int position) {
        categoryPosition = position;
        item.mParent.setDefSelect(position);
        productModels = categories.get(position).getProducts();
        //setProductAdapter();
        productAdapter.refresh(productModels);
    }

    public class PublishListener implements ProductAdapter.OnItemClickListener{
        @Override
        public void onItemClick(ProductAdapter.ProductViewHolder holder, final int position) {
            Intent intent = new Intent(mContext,PublishSellActivity.class);
            intent.putExtra("productId",
                    categories.get(categoryPosition).getProducts().get(position).getId());
            intent.putExtra("productName",categories.get(categoryPosition).getProducts().get(position).getName());
            mContext.startActivity(intent);
            finish();
        }
    }

    public class FollowListener implements ProductAdapter.OnItemClickListener{
        @Override
        public void onItemClick(ProductAdapter.ProductViewHolder item, final int position) {
            if (ArrayUtil.c(tags,categories.get(categoryPosition).getProducts().get(position).getName())) {
                T.showShort(mContext,"您已经添加过此货品的关注");
            } else {
                XinongHttpCommend.getInstance(mContext).addFavs(
                        categories.get(categoryPosition).getProducts().get(position).getId(),
                        new AbsXnHttpCallback(mContext) {
                            @Override
                            public void onSuccess(String info, String result) {
                                T.showShort(mContext, "添加成功");
                                editor.putString(XnsConstant.FAvID,favsStr+categories.get(categoryPosition).getProducts().get(position).getId()+",");
                                editor.putString(XnsConstant.FAVNAME,favsNames+categories.get(categoryPosition).getProducts().get(position).getName()+",");
                                editor.commit();
                                PublishSelectActivity.this.finish();
                            }
                        });
            }
        }
    }

    public class SellListener implements ProductAdapter.OnItemClickListener{
        @Override
        public void onItemClick(ProductAdapter.ProductViewHolder holder, int position) {
            Intent intent = new Intent(mContext,PublishBuyActivity.class);
            intent.putExtra("productId",
                    categories.get(categoryPosition).getProducts().get(position).getId());
            intent.putExtra("productName",categories.get(categoryPosition).getProducts().get(position).getName());
            mContext.startActivity(intent);
            finish();
        }
    }

    public class SearchListener implements ProductAdapter.OnItemClickListener{
        @Override
        public void onItemClick(ProductAdapter.ProductViewHolder holder, int position) {
            Intent intent = new Intent();
            intent.putExtra("result",categories.get(categoryPosition).getProducts().get(position).getName());
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    public class ProductListener implements ProductAdapter.OnItemClickListener{
        @Override
        public void onItemClick(ProductAdapter.ProductViewHolder holder, final int position) {

        }
    }


    public class QuotesListener implements ProductAdapter.OnItemClickListener{
        @Override
        public void onItemClick(ProductAdapter.ProductViewHolder holder, final int position) {
            Intent intent = new Intent(mContext,ProductQuotesActivity.class);

            ProductDTO productDTO = categories.get(categoryPosition).getProducts().get(position);
            intent.putExtra("product",productDTO);
            List<PriceFav> priceFav = DataSupport.where("productId = ?",productDTO.getId()).find(PriceFav.class);
            if (priceFav.size()>0){
                priceFav.get(0).setCount(priceFav.get(0).getCount()+1);
                priceFav.get(0).save();
            }else {
                PriceFav price = new PriceFav();
                price.setProductId(productDTO.getId());
                price.setName(productDTO.getName());
                price.setCount(1);
                price.save();
            }
            mContext.startActivity(intent);
            finish();
        }
    }



    public static void skip(Context context,int opStr,int defaultPosition){
        Intent intent = new Intent(context,PublishSelectActivity.class);
        intent.putExtra("op",opStr);
        intent.putExtra("defaultPosition",defaultPosition);
        context.startActivity(intent);
    }

    public static void skip(Context context,int opStr,int defaultPosition,String[] tags){
        Intent intent = new Intent(context,PublishSelectActivity.class);
        intent.putExtra("op",opStr);
        intent.putExtra("defaultPosition",defaultPosition);
        intent.putExtra("tags",tags);
        context.startActivity(intent);
    }
}
