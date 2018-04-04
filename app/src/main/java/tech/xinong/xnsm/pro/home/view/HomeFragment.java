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
import tech.xinong.xnsm.pro.base.view.BaseView;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.buy.presenter.BuyPresenter;
import tech.xinong.xnsm.pro.buy.view.GoodsDetailActivity;
import tech.xinong.xnsm.pro.buy.view.SearchActivity;
import tech.xinong.xnsm.pro.home.model.AdsModel;
import tech.xinong.xnsm.pro.home.model.RecommendedSellModel;
import tech.xinong.xnsm.pro.home.view.abs.HomeView;
import tech.xinong.xnsm.pro.home.view.adapter.RecommendAdapter;
import tech.xinong.xnsm.pro.user.model.adapter.MyPagerAdapter;
import tech.xinong.xnsm.pro.user.view.MyOrdersActivity;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.imageloder.ImageLoader;
import tech.xinong.xnsm.views.ADTextView;
import tech.xinong.xnsm.views.CircleIndicator;
import tech.xinong.xnsm.views.GridViewForScrollView;
import tech.xinong.xnsm.views.entity.ADEntity;


/**
 * 主页面
 * Created by Administrator on 2016/8/10.
 */
public class HomeFragment extends BaseFragment<BuyPresenter, BaseView> implements HomeView, SwipeRefreshLayout.OnRefreshListener, ImageLoader.DownloadSuccessListener, RecommendAdapter.OnItemClickListener {

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
    private ViewPager my_order_vp_view;
    private GridViewForScrollView recommendProductLv;
    private GridViewForScrollView recommendSellerLv;
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
        return R.layout.fragment_home;
    }

    @Override
    protected void initContentView(View contentView) {
        recommendProductList = new ArrayList<>();
        recommendedSellModels = new ArrayList<>();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_lv_recommend, null);
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.item_lv_recommend, null);
        recommendProductLv = (GridViewForScrollView) view.findViewById(R.id.grid_home_push);
        recommendSellerLv = (GridViewForScrollView) view1.findViewById(R.id.grid_home_push);
        viewPagerBanner = (ViewPager) contentView.findViewById(R.id.view_pager);
        setViewPagerBanner(contentView);
        home_search = (LinearLayout) contentView.findViewById(R.id.home_search);
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
        btMyOrders = (ImageView) contentView.findViewById(R.id.home_my_orders);
        btMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(MyOrdersActivity.class);
                //startActivity(new Intent(mContext, MyOrdersActivity.class));
            }
        });

        btMyQuotes = (ImageView) contentView.findViewById(R.id.home_my_quotes);
        btMyQuotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(QuotesActivity.class);
            }
        });

        btMyFollow = (ImageView) contentView.findViewById(R.id.home_my_follow);
        btMyFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipActivity(FollowActivity.class);
            }
        });

        adTv = (ADTextView) contentView.findViewById(R.id.tv_ad);
        adTv.setmTexts(getStringList());

        my_order_vp_view = (ViewPager) contentView.findViewById(R.id.my_order_vp_view);
//        initNavigation(contentView);//暂时没用
        /*初始化图片推荐列表*/
        initRecommendImgs(contentView);

        //初始化Tablayout标题
        homeRecommendTabs = (TabLayout) contentView.findViewById(R.id.home_recommend_tabs);
        homeRecommendTabs.setTabMode(TabLayout.MODE_FIXED);
        for (String title : tabDatas) {
            homeRecommendTabs.addTab(homeRecommendTabs.newTab().setText(title));
        }


         /*得到货品列表*/
        getListings(0,10);
        /*得到推荐商家列表*/
       // getRecommendedSellerListings();
         /*初始化推荐商品的适配器*/

      //  initViewPager();


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
                viewHolder.setTextForTextView(R.id.item_grid_price, item.getUnitPrice() + "/斤");
                viewHolder.setTextForTextView(R.id.item_grid_description, item.getProvinceName() + "   " + item.getCityName() + "  " + item.getSellerName());
                viewHolder.setTextForTextView(R.id.item_grid_product, item.getTitle());
                SimpleDraweeView defaultImage = (SimpleDraweeView) viewHolder.getView(R.id.product_iv_show);
                if (!TextUtils.isEmpty(item.getCoverImg())) {
                    //String imageUrl = String.format(HttpConstant.HOST+HttpConstant.URL_SHOW_IMAGE,item.getId(),item.getCoverImg());
                    String imageUrl = ImageUtil.getImgUrl(item.getCoverImg());
                    defaultImage.setImageURI(ImageUtil.getImgUrl(imageUrl));
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
            protected void fillItemData(CommonViewHolder viewHolder, int position, RecommendedSellModel item) {
                SimpleDraweeView sellerImg = (SimpleDraweeView) viewHolder.getView(R.id.seller_cover_img);
                String sellerImgUrl = item.getCoverImg();
                if (!TextUtils.isEmpty(sellerImgUrl)){
                    sellerImg.setImageURI(ImageUtil.getImgUrl(sellerImgUrl));
                }
                viewHolder.setTextForTextView(R.id.seller_name,item.getFullName());
                viewHolder.setTextForTextView(R.id.sell_address,item.getAddress());
            }
        };
    }


    private List<SellerListingInfoDTO> getL(int i, int i1) {
        List<SellerListingInfoDTO> infos = new ArrayList<>();
        SellerListingInfoDTO s1 = new SellerListingInfoDTO();
        for (int j = 0; j < 10; j++) {
            s1.setCoverImg("http://img05.tooopen.com/images/20141231/sy_78252221416.jpg");
            s1.setSellerName("s1");
            s1.setAddress("address");
            s1.setProvinceName("province1");
            s1.setCityName("city1");
            infos.add(s1);
        }
        return infos;
    }

    /*获得推荐赏家数据*/
    private void getRecommendedSellerListings() {
        XinongHttpCommend.getInstance(mContext).sells(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                JSONObject jsonObject = JSON.parseObject(result);
                recommendedSellModels = JSON.parseArray(jsonObject.getString("content"),RecommendedSellModel.class);
                initViewPager();
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
        my_order_vp_view.setAdapter(mAdapter);
        homeRecommendTabs.setTabsFromPagerAdapter(mAdapter);
        homeRecommendTabs.setupWithViewPager(my_order_vp_view);
    }


    private void initRecommendImgs(View contentView) {
        home_recommend_imgs = (RecyclerView) contentView.findViewById(R.id.home_recommend_imgs);
        XinongHttpCommend.getInstance(mContext).ads(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                adsModels = JSONArray.parseArray(result, AdsModel.class);
                RecommendAdapter adapter = new RecommendAdapter(getContext(), adsModels);
                adapter.setmOnItemClickListener(HomeFragment.this);
                home_recommend_imgs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                home_recommend_imgs.setAdapter(adapter);
            }
        });

    }

    /**
     * 初始化viewpager，给他设置轮播监听
     *
     * @param contentView
     */
    private void setViewPagerBanner(final View contentView) {
        String[] productNames = {""};
        XinongHttpCommend.getInstance(mContext).campaigns(productNames, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                //JSONObject jb = JSON.parseObject(result);
                ads = JSON.parseArray(result, AdsModel.class);
                List<View> views = new ArrayList<>();
                for (int i = 0; i < ads.size(); i++) {
                    View view = LayoutInflater.from(mContext).inflate(R.layout.item_banner, null, false);
                    SimpleDraweeView sdv = (SimpleDraweeView) view.findViewById(R.id.im_banner);
                    sdv.setImageURI(ads.get(i).getCoverImg());
                    views.add(view);
                }

                viewPagerBanner.setAdapter(new ImageAdapter(getActivity(), ads, views));
                indianCalendarBanner = contentView.findViewById(R.id.circle_indicator);
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
        public void handleMessage(android.os.Message msg) {
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
        T.showShort(getContext(), position + "");
        Intent intent = new Intent(getActivity(), H5Activity.class);
        intent.putExtra("contentUrl", adsModels.get(position).getContentUrl());
        getActivity().startActivity(intent);
    }
}
