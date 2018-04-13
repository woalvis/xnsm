package tech.xinong.xnsm.pro.home.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.presenter.BasePresenter;
import tech.xinong.xnsm.pro.base.sql.PriceFav;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.home.adapter.CategoryAdapter;
import tech.xinong.xnsm.pro.home.model.PriceModel;
import tech.xinong.xnsm.pro.home.presenter.QuotesPresenter;
import tech.xinong.xnsm.pro.publish.view.PublishSelectActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.MyListView;
import tech.xinong.xnsm.views.PagerScrollView1;

import static com.lzy.okgo.OkGo.getContext;

/**
 * Created by xiao on 2017/10/17.
 * 行情页面
 */
@ContentView(R.layout.activity_quotes)
public class QuotesActivity extends BaseActivity implements CategoryAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    @ViewInject(R.id.home_swipe_ly)
    private SwipeRefreshLayout home_swipe_ly;
    private RecyclerView recyc_category;
    private PullToRefreshListView quotes_lv_show;
    private RecyclerView rv_tags;
    private MyListView lv_show;
    private CategoryAdapter adapter;
    private List<CategoryModel> categoryModels;
    private List<String> tags;
    private PagerScrollView1 scroll;
    private List<PriceModel> priceModelList;
    private List<PriceFav> priceFavs;
    private static final int REFRESH_COMPLETE = 0x110;//刷新页面handler状态码


    @Override
    public BasePresenter bindPresenter() {
        return new QuotesPresenter(this);
    }

    @Override
    public void initData() {
        Connector.getDatabase();
        lv_show = findViewById(R.id.lv_show);
        recyc_category = findViewById(R.id.recyc_category);
        home_swipe_ly.setOnRefreshListener(this);

        home_swipe_ly.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        rv_tags = findViewById(R.id.rv_tags);
        scroll = findViewById(R.id.scroll);
        scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction()){
                    case MotionEvent.ACTION_MOVE:{
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        //当文本的measureheight 等于scroll滚动的长度+scroll的height
                        if(scroll.getChildAt(0).getMeasuredHeight()<=scroll.getScrollY()+scroll.getHeight()){
                            T.showShort(mContext,"到底了");
                        }else{

                        }
                        break;
                    }
                }
                return false;
            }
        });

        if(!TextUtils.isEmpty(mSharedPreferences.getString(XnsConstant.USER_NAME,""))){
            XinongHttpCommend.getInstance(mContext).favs(new AbsXnHttpCallback(mContext) {
                @Override
                public void onSuccess(String info, String result) {
                    JSONObject jsonObject = JSON.parseObject(result);
                    List<ProductDTO> products = JSON.parseArray(jsonObject.getString("products"),ProductDTO.class);
                    List<CategoryModel> categorys = JSON.parseArray(jsonObject.getString("categorys"),CategoryModel.class);
                    categoryModels = categorys;
                    adapter = new CategoryAdapter(categorys);
                    adapter.setListener(QuotesActivity.this);
                    recyc_category.setLayoutManager(new GridLayoutManager(getContext(),
                            2,
                            LinearLayoutManager.VERTICAL,
                            false));
                    recyc_category.setAdapter(adapter);
                    initTag();
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(),
                            4,
                            LinearLayoutManager.VERTICAL,
                            false){
                        @Override
                        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                            if (getChildCount() > 0) {
                                View firstChildView = recycler.getViewForPosition(0);
                                measureChild(firstChildView, widthSpec, heightSpec);
                                int widthSize = View.MeasureSpec.getSize(widthSpec);
                                int heightSize = firstChildView.getMeasuredHeight()*3;
                                setMeasuredDimension(widthSize, heightSize+100);
                            } else {
                                super.onMeasure(recycler, state, widthSpec, heightSpec);
                            }
                        }
                    };
                    layoutManager.setAutoMeasureEnabled(true);

                    rv_tags.setLayoutManager(layoutManager);
                    rv_tags.setAdapter(new com.zhy.adapter.recyclerview.CommonAdapter<String>(mContext,R.layout.item_history,tags){
                        @Override
                        protected void convert(ViewHolder holder, String s, final int position) {
                            holder.setText(R.id.tv,s);
                            holder.setOnClickListener(R.id.tv, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext,ProductQuotesActivity.class);
                                    ProductDTO productDTO = new ProductDTO();
                                    productDTO.setId(priceFavs.get(position).getProductId());
                                    productDTO.setName(priceFavs.get(position).getName());
                                    intent.putExtra("product",productDTO);
                                    mContext.startActivity(intent);
                                }
                            });
                        }
                    });

                }
            });
        }

        String configId = "";
        if (getConfigIds()!=null&&getConfigIds().length>0){
            configId = getConfigIds()[0];
            recyc_category.setVisibility(View.GONE);
        }
        XinongHttpCommend.getInstance(mContext).pricesProduct(configId, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                priceModelList = JSON.parseArray(JSON.parseObject(result).getString("content"),PriceModel.class);
                lv_show.setAdapter(new CommonAdapter<PriceModel>(mContext,R.layout.item_quotes_show,priceModelList) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder, int position, final PriceModel item) {
                        viewHolder.setTextForTextView(R.id.tv_name,item.getSpecName());
                        viewHolder.setTextForTextView(R.id.tv_address,item.getProvince()+item.getCity());
                        double currentPrice = 0;
                        double lastPrice = 0;
                        double disparity = 0;
                        if (item.getCurrentAveragePrice()!=null){
                            currentPrice = item.getCurrentAveragePrice().
                                    setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                        }
                        if (item.getLastAveragePrice()!=null){
                            lastPrice = item.getLastAveragePrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }
                        viewHolder.setTextForTextView(R.id.tv_current_price,currentPrice+"");
                        disparity = item.getCurrentAveragePrice().subtract(item.getLastAveragePrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        if (disparity<0){
                            viewHolder.setTextColor(R.id.tv_current_price,getResources().getColor(R.color.green));
                            viewHolder.setTextColor(R.id.tv_last_price,getResources().getColor(R.color.green));
                            viewHolder.setTextForTextView(R.id.tv_last_price,""+disparity);
                        }else if(disparity>0){
                            viewHolder.setTextForTextView(R.id.tv_last_price,"+"+disparity);
                            viewHolder.setTextColor(R.id.tv_current_price,getResources().getColor(R.color.red));
                            viewHolder.setTextColor(R.id.tv_last_price,getResources().getColor(R.color.red));
                        }
                        viewHolder.setOnClickListener(R.id.ll_quotes, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext,QuotesDetailActivity.class);
                                intent.putExtra("specId",item.getSpecId());
                                intent.putExtra("cityId",item.getCityId());
                                intent.putExtra("model",item);
                                startActivity(intent);
                            }
                        });


                    }
                });
            }
        });

    }

    private void initTag() {
        tags = new ArrayList<>();
        priceFavs = new ArrayList<>();
        if (getConfigIds()!=null&&getConfigIds().length>0){
            tags.add(getConfigNames()[0]);
            PriceFav priceFav = new PriceFav();
            priceFav.setProductId(getConfigIds()[0]);
            priceFav.setName(getConfigNames()[0]);
            priceFavs.add(priceFav);
        }else {
            priceFavs = DataSupport.order("count desc").find(PriceFav.class);
            for (int i=0;i<priceFavs.size();i++){
                if (i<8)
                    tags.add(priceFavs.get(i).getName());
            }
        }

    }

    @Override
    public void initWidget() {
        super.initWidget();
    }


    @Override
    public String setToolBarTitle() {
        return "行情";
    }

    @Override
    public void onItemClick(CategoryAdapter.CategoryViewHolder holder, int position) {
        PublishSelectActivity.skip(mContext, PublishSelectActivity.QUOTES, position);
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
    }



    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:
                    home_swipe_ly.setRefreshing(false);
                    T.showShort(mContext, "刷新成功");
                    break;
            }
        }

        ;
    };

}
