package tech.xinong.xnsm.pro.user.model.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.buy.view.GoodsDetailActivity;
import tech.xinong.xnsm.pro.publish.view.PublishSellActivity;
import tech.xinong.xnsm.pro.user.model.PublishStates;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;

/**
 * Created by xiao on 2018/1/11.
 */

public class MyPublishSellAdapter extends CommonAdapter<SellerListingInfoDTO> {

    private BaseActivity mContext;
    private List<SellerListingInfoDTO> datas;

    /**
     * @param context         Context
     * @param itemLayoutResId 每一项(适用于listview、gridview等AbsListView子类)的布局资源id,例如R.layout.
     *                        my_listview_item.
     * @param dataSource      数据源
     */
    public MyPublishSellAdapter(BaseActivity context, int itemLayoutResId, List<SellerListingInfoDTO> dataSource) {
        super(context, itemLayoutResId, dataSource);
        mContext = context;
        datas = dataSource;
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, final SellerListingInfoDTO item) {
        viewHolder.setTextForTextView(R.id.product_title,item.getTitle());
        String[] titles = item.getTitle().split(" ");
        String productName = titles[0];
        TextView tvOp = (TextView) viewHolder.getView(R.id.tv_op);
        TextView tvDel = (TextView) viewHolder.getView(R.id.tv_delete);
        TextView tvUpdate = (TextView) viewHolder.getView(R.id.tv_update);
        String specName ="";
        if (titles.length>1){
            for (int i=1;i<titles.length;i++)
            specName+=titles[i];
        }
        viewHolder.setTextForTextView(R.id.tv_product_spec_config,specName);
        viewHolder.setTextForTextView(R.id.tv_title,item.getTitle());
        viewHolder.setTextForTextView(R.id.tv_state,item.getStatus().getName());
        final SimpleDraweeView coverImg = (SimpleDraweeView) viewHolder.getView(R.id.product_show);
        final String imageUrl = ImageUtil.getImgUrl(item.getCoverImg());
        if (!TextUtils.isEmpty(imageUrl)) {
            coverImg.setImageURI(ImageUtil.getImgUrl(imageUrl));
        }else {
            XinongHttpCommend.getInstance(mContext).getProductImg(new AbsXnHttpCallback(mContext) {
                @Override
                public void onSuccess(String info, String result) {
                    coverImg.setImageURI(ImageUtil.getProductImg(result));
                }
            },item.id);
        }
        viewHolder.setTextForTextView(R.id.tv_price,item.getUnitPrice().toString()+"元/"+item.getWeightUnit().getDisplayName());
        switch (item.getStatus()){
            case APPROVED:
                tvDel.setVisibility(View.GONE);
                tvOp.setVisibility(View.VISIBLE);
                viewHolder.setTextForTextView(R.id.tv_op,"下架");
                break;
            case OFF_SHELVE:
                tvDel.setVisibility(View.VISIBLE);
                tvOp.setVisibility(View.VISIBLE);
                viewHolder.setTextForTextView(R.id.tv_op,"上架");
                break;
            case PENDING:
                tvOp.setVisibility(View.GONE);
                tvDel.setVisibility(View.VISIBLE);
                break;
            case REJECTED:
                tvOp.setVisibility(View.GONE);
                tvDel.setVisibility(View.VISIBLE);
                break;
        }


        viewHolder.setOnClickListener(R.id.tv_op, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opStr = ((TextView)v).getText().toString().trim();
                if (opStr.equals("上架")){
                    XinongHttpCommend.getInstance(mContext).publishStateChange(new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext,"上架成功");
                            item.setStatus(PublishStates.APPROVED);
                            notifyDataSetChanged();
                        }
                    }, PublishStates.APPROVED,item.getId());
                }else if (opStr.equals("下架")){
                    XinongHttpCommend.getInstance(mContext).publishStateChange(new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext,"下架成功");
                            item.setStatus(PublishStates.OFF_SHELVE);
                            notifyDataSetChanged();
                        }
                    }, PublishStates.OFF_SHELVE,item.getId());
                }
            }
        });

        viewHolder.setOnClickListener(R.id.tv_update, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(mContext,PublishSellActivity.class);
                intent.putExtra("updateId",item.getId());
                mContext.startActivity(intent);
            }
        });

        viewHolder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XinongHttpCommend.getInstance(mContext).publishDeleteById(new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        T.showShort(mContext,"删除成功");
                        mData.remove(item);
                        notifyDataSetChanged();
                    }
                },item.getId());
            }
        });

        viewHolder.setOnClickListener(R.id.ll_show, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("id",item.getId());
                mContext.startActivity(intent);
            }
        });
    }
}
