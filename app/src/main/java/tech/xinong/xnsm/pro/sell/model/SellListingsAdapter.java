package tech.xinong.xnsm.pro.sell.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.sell.view.SellDetailActivity;
import tech.xinong.xnsm.util.NumUtil;

/**
 * Created by xiao on 2017/12/14.
 */

public class SellListingsAdapter extends CommonAdapter<BuyerListingSum> {
    private  List<BuyerListingSum> mData;
    private Context mContext;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * @param context         Context
     * @param itemLayoutResId 每一项(适用于listview、gridview等AbsListView子类)的布局资源id,例如R.layout.
     *                        my_listview_item.
     * @param dataSource      数据源
     */
    public SellListingsAdapter(Context context, int itemLayoutResId, List<BuyerListingSum> dataSource) {
        super(context, itemLayoutResId, dataSource);
        mData = dataSource;
        mContext = context;
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, final BuyerListingSum item) {
        String addressStr = item.getProvince()+item.getCity();
        String amountStr = item.getAmount()+item.getWeightUnit().getDisplayName();
        String helpStr = "距截止日还有"+ NumUtil.days(sdf.format(item.getListingEnd()))+"天";
        String productName = item.getTitle().split(" ")[0];
        String[] titles = item.getTitle().split(" ");
        String specName = titles.length>1?item.getTitle().split(" ")[1]:item.getTitle().split(" ")[0];
        viewHolder.setTextForTextView(R.id.help,helpStr);
        viewHolder.setTextForTextView(R.id.tv_product_name,productName);
        viewHolder.setTextForTextView(R.id.tv_product_spec,specName);
        viewHolder.setTextForTextView(R.id.tv_product_address,"所在地："+addressStr);
        viewHolder.setTextForTextView(R.id.period,item.getPeriod());
        viewHolder.setTextForTextView(R.id.tv_amount,amountStr);

        TagFlowLayout fl_tag = (TagFlowLayout) viewHolder.getView(R.id.fl_tag);
        String[] tags = item.getTitle().split(" ");
        List<String> tagList = new ArrayList<>();
        for(int i=0;i< tags.length;i++){
            if (i>0)
                tagList.add(tags[i]);
        }
        fl_tag.setAdapter(new TagAdapter<String>(tagList) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView tvTag = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag,parent,false);
                tvTag.setText(o);
                return tvTag;
            }
        });

        viewHolder.setOnClickListener(R.id.tv_offer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SellDetailActivity.class);
                intent.putExtra("id",item.getId());
                mContext.startActivity(intent);
            }
        });
        viewHolder.setOnClickListener(R.id.product_show_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SellDetailActivity.class);
                intent.putExtra("id",item.getId());
                mContext.startActivity(intent);
            }
        });
    }
}
