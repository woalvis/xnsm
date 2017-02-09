package tech.xinong.xnsm.pro.base.model;

import android.content.Context;

import tech.xinong.xnsm.mvp.model.impl.MvpBaseModel;

/**
 * Created by xiao on 2016/11/7.
 */

public abstract class BaseModel extends MvpBaseModel {
    public Context context;
    public BaseModel(Context context){
        this.context = context;
    }
    public Context getContext(){
        return context;
    }
}
