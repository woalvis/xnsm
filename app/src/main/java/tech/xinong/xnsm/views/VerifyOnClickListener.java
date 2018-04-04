package tech.xinong.xnsm.views;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import tech.xinong.xnsm.R;

/**
 * Created by xiao on 2018/2/5.
 */

public class VerifyOnClickListener implements View.OnClickListener {

    private CountDownTimer timer;
    private Context mContext;
    @Override
    public void onClick(View v) {

    }

    private void setTimer(final TextView tv){
        timer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv.setText(millisUntilFinished/1000+"s后重新发送");
                tv.setBackgroundResource(R.drawable.tv_grey_bg);
                tv.setTextColor(mContext.getResources().getColor(R.color.black_404040));
            }

            @Override
            public void onFinish() {
                tv.setBackgroundResource(R.drawable.button_shape_orange);
                tv.setText("重新发送验证码");
                tv.setTextColor(mContext.getResources().getColor(R.color.white));
                tv.setPadding(10,10,10,10);
                tv.setClickable(true);
            }
        };
    }
}
