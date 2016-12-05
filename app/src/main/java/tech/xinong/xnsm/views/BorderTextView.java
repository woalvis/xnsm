package tech.xinong.xnsm.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by xiao on 2016/12/2.
 */

public class BorderTextView extends TextView{

    private int strokeWidth = 1;

    public BorderTextView(Context context) {
        this(context,null);
    }

    public BorderTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BorderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        //  将边框设为黑色
        paint.setColor(Color.GRAY);
        //  画TextView的4个边
        canvas.drawLine(0, 0, this.getWidth() - strokeWidth, 0, paint);
        canvas.drawLine(0, 0, 0, this.getHeight() - strokeWidth, paint);
        canvas.drawLine(this.getWidth() - strokeWidth, 0, this.getWidth() - strokeWidth, this.getHeight() - strokeWidth, paint);
        canvas.drawLine(0, this.getHeight() - strokeWidth, this.getWidth() - strokeWidth, this.getHeight() - strokeWidth, paint);
        super.onDraw(canvas);
    }
}
