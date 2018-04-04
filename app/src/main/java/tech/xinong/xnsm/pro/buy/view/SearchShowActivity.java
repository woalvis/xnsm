package tech.xinong.xnsm.pro.buy.view;

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.adapter.ProductShowAdapter;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.SearchListModel;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.publish.view.PublishSelectActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

import static tech.xinong.xnsm.pro.publish.view.PublishSellActivity.REQ_CODE_SELECT_SPEC;

/**
 * Created by xiao on 2017/11/15.
 */
@ContentView(R.layout.activity_search_show)
public class SearchShowActivity extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.lv_result)
    private PullToRefreshListView lv_result;
    @ViewInject(R.id.ll_name)
    private View llName;
    @ViewInject(R.id.ll_address)
    private View llAddress;
    @ViewInject(R.id.ll_spec)
    private View llSpec;
    @ViewInject(R.id.tv_product_name)
    private TextView tv_product_name;
    @ViewInject(R.id.tv_product_spec)
    private TextView tv_product_spec;
    @ViewInject(R.id.tv_product_spec_config)
    private TextView tv_product_spec_config;
    @ViewInject(R.id.tv_product_address)
    private TextView tv_product_address;
    private SearchListModel item;




    private List<SellerListingInfoDTO> infos;
    private ProductShowAdapter adapter;

    private EasyPopup namePopup;
    private EasyPopup addressPopup;
    private EasyPopup specPopup;
    private String productId;

    @Override
    public void initData() {
        infos = new ArrayList<>();
        llName.setOnClickListener(this);
        llAddress.setOnClickListener(this);
        llSpec.setOnClickListener(this);
        String result = getIntent().getStringExtra("result");
        item = (SearchListModel) getIntent().getSerializableExtra("item");
        if (item!=null){
            tv_product_name.setText(item.getProduct().getName());
            if (item.getSpec()!=null){
                tv_product_spec.setText(item.getSpec().getName());
            }
        }
        JSONObject jb =JSON.parseObject(result);
        infos = JSON.parseArray(jb.getString("content"),SellerListingInfoDTO.class);
        setAdapter(lv_result);
        refreshLv(infos);
    }

    private void refreshLv(List<SellerListingInfoDTO> infos){
        adapter = new ProductShowAdapter(mContext,infos);
        lv_result.setAdapter(adapter);
    }


    public static void skip(String result, BaseActivity context,SearchListModel item){
        Intent intent = new Intent(context,SearchShowActivity.class);
        intent.putExtra("result",result);
        intent.putExtra("item",item);
        context.startActivity(intent);
        context.finish();
    }

    private void setAdapter(final PullToRefreshListView lv){
        ILoadingLayout startLabels = lv
                .getLoadingLayoutProxy();
        startLabels.setPullLabel("你可劲拉，拉...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("好嘞，正在刷新...");// 刷新时
        startLabels.setReleaseLabel("你敢放，我就敢刷新...");// 下来达到一定距离时，显示的提示
        String label = DateUtils.formatDateTime(
                mContext.getApplicationContext(),
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);

        lv.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                        lv.onRefreshComplete();
                    }
                },1500);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                        lv.onRefreshComplete();
                    }
                },1500);

            }
            }
        );
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.ll_address:
                break;
            case R.id.ll_name:
//                XinongHttpCommend.getInstance(mContext).favs(new AbsXnHttpCallback(mContext) {
//                    @Override
//                    public void onSuccess(String info, String result) {
//
//
//                        namePopup.showAtAnchorView(v, VerticalGravity.BELOW, HorizontalGravity.CENTER, 0, 20);
//                    }
//                });
//                PublishSelectActivity.skip(mContext,PublishSelectActivity.SEARCH,0);
                Intent intent = new Intent(mContext,PublishSelectActivity.class);
                intent.putExtra("op",PublishSelectActivity.SEARCH);
                intent.putExtra("defaultPosition",0);
                startActivityForResult(intent,1001);
                overridePendingTransition(R.anim.slide_in_from_top,R.anim.slide_out_to_bottom);
                break;
            case R.id.ll_spec:
                Intent intentSelectSpec = new Intent(this, SpecActivity.class);
                intentSelectSpec.putExtra("productId", productId);
                startActivityForResult(intentSelectSpec, REQ_CODE_SELECT_SPEC);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            if (requestCode==1001){
                tv_product_name.setText(data.getStringExtra("result"));
            }
        }
    }

    private void initPopup(){
        namePopup = new EasyPopup(this).setContentView(R.layout.layout_product_select)
                .setAnimationStyle(R.style.showPopAnim)
                .setAnchorView(llName)
                .setFocusAndOutsideEnable(false)
                .createPopup();
    }
}
