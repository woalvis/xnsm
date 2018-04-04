package tech.xinong.xnsm.pro.sell.model;

import android.content.Context;

import java.util.List;

import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;

/**
 * Created by xiao on 2017/12/28.
 */

public class SellerShowAdapter extends CommonAdapter<BuyerListingSum> {
    /**
     * @param context         Context
     * @param itemLayoutResId 每一项(适用于listview、gridview等AbsListView子类)的布局资源id,例如R.layout.
     *                        my_listview_item.
     * @param dataSource      数据源
     */
    public SellerShowAdapter(Context context, int itemLayoutResId, List<BuyerListingSum> dataSource) {
        super(context, itemLayoutResId, dataSource);
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, BuyerListingSum item) {

    }
}
