package tech.xinong.xnsm.views;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mylhyl.circledialog.BaseCircleDialog;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.T;

/**
 * Created by xiao on 2018/1/22.
 */

public class RefuseDialog extends BaseCircleDialog implements View.OnClickListener {
    private EditText et_req_pay_reason;
    private Button bt_submit;
    private BaseActivity mContext;
    private String orderId;
    private ImageButton but_cancel;
    private RefuseCallback callback;


    public interface RefuseCallback{
        void callback();
    }

    public static RefuseDialog getInstance() {
        RefuseDialog dialogFragment = new RefuseDialog();
        dialogFragment.setCanceledBack(false);
        dialogFragment.setCanceledOnTouchOutside(false);
        dialogFragment.setGravity(Gravity.BOTTOM);
        dialogFragment.setWidth(1f);
        return dialogFragment;
    }

    public RefuseDialog setCallback(RefuseCallback callback){
        this.callback = callback;
        return this;
    }


    @Override
    public View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_refuse, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        et_req_pay_reason = view.findViewById(R.id.et_req_pay_reason);
        bt_submit = view.findViewById(R.id.bt_submit);
        but_cancel = view.findViewById(R.id.but_cancel);
        but_cancel.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_submit:
                if (textViewIsEmpty(et_req_pay_reason)){
                    T.showShort(mContext,"拒绝退款原因不能为空");
                    return;
                }
                XinongHttpCommend.getInstance(mContext).refuseRefund(orderId,
                        et_req_pay_reason.getText().toString().trim(),
                        new AbsXnHttpCallback(mContext) {
                            @Override
                            public void onSuccess(String info, String result) {
                                T.showShort(mContext,"拒绝退款成功");
                                dismiss();
                                if (callback!=null)
                                    callback.callback();
//                                mContext.finish();
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

    public RefuseDialog setParam(BaseActivity context,String orderId){
        mContext = context;
        this.orderId = orderId;
        return this;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
