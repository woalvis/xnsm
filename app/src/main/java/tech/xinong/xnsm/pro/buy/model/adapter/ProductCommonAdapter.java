package tech.xinong.xnsm.pro.buy.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.buy.view.GoodsDetailActivity;
import tech.xinong.xnsm.pro.publish.view.PublishSellActivity;
import tech.xinong.xnsm.pro.user.model.PublishStates;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;

/**
 * Created by xiao on 2017/12/11.
 */

public class ProductCommonAdapter extends CommonAdapter<SellerListingInfoDTO> {
    private Context mContext;
    private boolean isState;
    private List<SellerListingInfoDTO> dataSource;

    /**
     * @param context         Context
     * @param itemLayoutResId 每一项(适用于listview、gridview等AbsListView子类)的布局资源id,例如R.layout.
     *                        my_listview_item.
     * @param dataSource      数据源
     */
    public ProductCommonAdapter(Context context, int itemLayoutResId, List<SellerListingInfoDTO> dataSource, boolean isState) {
        super(context, itemLayoutResId, dataSource);
        mContext = context;
        this.isState = isState;
        this.dataSource = dataSource;
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, final SellerListingInfoDTO item) {
        final SimpleDraweeView defaultImage = (SimpleDraweeView) viewHolder.getView(R.id.product_iv_show);
        if (!TextUtils.isEmpty(item.getCoverImg())) {
            if (defaultImage.getTag()==null){
                String imageUrl = ImageUtil.getImgUrl(item.getCoverImg());
                defaultImage.setImageURI(ImageUtil.getImgUrl(imageUrl));
                defaultImage.setTag(imageUrl);
            }

        } else {
            if (defaultImage.getTag()==null){
                XinongHttpCommend.getInstance(mContext).getProductImg(new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        defaultImage.setImageURI(ImageUtil.getProductImg(result));
                        defaultImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        defaultImage.setTag(result);
                    }
                }, item.id);
            }
        }
        TagFlowLayout fl_tag = (TagFlowLayout) viewHolder.getView(R.id.fl_tag);
        String productDesc = item.getTitle();
        if (!TextUtils.isEmpty(item.getTitle())) {
            String[] tags = item.getTitle().split(" ");

            List<String> tagList = new ArrayList<>();
            for (int i = 0; i < tags.length; i++) {
                if (i > 0)
                    tagList.add(tags[i]);

                fl_tag.setAdapter(new TagAdapter<String>(tagList) {
                    @Override
                    public View getView(FlowLayout parent, int position, String o) {
                        TextView tvTag = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag, parent, false);
                        tvTag.setText(o);
                        return tvTag;
                    }
                });
            }

        }
        viewHolder.setTextForTextView(R.id.product_desc, productDesc);
        String productPrice = item.getUnitPrice() + "元/" + item.getWeightUnit().getDisplayName();
        viewHolder.setTextForTextView(R.id.product_price, productPrice);
        String productAddress = item.getAddress() + "  " + item.getSellerName();
        viewHolder.setTextForTextView(R.id.product_address, productAddress);
        viewHolder.setOnClickListener(R.id.product_show_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("id", item.getId());
                //T.showShort(mContext,  item.getProductName());
                mContext.startActivity(intent);
            }
        });

        if (isState) {
            PublishStates states = item.getStatus();
            LinearLayout llState = (LinearLayout) viewHolder.getView(R.id.ll_state);
            llState.setVisibility(View.VISIBLE);
            TextView tv_change = (TextView) viewHolder.getView(R.id.tv_change);
            TextView tv_update = (TextView) viewHolder.getView(R.id.tv_update);
            TextView tv_delete = (TextView) viewHolder.getView(R.id.tv_delete);
            TextView publish_state = (TextView) viewHolder.getView(R.id.publish_state);
            if (states == PublishStates.APPROVED) {
                tv_change.setText("下架");
                tv_change.setVisibility(View.VISIBLE);
                states = PublishStates.OFF_SHELVE;
                publish_state.setText("热销中");
                publish_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tag_bg_green));
            } else if (states == PublishStates.OFF_SHELVE) {
                tv_change.setText("上架");
                tv_change.setVisibility(View.VISIBLE);
                states = PublishStates.APPROVED;
                tv_delete.setVisibility(View.VISIBLE);
                publish_state.setText("已下架");
                publish_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tag_bg_green));
            } else if (states == PublishStates.PENDING) {
                tv_change.setText("审核中");
                tv_change.setVisibility(View.GONE);
                publish_state.setText("审核中");
                publish_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tag_bg_blue));
            } else if (states == PublishStates.REJECTED) {
                tv_change.setText("被拒绝");
                tv_change.setVisibility(View.GONE);
                publish_state.setText("审核中");
                publish_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.tag_bg_red));
            }
            final PublishStates finalStates = states;
            tv_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XinongHttpCommend.getInstance(mContext).publishStateChange(new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext, result);
                            item.setStatus(finalStates);
                            notifyDataSetChanged();
                        }
                    }, finalStates, item.getId());
                }
            });

            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PublishSellActivity.class);
                    intent.putExtra("updateId", item.getId());
                    mContext.startActivity(intent);
                }
            });

            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XinongHttpCommend.getInstance(mContext).publishDeleteById(new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext, result);
                            dataSource.remove(item);
                            notifyDataSetChanged();
                        }
                    }, item.getId());
                }
            });
        }

    }
}