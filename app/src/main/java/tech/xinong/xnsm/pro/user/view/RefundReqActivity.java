package tech.xinong.xnsm.pro.user.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.OrderStatus;
import tech.xinong.xnsm.pro.user.model.RefundModel;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.BaseEditDialog;

@ContentView(R.layout.activity_refund_req)
public class RefundReqActivity extends BaseActivity {

    @ViewInject(R.id.lv_refund)
    private PullToRefreshListView lv_refund;
    @ViewInject(R.id.tv_order_no)
    private TextView tv_order_no;
    @ViewInject(R.id.ll_op)
    private LinearLayout ll_op;
    @ViewInject(R.id.tv_approve)
    private TextView tv_approve;
    @ViewInject(R.id.tv_refuse)
    private TextView tv_refuse;
    @ViewInject(R.id.tv_state)
    private TextView tv_state;
    private String orderId;
    private String orderNo;
    private List<RefundModel> refundModels;
    private CommonAdapter adapter;
    private OrderStatus status;
    private boolean isSell;

    @Override
    public void initData() {
        refundModels = new ArrayList<>();
        orderId = intent.getStringExtra("orderId");
        orderNo = intent.getStringExtra("orderNo");
        status = (OrderStatus) intent.getSerializableExtra("status");
        tv_state.setText(status.getDisplayName());
        isSell = intent.getBooleanExtra("isSell",false);
        if (status.getCode()==12 && isSell){
            ll_op.setVisibility(View.VISIBLE);
            tv_approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XinongHttpCommend.getInstance(mContext).approveRefund(
                            orderId, new AbsXnHttpCallback(mContext) {
                                @Override
                                public void onSuccess(String info, String result) {
                                    T.showShort(mContext,"已同意退款");
                                    refresh();
                                }
                            });
                }
            });
            tv_refuse.setOnClickListener(new View.OnClickListener() {//拒绝退款
                @Override
                public void onClick(View v) {
//                    RefuseDialog.getInstance().setParam(RefundReqActivity.this,orderId).setCallback(
//                            new RefuseDialog.RefuseCallback() {
//                                @Override
//                                public void callback() {
//                                    refresh();
//                                }
//                            }
//                    ).show(RefundReqActivity.this.getSupportFragmentManager(), "DialogRefuse");


                    XinongHttpCommend.getInstance(mContext).feerate(new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            double fees = Double.parseDouble(result);

                            BaseEditDialog.getInstance().
                                    setConfirmText("确认")
                                    .setTitle("拒绝退款")
                                    .setHintText("请输入拒绝退款理由")
                                    .setCancelText("取消")
                                    .setContentText("退款会产生"+fees*100+"%的手续费（由买家承担）")
                                    .setConfirmListener(new BaseEditDialog.OnEditClickListener() {
                                        @Override
                                        public void onClick(String text) {
                                            if (TextUtils.isEmpty(text)){
                                                T.showShort(mContext,"退款理由不能为空");
                                                return;
                                            }
                                            XinongHttpCommend.getInstance(mContext).refuseRefund(orderId,
                                                    text,
                                                    new AbsXnHttpCallback(mContext) {
                                                        @Override
                                                        public void onSuccess(String info, String result) {
                                                            T.showShort(mContext,"拒绝退款成功");
                                                            refresh();
                                                        }
                                                    });
                                        }
                                    }).show(getSupportFragmentManager(),"base");
                        }
                    });


                }
            });
        }else {
            ll_op.setVisibility(View.GONE);
//            if (!isSell&&status.getCode()<12){
//                ll_op.setVisibility(View.VISIBLE);
//                tv_refuse.setText("申请退款");
//                tv_refuse.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    XinongHttpCommend.getInstance(mContext).feerate(new AbsXnHttpCallback(mContext) {
//                        @Override
//                        public void onSuccess(String info, String result) {
//                            final double fees = Double.parseDouble(result);
//                            twoButtonDialog("退款", "退款会产生"+fees*100+"%的手续费（由买家承担）",
//                                    "确定退款", "再想想",
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            ReqPayDialog.getInstance().
//                                                    setParam((BaseActivity) mContext,orderId,fees).
//                                                    show(getSupportFragmentManager(), "DialogLogout");
//                                        }
//                                    },null);
//                        }
//                    });
//                    }
//                });
//                tv_approve.setVisibility(View.GONE);
//            }
        }
        tv_order_no.setText(orderNo);
        lv_refund.setMode(PullToRefreshBase.Mode.BOTH);
        setAdapter();
        XinongHttpCommend.getInstance(mContext).refundReq(orderId, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                RefundModel refundModel = JSONObject.parseObject(result,RefundModel.class);
                //refundModels = JSONObject.parseArray(result, RefundModel.class);
                refundModels.add(refundModel);
                adapter.refresh(refundModels);
            }
        });
    }

    private void refresh(){
        XinongHttpCommend.getInstance(mContext).refundReq(orderId, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                refundModels.clear();
                RefundModel refundModel = JSONObject.parseObject(result, RefundModel.class);
                refundModels.add(refundModel);
                adapter.refresh(refundModels);
                lv_refund.onRefreshComplete();
                ll_op.setVisibility(View.GONE);
            }
        });
    }


    private void setAdapter() {
        adapter = new CommonAdapter<RefundModel>(mContext,R.layout.item_refund,refundModels) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, RefundModel item) {
                viewHolder.setTextForTextView(R.id.tv_order_no,item.getOrderNo());
                viewHolder.setTextForTextView(R.id.tv_refund_no,item.getRefundNo());
                viewHolder.setTextForTextView(R.id.tv_refund_state,item.getStatus().getDisplayName());
                viewHolder.setTextForTextView(R.id.tv_refund_amount,item.getAmount().doubleValue() + "元");
                viewHolder.setTextForTextView(R.id.tv_refund_reason,item.getRefundReason());
                viewHolder.setTextForTextView(R.id.tv_refund_time,item.getApproveTime());
                viewHolder.setTextForTextView(R.id.tv_refund_approve_msg,item.getApproveMsg());
                if (item.getFees()!=null)
                viewHolder.setTextForTextView(R.id.tv_refund_fees,item.getFees().doubleValue()+"元");
                else
                 viewHolder.setTextForTextView(R.id.tv_refund_fees,0.0+"元");
            }
        };
        lv_refund.setAdapter(adapter);
        lv_refund.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                },1000);
            }

            @Override
            public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.onRefreshComplete();
                    }
                },1000);
            }
        });
    }
    private void setData(RefundModel refundModel){

    }

    @Override
    public String setToolBarTitle() {
        return "查看退款申请";
    }
}
