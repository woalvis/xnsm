package tech.xinong.xnsm.pro.home.view;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import net.wujingchao.android.view.SimpleTagImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.home.model.AuctionShow;
import tech.xinong.xnsm.pro.home.presenter.AuctionPresenter;
import tech.xinong.xnsm.pro.home.view.abs.AuctionView;
import tech.xinong.xnsm.util.DensityUtil;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_auction_show)
public class AuctionShowActivity extends BaseActivity<AuctionPresenter> implements AuctionView {
    @ViewInject(R.id.auction_gv_show)
    private GridView gvAuctionShow;
    @ViewInject(R.id.auction_detail_show)
    private FrameLayout itemAuctionLayout;
    private List<AuctionShow> auctionShowList;
    /*导航栏标题显示*/
    @ViewInject(R.id.tv_center)
    private TextView tvCenter;

    @Override
    public void initData() {
        auctionShowList = new ArrayList<>();
        initNavigation();
        getAuctions();
    }


    /**
     * 绑定presenter
     * @return
     */
    @Override
    public AuctionPresenter bindPresenter() {
        return new AuctionPresenter(this);
    }


    /**
     * 通过P层调用M层访问网络数据，并最终成功回调onGetAuctionsSuccess接口
     */
    public void getAuctions() {
        getPresenter().getAuctions();
    }


    /**
     * 得到拍卖信息列表的成功回调，在里面进行数据组装，参数为拍卖的信息集合
     * @param auctionShows
     */
    @Override
    public void onGetAuctionsSuccess(List<AuctionShow> auctionShows) {
        gvAuctionShow.setAdapter(new CommonAdapter<AuctionShow>(mContext,R.layout.item_auction_show,auctionShows) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final AuctionShow item) {
                viewHolder.setTextForTextView(R.id.auction_show_product_name,item.getProductName());
                viewHolder.setTextForTextView(R.id.auction_show_product_unit,item.getTotalAmount().intValue()+item.getUnit().getDisplayName());
                viewHolder.setTextForTextView(R.id.auction_show_product_current_price,item.getCurrentPrice()+"元");
                viewHolder.setTextForTextView(R.id.auction_show_product_start_date,convertDate(item.getBeginTime())+"开始");
                viewHolder.setTextForTextView(R.id.auction_show_product_end_date,convertDate(item.getEndTime())+"结束");
                viewHolder.setTextForTextView(R.id.auction_show_bid_times,"出价 "+item.getBidNumber());
                SimpleTagImageView simpleTagImageView = (SimpleTagImageView) viewHolder.getView(R.id.stiv);

                setAuctionState(item,simpleTagImageView);
                viewHolder.setOnClickListener(R.id.auction_detail_show, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,AuctionDetailActivity.class);
                        intent.putExtra("auctionId",item.getId());
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void setAuctionState(AuctionShow item,SimpleTagImageView simpleTagImageView) {

        if (item.isHasBid()){
            simpleTagImageView.setTagWidth(DensityUtil.dip2px(mContext,20));
            simpleTagImageView.setTagText("已投标");
            return;
        }else {
            simpleTagImageView.setTagWidth(DensityUtil.dip2px(mContext,0));
        }

        switch (item.getState()){
            case CLOSED:
                simpleTagImageView.setTagWidth(DensityUtil.dip2px(mContext,20));
                simpleTagImageView.setTagBackgroundColor(R.color.alert_red);
                break;
        }

        simpleTagImageView.setTagText(item.getState().getDescription());



    }

    private String convertDate(String dateStr){
        String convertStr;
        Date date = new Date();
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        try {
            date = sdf.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        convertStr = sdf1.format(date);
        return convertStr;
    }

    private void initNavigation(){
        tvCenter.setVisibility(View.VISIBLE);
        tvCenter.setText("拍卖");
    }
}
