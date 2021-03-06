package tech.xinong.xnsm.pro.home.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.PageInfo;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.buy.model.VerificationReqType;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;
import tech.xinong.xnsm.pro.buy.view.GoodsDetailActivity;
import tech.xinong.xnsm.pro.buy.view.SearchActivity;
import tech.xinong.xnsm.pro.home.model.AdsModel;
import tech.xinong.xnsm.pro.home.model.RecommendedSellModel;
import tech.xinong.xnsm.pro.home.view.abs.HomeView;
import tech.xinong.xnsm.pro.home.view.adapter.RecommendAdapter;
import tech.xinong.xnsm.pro.user.model.adapter.MyPagerAdapter;
import tech.xinong.xnsm.pro.user.view.MyOrdersActivity;
import tech.xinong.xnsm.util.ArrayUtil;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.imageloder.ImageLoader;
import tech.xinong.xnsm.views.ADTextView;
import tech.xinong.xnsm.views.CircleIndicator;
import tech.xinong.xnsm.views.ListViewForScrollView;
import tech.xinong.xnsm.views.WrapContentHeightViewPager;
import tech.xinong.xnsm.views.entity.ADEntity;

/**
 * Created by xiao on 2018/1/5.
 */

public class HomeFragment2 extends BaseFragment implements HomeView, SwipeRefreshLayout.OnRefreshListener, ImageLoader.DownloadSuccessListener, RecommendAdapter.OnItemClickListener {

    private BuyPresenter buyPresenter;
    private ViewPager viewPagerBanner;
    private CircleIndicator indianCalendarBanner;
    // private GridViewForScrollView gridHomePush;
    private ImageView btMyOrders;//订单
    private ImageView btMyQuotes;//行情
    private ImageView btMyFollow;//关注
    // private SwipeRefreshLayout swipeLayout;
    private ADTextView adTv;
    private TabLayout homeRecommendTabs;
    private static final int REFRESH_COMPLETE = 0x110;//刷新页面handler状态码
    private boolean isLooper;
    private List<String> tabDatas = new ArrayList<>(Arrays.asList("推荐商品", "优质商家"));
    private LinearLayout home_search;
    private RecyclerView home_recommend_imgs;
    private List<Integer> imgIds;
    private List<AdsModel> adsModels;
    private WrapContentHeightViewPager my_order_vp_view;
    private ListViewForScrollView recommendProductLv;
    private ListViewForScrollView recommendSellerLv;
    private int productPage;
    private int productSize;
    private CommonAdapter<SellerListingInfoDTO> recommendProductAdapter;
    private CommonAdapter<RecommendedSellModel> recommendSellerAdapter;
    private List<SellerListingInfoDTO> recommendProductList;
    private List<RecommendedSellModel> recommendedSellModels;
    private List<AdsModel> ads;
    private int currentPosition;
    private static final int AUTO_PLAY_TIME_INTERVAL = 3000;//主页Banner轮播时间间隔控制
    private MyPagerAdapter mAdapter;



    //创建对象,绑定presenter
    @Override
    public BuyPresenter bindPresenter() {
        buyPresenter = new BuyPresenter(getContext());
        return buyPresenter;
    }


    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_home2;
    }

    @Override
    protected void initContentView(View contentView) {
        recommendProductList = new ArrayList<>();
        recommendedSellModels = new ArrayList<>();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_lv_recommend, null);
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.item_lv_recommend, null);


        recommendProductLv = view.findViewById(R.id.grid_home_push);
        recommendSellerLv =  view1.findViewById(R.id.grid_home_push);



        viewPagerBanner =  contentView.findViewById(R.id.view_pager);

        home_search = contentView.findViewById(R.id.home_search);
        home_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(SearchActivity.class);
            }
        });

//        swipeLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.home_swipe_ly);
//        swipeLayout.setOnRefreshListener(this);
//        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
//                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        btMyOrders = contentView.findViewById(R.id.home_my_orders);
        btMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(MyOrdersActivity.class);
                //startActivity(new Intent(mContext, MyOrdersActivity.class));
            }
        });

        btMyQuotes = contentView.findViewById(R.id.home_my_quotes);
        btMyQuotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(QuotesActivity.class);
            }
        });

        btMyFollow = contentView.findViewById(R.id.home_my_follow);
        btMyFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(FollowActivity.class);
            }
        });

        adTv = contentView.findViewById(R.id.tv_ad);
        adTv.setmTexts(getStringList());

        my_order_vp_view = contentView.findViewById(R.id.my_order_vp_view);
//        initNavigation(contentView);//暂时没用
        //初始化Tablayout标题
        homeRecommendTabs = contentView.findViewById(R.id.home_recommend_tabs);
        homeRecommendTabs.setTabMode(TabLayout.MODE_FIXED);
        for (String title : tabDatas) {
            homeRecommendTabs.addTab(homeRecommendTabs.newTab().setText(title));
        }

        indianCalendarBanner = contentView.findViewById(R.id.circle_indicator);
        home_recommend_imgs = contentView.findViewById(R.id.home_recommend_imgs);

        /*得到推荐商家列表*/
        // getRecommendedSellerListings();
         /*初始化推荐商品的适配器*/

        //  initViewPager();


    }

    @Override
    public void initData() {
        super.initData();
        setViewPagerBanner();
        /*初始化图片推荐列表*/
        initRecommendImgs();
         /*得到货品列表*/
        getListings(0,10);
    }

    /*初始化推荐商品的适配器*/
    private void initRecommendAdapter() {
        recommendProductAdapter = new CommonAdapter<SellerListingInfoDTO>(
                mContext, R.layout.item_product_show, recommendProductList) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final SellerListingInfoDTO item) {
                viewHolder.setOnClickListener(R.id.product_show_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                        intent.putExtra("id", item.getId());
                        mContext.startActivity(intent);
                    }
                });
                if (item.getRecommend()==2){
                    viewHolder.getView(R.id.tv_ad).setVisibility(View.VISIBLE);
                }else if (item.getRecommend()==1){
                    viewHolder.getView(R.id.tv_ad).setVisibility(View.VISIBLE);
                    ((TextView)viewHolder.getView(R.id.tv_ad)).setText("推荐");
                }
                viewHolder.setTextForTextView(R.id.product_price, item.getUnitPrice() + "/"+item.getWeightUnit().getDisplayName());
                viewHolder.setTextForTextView(R.id.product_address, item.getProvinceName() + "   " + item.getCityName() + "  " + item.getSellerName());
                viewHolder.setTextForTextView(R.id.product_desc, item.getTitle());
                final SimpleDraweeView defaultImage = (SimpleDraweeView) viewHolder.getView(R.id.product_iv_show);
                final String imageUrl = ImageUtil.getImgUrl(item.getCoverImg());
                if (!TextUtils.isEmpty(imageUrl)) {
                    defaultImage.setImageURI(ImageUtil.getImgUrl(imageUrl));
                }else {
                    XinongHttpCommend.getInstance(mContext).getProductImg(new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            defaultImage.setImageURI(ImageUtil.getProductImg(result));
                        }
                    },item.id);
                }
            }
        };

    }

    private void initRecommendedSellerAdapter(){

        recommendSellerAdapter = new CommonAdapter<RecommendedSellModel>(mContext,
                R.layout.item_recommended_seller,
                recommendedSellModels
        ) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final RecommendedSellModel item) {
                viewHolder.getView(R.id.ll_sell_show).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,SellerDetailActivity.class);
                        intent.putExtra("sellerId",item.getId());
                        startActivity(intent);
                    }
                });

                SimpleDraweeView sellerImg = (SimpleDraweeView) viewHolder.getView(R.id.seller_cover_img);
                TextView tv_personal = (TextView) viewHolder.getView(R.id.tv_personal);
                TextView tv_enterprise = (TextView) viewHolder.getView(R.id.tv_enterprise);
                String sellerImgUrl = item.getCoverImg();
                if (!TextUtils.isEmpty(sellerImgUrl)){
                    sellerImg.setImageURI(ImageUtil.getImgUrl(sellerImgUrl));
                }
                if (item.getTags()!=null){
                    String[] tags = item.getTags().split(",");
                    if (tags==null||tags.length==0){
                        tv_personal.setVisibility(View.GONE);
                        tv_enterprise.setVisibility(View.GONE);
                    }else {
                        if (!ArrayUtil.c(tags, VerificationReqType.ENTERPRISE.name())){
                            tv_enterprise.setVisibility(View.GONE);
                        }
                        if (!ArrayUtil.c(tags, VerificationReqType.PERSONAL.name())){
                            tv_personal.setVisibility(View.GONE);
                        }
                    }
                }else {
                    tv_personal.setVisibility(View.GONE);
                    tv_enterprise.setVisibility(View.GONE);
                }
                viewHolder.setTextForTextView(R.id.seller_name,item.getFullName());
                if (!TextUtils.isEmpty(item.getAddress()))
                    viewHolder.setTextForTextView(R.id.sell_address,item.getAddress());
                if (!TextUtils.isEmpty(item.getBusiness()))
                    viewHolder.setTextForTextView(R.id.tv_business,"经营："+item.getBusiness());
                else
                    viewHolder.setTextForTextView(R.id.tv_business,"经营："+"暂无");
            }
        };
    }


    /*获得推荐商家数据*/
    private void getRecommendedSellerListings() {
        XinongHttpCommend.getInstance(mContext).sells(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                JSONObject jsonObject = JSON.parseObject(result);
                recommendedSellModels = JSON.parseArray(jsonObject.getString("content"),RecommendedSellModel.class);
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initViewPager();
                    }
                });
            }
        });
    }

    private void initViewPager() {
        List<View> views = new ArrayList<>();
        initRecommendedSellerAdapter();
        initRecommendAdapter();
        recommendProductLv.setAdapter(recommendProductAdapter);
        recommendSellerLv.setAdapter(recommendSellerAdapter);
        views.add(recommendProductLv);
        views.add(recommendSellerLv);
        mAdapter = new MyPagerAdapter(mContext,views, tabDatas);
        if(my_order_vp_view.getAdapter() == null) {
            my_order_vp_view.setAdapter(mAdapter);
        }
//        my_order_vp_view.setObjectForPosition(recommendProductLv,1);
//        my_order_vp_view.setObjectForPosition(recommendSellerLv,2);
        if (homeRecommendTabs == null) {
            homeRecommendTabs.setTabsFromPagerAdapter(mAdapter);
        }
        homeRecommendTabs.setupWithViewPager(my_order_vp_view);
//        my_order_vp_view.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                my_order_vp_view.resetHeight(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }


    private void initRecommendImgs() {

        XinongHttpCommend.getInstance(mContext).ads(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                adsModels = JSONArray.parseArray(result, AdsModel.class);
                RecommendAdapter adapter = new RecommendAdapter(getContext(), adsModels);
                adapter.setmOnItemClickListener(HomeFragment2.this);
                home_recommend_imgs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                home_recommend_imgs.setAdapter(adapter);
            }
        });

    }

    /**
     * 初始化viewpager，给他设置轮播监听
     *
     */
    private void setViewPagerBanner() {
        String[] productNames = {""};
        XinongHttpCommend.getInstance(mContext).campaigns(productNames, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                //JSONObject jb = JSON.parseObject(result);
                ads = JSON.parseArray(result, AdsModel.class);
                List<View> views = new ArrayList<>();
                for (int i = 0; i < ads.size(); i++) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.item_banner, null, false);
                    SimpleDraweeView sdv = view.findViewById(R.id.im_banner);
                    sdv.setImageURI(ads.get(i).getCoverImg());
                    final int finalI = i;
                    sdv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), H5Activity.class);
                            intent.putExtra("contentUrl", adsModels.get(finalI).getContentUrl());
                            getActivity().startActivity(intent);
                        }
                    });
                    views.add(view);
                }

                viewPagerBanner.setAdapter(new ImageAdapter(getActivity(), ads, views));
                indianCalendarBanner.setViewPager(viewPagerBanner);
                autoPlay();
                viewPagerBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (position == 0) {    //判断当切换到第0个页面时把currentPosition设置为images.length,即倒数第二个位置，小圆点位置为length-1
                            currentPosition = ads.size();
                        } else if (position == ads.size() + 1) {//当切换到最后一个页面时currentPosition设置为第一个位置，小圆点位置为0
                            currentPosition = 1;
                        } else {
                            currentPosition = position;
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (state == ViewPager.SCROLL_STATE_IDLE) {
                            viewPagerBanner.setCurrentItem(currentPosition, false);
                        }
                    }
                });
            }
        });
    }

    /**
     * 设置线程，自动轮播的时间间隔
     */
    private void autoPlay() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                while (true) {
                    SystemClock.sleep(AUTO_PLAY_TIME_INTERVAL);
                    if (currentPosition >= ads.size() - 1) {
                        currentPosition = -1;
                    }
                    currentPosition++;
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                viewPagerBanner.setCurrentItem(currentPosition, false);
            }
        }
    };

    /**
     * 得到listings的方法
     */
    public void getListings(int page, int size) {
        showProgress();
        XinongHttpCommend.getInstance(mContext).recommendations(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                cancelProgress();
                PageInfo pageInfo = JSON.parseObject(result, PageInfo.class);
                if (pageInfo != null) {
                    recommendProductList = JSONArray.parseArray(pageInfo.getContent(), SellerListingInfoDTO.class);
                    getRecommendedSellerListings();
                }
            }

            @Override
            public void onHttpError(Call call, Response response, Exception e) {
                super.onHttpError(call, response, e);
                cancelProgress();
            }

            @Override
            public void onError(String info) {
                super.onError(info);
                cancelProgress();
            }
        }, size, page);
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


    /*下拉刷新*/
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    getListings(0, 10);
                    // swipeLayout.setRefreshing(false);
                    T.showShort(mContext, "刷新成功");
                    break;
            }
        }

        ;
    };


    @Override
    public void onListingRefresh() {

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDownloadSuccess(final Bitmap bitmap, final ImageView imageView) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });

    }


    /**
     * 设置消息推送的内容
     *
     * @return
     */
    private List<ADEntity> getStringList() {
        List<ADEntity> ads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ads.add(new ADEntity("前缀" + i + ":  ", "正文我是一个好消息" + i, "http://www.baidu.com"));
        }
        return ads;
    }

    @Override
    public void onItemClick(RecommendAdapter.RecommendViewHolder item, int position) {
        Intent intent = new Intent(getActivity(), H5Activity.class);
        intent.putExtra("contentUrl", adsModels.get(position).getContentUrl());
        getActivity().startActivity(intent);
    }

}
