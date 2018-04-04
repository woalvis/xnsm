package tech.xinong.xnsm.pro.home.view;

import android.content.Intent;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.math.BigDecimal;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.pro.home.model.PriceModel;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_product_quotes)
public class ProductQuotesActivity extends BaseActivity {
    @ViewInject(R.id.quotes_lv_show)
    private PullToRefreshListView quotes_lv_show;
    private ProductDTO product;
    private List<SpecificationConfigDTO> spes;

    private List<PriceModel> priceModelList;


    @Override
    public void initData() {
        product = (ProductDTO) intent.getSerializableExtra("product");
        setToolBarTitle(product.getName()+"的行情");
        XinongHttpCommend.getInstance(mContext).getSpecByProductId(product.getId(),
                new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        spes = JSON.parseArray(result,SpecificationConfigDTO.class);

//                        gridSpec.setAdapter(new CommonAdapter<SpecificationConfigDTO>(mContext,R.layout.item_border_text,spes) {
//                            @Override
//                            protected void fillItemData(CommonViewHolder viewHolder, int position, final SpecificationConfigDTO item) {
//                                viewHolder.setTextForTextView(R.id.tv_show,item.getName());
//                                viewHolder.setOnClickListener(R.id.tv_show, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Intent myIntent = new Intent();
//                                        myIntent.putExtra("id",item.getId());
//                                        myIntent.putExtra("spec",item.getName());
//                                        setResult(RESULT_OK,myIntent);
//                                        finish();
//                                    }
//                                });
//                            }
//                        });
                    }
                });


        XinongHttpCommend.getInstance(mContext).pricesProduct(product.getId(), new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                priceModelList = JSON.parseArray(JSON.parseObject(result).getString("content"),PriceModel.class);
                quotes_lv_show.setAdapter(new CommonAdapter<PriceModel>(mContext,R.layout.item_quotes_show,priceModelList) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder, int position, final PriceModel item) {
                        viewHolder.setTextForTextView(R.id.tv_name,item.getSpecName());
                        viewHolder.setTextForTextView(R.id.tv_address,item.getProvince()+item.getCity());
                        double currentPrice = 0;
                        double lastPrice = 0;
                        double disparity = 0;
                        if (item.getCurrentAveragePrice()!=null){
                            currentPrice = item.getCurrentAveragePrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                        }

                        if (item.getLastAveragePrice()!=null){
                            lastPrice = item.getLastAveragePrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }

                        viewHolder.setTextForTextView(R.id.tv_current_price,currentPrice+"");


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
}
