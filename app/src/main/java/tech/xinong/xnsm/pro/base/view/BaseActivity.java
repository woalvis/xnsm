package tech.xinong.xnsm.pro.base.view;

import tech.xinong.xnsm.mvp.view.impl.MvpActivity;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;

/**
 * Created by xiao on 2016/11/7.
 */

public class BaseActivity <p extends BasePresenter> extends MvpActivity<p> {
    @Override
    public p bindPresenter() {
        return null;
    }
}
