package tech.xinong.xnsm.pro.publish.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;

/**
 * 发布页面
 * Created by xiao on 2016/11/5.
 */
public class PublishFragment extends BaseFragment<BuyPresenter,BaseView> {

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
        
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_publish;
    }
}
