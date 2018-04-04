package tech.xinong.xnsm.pro.buy.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.ListingDetailsDTO;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.buy.view.GoodsDetailActivity;
import tech.xinong.xnsm.util.ImageUtil;

/**
 * Created by xiao on 2017/11/23.
 */

public class GoodsOtherProviderFragment extends Fragment{
    private ListingDetailsDTO data;
    private TextView tv_goods_note;
    private BaseActivity mContext;
    private ListView lv;
    private static final String ARG_PARAM1 = "data";
    private CommonAdapter<SellerListingInfoDTO> adapter;
    private List<SellerListingInfoDTO> listings;

    public static GoodsOtherProviderFragment newInstance(ListingDetailsDTO param1) {
        GoodsOtherProviderFragment fragment = new GoodsOtherProviderFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (BaseActivity) getActivity();
        initData();
    }

    private void initData() {
        listings = new ArrayList<>();
        XinongHttpCommend.getInstance(mContext).sellerListingsBySellerId(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                PageInfo pageInfo = JSONObject.parseObject(result, PageInfo.class);
                listings = JSONObject.parseArray(pageInfo.getContent(), SellerListingInfoDTO.class);
                adapter = new CommonAdapter<SellerListingInfoDTO>(
                        mContext, R.layout.item_product_show, listings) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder, int position, final SellerListingInfoDTO item) {
                        viewHolder.setOnClickListener(R.id.product_show_layout, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                                intent.putExtra("id", item.getId());
                                mContext.startActivity(intent);
                                mContext.finish();
                            }
                        });
                        if (item.getRecommend()==2){
                            viewHolder.getView(R.id.tv_ad).setVisibility(View.VISIBLE);
                        }else if (item.getRecommend()==1){
                            viewHolder.getView(R.id.tv_ad).setVisibility(View.VISIBLE);
                            ((TextView)viewHolder.getView(R.id.tv_ad)).setText("推荐");
                        }
                        viewHolder.setTextForTextView(R.id.product_price, item.getUnitPrice() + "/"+item.getWeightUnit().getDisplayName());
                        viewHolder.setTextForTextView(R.id.product_address, item.getProvinceName() + "   " + item.getCityName() + "  " + item.getSellerName());
                        viewHolder.setTextForTextView(R.id.product_desc, item.getTitle());
                        final SimpleDraweeView defaultImage = (SimpleDraweeView) viewHolder.getView(R.id.product_iv_show);
                        final String imageUrl = ImageUtil.getImgUrl(item.getCoverImg());
                        if (!TextUtils.isEmpty(imageUrl)) {
                            if (defaultImage.getTag()==null){
                                defaultImage.setTag(imageUrl);
                                defaultImage.setImageURI(ImageUtil.getImgUrl(imageUrl));
                            }

                        }else {
                            if (defaultImage.getTag()==null){
                                XinongHttpCommend.getInstance(mContext).getProductImg(new AbsXnHttpCallback(mContext) {
                                    @Override
                                    public void onSuccess(String info, String result) {
                                        defaultImage.setTag(result);
                                        defaultImage.setImageURI(ImageUtil.getProductImg(result));
                                    }
                                },item.id);
                            }

                        }
                    }
                };
                lv.setAdapter(adapter);
            }
        },data.getSellerId(),0,20);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            data = (ListingDetailsDTO) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_other_provider,container,false);
        lv = view.findViewById(R.id.listing_lv);
        return view;
    }
}
