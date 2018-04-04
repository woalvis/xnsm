package tech.xinong.xnsm.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tech.xinong.xnsm.R;


/**
 * @Author：Administrator
 * @Since on 2016/6/4 21:37
 * @Description：自定义View，继承RelativeLayout
 * 对子控件，显示和隐藏操作，事件监听
 */
public class LoadView extends RelativeLayout {
    public ProgressBar progBar;
    public LinearLayout downLoadErrMsgBox;
    public LinearLayout downLoadStatusPbBox;
    public TextView downLoadErrText;
    public TextView textMessage;
    public Button btnListLoadErr;

    //new一个视图对象，不在布局文件
    public LoadView(Context context) {
        super(context);
        //初始化隐藏
        setVisibility(View.GONE);
    }

    //xml布局需要的
    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化隐藏
        setVisibility(View.GONE);
    }

    //初始化子控件
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progBar = (ProgressBar)findViewById(R.id.downLoadLoading);
        textMessage = (TextView)findViewById(R.id.downLoad_tv);
        downLoadErrMsgBox = (LinearLayout)findViewById(R.id.downLoadErrMsgBox);
        downLoadStatusPbBox = (LinearLayout)findViewById(R.id.downLoadStatusPbBox);
        downLoadErrText = (TextView) findViewById(R.id.downLoadErrText);
        btnListLoadErr = (Button) findViewById(R.id.downLoadbtnListLoadErr);
    }


    /**
     * Title : 开始加载
     */
    public void startLoad() {
        setVisibility(View.VISIBLE);
        downLoadErrMsgBox.setVisibility(View.GONE);
        downLoadStatusPbBox.setVisibility(View.VISIBLE);
    }

    public  void setTextMessage(String message){
        if(!TextUtils.isEmpty(message)){
            textMessage.setText(message);
        }

    }
    public  void setTextMessage(int message){
        textMessage.setText(message);
    }


    /**
     *
     * Title : 停止加载
     */
    public void stopLoad() {
        setVisibility(View.GONE);
        downLoadStatusPbBox.setVisibility(View.GONE);
    }

    /**
     * 是否正在加载
     * @author :  ZL
     * @return
     */
    public boolean isLoading(){
        return downLoadStatusPbBox.getVisibility() == View.VISIBLE;
    }
    /**
     *
     * Title : 显示网络错误 视图
     */
    public void showError() {
        setVisibility(View.VISIBLE);
        downLoadErrMsgBox.setVisibility(View.VISIBLE);
        btnListLoadErr.setText("");
        btnListLoadErr.setBackgroundDrawable(null);
        downLoadStatusPbBox.setVisibility(View.GONE);
    }

    /**
     * Title :
     */
    public void showEmpty() {
        setClickable(false);
        downLoadErrMsgBox.setVisibility(View.VISIBLE);
        btnListLoadErr.setText("");
        btnListLoadErr.setBackgroundDrawable(null);
        downLoadStatusPbBox.setVisibility(View.GONE);
    }

    /**
     *
     * Title :   设置重新操作按钮事件监听器
     * @param onReloadClickListener
     */
    public void setOnReloadClickListener(OnClickListener onReloadClickListener) {
        btnListLoadErr.setOnClickListener(onReloadClickListener);
    }


}
