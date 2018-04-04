package tech.xinong.xnsm.pro.sell.view;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.pro.publish.view.TestSelectAddressActivity;
import tech.xinong.xnsm.pro.sell.model.BuyerListing;
import tech.xinong.xnsm.pro.sell.model.ListShowAdapter;
import tech.xinong.xnsm.pro.sell.model.QuotationModel;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2017/12/22.
 */
@ContentView(R.layout.activity_submit_price)
public class SubmitPriceActivity extends BaseActivity implements ListShowAdapter.OnLlClickListener{
    @ViewInject(R.id.et_buyer_intention)
    private EditText et_buyer_intention;
    @ViewInject(R.id.lv_publish)
    private RecyclerView lv_publish;
    @ViewInject(R.id.et_buyer_amount)
    private EditText et_buyer_amount;
    @ViewInject(R.id.tv_goods_address)
    private TextView tv_goods_address;
    @ViewInject(R.id.et_supply_product)
    private EditText et_supply_product;
    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;
    @ViewInject(R.id.unit)
    private TextView unit;
    @ViewInject(R.id.tv_price_unit)
    private TextView tv_price_unit;
    @ViewInject(R.id.im_select)
    private ImageView im_select;
    private BuyerListing buyerListing;
    private List<SellerListingInfoDTO> listings;
    private QuotationModel quotation;
    private int currentPosition;
    private ListShowAdapter.OnLlClickListener llClickListener;
    private ListShowAdapter listShowAdapter;
    @Override
    public void initData() {
        quotation = new QuotationModel();
        buyerListing = (BuyerListing) intent.getSerializableExtra("buyerListing");
        listings = JSONObject.parseArray(intent.getStringExtra("result"), SellerListingInfoDTO.class);
        if (listings != null && listings.size() > 0) {
            listShowAdapter = new ListShowAdapter(mContext,listings);
            lv_publish.setLayoutManager(new GridLayoutManager(mContext,
                            1,
                            LinearLayoutManager.HORIZONTAL,
                    false));
            listShowAdapter.setListener(this);
            lv_publish.setAdapter(listShowAdapter);
        }
        if (buyerListing != null) {
            et_buyer_intention.setHint("买家意向：" + buyerListing.getMinPrice() + "-" + buyerListing.getMaxPrice());
            unit.setText("元/" + buyerListing.getWeightUnit().getDisplayName());
            et_buyer_amount.setHint("买家需求：" + buyerListing.getAmount());
            tv_price_unit.setText(buyerListing.getWeightUnit().getDisplayName());
            tv_goods_address.setText("点击选择");
        }
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etAmount = et_buyer_amount.getText().toString();
                if (!TextUtils.isEmpty(etAmount)) {
                    double amount = Double.parseDouble(etAmount);
                    quotation.setAmount(amount);
                }else {
                    T.showShort(mContext,"供货量填写有误");
                    return;
                }
                String etPrice = et_buyer_intention.getText().toString();
                if (!TextUtils.isEmpty(etPrice)){
                    double price = Double.parseDouble(etPrice);
                    if (price<=0){
                        T.showShort(mContext,"价格填写有误");
                        return;
                    }
                    quotation.setPrice(price);
                }else {
                    T.showShort(mContext,"价格填写有误");
                    return;
                }
                quotation.setBuyerListingId(buyerListing.getId());
                quotation.setSellerListingId(listings.get(currentPosition).getId());
                quotation.setComment(et_supply_product.getText().toString());
                XinongHttpCommend.getInstance(mContext).postQuotation(new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        T.showShort(mContext,"提交成功");
                        finish();
                    }
                },quotation);
            }
        });
        tv_goods_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, TestSelectAddressActivity.class);
                startActivityForResult(intent1, 1001);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode==1001){
                String address = data.getStringExtra("address");
                String[] addresses = address.split(" ");
                String[] idsAddress = data.getStringExtra("ids").split(",");
                tv_goods_address.setText(address);
                switch (addresses.length){
                    case 3:
                    case 2:
                        quotation.setCity(addresses[1]);
                    case 1:
                        quotation.setProvince(addresses[0]);
                        break;

                }
            }
        }
    }


    @Override
    public String setToolBarTitle() {
        return "提交报价";
    }

    @Override
    public void onItemClick(ListShowAdapter.ListViewHolder holder, int position) {
        holder.setImSelect(R.drawable.date_select);
        currentPosition = position;
        int size = listings.size();
        for (int i=0;i<size;i++){
            if (i == position){
                listings.get(i).setSelect(true);
            }else {
                listings.get(i).setSelect(false);
            }
        }
        listShowAdapter.notifyDataSetChanged();
    }
}
