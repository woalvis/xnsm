package tech.xinong.xnsm.pro.home.model;

import android.content.Context;

import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.BaseModel;

/**
 * Created by xiao on 2017/1/17.
 */

public class AuctionModel extends BaseModel {
    public AuctionModel(Context context) {
        super(context);
    }

    public void getAuctions(AbsXnHttpCallback callback){
        XinongHttpCommend.getInstance(context).getAuctions(callback);
    }
}
