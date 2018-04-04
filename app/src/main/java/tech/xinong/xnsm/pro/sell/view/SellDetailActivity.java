package tech.xinong.xnsm.pro.sell.view;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.pro.sell.model.BuyerListing;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2017/12/14.
 */

@ContentView(R.layout.activity_sell_detail)
public class SellDetailActivity extends BaseActivity {
    @ViewInject(R.id.im_head)
    private SimpleDraweeView im_head;
    @ViewInject(R.id.tv_buyer_name)
    private TextView tv_buyer_name;
    @ViewInject(R.id.tv_buyer_address)
    private TextView tv_buyer_address;
    @ViewInject(R.id.goods_category)
    private TextView goods_category;
    @ViewInject(R.id.goods_num)
    private TextView goods_num;
    @ViewInject(R.id.goods_origin)
    private TextView goods_origin;
    @ViewInject(R.id.goods_time)
    private TextView goods_time;//要货时间
    @ViewInject(R.id.goods_expect_price)
    private TextView goods_expect_price;
    @ViewInject(R.id.goods_quality_requirement)
    private TextView goods_quality_requirement;//品质要求
    @ViewInject(R.id.confirm_offer)
    private TextView confirm_offer;
    @ViewInject(R.id.goods_free_shipping)
    private TextView goods_free_shipping;
    private SimpleDateFormat sdf;
    private BuyerListing buyerListing;

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void initData() {

        sdf = new SimpleDateFormat("yyyy年MM月dd日");
        showProgress();
        if (intent != null) {
            XinongHttpCommend.getInstance(mContext).buyerListingById(intent.getStringExtra("id"), new AbsXnHttpCallback(mContext) {
                @Override
                public void onSuccess(String info, String result) {
                    cancelProgress();
                    buyerListing = JSONObject.parseObject(result, BuyerListing.class);
                    tv_buyer_name.setText(buyerListing.getBuyer().getFullName());
                    String priceStr = buyerListing.getMinPrice() + "-" + buyerListing.getMaxPrice() + "元/" + buyerListing.getWeightUnit().getDisplayName();
                    String urlStr = buyerListing.getBuyer().getHeadImg();
                    String specConfigStr = "";
                    if (buyerListing.getSpecConfigs() == null || buyerListing.getSpecConfigs().size() == 0) {
                        specConfigStr = "不限";
                    } else {
                        for (SpecificationConfigDTO s : buyerListing.getSpecConfigs()) {
                            specConfigStr += s.getItem() + " ";
                        }
                    }
                    if (!TextUtils.isEmpty(urlStr)) {
                        im_head.setImageURI(ImageUtil.getImgUrl(urlStr));
                    }
                    tv_buyer_address.setText(buyerListing.getProvince() + buyerListing.getCity());
                    goods_category.setText(buyerListing.getProductSpec().getName());
                    goods_num.setText(buyerListing.getAmount() + buyerListing.getWeightUnit().getDisplayName());
                    goods_origin.setText(buyerListing.getOriginProvince() + buyerListing.getOriginCity());
                    goods_time.setText(sdf.format(buyerListing.getListingEnd()) + "之前");
                    goods_expect_price.setText(priceStr);
                    goods_quality_requirement.setText(specConfigStr);
                    String freeShippingStr = buyerListing.getFreeShipping()?"包邮":"不包邮";
                    goods_free_shipping.setText(freeShippingStr);
                }
            });
        }

        confirm_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XinongHttpCommend.getInstance(mContext).sellerListingMeSpec(new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        PageInfo pageInfo = JSONObject.parseObject(result,PageInfo.class);
                        List<SellerListingInfoDTO> listings;
                        listings = JSONObject.parseArray(pageInfo.getContent(),SellerListingInfoDTO.class);
                        if (listings.size()>0){
                            Intent intent = new Intent(mContext, SubmitPriceActivity.class);
                            intent.putExtra("buyerListing",buyerListing);
                            intent.putExtra("result",pageInfo.getContent());
                            mContext.startActivity(intent);
                        }else {
                            T.showShort(mContext,"您暂时没有发布相关商品！");
                        }
                    }
                },getCustmerId(),
                  buyerListing.getProductSpec().getId());
            }
        });

    }


    @Override
    public String setToolBarTitle() {
        return "需求详情";
    }
}
