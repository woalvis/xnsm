package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.ListingDocDTO;
import tech.xinong.xnsm.pro.sell.model.BuyerListingSum;
import tech.xinong.xnsm.pro.user.model.Customer;
import tech.xinong.xnsm.pro.user.model.QuoteModel;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.MyListView;

/**
 * Created by xiao on 2018/1/11.
 */
@ContentView(R.layout.activity_offer_detail)
public class OfferDetailActivity extends BaseActivity {
    @ViewInject(R.id.scroll)
    private ScrollView scroll;
    @ViewInject(R.id.im_head)
    private SimpleDraweeView im_head;
    @ViewInject(R.id.tv_seller_name)
    private TextView tv_seller_name;
    @ViewInject(R.id.tv_product_name)
    private TextView tv_product_name;
    @ViewInject(R.id.tv_address)
    private TextView tv_address;
    @ViewInject(R.id.tv_price)
    private TextView tv_price;
    @ViewInject(R.id.tv_note)
    private TextView tv_note;
    @ViewInject(R.id.ll_images)
    private MyListView ll_images;
    @ViewInject(R.id.buy_now)
    private TextView buy_now;

    private List<Integer> resId;
    private QuoteModel quoteModel;
    private BuyerListingSum buyerListingSum;
    private List<ListingDocDTO> docDTOList;

    @Override
    public void initData() {
        docDTOList = new ArrayList<>();
        quoteModel = (QuoteModel) getIntent().getSerializableExtra("quote");
        buyerListingSum = (BuyerListingSum) getIntent().getSerializableExtra("buyerListingSum");


        tv_product_name.setText(buyerListingSum.getTitle());
        tv_address.setText(quoteModel.getProvince()+quoteModel.getCity());
        tv_price.setText(quoteModel.getPrice()+"元/"+quoteModel.getWeightUnit().getDisplayName());
        tv_note.setText(quoteModel.getComment());

        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,BuyListingActivity.class);
                intent.putExtra("quote",quoteModel);
                intent.putExtra("buyListing",buyerListingSum);
                if (docDTOList.size()>0)
                intent.putExtra("coverImg",docDTOList.get(0).getDocName());
                startActivity(intent);
            }
        });

        XinongHttpCommend.getInstance(mContext).quotationOwner(quoteModel.getId(), new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                Customer customer = JSONObject.parseObject(result,Customer.class);
                tv_seller_name.setText(customer.getFullName());
                im_head.setImageURI(ImageUtil.getImgUrl(customer.getHeadImg()));
            }
        });

        XinongHttpCommend.getInstance(mContext).quotationDocs(quoteModel.getId(), new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                docDTOList = JSONObject.parseArray(result,ListingDocDTO.class);
                ll_images.setAdapter(new CommonAdapter<ListingDocDTO>(mContext,R.layout.item_img_show,docDTOList) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder, int position, ListingDocDTO item) {
                        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) viewHolder.getView(R.id.img);
                        simpleDraweeView.setImageURI(ImageUtil.getImgUrl(item.getDocName()));
                    }
                });
                scroll.smoothScrollTo(0, 0);
            }
        });
    }

    @Override
    public String setToolBarTitle() {
        return "报价详情";
    }
}
