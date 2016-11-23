package tech.xinong.xnsm.pro.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.base.view.navigation.impl.DefaultNavigation;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;
import tech.xinong.xnsm.pro.buy.view.GoodsDetailActivity;
import tech.xinong.xnsm.pro.publish.model.PublishInfoModel;
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

        getListings();

        autoPlay();
    }


    private void initNavigation(View contentView){
        DefaultNavigation.Builder builder = new DefaultNavigation.Builder(getContext(),(ViewGroup)contentView);
        builder.setLeftText(R.string.register)
                .setCenterText(R.string.tabbar_home_text)
                .setRightText(R.string.login)
                .setLeftTextColor(R.color.app_text_orange_color)
                .setRightTextColor(R.color.app_text_orange_color)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "注册", Toast.LENGTH_SHORT).show();
                    }
                })
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "登录", Toast.LENGTH_SHORT).show();
                    }
                }).create();


    }


    private void autoPlay() {


    }





    /**
     * 得到listings的方法
     */
    public void getListings(){
        XinongHttpCommend.getInstence(mContext).getListings(new AbsXnHttpCallback() {

            @Override
            public void onSuccess(String info, String result) {

                JSONObject rJson = JSON.parseObject(result);
                if (rJson!=null){
                    List<PublishInfoModel> publishInfoModelList = JSONArray.parseArray(rJson.getString("content"),PublishInfoModel.class);
                    Log.d("xx",publishInfoModelList.toString());
                    gridHomePush.setFocusable(false);
                    gridHomePush.setAdapter(new CommonAdapter<PublishInfoModel>(getContext(),R.layout.item_grid_pushed,publishInfoModelList) {
                        @Override
                        protected void fillItemData(CommonViewHolder viewHolder, int position, final PublishInfoModel item) {
                            viewHolder.setTextForTextView(R.id.item_grid_price,item.getUnitPrice()+"/斤");
                            viewHolder.setTextForTextView(R.id.item_grid_description,item.getOrigin()+"  "+item.getOwnerFullName());
                            if (item.getPhoto()!=null&&item.getPhoto().length>=1){

                            }
                            viewHolder.setOnClickListener(R.id.item_grid_layout, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                                    intent.putExtra("detail",item);
                                    Toast.makeText(mContext,  item.getProductName(), Toast.LENGTH_SHORT).show();
                                    mContext.startActivity(intent);
                                }
                            });


                        }
                    });

                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_home;
    }

}
