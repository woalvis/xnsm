package tech.xinong.xnsm.pro.home.view.abs;

import java.util.List;

import tech.xinong.xnsm.mvp.view.MvpView;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;

/**
 * Created by xiao on 2017/10/16.
 */

public interface FollowView extends MvpView{
    /*得到现有关注品类的接口*/
    void onGetCategory(List<CategoryModel> categoryModels, List<ProductDTO> products);
    /*用户已经关注的数据*/
    void onGetUserFollowed();
}
