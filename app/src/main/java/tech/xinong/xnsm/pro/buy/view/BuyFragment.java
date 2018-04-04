package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.Util.ProperTies;
import tech.xinong.xnsm.pro.base.model.Area;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.pro.buy.model.adapter.ProductCommonAdapter;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;
import tech.xinong.xnsm.pro.publish.model.adapter.CategoryAdapter;
import tech.xinong.xnsm.pro.publish.model.adapter.ProductAdapter;
import tech.xinong.xnsm.pro.publish.model.adapter.ProvinceAdapter;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectSpecModel;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectSpecificationAdapter;
import tech.xinong.xnsm.pro.publish.view.PublishSelectActivity;
import tech.xinong.xnsm.pro.publish.view.PublishSellActivity;
import tech.xinong.xnsm.pro.user.view.LoginActivity;
import tech.xinong.xnsm.util.DeviceInfoUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;
import static android.app.Activity.RESULT_OK;
import static tech.xinong.xnsm.pro.buy.view.BuyFragment.OpMode.INIT;

/**
 * 我要买页面
 * Created by xiao on 2016/11/7.
 */
public class BuyFragment extends BaseFragment<BuyPresenter, BaseView> implements View.OnClickListener, CategoryAdapter.OnItemClickListener {

    private BuyPresenter buyPresenter;
    private PullToRefreshListView productShow;//被卖的产品的展示listview
    private List<SellerListingInfoDTO> publishInfoModelList;//发布的信息的列表<PublishInfoModel>
    private List<SellerListingInfoDTO> tempPublishInfoModelList;//从服务器拿到的数据临时做存储
    private CommonAdapter<SellerListingInfoDTO> commonAdapter = null;
    private List<CategoryModel> categories;
    public final static int REQ_SEARCH = 0x1001;
    private int currentPage = 0;
    private final static int PAGE_SIZE = 10;
    private LinearLayout ll_name;
    private LinearLayout ll_spec;
    private LinearLayout ll_spec_config;
    private LinearLayout ll_address;
    private TextView tv_product_name;
    private TextView tv_product_address;
    private TextView tv_product_spec;
    private TextView tv_product_spec_config;
    private EasyPopup mCirclePop;
    private int defaultPosition;
    private List<ProductDTO> productModels;
    private ProductAdapter.OnItemClickListener listener;
    private int categoryPosition;
    private ProductAdapter productAdapter;
    /*搜索筛选条件*/
    private String productId = "";
    private String productSpecId = "";
    private String provinceId = "";
    private String cityId = "";
    private String specConfigIds = "";
    private Map<String, List<SpecificationConfigDTO>> specsMap;
    private List<SelectSpecModel> selectSpecModels;
    private SelectSpecificationAdapter adapter;
    private List<SpecificationConfigDTO> specsModelList;
    private int provincePosition = 0;
    private List<Area> cityAreas;
    private CommonAdapter<Area> cityAdapter;
    private ImageView iv_sort;
    private TagFlowLayout fl_tag;
    private int totalPage = 0;
    private EasyPopup mPopup;
    private String sort;

    /*加载布局*/
    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_buy;
    }


    @Override
    public String setToolBarTitle() {
        return "我要买";
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.ll_name:
                if (mContext.getFavs()!=null&&mContext.getFavs().length>0){
                    tv_product_name.setText(mContext.getFavs()[0]);
                    productId = mContext.getConfigIds()[0];
                    return;
                }

                mCirclePop = getPopup(R.layout.popup_product_name, view);
                XinongHttpCommend.getInstance(mContext).getCategories(new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        categories = JSONArray.parseArray(result, CategoryModel.class);
                        CategoryAdapter adapter = new CategoryAdapter(categories);

                        RecyclerView lvCategory = mCirclePop.getView(R.id.rv_category);
                        RecyclerView lvProduct = mCirclePop.getView(R.id.rv_product);

                        lvCategory.setAdapter(adapter);
                        lvCategory.setLayoutManager(new LinearLayoutManager(
                                mContext,
                                LinearLayoutManager.VERTICAL,
                                false));
                        adapter.setListener(BuyFragment.this);
                        adapter.setDefSelect(defaultPosition);
                        productAdapter = new ProductAdapter(categories.get(defaultPosition).getProducts());
                        lvProduct.setAdapter(productAdapter);
                        lvProduct.setLayoutManager(new GridLayoutManager(
                                mContext, 3,
                                LinearLayoutManager.VERTICAL,
                                false));
                        productAdapter.setListener(listener);
                        productModels = categories.get(defaultPosition).getProducts();
                    }
                });
                mCirclePop.showAsDropDown(view, 0, 1);
                break;
            case R.id.ll_spec:
                mCirclePop = getPopup(R.layout.popup_product_spec, view);
                String productName = tv_product_name.getText().toString();
                if (!productName.equals("产品")) {
                    mCirclePop = getPopup(R.layout.popup_product_spec, view);
                    TextView tvAll = mCirclePop.getView(R.id.tv_all);
                    tvAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productSpecId = "";
                            tv_product_spec.setText("全部");
                            mCirclePop.dismiss();
                            filter();
                        }
                    });
                    XinongHttpCommend.getInstance(mContext).getSpecByProductId(productId, new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            final List<SpecificationConfigDTO> spes = JSON.parseArray(result, SpecificationConfigDTO.class);
                            SpecificationConfigDTO s = new SpecificationConfigDTO();
                            s.setName("");
                            if (spes.size()%3==1){
                                spes.add(s);
                                spes.add(s);
                            }else if(spes.size()%3==2){
                                spes.add(s);
                            }
                            GridView rv = mCirclePop.getView(R.id.rv_spec);
                            List<String> strings = new ArrayList<>();
                            for (SpecificationConfigDTO dto : spes) {
                                strings.add(dto.getName());
                            }
                            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, R.layout.item_text_model,
                                    R.id.tv, strings);
                            rv.setAdapter(adapter);
                            rv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (TextUtils.isEmpty(spes.get(i).getName())){
                                        return;
                                    }
                                    productSpecId = spes.get(i).getId();
                                    tv_product_spec.setText(spes.get(i).getName());
                                    filter();
                                }
                            });
                            mCirclePop.showAsDropDown(view, 0, 1);
                        }
                    });
                } else {
                    T.showShort(mContext, "请您先选择产品");
                }
                break;

            case R.id.ll_spec_config:
                String productName1 = tv_product_name.getText().toString();
                if (!productName1.equals("产品")) {
                    mCirclePop = getPopup(R.layout.popup_product_spec_config, view);
                    final ListView lvSpec = mCirclePop.getView(R.id.spec_lv);
                    selectSpecModels = new ArrayList<>();
                    specsMap = new LinkedHashMap<>();
                    XinongHttpCommend.getInstance(mContext).getSpecConfigsByProductId(productId, new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            cancelProgress();
                            specsModelList = JSON.parseArray(result, SpecificationConfigDTO.class);
                            List<SpecificationConfigDTO> specList;
                            for (SpecificationConfigDTO specsModel : specsModelList) {
                                if (specsMap.get(specsModel.getName()) == null) {
                                    specList = new ArrayList<>();
                                    specList.add(specsModel);
                                    specsMap.put(specsModel.getName(), specList);
                                } else {
                                    specsMap.get(specsModel.getName()).add(specsModel);
                                }

                            }
                            int i = 0;
                            Iterator iter = specsMap.entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                String title = (String) entry.getKey();
                                List<SpecificationConfigDTO> specs = (List<SpecificationConfigDTO>) entry.getValue();
                                SelectSpecModel selectSpecModel = new SelectSpecModel();
                                selectSpecModel.setId(specsModelList.get(i).getId());
                                selectSpecModel.setTitle(title);
                                selectSpecModel.setSpecs(specs);
                                selectSpecModels.add(selectSpecModel);
                                i++;
                            }

                            adapter = new SelectSpecificationAdapter(mContext, selectSpecModels);
                            lvSpec.setAdapter(adapter);
                        }
                    });
                    TextView tvConfirm = mCirclePop.getView(R.id.tv_confirm);
                    TextView tvCancel = mCirclePop.getView(R.id.tv_cancel);
                    tvConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            specConfigIds = adapter.getIds();
                            filter();
                        }
                    });
                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            specConfigIds = "";
                            filter();
                        }
                    });
                    mCirclePop.showAsDropDown(view, 0, 1);
                } else {
                    T.showShort(mContext, "请您先选择产品");
                }
                break;
            case R.id.ll_address:
                cityAreas = new ArrayList<>();
                mCirclePop = getPopup(R.layout.popup_address, view);
                final ListView lvProvince = mCirclePop.getView(R.id.lv_province);
                final ListView lvCity = mCirclePop.getView(R.id.lv_city);

                showProgress();
                XinongHttpCommend.getInstance(mContext).getAreas(new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        cancelProgress();
                        List<Area> areaAll = JSON.parseArray(result, Area.class);
                        areaAll.get(0).setName("全国");
                        final List<Area> areas = areaAll.get(0).getChildren();
                        Collections.sort(areas, new AreaSortByPinyin());
                        areas.add(0,areaAll.get(0));

                        cityAdapter = new CommonAdapter<Area>(mContext,
                                R.layout.item_text,
                                cityAreas) {
                            @Override
                            protected void fillItemData(CommonViewHolder viewHolder, int position, Area item) {
                                viewHolder.setTextForTextView(R.id.item_tv, item.getName());
                            }
                        };
                        lvCity.setAdapter(cityAdapter);
                        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                cityId = cityAreas.get(i).getId();
                                tv_product_address.setText(cityAreas.get(i).getName());
                                filter();
                            }
                        });
                        final ProvinceAdapter adapter = new ProvinceAdapter(mContext, areas);
                        lvProvince.setAdapter(adapter);
                        lvProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if(i == 0){
                                    provinceId = "";
                                    cityId = "";
                                    tv_product_address.setText("全国");
                                    filter();
                                }else{
                                    provincePosition = i;
                                    adapter.setDefItem(i);
                                    cityAreas = areas.get(i).getChildren();
                                    cityAdapter.refresh(cityAreas);
                                    provinceId = areas.get(i).getId();
                                }

                            }
                        });
                    }
                });
                mCirclePop.showAsDropDown(view, 0, 1);
                break;
            case R.id.iv_sort:
                mPopup = new EasyPopup(mContext)
                        .setContentView(R.layout.popup_set_sort)
                        .setAnimationStyle(R.style.PopAnim)
                        //是否允许点击PopupWindow之外的地方消失
                        .setFocusAndOutsideEnable(true)
                        .setBackgroundDimEnable(true)
                        .createPopup();
                mPopup.getView(R.id.tv_low_to_high).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sort = "unitPrice,ASC";
                        filter();
                        mPopup.dismiss();
                    }
                });
                mPopup.getView(R.id.tv_high_to_low).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sort = "unitPrice,DESC";
                        filter();
                        mPopup.dismiss();
                    }
                });
                mPopup.showAsDropDown(iv_sort);
                break;
        }
    }

    @Override
    public void onItemClick(CategoryAdapter.CategoryViewHolder item, int position) {
        categoryPosition = position;
        item.mParent.setDefSelect(position);
        productModels = categories.get(position).getProducts();
        productAdapter.refresh(productModels);
    }

    public enum OpMode {
        INIT, PULLDOWN, PULLUP
    }

    //创建对象
    @Override
    public BuyPresenter bindPresenter() {
        buyPresenter = new BuyPresenter(getContext());

        return buyPresenter;
    }

    @Override
    public void initData() {
        super.initData();
        initProductShow();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化类别字符串资源
        publishInfoModelList = new ArrayList<>();
        listener = new PublishListener();

    }

    /*初始化控件*/
    @Override
    protected void initContentView(View contentView) {
        setHasOptionsMenu(true);

        initNavigation(contentView);

        XinongHttpCommend.getInstance(mContext).getCategories(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                categories = JSONArray.parseArray(result, CategoryModel.class);
                ILoadingLayout startLabels = productShow
                        .getLoadingLayoutProxy();
                startLabels.setPullLabel("你可劲拉，拉...");// 刚下拉时，显示的提示
                startLabels.setRefreshingLabel("好嘞，正在刷新...");// 刷新时
                startLabels.setReleaseLabel("你敢放，我就敢刷新...");// 下来达到一定距离时，显示的提示

                String label = DateUtils.formatDateTime(
                        mContext.getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                productShow.getLoadingLayoutProxy().setLastUpdatedLabel(label);

            }
        });


        productShow = contentView.findViewById(R.id.buy_lv_show);
        productShow.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                currentPage = 0;
                filter();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.onRefreshComplete();
                    }
                },1000);
                //getListings(PAGE_SIZE, currentPage, OpMode.PULLDOWN);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多
                pullUpToRefresh(refreshView);
            }
        });

        iv_sort = contentView.findViewById(R.id.iv_sort);
        ll_name = contentView.findViewById(R.id.ll_name);
        ll_spec = contentView.findViewById(R.id.ll_spec);
        ll_spec_config = contentView.findViewById(R.id.ll_spec_config);
        ll_address = contentView.findViewById(R.id.ll_address);
        tv_product_name = contentView.findViewById(R.id.tv_product_name);
        tv_product_address = contentView.findViewById(R.id.tv_product_address);
        tv_product_spec = contentView.findViewById(R.id.tv_product_spec);
        tv_product_spec_config = contentView.findViewById(R.id.tv_product_spec_config);
        ll_name.setOnClickListener(this);
        ll_spec.setOnClickListener(this);
        ll_spec_config.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        iv_sort.setOnClickListener(this);
    }

    private void initProductShow() {
        if (mContext.getFavs()!=null&&mContext.getFavs().length>0){
            tv_product_name.setText(mContext.getFavs()[0]);
            productId = mContext.getConfigIds()[0];
        }
        getListings(PAGE_SIZE, currentPage, INIT);//得到listings的点击方法,刚进入时候的方法
    }


    /**
     * 上拉加载更多
     */
    private void pullUpToRefresh(final PullToRefreshBase<ListView> refreshView1) {

        if (currentPage+1 == totalPage){
            T.showShort(mContext,"已经到底了");
            refreshView1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshView1.onRefreshComplete();
                }
            }, 1000);
        }else {
            currentPage++;
            getListings(PAGE_SIZE, ++currentPage, OpMode.PULLUP);
        }

    }


    /**
     * 通过以下参数得到数据
     *
     * @param size 得到多少条数据
     * @param page 页码
     */
    public void getListings(int size, int page, final OpMode opMode) {
        XinongHttpCommend.getInstance(mContext).getListings(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                    PageInfo pageInfo = JSONObject.parseObject(result,PageInfo.class);
                    totalPage = pageInfo.getTotalPages();
                    if (info.equals(HttpConstant.OK)) {
                        updateProductList(result, opMode);
                    }
            }
        }, size, page);
    }


    /**
     * 根据搜索的返回结果更新展示列表
     *
     * @param requestCode 请求码
     * @param resultCode  响应码
     * @param data        返回的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_SEARCH) {
                String result = data.getStringExtra("result");
                updateProductList(result, INIT);
            }
        }
    }


    private void updateProductList(String result, OpMode opMode) {
        tempPublishInfoModelList = JSONArray.parseArray(
                JSON.parseObject(result).getString("content"),
                SellerListingInfoDTO.class);
        switch (opMode) {
            case PULLUP:
                this.publishInfoModelList.addAll(tempPublishInfoModelList);
                commonAdapter.notifyDataSetChanged();
                productShow.onRefreshComplete();
                break;
            case INIT:
                publishInfoModelList = tempPublishInfoModelList;
                commonAdapter = new ProductCommonAdapter(mContext,
                        R.layout.item_product_show,//布局
                        publishInfoModelList, false);
                productShow.setAdapter(commonAdapter);
                break;
            case PULLDOWN:
                this.publishInfoModelList = tempPublishInfoModelList;
                productShow.onRefreshComplete();
                break;
        }

        commonAdapter.refresh(publishInfoModelList);
    }


    /**
     * 发布供货或者采购信息（我要买卖）
     */
    private void publishInfo(boolean isBuy) {
        Properties proper = ProperTies.getProperties(mContext);
        String favs[] = proper.getProperty(XnsConstant.FAVS).split(",");
        if (favs.length>0&&!favs[0].equals("")){
            if (favs.length==1){
                Intent intent = new Intent(mContext,PublishSellActivity.class);
                String productId = mContext.getConfigIds()[0];
                String productName = mContext.getConfigNames()[0];
                intent.putExtra("productId",
                        productId);
                intent.putExtra("productName",productName);
                mContext.startActivity(intent);
            }
        }else {
            PublishSelectActivity.skip(mContext, PublishSelectActivity.SELL, 0);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_buy, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_publish:
                if (isLogin()){
                    publishInfo(true);
                }else {
                    twoButtonDialog("喜农市",
                            "您还没有登录账号，不能进行发布",
                            "去登陆",
                            "再看看",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    skipActivity(LoginActivity.class);
                                }
                            },
                            null);
                }
                break;
        }
        return true;
    }

    public class PublishListener implements ProductAdapter.OnItemClickListener {
        @Override
        public void onItemClick(ProductAdapter.ProductViewHolder holder, final int position) {
            String productName = holder.getTvText();
            productId = categories.get(categoryPosition).getProducts().get(position).getId();
            productSpecId = "";
            tv_product_spec.setText("品类");
            tv_product_name.setText(productName);
            mCirclePop.dismiss();
            filter();
        }
    }

    private void getListingsById(String productId) {
        XinongHttpCommend.getInstance(mContext).filterListings(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                updateProductList(result, INIT);
            }
        }, productId);
    }

    private EasyPopup getPopup(int layoutId, View view) {
        int height = (int) (DeviceInfoUtil.getScreenHeight() - view.getHeight() - view.getY());
        return new EasyPopup(mContext)
                .setContentView(layoutId)
                .setAnimationStyle(R.style.PopAnim)
                .setWidth(DeviceInfoUtil.getScreenWidth())
                .setHeight(height)
                //是否允许点击 PopupWindow 之外的地方消失
                .setFocusAndOutsideEnable(true)
                .createPopup();
    }


    private void filter() {

        Map<String, String> paramMap = new HashMap<>();
        if (!TextUtils.isEmpty(productId))
            paramMap.put("productIds", productId);
        if (!TextUtils.isEmpty(productSpecId))
            paramMap.put("productSpecId", productSpecId);
        if (!TextUtils.isEmpty(provinceId))
            paramMap.put("provinceId", provinceId);
        if (!TextUtils.isEmpty(cityId))
            paramMap.put("cityId", cityId);
        if (!TextUtils.isEmpty(specConfigIds)) {
            paramMap.put("specConfigIds", specConfigIds);
        }
        if (!TextUtils.isEmpty(sort)) {
            paramMap.put("sort", sort);
        }
        if (mCirclePop!=null)
        mCirclePop.dismiss();
        XinongHttpCommend.getInstance(mContext).filterListings(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                updateProductList(result, INIT);
            }
        }, paramMap);
    }

    class AreaSortByPinyin implements Comparator<Area> {

        @Override
        public int compare(Area area, Area t1) {
            return area.getPinYin().compareTo(t1.getPinYin());
        }
    }
}
