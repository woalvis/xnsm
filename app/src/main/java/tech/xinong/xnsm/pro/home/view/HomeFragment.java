package tech.xinong.xnsm.pro.home.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;
import tech.xinong.xnsm.views.CircleIndicator;
import tech.xinong.xnsm.views.GridViewForScrollView;

/**
 * 主页面
 * Created by Administrator on 2016/8/10.
 */
public class HomeFragment extends BaseFragment<BuyPresenter,BaseView> {

    private BuyPresenter buyPresenter;
    private ViewPager viewPagerBanner;
    private CircleIndicator indianCalendarBanner;
    private GridViewForScrollView gridHomePush;

    private boolean isLooper;

    //创建对象
    @Override
    public BuyPresenter bindPresenter() {
       buyPresenter = new BuyPresenter(getContext());

        return buyPresenter;
    }

//    @Override
//    public MvpView bindView() {
//        return null;
//    }

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
        viewPagerBanner = (ViewPager) contentView.findViewById(R.id.view_pager);
        viewPagerBanner.setAdapter(new ImageAdapter(getActivity()));

        indianCalendarBanner = (CircleIndicator)contentView.findViewById(R.id.circle_indicator);
        indianCalendarBanner.setViewPager(viewPagerBanner);

        gridHomePush = (GridViewForScrollView) contentView.findViewById(R.id.grid_home_push);

//        contentView.findViewById(R.id.tv_login).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                login();
//            }
//        });

        autoPlay();
    }

    private void autoPlay() {


    }

    private void login() {


    }

    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_home;
    }




}
