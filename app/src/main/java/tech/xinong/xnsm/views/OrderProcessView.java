package tech.xinong.xnsm.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.util.DensityUtil;


/**
 * Created by xiao on 2016/12/13.
 */

public class OrderProcessView extends View {
    private Paint roundPaint;
    private Paint bgPaint;
    private Paint statusRoundPaint;
    private Paint textPaint;
    private int status;
    private float radius = 15;
    private String[] orderStatus;
    private int size;
    private int backGroundColor;//背景色

    public void setStatus(int status) {
        this.status = status;
        invalidate();
    }




    public OrderProcessView(Context context) {
        this(context,null);
    }

    public OrderProcessView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public OrderProcessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.OrderProcessView);
        backGroundColor = ta.getColor(R.styleable.OrderProcessView_orderBackground,003344);
        ta.recycle();
        init(context);
    }


    private void init(Context context){

        roundPaint = new Paint();
        roundPaint.setAntiAlias(true);
        roundPaint.setColor(Color.GREEN);
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(backGroundColor);
        statusRoundPaint = new Paint();
        statusRoundPaint.setAntiAlias(true);
        statusRoundPaint.setColor(Color.WHITE);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.sp2px(context,12.0f));
        textPaint.setColor(Color.WHITE);
        orderStatus = getResources().getStringArray(R.array.order_status);
        size = orderStatus.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        canvas.drawRect(0,0,width,height,bgPaint);
        for (int i=0;i<size;i++){
            if (i>status){
                roundPaint.setColor(Color.WHITE);
            }
            if (i<6&&i>0) {
                canvas.drawLine(width / 20 + (i - 1) * width / size + radius * 2, height / 2, width / 20 + (i) * width / size, height / 2, roundPaint);
            }
            canvas.drawCircle(width/20+i*width/size+radius,height/2,radius,roundPaint);

            String str = orderStatus[i];
            Rect rect = new Rect();
            textPaint.getTextBounds(str,0,str.length(),rect);
            int textWidth = rect.width();
            int textHeight = rect.height();
            float left = width/20+i*width/size+radius-textWidth/2;
            float top = height/2+textHeight+radius*2;
            canvas.drawText(str,left,top,textPaint);
            roundPaint.setColor(Color.GREEN);
       }


        canvas.drawCircle(width/20+status*width/6+radius,height/2,radius/2,statusRoundPaint);
        super.onDraw(canvas);
    }
}
