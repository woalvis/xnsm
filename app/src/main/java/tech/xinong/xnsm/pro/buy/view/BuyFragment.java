package tech.xinong.xnsm.pro.buy.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;

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
        gridCategory = (GridView) contentView.findViewById(R.id.buy_grid_category);
        CommonAdapter<Category> adapter = new CommonAdapter<Category>(getActivity(),R.layout.item_category,getCategories()) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, final int position, Category item) {
                viewHolder.setImageForView(R.id.category_im,item.getImResId());
                viewHolder.setTextForTextView(R.id.category_tv,item.getName());
                viewHolder.setOnClickListener(R.id.category_im, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), categoryNames[position], Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        gridCategory.setAdapter(adapter);
        productShow = (ListView) contentView.findViewById(R.id.buy_lv_show);
    }


    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_buy;
    }


    private List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i=0;i<categoryResIds.length;i++){
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

}
