package tech.xinong.xnsm.pro.home.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.view.SelectPhotoTheWayActivity;
import tech.xinong.xnsm.pro.home.model.AuctionDetailModel;
import tech.xinong.xnsm.pro.home.model.AuctionState;
import tech.xinong.xnsm.pro.home.model.BidModel;
import tech.xinong.xnsm.util.FileUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.ColoredSnackbar;

@ContentView(R.layout.activity_auction_detail)
public class AuctionDetailActivity extends BaseActivity {
    @ViewInject(R.id.auction_detail_product_name) //拍卖的货品名称
    private TextView tvProductName;
    @ViewInject(R.id.auction_detail_current_price)//当前的价格
    private TextView tvCurrentPrice;
    @ViewInject(R.id.auction_detail_bid_amount)   //当前投标的次数
    private TextView tvBidAmount;
    @ViewInject(R.id.auction_detail_start_price)  //起拍价
    private TextView tvStartPrice;
    @ViewInject(R.id.auction_detail_deposit)      //保证金
    private TextView tvDeposit;
    @ViewInject(R.id.lv_auction_details)          //拍卖的具体的货品的列表
    private ListView lvAuctionDetails;
    @ViewInject(R.id.auction_detail_tv_state)     //拍卖的状态显示
    private TextView tvState;
    @ViewInject(R.id.auction_detail_tv_time)      //拍卖的时间文本展示（主要是针对状态的说明）
    private TextView tvTime;
    @ViewInject(R.id.auction_detail_tv_helper)    //
    private TextView tvHelper;
    @ViewInject(R.id.auction_detail_lv_bid_show)  //
    private ListView lvBidShow;
    @ViewInject(R.id.auction_detail_tv_bid)
    private TextView tvBid;
    @ViewInject(R.id.auction_detail_tv_operation)
    private TextView tvOperation;
    @ViewInject(R.id.auction_detail_mid_account_name)
    private TextView tvMidAccountName;
    @ViewInject(R.id.auction_detail_mid_account_bank)
    private TextView tvMidAccountBank;
    @ViewInject(R.id.auction_detail_mid_account_number)
    private TextView tvMidAccountNumber;
    @ViewInject(R.id.auction_detail_layout_confirm)
    private LinearLayout layoutConfirm;
    @ViewInject(R.id.auction_scroll)
    private ScrollView auction_scroll;
    @ViewInject(R.id.auction_detail_bid_step)
    private TextView tvStep;


    public static final int REQ_SELECT_PIC = 0x1001;

    private List<AuctionDetailModel> auctionDetailModelList;
    private AuctionDetailModel auctionDetailModel;
    private String auctionId;

    @Override
    public void initData() {
        auctionDetailModelList = new ArrayList<>();
        auctionId = getIntent().getStringExtra("auctionId");
        if (!TextUtils.isEmpty(auctionId)) {
            getAuctionDetailById(auctionId);
        }
    }

    private void getAuctionDetailById(String auctionId) {
        showProgress();
        XinongHttpCommend.getInstance(mContext).getAuctionDetailById(auctionId, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                cancelProgress();
                if (info.equals(HttpConstant.OK)) {
                    auctionDetailModel = JSONObject.parseObject(result, AuctionDetailModel.class);
                    tvProductName.setText(auctionDetailModel.getProductName());
                    tvStartPrice.setText(auctionDetailModel.getStartingPrice() + "元");
                    tvCurrentPrice.setText(auctionDetailModel.getCurrentPrice() + "元");
                    tvBidAmount.setText("已有" + auctionDetailModel.getBidNumber() + "次出价");
                    tvDeposit.setText(auctionDetailModel.getDeposit().toString() + "元");
                    tvStep.setText(auctionDetailModel.getStepPrice().toString());
                    if (auctionDetailModel.isHasDeposit()) {

                        tvHelper.setVisibility(View.GONE);
                        tvOperation.setVisibility(View.GONE);
                        tvBid.setVisibility(View.VISIBLE);
                    } else {//没有交过保证金
                        tvBid.setVisibility(View.GONE);
                        tvHelper.setVisibility(View.VISIBLE);
                        tvOperation.setVisibility(View.VISIBLE);
                        tvHelper.setText("(保证金金额￥" + auctionDetailModel.getDeposit() + ")");
                    }
                    setAuctionState(auctionDetailModel.getState(), tvState, tvTime, auctionDetailModel);
                    lvAuctionDetails.setAdapter(new CommonAdapter<AuctionDetailModel.AuctionDetail>(
                            mContext,
                            R.layout.item_auction_details,
                            auctionDetailModel.getAuctionDetails()) {
                        @Override
                        protected void fillItemData(CommonViewHolder viewHolder, int position, AuctionDetailModel.AuctionDetail item) {
                            viewHolder.setTextForTextView(R.id.item_auction_detail_state, auctionDetailModel.getState().getDescription());
                            viewHolder.setTextForTextView(R.id.item_auction_detail_origin, item.getOrigin());
                            viewHolder.setTextForTextView(R.id.item_auction_detail_product_name, item.getSpecDesc());
                            viewHolder.setTextForTextView(R.id.item_auction_detail_total_unit,
                                    item.getAmount() + item.getUnit().getDisplayName());
                        }
                    });
                    lvBidShow.setAdapter(new CommonAdapter<BidModel>(mContext,
                            R.layout.item_auction_detail_bid_show,
                            auctionDetailModel.getBids()) {
                        @Override
                        protected void fillItemData(CommonViewHolder viewHolder, int position, BidModel item) {
                            viewHolder.setTextForTextView(R.id.item_bid_time, item.getCreateTime());
                            viewHolder.setTextForTextView(R.id.item_bid_price, item.getPrice().toString() + "元");
                        }
                    });
                    /**
                     * 设置中间人支付信息
                     */
                    tvMidAccountBank.setText(auctionDetailModel.getMidAccountBank());
                    tvMidAccountName.setText(auctionDetailModel.getMidAccountName());
                    tvMidAccountNumber.setText(auctionDetailModel.getMidAccountNumber());

                }
            }
        });
    }


    /**
     * 控件点击的方法，注解里面的集合为控件统一进行OnClick事件注册
     *
     * @param view
     */
    @OnClick({R.id.auction_detail_layout_confirm, R.id.auction_detail_tv_bid,R.id.auction_detail_tv_bid})
    public void weigetClick(View view) {
        switch (view.getId()) {
            case R.id.auction_detail_layout_confirm:

                T.showShort(mContext, "报名");
                deposit();
                break;
            case R.id.auction_detail_tv_bid:
                bidNow();
                break;
        }

    }

    private void bidNow() {
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        final BigDecimal current = auctionDetailModel.getCurrentPrice();
        new AlertDialog.Builder(this).setTitle("当前价格为：" + auctionDetailModel.getCurrentPrice()+"元")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "搜索内容不能为空！" + input, Toast.LENGTH_LONG).show();
                        }
                        else {
                            long inputLong = Long.parseLong(input);
                            BigDecimal inputBigDecimal = new BigDecimal(input);
                            if (current.compareTo(inputBigDecimal)>0){
                                T.showShort(mContext,"投标价格不能小于当前价格");
                                return;
                            }else {
                                T.showShort(mContext,"投标价格为："+inputBigDecimal.toString());
                                showProgress();
                                XinongHttpCommend.getInstance(mContext).auctionBid(
                                        auctionDetailModel.getId(),
                                        inputBigDecimal.doubleValue(),
                                        new AbsXnHttpCallback(mContext) {
                                            @Override
                                            public void onSuccess(String info, String result) {
                                                cancelProgress();
                                                T.showShort(mContext,"投标成功");
                                                getAuctionDetailById(auctionId);
                                            }
                                        }
                                );

                            }

                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();


    }


    /**
     * 去提交保证金，让客户选择支付凭证并上传等待审核
     */
    private void deposit() {
        Intent intent = new Intent(this, SelectPhotoTheWayActivity.class);
        startActivityForResult(intent, REQ_SELECT_PIC);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_SELECT_PIC) {
                Bitmap bitmap = data.getParcelableExtra("bitmap");
                if (bitmap != null) {
                    T.showShort(mContext, "成功");
                    File file = FileUtil.bitmapToFile(mContext, bitmap, "uploadAuctionFile");
                    XinongHttpCommend.getInstance(mContext).auctionDeposits(
                            auctionId,
                            file,
                            new AbsXnHttpCallback(mContext) {
                                @Override
                                public void onSuccess(String info, String result) {
                                    snackbarShow("报名成功，可以进行投标了");
                                    getAuctionDetailById(auctionId);
                                }
                            });
                }
            }
        }
    }


    /**
     * 显示snackBar
     * @param msg 显示的信息
     */
    private void snackbarShow(String msg) {
        final Snackbar snackbar = Snackbar.make(auction_scroll,msg,5000);
        ColoredSnackbar.setSnackbarButtonTextColor(snackbar, Color.parseColor("#FFFFFF"));
        ColoredSnackbar.alert(snackbar).show();

        snackbar.setAction("朕知道了，爱卿退下", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
    }

    /**
     *
     * @param state
     * @param tvState
     * @param tvTime
     * @param item
     */
    private void setAuctionState(AuctionState state, TextView tvState, TextView tvTime, AuctionDetailModel item) {
        int leftDrawableResId = 0;
        String tvText = "";
        String tvTimeText = "";
        Drawable drawable = null;
        switch (state) {
            case INITIATED:
                leftDrawableResId = R.mipmap.auction_state_initiated;
                tvTimeText = "将在" + item.getBeginTime() + "开始";
                break;
            case BIDDING:
                leftDrawableResId = R.mipmap.auction_state_bindding;
                tvTimeText = "将在" + item.getEndTime() + "结束";
                break;
            case CLOSED:
                leftDrawableResId = R.mipmap.auction_state_close;
                tvTimeText = "已在" + item.getEndTime() + "结束";
                break;
            case TRANSFERED:
                leftDrawableResId = R.mipmap.auction_state_transfered;
                tvTimeText = "权属已经发生转移";
                break;
            case DISCARDED:
                leftDrawableResId = R.mipmap.auction_state_discarded;

                tvTimeText = "已在" + item.getEndTime() + "结束";

                layoutConfirm.setVisibility(View.GONE);
                break;
            default:
                throw new IllegalArgumentException("没有此拍卖状态");
        }
        tvText = state.getDescription();
        drawable = getResources().getDrawable(leftDrawableResId);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvState.setCompoundDrawables(drawable, null, null, null);
        tvState.setText(tvText);
        tvTime.setText(tvTimeText);
    }
}
