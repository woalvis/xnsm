package tech.xinong.xnsm.pro.user.model.adapter;

import android.content.Context;

import java.util.List;

import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.sell.model.BuyerListing;

/**
 * Created by xiao on 2017/12/27.
 */

public class BuyerListingAdapter extends CommonAdapter<BuyerListing> {
    /**
     * @param context         Context
     * @param itemLayoutResId 每一项(适用于listview、gridview等AbsListView子类)的布局资源id,例如R.layout.
     *                        my_listview_item.
     * @param dataSource      数据源
     */
    public BuyerListingAdapter(Context context, int itemLayoutResId, List<BuyerListing> dataSource) {
        super(context, itemLayoutResId, dataSource);
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, BuyerListing item) {

    }
}
