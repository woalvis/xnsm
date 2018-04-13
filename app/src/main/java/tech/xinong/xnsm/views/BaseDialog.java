package tech.xinong.xnsm.views;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mylhyl.circledialog.BaseCircleDialog;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;

/**
 * Created by xiao on 2018/4/3.
 */

public class BaseDialog extends BaseCircleDialog {
    private TextView tv_title;

    private TextView bt_submit;
    private BaseActivity mContext;
    private String orderId;
    private TextView but_cancel;
    private TextView tv_content;

    private String titleText ="喜农市";
    private View.OnClickListener confirmListener;
    private String confirmText = "确认";
    private String cancelText = "取消";
    private View.OnClickListener cancelListener;
    private String contextText;

    public static BaseDialog getInstance() {
        BaseDialog dialogFragment = new BaseDialog();
        dialogFragment.setCanceledBack(false);
        dialogFragment.setCanceledOnTouchOutside(false);
        dialogFragment.setGravity(Gravity.CENTER);
        dialogFragment.setWidth(1f);
        return dialogFragment;
    }


    public BaseDialog setTitle(String title){
        titleText = title;
        return this;
    }

    public BaseDialog setConfirmListener(View.OnClickListener listener){
        confirmListener = listener;
        return this;
    }

    public BaseDialog setConfirmText(String text){
        confirmText = text;
        return this;
    }


    public BaseDialog setContentText(String text){
        contextText = text;
        return this;
    }


    public BaseDialog setCancelListener(View.OnClickListener listener){
        cancelListener = listener;
        return this;
    }


    public BaseDialog setCancelText(String text){
        cancelText = text;
        return this;
    }

    @Override
    public View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_base_layout, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        tv_title = view.findViewById(R.id.tv_title);
        bt_submit = view.findViewById(R.id.bt_submit);
        but_cancel = view.findViewById(R.id.but_cancel);
        tv_content = view.findViewById(R.id.tv_content);

        tv_title.setText(titleText);
        bt_submit.setText(confirmText);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmListener.onClick(v);
                dismiss();
            }
        });
        but_cancel.setText(cancelText);
        tv_content.setText(contextText);
        if (cancelListener!=null)
        but_cancel.setOnClickListener(cancelListener);
        else but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
