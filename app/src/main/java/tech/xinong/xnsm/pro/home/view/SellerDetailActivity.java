package tech.xinong.xnsm.pro.home.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.model.OpMode;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.buy.model.adapter.ProductCommonAdapter;
import tech.xinong.xnsm.pro.home.model.RecommendedSellModel;
import tech.xinong.xnsm.util.ArrayUtil;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.MyListView;

/**
 * Created by xiao on 2018/1/4.
 */
@ContentView(R.layout.activity_seller_detail)
public class SellerDetailActivity extends BaseActivity {

    @ViewInject(R.id.head_img)
    private SimpleDraweeView head_img;
    @ViewInject(R.id.cover_img)
    private SimpleDraweeView cover_img;
    @ViewInject(R.id.tv_seller_name)
    private TextView tv_seller_name;
    @ViewInject(R.id.tv_ranking)
    private TextView tv_ranking;
    @ViewInject(R.id.tv_address)
    private TextView tv_address;
    @ViewInject(R.id.tv_personal)
    private TextView tv_personal;
    @ViewInject(R.id.tv_enterprise)
    private TextView tv_enterprise;
    @ViewInject(R.id.buy_lv_show)
    private MyListView buy_lv_show;
    private List<SellerListingInfoDTO> listings;
    private int page = 0;
    private int size = 10;
    private int totalPage;
    private List<SellerListingInfoDTO> tempPublishInfoModelList;
    private CommonAdapter<SellerListingInfoDTO> commonAdapter = null;
    private RecommendedSellModel seller;
    private String sellerId;

    @Override
    public void initData() {
        listings = new ArrayList<>();
        tempPublishInfoModelList = new ArrayList<>();
        sellerId = intent.getStringExtra("sellerId");

        if (!TextUtils.isEmpty(sellerId)){
            XinongHttpCommend.getInstance(mContext).sellerById(sellerId, new AbsXnHttpCallback(mContext) {
                @Override
                public void onSuccess(String info, String result) {
                    seller = JSONObject.parseObject(result,RecommendedSellModel.class);
                    String coverImgStr = seller.getCoverImg();
                    String headImg = seller.getHeadImg();
                    if (!TextUtils.isEmpty(coverImgStr)){
                        cover_img.setImageURI(ImageUtil.getImgUrl(coverImgStr));
                    }else {
                        cover_img.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(headImg)){
                        head_img.setImageURI(ImageUtil.getImgUrl(headImg));
                    }
                    tv_seller_name.setText(seller.getFullName());
                    tv_address.setText(seller.getAddress());
                    tv_ranking.setText(seller.getRanking()+"");
                    if (seller.getTags()!=null) {
                        String[] tags = seller.getTags().split(",");
                        if (ArrayUtil.c(tags,"PERSONAL")){
                            tv_personal.setVisibility(View.VISIBLE);
                        }
                        if (ArrayUtil.c(tags,"ENTERPRISE")){
                            tv_enterprise.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
        getListing(OpMode.INIT);
    }

    private void getListing(final OpMode opMode){
        XinongHttpCommend.getInstance(mContext).listingsBySellerId(sellerId, page, size, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                PageInfo pageInfo = JSONObject.parseObject(result,PageInfo.class);
                totalPage = pageInfo.getTotalPages();
                if (info.equals(HttpConstant.OK)) {
                    updateProductList(result, opMode);
                }
            }
        });
    }

    private void updateProductList(String result, OpMode opMode) {
        tempPublishInfoModelList = JSONArray.parseArray(
                JSON.parseObject(result).getString("content"),
                SellerListingInfoDTO.class);
        switch (opMode) {
            case PULLUP:
                this.listings.addAll(tempPublishInfoModelList);
                commonAdapter.notifyDataSetChanged();
                //buy_lv_show.onRefreshComplete();
                break;
            case INIT:
                listings = tempPublishInfoModelList;
                commonAdapter = new ProductCommonAdapter(mContext,
                        R.layout.item_product_show,//布局
                        listings, false);
                buy_lv_show.setAdapter(commonAdapter);
                break;
            case PULLDOWN:
                this.listings = tempPublishInfoModelList;
               // buy_lv_show.onRefreshComplete();
                break;
        }
        commonAdapter.refresh(listings);
    }


    @Override
    public String setToolBarTitle() {
        return "商家详情";
    }
}
