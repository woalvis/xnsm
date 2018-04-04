package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.user.model.VerificationReqModel;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_verification)
public class VerificationActivity extends BaseActivity {

    @ViewInject(R.id.tv_verify)
    private TextView tv_verify;
    @ViewInject(R.id.tv_status)
    private TextView tv_status;
    @ViewInject(R.id.im_verify)
    private ImageView im_verify;
    @ViewInject(R.id.tv_info)
    private TextView tv_info;

    @Override
    public void initData() {

    }


    @Override
    protected void onStart() {
        super.onStart();
        checkVerification();
    }

    private void checkVerification() {
        XinongHttpCommend.getInstance(mContext).verificationreqs(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(final String info, String result) {
                final VerificationReqModel verificationReq = JSONObject.parseObject(result,VerificationReqModel.class);
                tv_status.setText(verificationReq.getStatus().getName());
                tv_verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,PersonalVerifyActivity.class);
                        intent.putExtra("req",verificationReq);
                        startActivity(intent);
                    }
                });
                switch (verificationReq.getStatus()){
                    case UNCRETIFIED:
                        im_verify.setImageResource(R.mipmap.uncretified);
                        break;
                    case SUBMITTED:
                        im_verify.setImageResource(R.mipmap.uncretified);
                        break;
                    case VERIFIED:
                        im_verify.setImageResource(R.mipmap.verified);
                        tv_verify.setText("已认证");
                        tv_verify.setClickable(false);
                        break;
                    case REJECTED:
                        im_verify.setImageResource(R.mipmap.uncretified);
//                        tv_info.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    public String setToolBarTitle() {
        return "我的认证";
    }
}
