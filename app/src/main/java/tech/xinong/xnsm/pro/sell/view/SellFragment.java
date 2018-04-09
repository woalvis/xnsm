package tech.xinong.xnsm.pro.sell.view;

import android.content.Intent;
import android.os.Bundle;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
import tech.xinong.xnsm.pro.base.Util.ProperTies;
import tech.xinong.xnsm.pro.base.application.XnsApplication;
import tech.xinong.xnsm.pro.base.model.Area;
import tech.xinong.xnsm.pro.base.model.OpMode;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;
import tech.xinong.xnsm.pro.publish.model.adapter.CategoryAdapter;
import tech.xinong.xnsm.pro.publish.model.adapter.ProductAdapter;
import tech.xinong.xnsm.pro.publish.model.adapter.ProvinceAdapter;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectSpecModel;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectSpecificationAdapter;
import tech.xinong.xnsm.pro.publish.view.PublishBuyActivity;
import tech.xinong.xnsm.pro.publish.view.PublishSelectActivity;
import tech.xinong.xnsm.pro.sell.model.BuyerListingSum;
import tech.xinong.xnsm.pro.sell.model.SellListingsAdapter;
import tech.xinong.xnsm.pro.user.view.LoginActivity;
import tech.xinong.xnsm.util.DeviceInfoUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;


/**
 * 我要卖页面
 * Created by Administrator on 2016/8/10.
 */
public class SellFragment extends BaseFragment<BuyPresenter,BaseView> implements View.OnClickListener , CategoryAdapter.OnItemClickListener{

    private BuyPresenter buyPresenter;
    private PullToRefreshListView productLv;
    private int SIZE = 10;
    private int currentPage;
    private List<BuyerListingSum> lists;
    private SellListingsAdapter sellAdapter;
    private int totalPage = 0;
    private LinearLayout ll_name;
    private LinearLayout ll_spec;
    private LinearLayout ll_spec_config;
    private LinearLayout ll_address;
    private TextView tv_product_name;
    private TextView tv_product_address;
    private TextView tv_product_spec;
    private ImageView iv_sort;
    private EasyPopup mPopup;
    private String sort;
    private String productId = "";
    private String productSpecId = "";
    private String province = "";
    private String city = "";
    private String specConfigIds = "";
    private EasyPopup mCirclePop;
    private List<CategoryModel> categories;
    private List<ProductDTO> productModels;
    private ProductAdapter.OnItemClickListener listener;
    private int categoryPosition;
    private ProductAdapter productAdapter;
    private int defaultPosition;
    private List<SelectSpecModel> selectSpecModels;
    private SelectSpecificationAdapter spceAdapter;
    private List<SpecificationConfigDTO> specsModelList;
    private int provincePosition = 0;
    private List<Area> cityAreas;
    private CommonAdapter<Area> cityAdapter;
    private Map<String, List<SpecificationConfigDTO>> specsMap;
    private List<Area> areaAll;
    private  List<Area> areas;

    //创建对象
    @Override
    public BuyPresenter bindPresenter() {
       buyPresenter = new BuyPresenter(getContext());

        return buyPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lists = new ArrayList<>();
        cityAreas = new ArrayList<>();

    }


    @Override
    public void initData() {
        sellAdapter = new SellListingsAdapter(mContext,R.layout.item_buy_show,lists);
        productLv.setMode(PullToRefreshBase.Mode.BOTH);
        productLv.setAdapter(sellAdapter);
        productLv.setOnRefreshListener(new MyPullListener());
        iv_sort.setVisibility(View.GONE);
        currentPage = 0;
        getBuyerListings(currentPage,10,OpMode.INIT);
    }

    @Override
    protected void initContentView(View contentView) {
        setHasOptionsMenu(true);
        initNavigation(contentView);
        lists = new ArrayList<>();
        productLv = contentView.findViewById(R.id.lv_product);
        iv_sort = contentView.findViewById(R.id.iv_sort);
        ll_name = contentView.findViewById(R.id.ll_name);
        ll_spec = contentView.findViewById(R.id.ll_spec);
        ll_spec_config = contentView.findViewById(R.id.ll_spec_config);
        ll_address = contentView.findViewById(R.id.ll_address);
        tv_product_name = contentView.findViewById(R.id.tv_product_name);
        tv_product_address = contentView.findViewById(R.id.tv_product_address);
        tv_product_spec = contentView.findViewById(R.id.tv_product_spec);
        ll_name.setOnClickListener(this);
        ll_spec.setOnClickListener(this);
        ll_spec_config.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        iv_sort.setOnClickListener(this);

    }

    private void getBuyerListings(int page, int size, final OpMode mode){
        XinongHttpCommend.getInstance(mContext).buyerListings(page, size, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                PageInfo pageInfo = JSONObject.parseObject(result,PageInfo.class);
                totalPage = pageInfo.getTotalPages();
                if (productLv!=null)
                productLv.onRefreshComplete();
                updateListings(pageInfo.getContent(),mode);
            }
        });
    }

    private void updateListings(String result, OpMode mode) {

        List<BuyerListingSum> tempLists = JSONObject.parseArray(result,BuyerListingSum.class);
        switch (mode){
            case INIT:
                lists = tempLists;
                break;
            case PULLUP:
                lists.addAll(tempLists);
                break;
            case PULLDOWN:
                lists = tempLists;
                break;
        }
        sellAdapter.refresh(lists);
    }


    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_sell;
    }

    @Override
    public String setToolBarTitle() {

        return "我要卖";
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
                        adapter.setListener(SellFragment.this);
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

                            spceAdapter = new SelectSpecificationAdapter(mContext, selectSpecModels);
                            lvSpec.setAdapter(spceAdapter);
                        }
                    });
                    TextView tvConfirm = mCirclePop.getView(R.id.tv_confirm);
                    TextView tvCancel = mCirclePop.getView(R.id.tv_cancel);
                    tvConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            specConfigIds = spceAdapter.getIds();
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

                mCirclePop = getPopup(R.layout.popup_address, view);

                cityAreas = new ArrayList<>();
                if (areaAll==null||areaAll.size()==0){
                    showProgress();
                    XinongHttpCommend.getInstance(mContext).getAreas(new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            cancelProgress();
                            areaAll = JSON.parseArray(result, Area.class);
                            areaAll.get(0).setName("全国");
                            areas = areaAll.get(0).getChildren();
                            Collections.sort(areas, new SellFragment.AreaSortByPinyin());
                            areas.add(0,areaAll.get(0));
                            areas();
                        }
                    });
                }else {
                    areas();
                }

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

    private void areas(){
        final ListView lvProvince = mCirclePop.getView(R.id.lv_province);
        final ListView lvCity = mCirclePop.getView(R.id.lv_city);

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
                city = cityAreas.get(i).getName();
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
                    province = "";
                    city = "";
                    tv_product_address.setText("全国");
                    filter();
                }else{
                    provincePosition = i;
                    adapter.setDefItem(i);
                    cityAreas = areas.get(i).getChildren();
                    cityAdapter.refresh(cityAreas);
                    province = areas.get(i).getName();
                }

            }
        });
    }

    @Override
    public void onItemClick(CategoryAdapter.CategoryViewHolder item, int position) {
        categoryPosition = position;
        item.mParent.setDefSelect(position);
        productModels = categories.get(position).getProducts();
        productAdapter.refresh(productModels);
    }


    class MyPullListener implements PullToRefreshBase.OnRefreshListener2<ListView>{

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            String label = DateUtils.formatDateTime(
                    XnsApplication.getAppContext(),
                    System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);

            // Update the LastUpdatedLabel
            refreshView.getLoadingLayoutProxy()
                    .setLastUpdatedLabel(label);
            getBuyerListings(0,10,OpMode.PULLDOWN);
        }

        @Override
        public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView) {
            if (currentPage+1 == totalPage){
                T.showShort(mContext,"已经到底了");
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.onRefreshComplete();
                    }
                }, 1000);
            }else {
                currentPage++;
                getBuyerListings(currentPage, SIZE, OpMode.PULLUP);
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_buy,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_publish:
                if (isLogin()){
                    Properties proper = ProperTies.getProperties(mContext);
                    String favs[] = proper.getProperty(XnsConstant.FAVS).split(",");
                    if (favs.length>0&&!favs[0].equals("")){
                        if (favs.length==1){
                            Intent intent = new Intent(mContext,PublishBuyActivity.class);
                            String productId = mContext.getConfigIds()[0];
                            String productName = mContext.getConfigNames()[0];
                            intent.putExtra("productId",
                                    productId);
                            intent.putExtra("productName",productName);
                            mContext.startActivity(intent);
                        }
                    }else {
                        PublishSelectActivity.skip(getActivity(),PublishSelectActivity.PUBLISH,0);
                    }
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



    private void filter() {

        Map<String, String> paramMap = new HashMap<>();
        if (!TextUtils.isEmpty(productId))
            paramMap.put("productIds", productId);
        if (!TextUtils.isEmpty(productSpecId))
            paramMap.put("productSpecId", productSpecId);
        if (!TextUtils.isEmpty(province))
            paramMap.put("province", province);
        if (!TextUtils.isEmpty(city))
            paramMap.put("city", city);
        if (!TextUtils.isEmpty(specConfigIds)) {
            paramMap.put("specConfigIds", specConfigIds);
        }
        if (!TextUtils.isEmpty(sort)) {
            paramMap.put("sort", sort);
        }
        if (mCirclePop!=null)
            mCirclePop.dismiss();
        XinongHttpCommend.getInstance(mContext).filterSellListings(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                PageInfo pageInfo = JSONObject.parseObject(result,PageInfo.class);
                totalPage = pageInfo.getTotalPages();
                updateListings(pageInfo.getContent(), OpMode.INIT);
            }
        }, paramMap);
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


    class AreaSortByPinyin implements Comparator<Area> {

        @Override
        public int compare(Area area, Area t1) {
            return area.getPinYin().compareTo(t1.getPinYin());
        }
    }
}
