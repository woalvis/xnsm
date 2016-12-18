package tech.xinong.xnsm.pro.base.view.navigation.impl;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tech.xinong.xnsm.R;

/**
 * Created by xiao on 2016/11/17.
 */

public class DefaultNavigation extends AbsNavigation<DefaultNavigation.Builder.DefaultNavigationParams>{


    protected DefaultNavigation(Builder.DefaultNavigationParams params) {
        super(params);
    }

    @Override
    public int getLayoutId() {
        return R.layout.navigation_default;
    }


    @Override
    public void createAndBind() {
        super.createAndBind();
        //给我们的TextView绑定文本和监听
        setText(R.id.tv_left,getParams().leftTextColor,getParams().leftText,getParams().leftOnClickListener);
        //给我们的TextView绑定文本和监听
        setText(R.id.tv_center,getParams().centerText,getParams().centerOnClickListener);
        //给我们的TextView绑定文本和监听
        setText(R.id.tv_right,getParams().rightTextColor,getParams().rightText,getParams().rightOnClickListener);

    }


    //设置样式,绑定监听
    protected void setText(int viewId, int text, View.OnClickListener onClickListener){
        setText(viewId,0,getString(text),onClickListener);
    }

    //设置样式,绑定监听
    protected void setText(int viewId, String text, View.OnClickListener onClickListener){
        setText(viewId,0,text,onClickListener);
    }

    //设置样式,绑定监听
    protected void setText(int viewId,int textColor, int text, View.OnClickListener onClickListener){
        setText(viewId,textColor,getString(text),onClickListener);
    }

    //设置样式,绑定监听
    protected void setText(int viewId,int textColor, String text, View.OnClickListener onClickListener){
        View view = findViewById(viewId);
        if (view == null && !(view instanceof TextView)){
            return;
        }
        TextView textView = (TextView) view;
        if (TextUtils.isEmpty(text)){
            textView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
            textView.setOnClickListener(onClickListener);

            if (textColor != 0){
                textView.setTextColor(getColor(textColor));
            }
        }

    }

    public static class Builder extends AbsNavigation.Builder{

        protected DefaultNavigation.Builder.DefaultNavigationParams params;

        public Builder(Context context,ViewGroup parent){
            this.params = new DefaultNavigationParams(context,parent);
        }


        public DefaultNavigation.Builder setLeftText(int leftText){
            this.params.leftText = this.params.getString(leftText);
            return this;
        }


        public DefaultNavigation.Builder setLeftText(String leftText){
            this.params.leftText = leftText;
            return this;
        }


        public DefaultNavigation.Builder setRightText(String rightText){
            this.params.rightText = rightText;
            return this;
        }

        public Builder setCenterText(int centerText){
            this.params.centerText = this.params.getString(centerText);
            return this;
        }

        public Builder setRightText(int rightText){
            this.params.rightText = this.params.getString(rightText);
            return this;
        }

        public Builder setRightTextColor(int rightTextColor){
            this.params.rightTextColor = rightTextColor;
            return this;
        }

        public Builder setLeftTextColor(int leftTextColor){
            this.params.leftTextColor = leftTextColor;
            return this;
        }

        public Builder setLeftOnClickListener(View.OnClickListener leftOnClickListener){
            this.params.leftOnClickListener = leftOnClickListener;
            return this;
        }

        public Builder setCenterOnClickListener(View.OnClickListener centerOnClickListener){
            this.params.centerOnClickListener = centerOnClickListener;
            return this;
        }

        public Builder setRightOnClickListener(View.OnClickListener rightOnClickListener){
            this.params.rightOnClickListener = rightOnClickListener;
            return this;
        }
        @Override
        public  AbsNavigation create() {
            DefaultNavigation defaultNavigation = new DefaultNavigation(params);
            defaultNavigation.createAndBind();
            return defaultNavigation;
        }




        //自定义参数类
        public static class DefaultNavigationParams extends AbsNavigation.Builder.NavigationParams{

            //扩展属性
            //左边文本
            public String leftText;
            //中间的文本
            public String centerText;
            //右边的文本
            public String rightText;
            //左边字体颜色
            public int leftTextColor;
            //右边字体颜色
            public int rightTextColor;
            //左边的视图点击事件
            public View.OnClickListener leftOnClickListener;
            //中间的视图点击事件
            public View.OnClickListener centerOnClickListener;
            //右边的视图点击事件
            public View.OnClickListener rightOnClickListener;
            public DefaultNavigationParams(Context context,ViewGroup parent){
                super(context,parent);

            }
            public String getString(int id){
                return mContext.getResources().getString(id);
            }
        }


    }




}
