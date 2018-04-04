package tech.xinong.xnsm.pro.home.model;

import android.content.Context;

import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.mvp.model.MvpModel;
import tech.xinong.xnsm.pro.base.model.BaseModel;

/**
 * Created by xiao on 2017/10/16.
 */

public class FollowModel extends BaseModel implements MvpModel {
    public FollowModel(Context context) {
        super(context);
    }

   public void getUserFollowed(AbsXnHttpCallback callback){
       XinongHttpCommend.getInstance(context).favs(callback);
   }


   public void favs(AbsXnHttpCallback callback){
       XinongHttpCommend.getInstance(context).favs(callback);
   }
}
