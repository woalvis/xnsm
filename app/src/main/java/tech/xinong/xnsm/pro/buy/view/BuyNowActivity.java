package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.BuyOrderModel;
import tech.xinong.xnsm.pro.buy.model.ListingDetailsDTO;
import tech.xinong.xnsm.pro.buy.model.OrderDetailModel;
import tech.xinong.xnsm.pro.publish.view.SelectLogisticMethodActivity;
import tech.xinong.xnsm.pro.user.model.Address;
import tech.xinong.xnsm.pro.user.model.Customer;
import tech.xinong.xnsm.pro.user.view.AddAddressActivity;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_buy_now)
public class BuyNowActivity extends BaseActivity {
    @ViewInject(R.id.tv_buyer_name)
    private TextView tv_buyer_name;//买家姓名
    @ViewInject(R.id.tv_buyer_mobile)
    private TextView tv_buyer_mobile;//买家电话
    @ViewInject(R.id.tv_receive_address)
    private TextView tv_receive_address;//买家收货地址
    @ViewInject(R.id.tv_link_name)//卖家
    private TextView tv_link_name;
    @ViewInject(R.id.img_product)
    private SimpleDraweeView img_product;
    @ViewInject(R.id.tv_product_title)
    private TextView tv_product_title;
    @ViewInject(R.id.tv_product_price)
    private TextView tv_product_price;
    @ViewInject(R.id.btn_minus)
    private TextView btn_minus;
    @ViewInject(R.id.btn_add)
    private TextView btn_add;
    @ViewInject(R.id.edit_num)
    private EditText edit_num;
    @ViewInject(R.id.service_lb)
    private TextView service_lb;
    @ViewInject(R.id.tv_service_way)
    private TextView tv_service_way;//点击选择服务方式
    @ViewInject(R.id.service_img)
    private ImageView service_img;
    @ViewInject(R.id.edit_leave_msg)
    private EditText edit_leave_msg;
    @ViewInject(R.id.tv_leave_msg_num)
    private TextView tv_leave_msg_num;
    @ViewInject(R.id.btn_commit)
    private TextView btn_commit;
    @ViewInject(R.id.text_amount)
    private TextView text_amount;
    private BuyOrderModel order;
    private int amount = 1;
    private Address defaultAddress;
    private String userId;
    private ListingDetailsDTO listingDetailsDTO;
    private static final int REQ_CODE_SELECT_LOGISTIC_METHOD = 1001;
    private static final int REQ_CODE_ADD_ADDRESS = 1002;
    private static final int REQ_CODE_SELECT_ADDRESS = 1003;
    @ViewInject(R.id.lay_no_address)
    private LinearLayout lay_no_address;
    @ViewInject(R.id.lay_has_address)
    private RelativeLayout lay_has_address;

    private BigDecimal subPrice;
    @Override
    public String setToolBarTitle() {
        return "确认订单";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        order = new BuyOrderModel();
        defaultAddress = new Address();
        userId = mSharedPreferences.getString(XnsConstant.CUSTOMERID, "");
        final Intent intent = getIntent();
        listingDetailsDTO = (ListingDetailsDTO) intent.getSerializableExtra("item");
        initPage(listingDetailsDTO);


        lay_no_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext,AddAddressActivity.class);
                intent1.putExtra("isBuyNow",true);
                startActivityForResult(intent1,REQ_CODE_ADD_ADDRESS);
            }
        });
        lay_has_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(mContext,SelectAddressActivity.class);
                intent1.putExtra("isBuyNow",true);
                startActivityForResult(intent1,REQ_CODE_SELECT_ADDRESS);
            }
        });

        XinongHttpCommend.getInstance(mContext).address(userId, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                List<Address> addresses = JSON.parseArray(result, Address.class);
                for (Address address : addresses) {
                    if (address.isPrimary()) {
                        defaultAddress = address;
                    }
                }
                if (TextUtils.isEmpty(defaultAddress.getId())) {
                    lay_has_address.setVisibility(View.GONE);
                    lay_no_address.setVisibility(View.VISIBLE);
                }else {
                    tv_buyer_name.setText("收货人:" + defaultAddress.getReceiver());
                    tv_buyer_mobile.setText(defaultAddress.getReceiverPhone());
                    tv_receive_address.setText(defaultAddress.getProvince() +
                            defaultAddress.getCity() +
                            defaultAddress.getDistrict() +
                            defaultAddress.getStreet());
                    lay_has_address.setVisibility(View.VISIBLE);
                    lay_no_address.setVisibility(View.GONE);
                }
            }
        });

        XinongHttpCommend.getInstance(mContext).getCurrentInfo(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                Customer customer = JSONObject.parseObject(result, Customer.class);
//                tv_buyer_name.setText("收货人:" + customer.getFullName());
//                tv_buyer_mobile.setText(defaultAddress.getReceiverPhone());
            }
        });
    }

    private void initPage(ListingDetailsDTO listingDetailsDTO) {
        amount = listingDetailsDTO.getMinQuantity().intValue();
        edit_num.setText(amount+ "");
        edit_num.addTextChangedListener(new MyTextWatcher());
        subPrice = listingDetailsDTO.getUnitPrice().multiply(new BigDecimal(amount));
        text_amount.setText("¥" + subPrice);

        tv_link_name.setText("卖家：" + listingDetailsDTO.getSellerName());
        if (!TextUtils.isEmpty(listingDetailsDTO.getCoverImg())) {
            String imageUrl = ImageUtil.getImgUrl(listingDetailsDTO.getCoverImg());
            img_product.setImageURI(ImageUtil.getImgUrl(imageUrl));
        } else {
            XinongHttpCommend.getInstance(mContext).getProductImg(new AbsXnHttpCallback(mContext) {
                @Override
                public void onSuccess(String info, String result) {
                    img_product.setImageURI(ImageUtil.getProductImg(result));
                }
            }, listingDetailsDTO.id);
        }
        tv_product_title.setText(listingDetailsDTO.getTitle());
        String productPrice = listingDetailsDTO.getUnitPrice() + "元/" + listingDetailsDTO.getWeightUnit().getDisplayName();
        tv_product_price.setText(productPrice);

    }

    @Override
    @OnClick({R.id.btn_minus, R.id.btn_add, R.id.btn_commit, R.id.tv_service_way})
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                amount++;
                edit_num.setText(amount + "");
                break;
            case R.id.btn_minus:
                if (amount > 1) {
                    amount--;
                    edit_num.setText(amount + "");
                }
                break;
            case R.id.btn_commit:
                if (check()) {
                    XinongHttpCommend.getInstance(mContext).buyNow(order, new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext, "下单成功");
                            XinongHttpCommend.getInstance(mContext)
                                    .getOrderDetailById(result, new AbsXnHttpCallback(mContext) {
                                        @Override
                                        public void onSuccess(String info, String result) {
                                            OrderDetailModel orderDetailModel = JSON.parseObject(result,OrderDetailModel.class);
                                            Intent intent = new Intent(mContext,OrderDetailActivity.class);
                                            intent.putExtra("orderId",orderDetailModel.getId());
                                            intent.putExtra("orderNo", orderDetailModel.getOrderNo()+"");
                                            intent.putExtra("isSeller",false);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });



                        }
                    });
                }

                break;
            case R.id.tv_service_way:
                Intent intentSelectLogisticMethod = new Intent(mContext, SelectLogisticMethodActivity.class);
                startActivityForResult(intentSelectLogisticMethod, REQ_CODE_SELECT_LOGISTIC_METHOD);
                break;
        }
    }

    private boolean check() {
        order.setAmount(Integer.parseInt(edit_num.getText().toString()));
        if (TextUtils.isEmpty(defaultAddress.getId())){
            T.showShort(mContext,"地址不能为空");
            return false;
        }else {
            order.setAddressId(defaultAddress.getId());
        }
        if (subPrice.compareTo(new BigDecimal(100000000))>=0){
            T.showShort(mContext,"订单总额不能超过一个亿");
            return false;
        }

        order.setBuyerMsg(edit_leave_msg.getText().toString());
        order.setListingId(listingDetailsDTO.getId());
        return true;
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(edit_num.getText().toString())) {

                amount = Integer.parseInt(edit_num.getText().toString());
                BigDecimal num = new BigDecimal(amount);
                BigDecimal subPrice = listingDetailsDTO.getUnitPrice().multiply(num);

                text_amount.setText("¥" + subPrice);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_SELECT_LOGISTIC_METHOD:
                    String logisticMethods = data.getStringExtra("result");
                    tv_service_way.setText(logisticMethods);
                    order.setProvideSupport(logisticMethods);
                    break;
                case REQ_CODE_ADD_ADDRESS:
                    Address address = (Address) data.getSerializableExtra("address");
                    defaultAddress = address;
                    lay_has_address.setVisibility(View.VISIBLE);
                    lay_no_address.setVisibility(View.GONE);
                    tv_buyer_name.setText("收货人:" + address.getReceiver());
                    tv_buyer_mobile.setText(address.getReceiverPhone());
                    tv_receive_address.setText(address.getProvince() +
                            address.getCity() +
                            address.getDistrict() +
                            address.getStreet());
                    break;
                case REQ_CODE_SELECT_ADDRESS:
                    Address addressSelect = (Address) data.getSerializableExtra("address");
                    defaultAddress = addressSelect;
                    lay_has_address.setVisibility(View.VISIBLE);
                    lay_no_address.setVisibility(View.GONE);
                    tv_buyer_name.setText("收货人:" + defaultAddress.getReceiver());
                    tv_buyer_mobile.setText(defaultAddress.getReceiverPhone());
                    tv_receive_address.setText(defaultAddress.getProvince() +
                            defaultAddress.getCity() +
                            defaultAddress.getDistrict() +
                            defaultAddress.getStreet());
                    break;
            }
        }
    }


    //    @ViewInject(R.id.buynow_seller_name)
//    private TextView sellerName;
//    @ViewInject(R.id.buynow_variety)
//    private TextView variety;
//    @ViewInject(R.id.buynow_specs)
//    private TextView specs;
//    @ViewInject(R.id.buynow_address)
//    private TextView address;
//    @ViewInject(R.id.buynow_unit_price)
//    private TextView unitPrice;
//    @ViewInject(R.id.buynow_et_num)
//    private EditText etNum;
//    @ViewInject(R.id.buynow_tv_sub_money)
//    private TextView tvSubMoney;
//    @ViewInject(R.id.buynow_buyer_require)
//    private EditText buyerRequire;
//    @ViewInject(R.id.buynow_et_address)
//    private EditText etAddress;
//    @ViewInject(R.id.buynow_logistic)
//    private LinearLayout logistic;
//    @ViewInject(R.id.buynow_buynow)
//    private Button btBuyNow;
//    private String logsticMethodStr;
//
//
//
//    @Override
//    public void initData() {
//
//        Intent intent = getIntent();
//        final ListingDetailsDTO listingDetailsDTO = (ListingDetailsDTO) intent.getSerializableExtra("item");
//        initPage(listingDetailsDTO);
//        btBuyNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                buyNow(listingDetailsDTO);
//            }
//        });
//        logsticMethodStr = "";
//    }
//
//    private void buyNow(ListingDetailsDTO publishSellInfoModel) {
//        BuyOrderModel buyOrder = new BuyOrderModel();
//        buyOrder.setAddress(etAddress.getText().toString());
//        buyOrder.setAmount(Integer.parseInt(etNum.getText().toString()));
//        buyOrder.setBuyerRequire(buyerRequire.getText().toString());
//        buyOrder.setLogisticMethodTag(logsticMethodStr);
//        BaseBean id = new BaseBean();
//        id.setId(publishSellInfoModel.getId());
//        buyOrder.setSellerListing(id);
//        showProgress();
//        XinongHttpCommend.getInstance(mContext).buyNow(buyOrder, new AbsXnHttpCallback(mContext) {
//            @Override
//            public void onSuccess(String info, String result) {
//                cancelProgress();
//                T.showShort(mContext, "下单成功");
//                Intent intent = new Intent(mContext, OrderDetailActivity.class);
//                intent.putExtra("orderId",result);
//                startActivity(intent);
//                BuyNowActivity.this.finish();
//            }
//        });
//    }
//
//
//    private void initPage(final ListingDetailsDTO p) {
//
//        sellerName.setText(p.getSellerName());
//        String varietyStr = "";
//        String specsStr = "";
////        for (SpecModel specModel:p.getSpecificationConfigs()){
////            if (specModel.getCategory()=="VARIETY"){
////                varietyStr = specModel.getItem();
////            }else {
////                specsStr+=specModel.getItem()+"/";
////            }
////        }
//        variety.setText(varietyStr);
//        specs.setText(specsStr);
//        address.setText(p.getAddress());
//
//        unitPrice.setText(p.getUnitPrice()+"元/斤  × ");
//        etNum.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!TextUtils.isEmpty(etNum.getText().toString())) {
//                    BigDecimal num = new BigDecimal(Double.parseDouble(etNum.getText().toString()));
//                    BigDecimal subPrice = p.getUnitPrice().multiply(num);
//                    tvSubMoney.setText("¥"+subPrice);
//                    tvSubMoney.setTextColor(Color.rgb(248,147,29));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//
//        String[] logisticMethods =  p.getProvideSupport().split(",");
//
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(20, 10, 20, 10);
//        lp.gravity = Gravity.CENTER_VERTICAL;
//
//
//        for (final String logisticMethod : logisticMethods){
//            final BorderTextView textView = new BorderTextView(this);
//            textView.setText(logisticMethod);
//            textView.setPadding(10,10,10,10);
//            textView.setLeft(20);
//            textView.setLayoutParams(lp);
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    int a = logistic.getChildCount();
//                    for (int i = 0; i < a; i++) {
//                        TextView tv = (TextView) logistic.getChildAt(i);
//                        tv.setTextColor(mContext.getResources().getColor(R.color.black));
//                    }
//
//
//
//                    String textViewStr = textView.getText().toString();
//                    if (logsticMethodStr.contains(textViewStr)){
//                        textView.setTextColor(Color.BLACK);
//                        logsticMethodStr = "";
//                    }else {
//                        textView.setTextColor(Color.GREEN);
//                        logsticMethodStr = textViewStr;
//                    }
//
//
//
//                }
//            });
//            logistic.addView(textView);
//        }
//
//    }
}
