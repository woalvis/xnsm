package tech.xinong.xnsm.pro.home.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.home.adapter.CategoryAdapter;
import tech.xinong.xnsm.pro.home.presenter.FollowPresenter;
import tech.xinong.xnsm.pro.home.view.abs.FollowView;
import tech.xinong.xnsm.pro.publish.view.PublishSelectActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

import static com.lzy.okgo.OkGo.getContext;

/**
 * Created by xiao on 2017/10/16.
 * 关注页面
 */
@ContentView(R.layout.activity_follow)
public class FollowActivity extends BaseActivity<FollowPresenter> implements FollowView, CategoryAdapter.OnItemClickListener {
    @ViewInject(R.id.recyc_category)
    private RecyclerView recyc_category;
    @ViewInject(R.id.flowlayout_favs)
    private TagFlowLayout flowlayout_favs;
    private List<CategoryModel> categoryModels;
    private CategoryAdapter adapter;
    private List<ProductDTO> products;
    private LayoutInflater mInflater;
    @ViewInject(R.id.user_my_follow_layout)
    private LinearLayout user_my_follow_layout;
    private String[] favs;
//    @ViewInject(R.id.bt_edit_follow)
//    private Button bt_edit_follow;
    private boolean isConfig;
    private String configFavs;
    private String[] configFavsNames;


    private List<TextView> deleteTvList;

    @Override
    public FollowPresenter bindPresenter() {
        return new FollowPresenter(this);
    }


    @Override
    public void initData() {
        configFavs = mSharedPreferences.getString(XnsConstant.IDS,"");
        mInflater = LayoutInflater.from(this);
        categoryModels = new ArrayList<>();
        deleteTvList = new ArrayList<>();
        products = new ArrayList<>();

    }

    @Override
    public void onGetCategory(List<CategoryModel> categoryModels, List<ProductDTO> products) {

        this.products = products;
        adapter = new CategoryAdapter(categoryModels);
        adapter.setListener(this);
        recyc_category.setLayoutManager(new GridLayoutManager(getContext(),
                2,
                LinearLayoutManager.VERTICAL,
                false));
        recyc_category.setAdapter(adapter);
        if (products.size() > 0) {
            user_my_follow_layout.setVisibility(View.VISIBLE);
            favs = new String[products.size()];
            int i = 0;
            for (ProductDTO p : products) {
                favs[i] = p.getName();
                i++;
            }
            initUserFollowed(favs);
        } else {
            user_my_follow_layout.setVisibility(View.GONE);
        }
    }

    private void initUserFollowed(final String[] favs) {
        if (favs.length == 0) {
            T.showShort(mContext, "没有关注内容");
            user_my_follow_layout.setVisibility(View.GONE);
        } else {
            flowlayout_favs.setAdapter(new TagAdapter<String>(favs) {
                @Override
                public View getView(FlowLayout parent, final int position, String s) {
                    FrameLayout fl = (FrameLayout) mInflater.inflate(R.layout.item_favs, parent, false);
                    final TextView tv = fl.findViewById(R.id.tv);
                    TextView tv_delete = fl.findViewById(R.id.tv_delete);
                    if (isConfig){
                        tv_delete.setVisibility(View.GONE);
                    }else {
                        tv_delete.setVisibility(View.VISIBLE);
                        tv_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //T.showShort(mContext, products.get(position).getName() + "id:" + products.get(position).getId());
                                XinongHttpCommend.getInstance(mContext).deleteFav(products.get(position).getId(),
                                        new AbsXnHttpCallback(mContext) {
                                            @Override
                                            public void onSuccess(String info, String result) {
                                                T.showShort(mContext, "删除成功");
                                                products.remove(position);
                                                String[] favs = new String[products.size()];
                                                String[] favIds = new String[products.size()];
                                                int i = 0;
                                                for (ProductDTO p : products) {
                                                    favs[i] = p.getName();
                                                    favIds[i] = p.getId();
                                                    i++;
                                                }
                                                String ids = "";
                                                String names = "";
                                                for (String id : favIds) {
                                                    ids += id + ",";
                                                }

                                                for (String name : favs) {
                                                    names += name + ",";
                                                }
                                                editor.putString(XnsConstant.FAvID, ids);
                                                editor.putString(XnsConstant.FAVNAME,names);
                                                editor.commit();
                                                //T.showShort(mContext, mSharedPreferences.getString(XnsConstant.FAvID, ""));
                                                initUserFollowed(favs);
                                            }
                                        });
                            }
                        });
                    }
                    deleteTvList.add(tv_delete);
                    tv.setText(s);
                    return fl;
                }
            });
        }
    }

    @Override
    public void onGetUserFollowed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(configFavs)){
            configFavsNames = mSharedPreferences.getString(XnsConstant.FAVS,"").split(",");
            isConfig = true;
            initUserFollowed(configFavsNames);
        }else {
            getPresenter().getFavs();
            isConfig = false;
        }
    }

    @Override
    public void onItemClick(CategoryAdapter.CategoryViewHolder holder, int position) {
        PublishSelectActivity.skip(mContext, PublishSelectActivity.FOLLOW, position,favs);
    }



    @Override
    public String setToolBarTitle() {
        return "关注";
    }
}
