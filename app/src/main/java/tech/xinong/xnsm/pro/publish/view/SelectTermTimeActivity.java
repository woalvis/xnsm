package tech.xinong.xnsm.pro.publish.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

import static tech.xinong.xnsm.R.id.select_term_time_confirm;

@ContentView(R.layout.activity_select_term_time)
public class SelectTermTimeActivity extends BaseActivity {
    /*选择上市时间的布局*/
    @ViewInject(R.id.select_time_layout_term_begin_date)
    private LinearLayout beginDate;
    /*选择下市时间的布局*/
    @ViewInject(R.id.select_time_layout_term_end_date)
    private LinearLayout endDate;
    /*显示上市时间的TextView控件*/
    @ViewInject(R.id.select_time_tv_begin_date)
    private TextView beginDateShow;
    /*显示下市时间的TextView控件*/
    @ViewInject(R.id.select_time_tv_end_date)
    private TextView endDateShow;
    /*选择完毕后，将结果返回给发布的Activity按钮*/
    @ViewInject(select_term_time_confirm)
    private Button selectTermTimeConfirm;


    @OnClick({R.id.select_time_layout_term_begin_date, R.id.select_time_layout_term_end_date,
            R.id.select_term_time_confirm})
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.select_time_layout_term_begin_date:
                showDatePickDlg(beginDateShow);
                break;
            case R.id.select_time_layout_term_end_date:
                showDatePickDlg(endDateShow);
                break;
            case R.id.select_term_time_confirm:
                confirm();
                break;
        }
    }

    /**
     * 提交按钮
     */
    private void confirm() {
        Intent myIntent = new Intent();
        myIntent.putExtra("beginDate",beginDateShow.getText().toString().trim());
        myIntent.putExtra("endDate",endDateShow.getText().toString().trim());
        setResult(RESULT_OK,myIntent);
        finish();
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
                    Toast.makeText(mContext, "您选择的日期不能小于当前日期", Toast.LENGTH_SHORT).show();
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
        String beginTermData = beginDateShow.getText().toString();
        String endTermData = endDateShow.getText().toString();


        if (!beginTermData.equals("请选择")&&(!endTermData.equals("请选择"))) {
            if (beginTermData.compareTo(endTermData) > 0) {
                Toast.makeText(mContext, "下市日期不能小于上市日期", Toast.LENGTH_SHORT).show();
                endDateShow.setText("请选择");
            }else {
                flag = true;
            }
        }
        return flag;
    }
}
