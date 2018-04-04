package tech.xinong.xnsm.views;

import android.content.Context;
import android.util.AttributeSet;

public class MyImageView extends android.support.v7.widget.AppCompatImageView {

	private OnMeasureListener onMeasureListener;
	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);      
        //将图片测量的大小回调到onMeasure
        if(onMeasureListener != null){  
            onMeasureListener.onMeasureSize(getMeasuredWidth(), getMeasuredHeight());  
        }  
    } 
	public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
		 this.onMeasureListener = onMeasureListener; 
	}
	public interface OnMeasureListener{  
        public void onMeasureSize(int width, int height);  
    } 

}
