package tech.xinong.xnsm.pro.sell.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.base.view.navigation.impl.DefaultNavigation;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;

/**
 * 我要卖页面
 * Created by Administrator on 2016/8/10.
 */
public class SellFragment extends BaseFragment<BuyPresenter,BaseView> {

    private BuyPresenter buyPresenter;

    //创建对象
    @Override
    public BuyPresenter bindPresenter() {
       buyPresenter = new BuyPresenter(getContext());

        return buyPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initContentView(View contentView) {
        initNavigation(contentView);
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_sell;
    }


    @Override
    public void initNavigation(View contentView) {
        DefaultNavigation.Builder builder = new DefaultNavigation.Builder(getContext(),(ViewGroup)contentView);
        builder.setCenterText(R.string.sell).create();
    }
}
