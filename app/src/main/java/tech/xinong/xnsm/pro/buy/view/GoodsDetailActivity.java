package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.FragmentInfo;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.ListingDetailsDTO;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.pro.buy.model.VerificationReqType;
import tech.xinong.xnsm.pro.buy.model.adapter.PageFragmentAdapter;
import tech.xinong.xnsm.pro.buy.view.fragment.GoodsDetailFragment;
import tech.xinong.xnsm.pro.buy.view.fragment.GoodsOtherProviderFragment;
import tech.xinong.xnsm.pro.user.view.LoginActivity;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.GridViewForScrollView;
import tech.xinong.xnsm.views.InsideViewPager;

/**
 * 商品详情
 * */
@ContentView(R.layout.activity_goods_detail)
public class GoodsDetailActivity extends BaseActivity {
    @ViewInject(R.id.goods_cover_img)
    private SimpleDraweeView goods_cover_img;/*封面*/
    /*产品描述*/
    @ViewInject(R.id.product_specification)
    private TextView productDescription;
    /*总量*/
    @ViewInject(R.id.product_total_quantity)
    private TextView totalQuantity;
    @ViewInject(R.id.product_address)
    private TextView address;
    @ViewInject(R.id.product_unit_price)
    private TextView unitPrice;
    @ViewInject(R.id.product_unit)
    private TextView productUnit;
    @ViewInject(R.id.product_min_quantity)
    private TextView minQuantity;
    @ViewInject(R.id.product_grid_specification_configs)
    private GridViewForScrollView gridSpecificationConfigs;
    @ViewInject(R.id.buy_now)
    private TextView btBuyNow;
    @ViewInject(R.id.gv_provide_support)
    private GridViewForScrollView gv_provide_support;
    private Map<String, String> weightUnit;
    @ViewInject(R.id.seller_name)
    private TextView seller_name;
    @ViewInject(R.id.sell_address)
    private TextView seller_address;
    @ViewInject(R.id.seller_header)
    private SimpleDraweeView seller_header;
    @ViewInject(R.id.ll_verification)
    private LinearLayout ll_verification;
    @ViewInject(R.id.im_verification_company)
    private ImageView im_verification_company;
    @ViewInject(R.id.im_verification_personal)
    private ImageView im_verification_personal;
    @ViewInject(R.id.tv_verification_personal)
    private TextView tv_verification_personal;
    @ViewInject(R.id.tv_verification_company)
    private TextView tv_verification_company;
    @ViewInject(R.id.tv_free_shipping)
    private TextView tv_free_shipping;
    @ViewInject(R.id.tabs)
    private TabLayout tabs;
    @ViewInject(R.id.view_pager)
    private InsideViewPager viewPager;


    @Override
    public String setToolBarTitle() {
        return "商品详情";
    }

    @Override
    public void initData() {
        init();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        showProgress();
        XinongHttpCommend.getInstance(this).getProductListings(id, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                cancelProgress();
                ListingDetailsDTO publishSellInfoModel = JSON.parseObject(result, ListingDetailsDTO.class);
                initPage(publishSellInfoModel);
            }
        });
    }




    private void init() {
        weightUnit = new HashMap<>();
        weightUnit.put("JIN", "斤");
        weightUnit.put("KG", "公斤");
        weightUnit.put("TON", "吨");
    }


    private void initPage(final ListingDetailsDTO listingDetailsDTO) {
        if (!TextUtils.isEmpty(listingDetailsDTO.getCoverImg()))
        goods_cover_img.setImageURI(ImageUtil.getImgUrl(listingDetailsDTO.getCoverImg()));
        else {
            goods_cover_img.setVisibility(View.GONE);
        }

        //Glide.with(mContext).load(ImageUtil.getImgUrl(listingDetailsDTO.getCoverImg())).into(goods_cover_img);
        String unit = listingDetailsDTO.getWeightUnit().getDisplayName();
        productDescription.setText(listingDetailsDTO.getTitle());
        String totalQuantityStr = String.valueOf(listingDetailsDTO.getTotalQuantity());
        totalQuantityStr+=unit;
        totalQuantity.setText(totalQuantityStr+"现货");
        address.setText(listingDetailsDTO.getAddress());
        unitPrice.setText(String.valueOf(listingDetailsDTO.getUnitPrice()));
        tv_free_shipping.setText(listingDetailsDTO.getFreeShipping()?"包邮":"不包邮");
        productUnit.setText(unit);
        minQuantity.setText(listingDetailsDTO.getMinQuantity()+unit+"起卖");
        if (!TextUtils.isEmpty(listingDetailsDTO.getSellerHeadImg()))
        seller_header.setImageURI(ImageUtil.getImgUrl(listingDetailsDTO.getSellerHeadImg()));
        seller_name.setText(listingDetailsDTO.getSellerName());
        seller_address.setText(listingDetailsDTO.getAddress());
        checkVerification(listingDetailsDTO.getVerifiedTags());
        List<SpecificationConfigDTO> provideShows = new ArrayList<>();
        for (String name : listingDetailsDTO.getProvideSupport().split(",")){
            SpecificationConfigDTO specModelTemp = new SpecificationConfigDTO();
            specModelTemp.setName(name);
            provideShows.add(specModelTemp);
        }
        gv_provide_support.setAdapter(new CommonAdapter<SpecificationConfigDTO>(mContext,
                R.layout.item_provide_support,provideShows) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, SpecificationConfigDTO item) {
                viewHolder.setTextForTextView(R.id.tv_show,item.getName());
            }
        });

        List<SpecificationConfigDTO> specShows = new ArrayList<>(listingDetailsDTO.getSpecificationConfigs());

        gridSpecificationConfigs.setAdapter(new CommonAdapter<SpecificationConfigDTO>(mContext,
                R.layout.item_grid_specification_configs,
                specShows) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, SpecificationConfigDTO item) {

                    viewHolder.setTextForTextView(R.id.item_grid_spec_name,
                           item.getName());
                    viewHolder.setTextForTextView(R.id.item_grid_spec_value,item.getItem());

            }
        });


        List<FragmentInfo> infos = new ArrayList<>();
        FragmentInfo fragmentInfo1 = new FragmentInfo("图文详情", GoodsDetailFragment.newInstance(listingDetailsDTO));
        FragmentInfo fragmentInfo2 = new FragmentInfo("他还供应", GoodsOtherProviderFragment.newInstance(listingDetailsDTO));
        infos.add(fragmentInfo1);
        infos.add(fragmentInfo2);
        PageFragmentAdapter pageFragmentAdapter = new PageFragmentAdapter(getSupportFragmentManager(),infos);

        if(viewPager.getAdapter() == null) {
            viewPager.setAdapter(pageFragmentAdapter);
        }
        viewPager.setObjectForPosition(fragmentInfo1.getFragment().getView(),2);
        viewPager.setObjectForPosition(fragmentInfo2.getFragment().getView(),1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        tabs.setupWithViewPager(viewPager);
        btBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLogin()){
                    Intent intent = new Intent(mContext,BuyNowActivity.class);
                    intent.putExtra("item",listingDetailsDTO);
                    startActivity(intent);
                }else {
                    skipActivity(LoginActivity.class);
                }
            }
        });



    }

    private void checkVerification(String verifiedTags) {
        if (TextUtils.isEmpty(verifiedTags)){
            ll_verification.setVisibility(View.GONE);
        }else {
            ll_verification.setVisibility(View.VISIBLE);
            String s = VerificationReqType.ENTERPRISE.name();
            if (!verifiedTags.contains(VerificationReqType.ENTERPRISE.name())){
                im_verification_company.setVisibility(View.GONE);
                tv_verification_company.setVisibility(View.GONE);
            }
            else if (!verifiedTags.contains(VerificationReqType.PERSONAL.name())){
                im_verification_personal.setVisibility(View.GONE);
                tv_verification_personal.setVisibility(View.GONE);
            }
        }
    }


    public enum SpecificationCategory {
        VARIETY("品种"), SIZE("大小"), COLOR("颜色"), GRADE("等级"), MISC("其他");
        SpecificationCategory(String desc){
            this.desc = desc;
        }
        public String desc;
        public static String get(String name){

            switch (name){
                case "VARIETY":
                    return VARIETY.desc;
                case "SIZE":
                    return SIZE.desc;
                case "COLOR":
                    return COLOR.desc;
                case "GRADE":
                    return GRADE.desc;
                case "MISC":
                    return MISC.desc;
                default:
                    return "";
            }
        }
    }
}
