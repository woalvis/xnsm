package tech.xinong.xnsm.pro.home.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.base.view.navigation.impl.DefaultNavigation;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;
import tech.xinong.xnsm.pro.buy.view.GoodsDetailActivity;
import tech.xinong.xnsm.pro.publish.model.PublishInfoModel;
import tech.xinong.xnsm.pro.user.view.MyOrdersActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.imageloder.ImageLoader;
import tech.xinong.xnsm.views.ADTextView;
import tech.xinong.xnsm.views.CircleIndicator;
import tech.xinong.xnsm.views.GridViewForScrollView;
import tech.xinong.xnsm.views.entity.ADEntity;

import static tech.xinong.xnsm.pro.home.view.ImageAdapter.IMAGES;

/**
 * 主页面
 * Created by Administrator on 2016/8/10.
 */
public class HomeFragment extends BaseFragment<BuyPresenter,BaseView> implements HomeView,SwipeRefreshLayout.OnRefreshListener,ImageLoader.DownloadSuccessListener{

    private BuyPresenter buyPresenter;
    private ViewPager viewPagerBanner;
    private CircleIndicator indianCalendarBanner;
    private GridViewForScrollView gridHomePush;
    private ImageView btMyOrders;
    private ImageView btAuction;
    private SwipeRefreshLayout swipeLayout;
    private static final int REFRESH_COMPLETE = 0x110;//刷新页面handler状态码
    private boolean isLooper;

    private int currentPosition;
    private static final int AUTO_PLAY_TIME_INTERVAL= 3000;//主页Banner轮播时间间隔控制

    private ADTextView adTv;
    //创建对象,绑定presenter
    @Override
    public BuyPresenter bindPresenter() {
       buyPresenter = new BuyPresenter(getContext());
        return buyPresenter;
    }


    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initContentView(View contentView) {
        setViewPagerBanner(contentView);
        gridHomePush = (GridViewForScrollView) contentView.findViewById(R.id.grid_home_push);
        swipeLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.home_swipe_ly);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        autoPlay();
        btMyOrders = (ImageView) contentView.findViewById(R.id.home_my_orders);
        btMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(MyOrdersActivity.class);
                //startActivity(new Intent(mContext, MyOrdersActivity.class));
            }
        });

        btAuction = (ImageView) contentView.findViewById(R.id.home_auction);

        btAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(AuctionShowActivity.class);
            }
        });

//        marqueeView = (MarqueeView) contentView.findViewById(R.id.tv_marquee);
//        marqueeView.setFocusable(true);
//        marqueeView.requestFocus();
//        marqueeView.setText("喜农市正式开业啦");//设置文本
//        marqueeView.startScroll(); //开始
//


        adTv = (ADTextView) contentView.findViewById(R.id.tv_ad);
        adTv.setmTexts(getStringList());

        initNavigation(contentView);

    }


    /**
     * 初始化viewpager，给他设置轮播监听
     * @param contentView
     */
    private void setViewPagerBanner(View contentView){
        viewPagerBanner = (ViewPager) contentView.findViewById(R.id.view_pager);
        viewPagerBanner.setAdapter(new ImageAdapter(getActivity()));
        indianCalendarBanner = (CircleIndicator)contentView.findViewById(R.id.circle_indicator);
        indianCalendarBanner.setViewPager(viewPagerBanner);
        viewPagerBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0){    //判断当切换到第0个页面时把currentPosition设置为images.length,即倒数第二个位置，小圆点位置为length-1
                    currentPosition=IMAGES.length;
                }else if(position==IMAGES.length+1){    //当切换到最后一个页面时currentPosition设置为第一个位置，小圆点位置为0
                    currentPosition=1;
                }else{
                    currentPosition=position;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==ViewPager.SCROLL_STATE_IDLE){
                    viewPagerBanner.setCurrentItem(currentPosition,false);
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        /*得到货品列表*/
        getListings();
    }

    @Override
    public void initNavigation(View contentView){
        DefaultNavigation.Builder builder = new DefaultNavigation.Builder(getContext(),(ViewGroup)contentView);
        builder.setCenterText(R.string.home).create();
    }


    /**
     * 设置线程，自动轮播的时间间隔
     */
    private void autoPlay() {
        new Thread(){
            @Override
            public void run() {
                super.run();

                while(true){
                    SystemClock.sleep(AUTO_PLAY_TIME_INTERVAL);
                    if (currentPosition>=IMAGES.length-1){
                        currentPosition = -1;
                    }
                    currentPosition++;
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                viewPagerBanner.setCurrentItem(currentPosition,false);
            }
        }
    };
    /**
     * 得到listings的方法
     */
    public void getListings(){


        XinongHttpCommend.getInstance(mContext).getListings(new AbsXnHttpCallback() {

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
//
//                            SimpleDraweeView defaultImage = (SimpleDraweeView) viewHolder.getView(R.id.item_grid_icon);
//                            if (!TextUtils.isEmpty(item.getDefaultImage())){
//                                String imageUrl = String.format(HttpConstant.HOST+HttpConstant.URL_SHOW_IMAGE,item.getId(),item.getDefaultImage());
//                                Uri uri = Uri.parse(imageUrl);
//                                defaultImage.setImageURI(uri);
//                            }

                            ImageView defaultImage = (ImageView) viewHolder.getView(R.id.item_grid_icon);
                            if (!TextUtils.isEmpty(item.getDefaultImage())){
                               ImageLoader.getInstance().setLis(HomeFragment.this)
                                       .displayImage(String.format(HttpConstant.HOST+HttpConstant.URL_SHOW_IMAGE,item.getId(),
                                               item.getDefaultImage()),
                                               defaultImage);
                            }


//                            if (item.getPhoto()!=null&&item.getPhoto().length>=1){
//                                ImageLoader.getInstance().setImageCache(new DoubleImageCache())
//                                        .displayImage(item.getPhoto()[0],
//                                                (ImageView)viewHolder.getView(R.id.item_grid_icon));
//                            }else {
//                                viewHolder.setImageForView(R.id.item_grid_icon,R.mipmap.default_pic_bg);
//                            }

                            viewHolder.setOnClickListener(R.id.item_grid_layout, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                                    intent.putExtra("id",item.getId());
                                    T.showShort(mContext,  item.getProductName());
                                    mContext.startActivity(intent);
                                }
                            });
                        }
                    });

                }

            }

        });
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

    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }


    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
                    getListings();
                    swipeLayout.setRefreshing(false);
                    T.showShort(mContext, "刷新成功");
                    break;

            }
        };
    };


    @Override
    public void onListingRefresh() {

    }


    @Override
    public void onDownloadSuccess(final Bitmap bitmap,final ImageView imageView) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });

    }



    private List<ADEntity> getStringList() {
        List<ADEntity> ads = new ArrayList<>();
        for (int i=0;i<10;i++){
            ads.add(new ADEntity("前缀"+i+":  ","正文我是一个好消息"+i,"http://www.baidu.com"));
        }
        return ads;
    }
}
