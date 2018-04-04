package tech.xinong.xnsm.pro.home.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.pro.home.model.AuctionModel;
import tech.xinong.xnsm.pro.home.model.AuctionShow;
import tech.xinong.xnsm.pro.home.view.abs.AuctionView;

/**
 * Created by xiao on 2017/1/17.
 */

public class AuctionPresenter extends BasePresenter<AuctionView>{

    private AuctionModel auctionModel;
    private List<AuctionShow> auctionShows;

    public AuctionPresenter(Context context) {
        super(context);
        auctionModel = new AuctionModel(context);
        auctionShows = new ArrayList<>();
    }

    public void getAuctions(){

        auctionModel.getAuctions(new AbsXnHttpCallback(getContext()) {
            @Override
            public void onSuccess(String info, String result) {
                if (info.equals(HttpConstant.OK)){
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    String contentStr = jsonObject.getString("content");

                    if (jsonObject!=null){
                       auctionShows = JSONArray.parseArray(contentStr,AuctionShow.class);
                    }
                }
                getView().onGetAuctionsSuccess(auctionShows);
            }
        });
    }
}
