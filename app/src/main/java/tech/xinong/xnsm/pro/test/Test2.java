package tech.xinong.xnsm.pro.test;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.buy.view.GoodsDetailActivity;
import tech.xinong.xnsm.pro.user.model.adapter.MyPagerAdapter;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2017/11/20.
 */
@ContentView(R.layout.test2)
public class Test2 extends BaseActivity {

    private List<String> tabDatas = new ArrayList<>(Arrays.asList("全部","审核中","采购中", "已停止", "被驳回"));
    private ListView recommendProductLv;
    private ListView recommendSellerLv;
    @ViewInject(R.id.my_order_vp_view)
    private ViewPager my_order_vp_view;
    @ViewInject(R.id.home_recommend_tabs)
    private TabLayout homeRecommendTabs;
    private CommonAdapter<SellerListingInfoDTO> recommendProductAdapter;
    private List<SellerListingInfoDTO> recommendProductList;
    private CommonAdapter<SellerListingInfoDTO> recommendSellerAdapter;
    private MyPagerAdapter mAdapter;
    private List<View> viewList;
    private List<SellerListingInfoDTO> initiatedListings;
    private List<SellerListingInfoDTO> approvedListings;
    private List<SellerListingInfoDTO> offShelveListings;
    private List<SellerListingInfoDTO> rejectedListings;
    private Map<String, List<SellerListingInfoDTO>> publishMap;
//    @ViewInject(R.id.sd)
//    private SimpleDraweeView sd;
    private String imgUrl;

    @Override
    public void initData() {
        publishMap = new HashMap<>();
        initiatedListings  = new ArrayList<>();
        approvedListings = new ArrayList<>();
        offShelveListings = new ArrayList<>();
        rejectedListings  = new ArrayList<>();
        recommendProductList = new ArrayList<>();

//        imgUrl = "http://img05.tooopen.com/images/20141231/sy_78252221416.jpg";
//        recommendProductList =  getL(0, 10);
//
//        homeRecommendTabs.setTabMode(TabLayout.MODE_FIXED);
//        homeRecommendTabs.setupWithViewPager(my_order_vp_view);
//
//        for (String title : tabDatas) {
//            homeRecommendTabs.addTab(homeRecommendTabs.newTab().setText(title));
//        }
//        viewList = new ArrayList<>();
//        initRecommendAdapter();
//        View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_lv,null,false);
//        View view1 = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_lv,null,false);
//        recommendProductLv = (ListView) view.findViewById(R.id.lv);
//        recommendProductLv.setAdapter(recommendProductAdapter);
//        recommendSellerLv = view1.findViewById(R.id.lv);
//        recommendSellerLv.setAdapter(recommendProductAdapter);
//        viewList.add(view);
//        viewList.add(view1);
//        mAdapter = new MyPagerAdapter(mContext, viewList, tabDatas);
//        my_order_vp_view.setAdapter(mAdapter);
//        homeRecommendTabs.setupWithViewPager(my_order_vp_view);//将TabLayout和ViewPager关联起来。
//        homeRecommendTabs.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
    }



    private List<SellerListingInfoDTO> getL(int i, int i1) {
        List<SellerListingInfoDTO> infos = new ArrayList<>();
        SellerListingInfoDTO s1 = new SellerListingInfoDTO();
        for (int j = 0; j < 10; j++) {
            s1.setCoverImg(imgUrl);
            s1.setSellerName("s1");
            s1.setAddress("address");
            s1.setProvinceName("province1");
            s1.setCityName("city1");
            infos.add(s1);
        }
       // sd.setImageURI(imgUrl);
        return infos;
    }

    /*初始化推荐商品的适配器*/
    private void initRecommendAdapter() {
        recommendProductAdapter = new CommonAdapter<SellerListingInfoDTO>(
                mContext, R.layout.item_product_show, recommendProductList) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final SellerListingInfoDTO item) {
                viewHolder.setTextForTextView(R.id.product_price, item.getUnitPrice() + "/斤");
                viewHolder.setTextForTextView(R.id.product_address, item.getProvinceName() + "   " + item.getCityName() + "  " + item.getSellerName());
                viewHolder.setTextForTextView(R.id.product_desc, item.getTitle());
                SimpleDraweeView defaultImage = (SimpleDraweeView) viewHolder.getView(R.id.product_iv_show);
                if (!TextUtils.isEmpty(item.getCoverImg())) {
                    //String imageUrl = String.format(HttpConstant.HOST+HttpConstant.URL_SHOW_IMAGE,item.getId(),item.getCoverImg());
                    String imageUrl = item.getCoverImg();
                    defaultImage.setImageURI(imageUrl);
                }
                viewHolder.setOnClickListener(R.id.item_grid_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                        intent.putExtra("id", item.getId());
//                                    T.showShort(mContext,  item.getProductName());
                        mContext.startActivity(intent);
                    }
                });
            }
        };
    }

}
