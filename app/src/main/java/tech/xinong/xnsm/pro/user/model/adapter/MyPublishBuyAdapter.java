package tech.xinong.xnsm.pro.user.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.publish.view.PublishBuyActivity;
import tech.xinong.xnsm.pro.sell.model.BuyerListingSum;
import tech.xinong.xnsm.pro.sell.model.BuyerStatus;
import tech.xinong.xnsm.pro.sell.view.SellDetailActivity;
import tech.xinong.xnsm.pro.user.view.CheckOfferActivity;
import tech.xinong.xnsm.util.NumUtil;
import tech.xinong.xnsm.util.T;

/**
 * Created by xiao on 2018/1/3.
 */

public class MyPublishBuyAdapter extends CommonAdapter<BuyerListingSum> {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private BaseActivity mContext;

    /**
     * @param context         Context
     * @param itemLayoutResId 每一项(适用于listview、gridview等AbsListView子类)的布局资源id,例如R.layout.
     *                        my_listview_item.
     * @param dataSource      数据源
     */


    public MyPublishBuyAdapter(Context context, int itemLayoutResId, List dataSource) {
        super(context, itemLayoutResId, dataSource);
        mContext = (BaseActivity)context;
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, final BuyerListingSum item) {
        String[] titles = item.getTitle().split(" ");
        String productName = titles[0];
        String specName = titles.length>1?titles[1]:titles[0];
       TextView tv_delete = (TextView) viewHolder.getView(R.id.tv_delete);
       TextView tv_check_quote = (TextView) viewHolder.getView(R.id.tv_check_quote);
       TextView tv_stop_buy = (TextView) viewHolder.getView(R.id.tv_stop_buy);
       TextView tv_update = (TextView) viewHolder.getView(R.id.tv_update);


        viewHolder.setTextForTextView(R.id.tv_state,item.getStatus().getDisplayName());
        viewHolder.setTextForTextView(R.id.tv_product_name,productName);
        viewHolder.setTextForTextView(R.id.tv_product_spec,specName);
        viewHolder.setTextForTextView(R.id.help,"距截止日还有"+ NumUtil.days(sdf.format(item.getListingEnd()))+"天" );
        viewHolder.setTextForTextView(R.id.tv_quote_num,item.getQuotationCount()+"");
        viewHolder.setTextForTextView(R.id.tv_amount,
                item.getMinPrice()+"-"+item.getMaxPrice()+"元/"+item.getWeightUnit().getDisplayName());

        switch (item.getStatus()){
            case REJECTED://修改 删除
            case PENDING://修改，删除   待审核
                tv_check_quote.setVisibility(View.GONE);
                tv_delete.setVisibility(View.VISIBLE);
                tv_stop_buy.setVisibility(View.GONE);
                tv_update.setVisibility(View.VISIBLE);
                break;
            case STOPPED://修改 ,删除
                tv_check_quote.setVisibility(View.GONE);
                tv_delete.setVisibility(View.VISIBLE);
                tv_stop_buy.setVisibility(View.GONE);
                tv_update.setVisibility(View.GONE);
                break;
            case APPROVED://停止采购 查看报价
                tv_check_quote.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.GONE);
                tv_stop_buy.setVisibility(View.VISIBLE);
                tv_update.setVisibility(View.GONE);
                break;
        }



        viewHolder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XinongHttpCommend.getInstance(mContext).delPublishBuy(item.getId(), new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        T.showShort(mContext,"删除成功");
                        mData.remove(item);
                        notifyDataSetChanged();
                    }
                });
            }
        });



        viewHolder.setOnClickListener(R.id.tv_check_quote, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XinongHttpCommend.getInstance(mContext).quotationsById(item.getId(), new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        PageInfo pageInfo = JSONObject.parseObject(result,PageInfo.class);
                        if (pageInfo.getTotalElements()>0){
                            Intent intent = new Intent(mContext, CheckOfferActivity.class);
                            intent.putExtra("pageInfo",pageInfo);
                            intent.putExtra("item",item);
                            mContext.startActivity(intent);
                        }else {
                            T.showShort(mContext,"还没有卖家给您进行报价");
                        }
                    }
                },0,10);
            }
        });
        viewHolder.setOnClickListener(R.id.tv_stop_buy, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XinongHttpCommend.getInstance(mContext).stopBuy(item.getId(), new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        T.showShort(mContext,"已停止");
                        item.setStatus(BuyerStatus.STOPPED);
                        notifyDataSetChanged();
                    }
                });
            }
        });

        viewHolder.setOnClickListener(R.id.product_show_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,SellDetailActivity.class);
                intent.putExtra("id",item.getId());
                mContext.startActivity(intent);
            }
        });


        viewHolder.setOnClickListener(R.id.tv_update, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PublishBuyActivity.class);
                intent.putExtra("updateId",item.getId());
                mContext.startActivity(intent);
            }
        });
    }
}
