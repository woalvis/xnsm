package tech.xinong.xnsm.views;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mylhyl.circledialog.BaseCircleDialog;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.user.view.MyOrdersActivity;
import tech.xinong.xnsm.util.T;

/**
 * Created by xiao on 2018/1/19.
 */

public class ReqPayDialog extends BaseCircleDialog implements View.OnClickListener {

    private TextView tv_title;
    private EditText et;
//    private EditText et_req_pay_explain;
    private TextView bt_submit;
    private BaseActivity mContext;
    private String orderId;
    private TextView but_cancel;
    private double fees;
    private TextView tv_content;

    public static ReqPayDialog getInstance() {
        ReqPayDialog dialogFragment = new ReqPayDialog();
        dialogFragment.setCanceledBack(false);
        dialogFragment.setCanceledOnTouchOutside(false);
        dialogFragment.setGravity(Gravity.CENTER);
        dialogFragment.setWidth(1f);
        return dialogFragment;
    }


    @Override
    public View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_edit_layout, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        et = view.findViewById(R.id.et);
        tv_title = view.findViewById(R.id.tv_title);
        bt_submit = view.findViewById(R.id.bt_submit);
        but_cancel = view.findViewById(R.id.but_cancel);
        tv_content = view.findViewById(R.id.tv_content);
        tv_content.setText("退款会产生"+fees*100+"%的手续费（由买家承担）");
        but_cancel.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_submit:
                    if (textViewIsEmpty(et)){
                        T.showShort(mContext,"退款原因不能为空");
                        return;
                    }
                XinongHttpCommend.getInstance(mContext).refundReq(orderId,
                        et.getText().toString().trim(),
                        new AbsXnHttpCallback(mContext) {
                            @Override
                            public void onSuccess(String info, String result) {
                                T.showShort(mContext,"申请成功");
                                dismiss();
                                mContext.skipActivity(MyOrdersActivity.class);
                                mContext.finish();
                            }
                        });
                break;
            case R.id.but_cancel:
                dismiss();
                break;
        }
    }

    private boolean textViewIsEmpty(TextView tv){
        if (tv==null){
            return true;
        }
        return TextUtils.isEmpty(tv.getText().toString().trim());
    }

    public ReqPayDialog setParam(BaseActivity context,String orderId,double fees){
        mContext = context;
        this.orderId = orderId;
        this.fees = fees;
        return this;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
