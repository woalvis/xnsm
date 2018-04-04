package tech.xinong.xnsm.pro.home.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.home.model.FollowModel;
import tech.xinong.xnsm.pro.home.view.abs.FollowView;

/**
 * Created by xiao on 2017/10/16.
 */

public class FollowPresenter extends BasePresenter<FollowView> {
    private FollowModel followModel;
    public FollowPresenter(Context context) {
        super(context);
        followModel = new FollowModel(context);
    }

    public void getUserFollowed(){
        followModel.getUserFollowed(new AbsXnHttpCallback(getContext()) {
            @Override
            public void onSuccess(String info, String result) {
                getView().onGetUserFollowed();
            }
        });
    }

    public void getFavs(){
        followModel.favs(new AbsXnHttpCallback(getContext()) {
            @Override
            public void onSuccess(String info, String result) {

                JSONObject jsonObject = JSON.parseObject(result);
                List<ProductDTO> products = JSON.parseArray(jsonObject.getString("products"),ProductDTO.class);
                List<CategoryModel> categorys = JSON.parseArray(jsonObject.getString("categorys"),CategoryModel.class);
                getView().onGetCategory(categorys,products);
            }
        });
    }
}
