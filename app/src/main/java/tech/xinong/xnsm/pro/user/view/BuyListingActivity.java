package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.BuyOrderModel;
import tech.xinong.xnsm.pro.buy.view.SelectAddressActivity;
import tech.xinong.xnsm.pro.sell.model.BuyerListingSum;
import tech.xinong.xnsm.pro.user.model.Address;
import tech.xinong.xnsm.pro.user.model.QuoteModel;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2018/1/17.
 */
@ContentView(R.layout.activity_buy_listing)
public class BuyListingActivity extends BaseActivity {
    @ViewInject(R.id.tv_seller_name)
    private TextView tv_seller_name;
    @ViewInject(R.id.im_product_img)
    private SimpleDraweeView im_product_img;
    @ViewInject(R.id.tv_product_name)
    private TextView tv_product_name;
    @ViewInject(R.id.tv_product_spec)
    private TextView tv_product_spec;
    @ViewInject(R.id.tv_address)
    private TextView tv_address;
    @ViewInject(R.id.tv_buy_condition)
    private TextView tv_buy_condition;
    @ViewInject(R.id.et_buy_condition)
    private EditText et_buy_condition;
    @ViewInject(R.id.tv_buy_address)
    private TextView tv_buy_address;
    @ViewInject(R.id.tv_buy_address_show)
    private TextView tv_buy_address_show;
    @ViewInject(R.id.tv_buy_now)
    private TextView tv_buy_now;
    @ViewInject(R.id.tv_unit_price)
    private TextView tv_unit_price;
    @ViewInject(R.id.tv_amount)
    private TextView tv_amount;
    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;
    @ViewInject(R.id.tv_change_address)
    private TextView tv_change_address;
    @ViewInject(R.id.lay_no_address)
    private LinearLayout lay_no_address;


    private QuoteModel quoteModel;
    private BuyerListingSum buyListing;
    private String coverImg;
    private List<Address> addresses;
    private Address addressSelect;
    private BuyOrderModel orderModel;

    private static final int REQ_CODE_SELECT_ADDRESS = 1001;
    private static final int REQ_CODE_ADD_ADDRESS = 1002;
    @Override
    public void initData() {
        addresses = new ArrayList<>();
        quoteModel = (QuoteModel) intent.getSerializableExtra("quote");
        buyListing = (BuyerListingSum) intent.getSerializableExtra("buyListing");
        String[] titles = buyListing.getTitle().split(" ");
        coverImg = intent.getStringExtra("coverImg");
        tv_seller_name.setText("卖家："+quoteModel.getOwnerName());
        if (!TextUtils.isEmpty(coverImg))
           im_product_img.setImageURI(ImageUtil.getImgUrl(coverImg));
        tv_product_name.setText(titles[0]);
        if (titles.length==1){
            tv_product_spec.setText("不限");
        }else {
            StringBuilder spec = new StringBuilder();
            for(int i=1;i<titles.length;i++){
               spec.append(titles[i]+" ");
            }
            tv_product_spec.setText("不限");
        }
        tv_address.setText(quoteModel.getProvince()+quoteModel.getCity());
        tv_unit_price.setText(quoteModel.getPrice()+"元/"+quoteModel.getWeightUnit().getDisplayName());
        tv_amount.setText(" X "+quoteModel.getAmount());
        tv_total_price.setText("￥"+quoteModel.getTotalPrice().doubleValue());

        XinongHttpCommend.getInstance(this).address(getCustmerId(),new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(final String info, String result) {
                addresses = JSON.parseArray(result,Address.class);
                for (Address address : addresses){
                    if (address.isPrimary()){
                        tv_buy_address_show.setText(address.getRealAddress());
                        addressSelect = address;
                    }
                }
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(tv_buy_address_show.getText().toString().trim())){
            T.showShort(mContext,"请您填写收货地址");
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){

            switch (requestCode){
                case REQ_CODE_SELECT_ADDRESS:
                    addressSelect = (Address) data.getSerializableExtra("address");
                    tv_buy_address_show.setText(addressSelect.getRealAddress());
                    break;
                case REQ_CODE_ADD_ADDRESS:
                    addressSelect = (Address) data.getSerializableExtra("address");
                    tv_buy_address_show.setText(addressSelect.getRealAddress());
                    break;
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    @OnClick({R.id.tv_buy_now,R.id.lay_no_address,R.id.tv_change_address})
    public void widgetClick(View view) {
        switch (view.getId()){
            case R.id.tv_buy_now:
                if (check()){
                    orderModel = new BuyOrderModel();
                    orderModel.setAddressId(addressSelect.getId());
                    orderModel.setBuyerMsg(et_buy_condition.getText().toString().trim());
                    orderModel.setQuotationId(quoteModel.getId());

                    XinongHttpCommend.getInstance(mContext).buyNowFromQuote(orderModel, new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext,"订单创建成功");
                            skipActivity(MyOrdersActivity.class,true);
                        }
                    });
                }
                break;
            case R.id.lay_no_address:
                Intent intent1 = new Intent(mContext,AddAddressActivity.class);
                intent1.putExtra("isBuyNow",true);
                startActivityForResult(intent1,REQ_CODE_ADD_ADDRESS);
                break;
            case R.id.tv_change_address:
                Intent intent2 = new Intent(mContext, SelectAddressActivity.class);
                startActivityForResult(intent2, REQ_CODE_SELECT_ADDRESS);
                break;
        }
    }

    @Override
    public String setToolBarTitle() {
        return "采购订单";
    }
}
