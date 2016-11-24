package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;

/**
 * 发布页面
 * Created by xiao on 2016/11/5.
 */
public class PublishFragment extends BaseFragment<BuyPresenter, BaseView> implements View.OnClickListener {

    private BuyPresenter buyPresenter;
    private TextView publishBuy;//发布购买信息按钮（我要买）
    private TextView publishSell;//发布供货信息按钮（我要卖）
    private TextView myBublish;//我发布的信息按钮

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

        publishBuy = (TextView) contentView.findViewById(R.id.publish_buy_tv);
        publishSell = (TextView) contentView.findViewById(R.id.publish_sell_tv);
        myBublish = (TextView) contentView.findViewById(R.id.publish_mylist_tv);
        publishBuy.setOnClickListener(this);
        myBublish.setOnClickListener(this);
        publishSell.setOnClickListener(this);
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_publish;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publish_buy_tv:
                publishInfo(true);
                break;
            case R.id.publish_sell_tv:
                publishInfo(false);
                break;
            case R.id.publish_mylist_tv:
                getMyPublishList();
                break;
        }
    }

    /**
     * 获取本人发布过的采购和供货信息
     */
    private void getMyPublishList() {
    }


    /**
     * 发布供货或者采购信息（我要买卖）
     */
    private void publishInfo(boolean isBuy) {
        Intent intent = new Intent(mContext, PublishSelectCategoryActivity.class);
        if (isBuy){
            intent.putExtra("info", CategoryModel.OP_SELECT.PUBLISH_BUY);
        }else {
            intent.putExtra("info", CategoryModel.OP_SELECT.PUBLISH_SELL);
        }
        mContext.startActivity(intent);
    }

}
