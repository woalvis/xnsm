package tech.xinong.xnsm.pro.publish.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.CustomDatePicker;

import static tech.xinong.xnsm.R.id.select_term_time_confirm;

@ContentView(R.layout.activity_select_term_time)
public class SelectTermTimeActivity extends BaseActivity {
    @ViewInject(R.id.rl_spot)
    private RelativeLayout rl_spot;
    @ViewInject(R.id.rl_presell)
    private RelativeLayout rl_presell;
    @ViewInject(R.id.tv_spot)
    private TextView tv_spot;
    @ViewInject(R.id.tv_spot_helper)
    private TextView tv_spot_helper;
    @ViewInject(R.id.tv_presell)
    private TextView tv_presell;
    @ViewInject(R.id.tv_presell_helper)
    private TextView tv_presell_helper;
    @ViewInject(R.id.im_spot_selector)
    private ImageView im_spot_selector;
    @ViewInject(R.id.im_presell_selector)
    private ImageView im_presell_selector;

    /*选择完毕后，将结果返回给发布的Activity按钮*/
    @ViewInject(select_term_time_confirm)
    private TextView selectTermTimeConfirm;
    @ViewInject(R.id.ll_spot)
    private LinearLayout ll_spot;
    @ViewInject(R.id.ll_select_time)
    private LinearLayout ll_select_time;
    @ViewInject(R.id.tv_start_time)
    private TextView tv_start_time;
    @ViewInject(R.id.tv_end_time)
    private TextView tv_end_time;
    @ViewInject(R.id.ll_endtime)
    private LinearLayout ll_endtime;

    private boolean isStart;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    String now = sdf.format(new Date());
    String startTimeStr = sdf1.format(new Date());
    private String endTimeStr;
    private CustomDatePicker customDatePicker1;
    private boolean isSpot = true;


    @Override
    public void initWidget() {
        super.initWidget();
        initDatePicker();
    }

    @OnClick({R.id.rl_spot,R.id.rl_presell,R.id.ll_spot,R.id.select_term_time_confirm,R.id.ll_endtime})
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.ll_spot:
                // 日期格式为yyyy-MM-dd
                isStart = true;
                customDatePicker1.show(now.substring(0,10));
                break;
            case R.id.ll_endtime:
                isStart = false;
                customDatePicker1.show(now.substring(0,10));
                break;
            case R.id.select_term_time_confirm:
                confirm();
                break;
            case R.id.rl_spot:
                isInStock(true);
                break;
            case R.id.rl_presell:
                isInStock(false);
                break;
        }
    }

    private void isInStock(boolean b) {
        ll_select_time.setVisibility(View.VISIBLE);
        if (b){
            isSpot = true;
            startTimeStr = now.substring(0,10);
            rl_spot.setBackgroundResource(R.drawable.rl_green_bg);
            tv_spot.setTextColor(getResources().getColor(R.color.primaryGreen));
            tv_spot_helper.setTextColor(getResources().getColor(R.color.primaryGreen));
            rl_presell.setBackgroundResource(R.drawable.rl_white_bg);
            tv_presell.setTextColor(getResources().getColor(R.color.text_black));
            tv_presell_helper.setTextColor(getResources().getColor(R.color.textGray));
            im_spot_selector.setImageResource(R.drawable.date_select);
            im_presell_selector.setImageResource(R.drawable.date_default);
            ll_spot.setVisibility(View.GONE);

        }else {
            isSpot = false;
            startTimeStr = "";
            rl_presell.setBackgroundResource(R.drawable.rl_green_bg);
            tv_presell.setTextColor(getResources().getColor(R.color.primaryGreen));
            tv_presell_helper.setTextColor(getResources().getColor(R.color.primaryGreen));
            rl_spot.setBackgroundResource(R.drawable.rl_white_bg);
            tv_spot.setTextColor(getResources().getColor(R.color.text_black));
            tv_spot_helper.setTextColor(getResources().getColor(R.color.textGray));
            im_spot_selector.setImageResource(R.drawable.date_default);
            im_presell_selector.setImageResource(R.drawable.date_select);
            ll_spot.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 提交按钮
     */
    private void confirm() {
        if (check()) {
            Intent myIntent = new Intent();
            myIntent.putExtra("beginDate", startTimeStr);
            myIntent.putExtra("endDate", endTimeStr);
            setResult(RESULT_OK, myIntent);
            finish();
        }
    }


    protected void showDatePickDlg(final TextView tvShow) {
        Calendar calendar = Calendar.getInstance();
        final String nowData =calendar.get(Calendar.YEAR)+""+(calendar.get(Calendar.MONTH)+1)+""+calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectDate =year+""+(monthOfYear+1)+""+dayOfMonth;
                String monthStr = (monthOfYear+1)+"";
                String dayStr = dayOfMonth+"";
                if (selectDate.compareTo(nowData)<0){
                    T.showShort(mContext, "您选择的日期不能小于当前日期");
                    return;
                }
                if (monthOfYear+1<10){
                    monthStr ="0"+(monthOfYear+1);
                }
                if (dayOfMonth<10){
                    dayStr = "0"+dayOfMonth;
                }
                tvShow.setText(year + "-" + monthStr + "-" + dayStr);
                if(check()){
                    selectTermTimeConfirm.setVisibility(View.VISIBLE);
                }

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private boolean check(){
        boolean flag = false;
        if (TextUtils.isEmpty(endTimeStr)){
            T.showShort(mContext,"请填写货物下架时间");
        }else {
            if (isSpot){
                if (now.substring(0,10).compareTo(endTimeStr)<0){
                    flag = true;
                }else {
                    T.showShort(mContext, "下市日期不能小于当前日期");
                }
            }else {
                if (TextUtils.isEmpty(startTimeStr)){
                    T.showShort(mContext,"请填写货物上架时间");
                }else {
                    if (startTimeStr.compareTo(endTimeStr)>0){
                        T.showShort(mContext, "下市日期不能小于上市日期");
                    }else {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    @Override
    public String setToolBarTitle() {
        return "选择上下市时间";
    }

    @Override
    public void initData() {

    }


    private void initDatePicker() {

        String endTime = (Integer.parseInt(startTimeStr.substring(0,4))+1)+startTimeStr.substring(4,10)+" 00:00";
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
               if (isStart){
                   startTimeStr = time.substring(0,10);
                   tv_start_time.setText(startTimeStr);
                   tv_start_time.setTextColor(getResources().getColor(R.color.primaryGreen));
               }else {
                   endTimeStr = time.substring(0,10);
                   tv_end_time.setText(time.substring(0,10));
                   tv_end_time.setTextColor(getResources().getColor(R.color.primaryGreen));
               }
            }
        }, now, endTime); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动
    }
}
