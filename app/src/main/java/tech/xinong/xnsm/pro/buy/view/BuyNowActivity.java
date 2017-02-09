package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.BaseBean;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.BuyOrderModel;
import tech.xinong.xnsm.pro.buy.model.SpecModel;
import tech.xinong.xnsm.pro.publish.model.PublishSellInfoModel;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.BorderTextView;

@ContentView(R.layout.activity_buy_now)
public class BuyNowActivity extends BaseActivity {
    @ViewInject(R.id.buynow_seller_name)
    private TextView sellerName;
    @ViewInject(R.id.buynow_variety)
    private TextView variety;
    @ViewInject(R.id.buynow_specs)
    private TextView specs;
    @ViewInject(R.id.buynow_address)
    private TextView address;
    @ViewInject(R.id.buynow_unit_price)
    private TextView unitPrice;
    @ViewInject(R.id.buynow_et_num)
    private EditText etNum;
    @ViewInject(R.id.buynow_tv_sub_money)
    private TextView tvSubMoney;
    @ViewInject(R.id.buynow_buyer_require)
    private EditText buyerRequire;
    @ViewInject(R.id.buynow_et_address)
    private EditText etAddress;
    @ViewInject(R.id.buynow_logistic)
    private LinearLayout logistic;
    @ViewInject(R.id.buynow_buynow)
    private Button btBuynow;

    private String logsticMethodStr;



    @Override
    public void initData() {

        Intent intent = getIntent();
        final PublishSellInfoModel publishSellInfoModel = (PublishSellInfoModel) intent.getSerializableExtra("item");
        initPage(publishSellInfoModel);
        btBuynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buynow(publishSellInfoModel);
            }
        });
        logsticMethodStr = "";
    }

    private void buynow(PublishSellInfoModel publishSellInfoModel) {
        BuyOrderModel buyOrder = new BuyOrderModel();
        buyOrder.setAddress(etAddress.getText().toString());
        buyOrder.setAmount(Integer.parseInt(etNum.getText().toString()));
        buyOrder.setBuyerRequire(buyerRequire.getText().toString());
        buyOrder.setLogisticMethodTag(logsticMethodStr);
        BaseBean id = new BaseBean();
        id.setId(publishSellInfoModel.getId());
        buyOrder.setSellerListing(id);
        XinongHttpCommend.getInstance(mContext).buyNow(buyOrder, new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                T.showShort(mContext, "下单成功");
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("orderId",result);
                startActivity(intent);
                BuyNowActivity.this.finish();
            }
        });
    }


    private void initPage(final PublishSellInfoModel p) {

        sellerName.setText(p.getOwnerFullName());
        String varietyStr = "";
        String specsStr = "";
        for (SpecModel specModel:p.getSpecificationConfigs()){
            if (specModel.getCategory()=="VARIETY"){
                varietyStr = specModel.getItem();
            }else {
                specsStr+=specModel.getItem()+"/";
            }
        }
        variety.setText(varietyStr);
        specs.setText(specsStr);
        address.setText(p.getAddress());

        unitPrice.setText(p.getUnitPrice()+"元/斤  × ");
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etNum.getText().toString())) {
                    BigDecimal num = new BigDecimal(Double.parseDouble(etNum.getText().toString()));
                    BigDecimal subPrice = p.getUnitPrice().multiply(num);
                    tvSubMoney.setText("¥"+subPrice);
                    tvSubMoney.setTextColor(Color.rgb(248,147,29));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        String[] logisticMethods =  p.getLogisticMethodTags().split(",");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 10, 20, 10);
        lp.gravity = Gravity.CENTER_VERTICAL;


        for (final String logisticMethod : logisticMethods){
            final BorderTextView textView = new BorderTextView(this);
            textView.setText(logisticMethod);
            textView.setPadding(10,10,10,10);
            textView.setLeft(20);
            textView.setLayoutParams(lp);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int a = logistic.getChildCount();
                    for (int i = 0; i < a; i++) {
                        TextView tv = (TextView) logistic.getChildAt(i);
                        tv.setTextColor(mContext.getResources().getColor(R.color.black));
                    }



                    String textViewStr = textView.getText().toString();
                    if (logsticMethodStr.contains(textViewStr)){
                        textView.setTextColor(Color.BLACK);
                        logsticMethodStr = "";
                    }else {
                        textView.setTextColor(Color.GREEN);
                        logsticMethodStr = textViewStr;
                    }



                }
            });
            logistic.addView(textView);
        }

    }
}
