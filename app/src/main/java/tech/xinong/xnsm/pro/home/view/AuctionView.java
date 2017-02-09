package tech.xinong.xnsm.pro.home.view;

import java.util.List;

import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.home.model.AuctionShow;

/**
 * Created by xiao on 2017/1/17.
 */

public interface AuctionView extends BaseView{
    void onGetAuctionsSuccess(List<AuctionShow> auctionShows);
}
