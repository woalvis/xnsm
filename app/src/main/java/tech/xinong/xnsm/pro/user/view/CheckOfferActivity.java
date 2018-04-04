package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.sell.model.BuyerListingSum;
import tech.xinong.xnsm.pro.user.model.QuoteModel;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2018/1/11.
 */
@ContentView(R.layout.activity_check_offer)
public class CheckOfferActivity extends BaseActivity {
    @ViewInject(R.id.lv)
    private PullToRefreshListView lv;
    private PageInfo pageInfoInit;
    private BuyerListingSum buyerListingSum;
    private CommonAdapter<QuoteModel> adapter;
    private List<QuoteModel> quotationModels;
    private void initAdapter(){
        adapter = new CommonAdapter<QuoteModel>(mContext,R.layout.item_check_offer,quotationModels) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final QuoteModel item) {
                viewHolder.setTextForTextView(R.id.tv_name,item.getOwnerName());
                viewHolder.setTextForTextView(R.id.tv_time,item.getCreateTime());
                viewHolder.setTextForTextView(R.id.tv_product_spec,buyerListingSum.getTitle());
                viewHolder.setTextForTextView(R.id.tv_price,item.getPrice()+"元/"+item.getWeightUnit().getDisplayName());
                viewHolder.setTextForTextView(R.id.tv_amount,item.getAmount()+item.getWeightUnit().getDisplayName());
                viewHolder.setTextForTextView(R.id.tv_address,item.getProvince()+item.getCity());
                viewHolder.setOnClickListener(R.id.layout_offer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,OfferDetailActivity.class);
                        intent.putExtra("quote",item);
                        intent.putExtra("buyerListingSum",buyerListingSum);
                        startActivity(intent);
                    }
                });
            }
        };

        lv.setAdapter(adapter);
    }

    @Override
    public String setToolBarTitle() {
        return "查看报价";
    }

    @Override
    public void initData() {
        quotationModels = new ArrayList<>();
        pageInfoInit = (PageInfo) getIntent().getSerializableExtra("pageInfo");
        buyerListingSum = (BuyerListingSum) getIntent().getSerializableExtra("item");
        if (pageInfoInit.isLast()){
            quotationModels = JSONObject.parseArray(pageInfoInit.getContent(),QuoteModel.class);
        }
        initAdapter();
    }
}
