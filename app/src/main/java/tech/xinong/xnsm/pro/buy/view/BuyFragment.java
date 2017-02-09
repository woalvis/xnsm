package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.base.view.navigation.impl.DefaultNavigation;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;
import tech.xinong.xnsm.pro.publish.model.PublishInfoModel;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.imageloder.ImageLoader;

import static android.app.Activity.RESULT_OK;

/**
 * 我要买页面
 * Created by xiao on 2016/11/7.
 */
public class BuyFragment extends BaseFragment<BuyPresenter, BaseView> {

    private BuyPresenter buyPresenter;
    private GridView gridCategory;//类别展示
    private ListView productShow;//被卖的产品的展示listview
    private int[] categoryResIds = {R.mipmap.category_food,//粮食作物
            R.mipmap.category_fruit,//水果
            R.mipmap.category_meat,//禽畜蛋肉
            R.mipmap.category_vegetables,//蔬菜
            R.mipmap.category_more};//更多
    private String[] categoryNames;
    private ImageView navigationBack;
    private EditText etSearch;

    private List<PublishInfoModel> publishInfoModelList;//发布的信息的列表<PublishInfoModel>

    private CommonAdapter<PublishInfoModel> commonAdapter = null;

    private List<CategoryModel> categories;

    public final static int REQ_SEARCH = 0x1001;


    /*测试用按钮*/
    private Button get_listings;


    /*加载布局*/
    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_buy;
    }


    //创建对象
    @Override
    public BuyPresenter bindPresenter() {
        buyPresenter = new BuyPresenter(getContext());

        return buyPresenter;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化类别字符串资源
        categoryNames = getContext().getResources().getStringArray(R.array.categories);
        publishInfoModelList = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initContentView(View contentView) {

        initNavigation(contentView);
        gridCategory = (GridView) contentView.findViewById(R.id.buy_grid_category);
        XinongHttpCommend.getInstance(mContext).getCategories(new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                categories = JSONArray.parseArray(result, CategoryModel.class);

                /*创建适配器*/
                CommonAdapter<Category> adapter = new CommonAdapter<Category>(getActivity(), R.layout.item_category, getCategories()) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder, final int position, Category item) {
                        viewHolder.setImageForView(R.id.category_im, item.getImResId());
                        viewHolder.setTextForTextView(R.id.category_tv, item.getName());
                        viewHolder.setOnClickListener(R.id.category_im, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                T.showShort(getContext(), categoryNames[position]);

                                if (categories != null || categories.size() != 0) {
                                    boolean flag = false;
                                    for (CategoryModel category : categories) {

                                        if (categoryNames[position].equals(category.getName())) {
                                            Intent intent = new Intent(getActivity(), ProductListActivity.class);
                                            intent.putExtra("selectOp", CategoryModel.OP_SELECT.FIND_GOODS);
                                            intent.putExtra("category", category);
                                            getActivity().startActivity(intent);
                                            flag = true;
                                        }
                                    }
                                    if (!flag) {
                                        T.showShort(mContext, "暂时还没有该品类，我们正在建设中。。。");
                                    }
                                }
                            }
                        });
                    }
                };
                /*设置适配器*/
                gridCategory.setAdapter(adapter);
            }
        });


        productShow = (ListView) contentView.findViewById(R.id.buy_lv_show);
        productShow.setAdapter(commonAdapter);
        getListings();//得到listings的点击方法,刚进入时候的方法

//        contentView.findViewById(R.id.bt_get_category).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                XinongHttpCommend.getInstance(mContext).getCategories(new AbsXnHttpCallback() {
//                    @Override
//                    public void onSuccess(String info, String result) {
//                        categories = JSONArray.parseArray(result, CategoryModel.class);
//                    }
//                });
//            }
//        });
//
//        get_listings = (Button) contentView.findViewById(R.id.get_listings);
//        get_listings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getListings();
//            }
//        });

    }


    /**
     * 得到listings的点击方法,刚进入时候的方法
     */
    public void getListings() {
        XinongHttpCommend.getInstance(mContext).getListings(new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                if (info.equals(HttpConstant.OK))
                updateProductList(result);
            }

        });
    }




    private List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < categoryResIds.length; i++) {
            Category category = new Category();
            category.setImResId(categoryResIds[i]);
            category.setName(categoryNames[i]);
            categories.add(category);
        }
        return categories;
    }

    private class Category {
        private int imResId;
        private String name;

        public int getImResId() {
            return imResId;
        }

        public void setImResId(int imResId) {
            this.imResId = imResId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * 初始化导航栏
     *
     * @param contentView
     */
    public void initNavigation(View contentView) {
        DefaultNavigation.Builder builder = new DefaultNavigation.Builder(getContext(), (ViewGroup) contentView);
        builder.setCenterText(R.string.search)
                .setCenterOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // skipActivity(SearchActivity.class);
                        Intent intent = new Intent(mContext,SearchActivity.class);

                        startActivityForResult(intent,REQ_SEARCH);
                    }
                }).create();

    }


    /**
     * 根据搜索的返回结果更新展示列表
     * @param requestCode 请求码
     * @param resultCode  响应码
     * @param data        返回的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode == REQ_SEARCH){
                String result  = data.getStringExtra("result");
                updateProductList(result);
            }
        }
    }


    /**
     *根据json字符串更新产品列表
     * @param result 返回的结果json串
     */
    public void updateProductList(String result){
        publishInfoModelList = JSONArray.parseArray(
                JSON.parseObject(result).getString("content"),
                PublishInfoModel.class);
        Log.d("xx", publishInfoModelList.toString());
        commonAdapter = new CommonAdapter<PublishInfoModel>(
                mContext,
                R.layout.item_product_show,//布局
                publishInfoModelList) {    //数据
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, PublishInfoModel item) {
                ImageView imageView = (ImageView) viewHolder.getView(R.id.product_iv_show);
                if (!TextUtils.isEmpty(item.getDefaultImage())){
                    ImageLoader.getInstance().displayImage(String.format(HttpConstant.HOST+HttpConstant.URL_SHOW_IMAGE,item.getId(),
                            item.getDefaultImage()),
                            imageView);
                }

                String productDesc = item.getSpecification();
                viewHolder.setTextForTextView(R.id.product_desc,productDesc);
                String productPrice = item.getUnitPrice()+"元/"+item.getQuantityUnit().getName();
                viewHolder.setTextForTextView(R.id.product_price,productPrice);
                String productAddress = item.getAddress()+"  "+item.getOwnerFullName();
                viewHolder.setTextForTextView(R.id.product_address,productAddress);
            }
        };

        productShow.setAdapter(commonAdapter);
    }
}
