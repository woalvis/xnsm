package tech.xinong.xnsm.pro.buy.presenter;

import android.content.Context;

import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.buy.model.BuyModel;


/**
 * Created by Administrator on 2016/11/5.
 */
public class BuyPresenter extends BasePresenter<BaseView> {
    private BuyModel buyModel;
    public BuyPresenter(Context context){
        super(context);
        this.buyModel = new BuyModel(context);
    }
}
