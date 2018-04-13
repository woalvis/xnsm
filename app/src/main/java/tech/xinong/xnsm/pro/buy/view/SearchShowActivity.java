package tech.xinong.xnsm.pro.buy.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
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
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.adapter.ProductShowAdapter;
import tech.xinong.xnsm.pro.base.model.Area;
import tech.xinong.xnsm.pro.base.model.OpMode;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.buy.model.SearchListModel;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.pro.publish.model.adapter.CategoryAdapter;
import tech.xinong.xnsm.pro.publish.model.adapter.ProductAdapter;
import tech.xinong.xnsm.pro.publish.model.adapter.ProvinceAdapter;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectSpecModel;
import tech.xinong.xnsm.pro.publish.model.adapter.SelectSpecificationAdapter;
import tech.xinong.xnsm.util.DeviceInfoUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2017/11/15.
 */
@ContentView(R.layout.activity_search_show)
public class SearchShowActivity extends BaseActivity implements View.OnClickListener, CategoryAdapter.OnItemClickListener {
    @ViewInject(R.id.lv_result)
    private PullToRefreshListView lv_result;
    @ViewInject(R.id.ll_name)
    private View llName;
    @ViewInject(R.id.ll_address)
    private View llAddress;
    @ViewInject(R.id.ll_spec)
    private View llSpec;
    @ViewInject(R.id.ll_spec_config)
    private LinearLayout ll_spec_config;
    @ViewInject(R.id.tv_product_name)
    private TextView tv_product_name;
    @ViewInject(R.id.tv_product_spec)
    private TextView tv_product_spec;
    @ViewInject(R.id.tv_product_spec_config)
    private TextView tv_product_spec_config;
    @ViewInject(R.id.tv_product_address)
    private TextView tv_product_address;
    private SearchListModel item;
    private EasyPopup mCirclePop;
    private List<CategoryModel> categories;
    private int defaultPosition;
    private ProductAdapter productAdapter;
    private List<ProductDTO> productModels;
    private List<Area> cityAreas;
    private CommonAdapter<Area> cityAdapter;
    private int provincePosition = 0;
    private List<SellerListingInfoDTO> infos;
    private ProductShowAdapter productShowAdapter;
    private EasyPopup namePopup;
    private EasyPopup addressPopup;
    private EasyPopup specPopup;
    private String productId;
    private String productSpecId = "";
    private String provinceId = "";
    private String cityId = "";
    private String specConfigIds = "";
    private ProductAdapter.OnItemClickListener listener;
    private Map<String, List<SpecificationConfigDTO>> specsMap;
    private List<SelectSpecModel> selectSpecModels;
    private SelectSpecificationAdapter specAdapter;
    private List<SpecificationConfigDTO> specsModelList;
    private ImageView iv_sort;
    private List<SellerListingInfoDTO> tempPublishInfoModelList;
    private int categoryPosition;


    @Override
    public void initData() {
        iv_sort = findViewById(R.id.iv_sort);
        iv_sort.setVisibility(View.GONE);
        infos = new ArrayList<>();
        llName.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        llSpec.setOnClickListener(this);
        ll_spec_config.setOnClickListener(this);
        String result = getIntent().getStringExtra("result");
        item = (SearchListModel) getIntent().getSerializableExtra("item");
        if (item!=null){
            tv_product_name.setText(item.getProduct().getName());
            productId = item.getProduct().getId();
            if (item.getSpec()!=null){
                tv_product_spec.setText(item.getSpec().getName());
            }
        }
        JSONObject jb =JSON.parseObject(result);
        infos = JSON.parseArray(jb.getString("content"),SellerListingInfoDTO.class);
        setAdapter(lv_result);
        refreshLv(infos);
    }

    private void refreshLv(List<SellerListingInfoDTO> infos){
        productShowAdapter = new ProductShowAdapter(mContext,infos);
        lv_result.setAdapter(productShowAdapter);
    }


    public static void skip(String result, BaseActivity context,SearchListModel item){
        Intent intent = new Intent(context,SearchShowActivity.class);
        intent.putExtra("result",result);
        intent.putExtra("item",item);
        context.startActivity(intent);
        context.finish();
    }

    private void setAdapter(final PullToRefreshListView lv){
        ILoadingLayout startLabels = lv
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

        lv.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        productShowAdapter.notifyDataSetChanged();
                        lv.onRefreshComplete();
                    }
                },1500);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        productShowAdapter.notifyDataSetChanged();
                        lv.onRefreshComplete();
                    }
                },1500);

            }
            }
        );
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
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
            case R.id.ll_name:
                if (getFavs()!=null&&getFavs().length>0){
                    tv_product_name.setText(getFavs()[0]);
                    productId = getConfigIds()[0];
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
                        adapter.setListener(SearchShowActivity.this);
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

                            specAdapter = new SelectSpecificationAdapter(mContext, selectSpecModels);
                            lvSpec.setAdapter(specAdapter);
                        }
                    });
                    TextView tvConfirm = mCirclePop.getView(R.id.tv_confirm);
                    TextView tvCancel = mCirclePop.getView(R.id.tv_cancel);
                    tvConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            specConfigIds = specAdapter.getIds();
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

        }
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

        XinongHttpCommend.getInstance(mContext).filterListings(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                updateProductList(result, OpMode.INIT);
            }
        }, paramMap);

        if (mCirclePop!=null)
            mCirclePop.dismiss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            if (requestCode==1001){
                tv_product_name.setText(data.getStringExtra("result"));
            }
        }
    }

    private void initPopup(){
        namePopup = new EasyPopup(this).setContentView(R.layout.layout_product_select)
                .setAnimationStyle(R.style.showPopAnim)
                .setAnchorView(llName)
                .setFocusAndOutsideEnable(false)
                .createPopup();
    }

    @Override
    public void onItemClick(CategoryAdapter.CategoryViewHolder item, int position) {
        categoryPosition = position;
        item.mParent.setDefSelect(position);
        productModels = categories.get(position).getProducts();
        productAdapter.refresh(productModels);
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


    private void updateProductList(String result, OpMode opMode) {
        tempPublishInfoModelList = JSONArray.parseArray(
                JSON.parseObject(result).getString("content"),
                SellerListingInfoDTO.class);
        switch (opMode) {
            case PULLUP:
                this.infos.addAll(tempPublishInfoModelList);
                productShowAdapter.notifyDataSetChanged();
                lv_result.onRefreshComplete();
                break;
            case INIT:
                infos = tempPublishInfoModelList;
                productShowAdapter = new ProductShowAdapter(mContext,
                        infos);
                lv_result.setAdapter(productShowAdapter);
                break;
            case PULLDOWN:
                this.infos = tempPublishInfoModelList;
                lv_result.onRefreshComplete();
                break;
        }

        productShowAdapter.refresh(infos);
    }

}
