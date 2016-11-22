package tech.xinong.xnsm.pro.base.view.navigation.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import tech.xinong.xnsm.pro.base.view.navigation.INavigation;

/**
 * Created by xiao on 2016/11/17.
 */

public abstract class AbsNavigation<P extends AbsNavigation.Builder.NavigationParams> implements INavigation{

    private P params;
    private View contentView;

    public AbsNavigation(P params){
        this.params = params;
    }


    @Override
    public void createAndBind() {
        //创建和绑定布局
        contentView = LayoutInflater.from(params.mContext).inflate(getLayoutId(),params.parent,false);
        //判断当前这个视图是否在父容器中(一个视图不能同时绑定2个父容器)
        ViewParent parent = contentView.getParent();
        if (null!=parent){
            ViewGroup viewGroup = (ViewGroup) parent;
            viewGroup.removeView(contentView);
        }
        params.parent.addView(contentView,0);
    }


    /**
     * 封装findViewById
     * @param id
     * @return
     */
    public View findViewById(int id){
        return contentView.findViewById(id);
    }

    /**
     * 封装从资源文件中拿取字符资源方法
     * @param id
     * @return
     */
    public String getString(int id){
        return params.mContext.getResources().getString(id);
    }

    public int getColor(int id){
        return params.mContext.getResources().getColor(id);
    }

    public P getParams() {
        return params;
    }

    public abstract static class Builder{


        public abstract AbsNavigation create();

        //自定义参数类
        public static class NavigationParams{
            public Context mContext;
            public ViewGroup parent;
            public NavigationParams(Context context,ViewGroup parent){
                mContext = context;
                this.parent = parent;
            }
        }

    }




}
