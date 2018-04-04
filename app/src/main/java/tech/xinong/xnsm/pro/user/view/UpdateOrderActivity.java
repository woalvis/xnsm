package tech.xinong.xnsm.pro.user.view;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2018/1/15.
 */

@ContentView(R.layout.activity_update_order)
public class UpdateOrderActivity extends BaseActivity{
    @ViewInject(R.id.tv_price)
    private TextView tv_price;
    @ViewInject(R.id.et_freight)
    private EditText et_freight;
    @ViewInject(R.id.et_cheapen)
    private EditText et_cheapen;
    @ViewInject(R.id.et_cheapen_off_sale)
    private EditText et_cheapen_off_sale;
    @ViewInject(R.id.tv_xn_fee_rate)
    private TextView tv_xn_fee_rate;
    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;
    @ViewInject(R.id.tv_sub)
    private TextView tv_sub;
    private TextWatcher tw1, tw2;
    private double price;
    private double totalPrice;
    private double feeRate;
    private String orderId;
    double freight = 0;
    double cheapen = 0;
    double xnFeeRate = 0;



    @Override
    public void initData() {
        price = Double.parseDouble(intent.getStringExtra("price"));
        orderId = intent.getStringExtra("orderId");
        totalPrice = price;
        tv_price.setText(price+"元");
        tv_submit.setText("完成（合计:￥"+getTotalPrice()+"元）");
        setEtListener();
    }

    @Override
    public String setToolBarTitle() {
        return "修改订单";
    }




    private void setEtListener(){
       XinongHttpCommend.getInstance(mContext).feeFate(new AbsXnHttpCallback(mContext) {
           @Override
           public void onSuccess(String info, String result) {
                feeRate = Double.parseDouble(result);
                tv_xn_fee_rate.setText("平台费用（"+feeRate*100+"%）： "+totalPrice*feeRate+"元");
           }
       });


        et_freight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_submit.setText("完成（合计:￥"+getTotalPrice()+"元）");
                tv_xn_fee_rate.setText("平台费用（"+feeRate*100+"%）： "+getXnFeeRate()+"元");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        tw1 = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                et_cheapen_off_sale.removeTextChangedListener(tw2);
                int et1val = 0;
                try {
                    et1val =Integer.parseInt(s.toString());
                    if(et1val>price){
                        et1val = (int) price;
                        et_cheapen.setText(et1val+"");
                    }
                    tv_sub.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    et1val = 0;
                    tv_sub.setVisibility(View.GONE);
                }

                int et2val = (int) (et1val/price*100);
                et_cheapen_off_sale.setText(et2val+"");

                tv_submit.setText("完成（合计:￥"+getTotalPrice()+"元）");
                tv_xn_fee_rate.setText("平台费用（"+feeRate*100+"%）： "+getXnFeeRate()+"元");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_cheapen_off_sale.addTextChangedListener(tw2);
            }
        };

        tw2 = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                tv_submit.setText("完成（合计:￥"+getTotalPrice()+"元）");
                et_cheapen.removeTextChangedListener(tw1);
                int et2val = 0;
                try {
                    et2val = Integer.parseInt(s.toString());
                } catch (Exception ex) {
                    et2val = 0;
                }
                Log.i("eee2", ""+et2val);


                int et1val = (int) (et2val * price/100);
                et_cheapen.setText("" + et1val);

                tv_submit.setText("完成（合计:￥"+getTotalPrice()+"元）");
                tv_xn_fee_rate.setText("平台费用（"+feeRate*100+"%）： "+getXnFeeRate()+"元");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                et_cheapen.addTextChangedListener(tw1);
            }
        };

        et_cheapen.addTextChangedListener(tw1);
        et_cheapen_off_sale.addTextChangedListener(tw2);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTotalPrice()<=0){
                    T.showShort(mContext,"订单总额不能小于0");
                    return;
                }

                XinongHttpCommend.getInstance(mContext).updateOrder(
                        orderId,
                        cheapen + "",
                        freight + "",
                        new AbsXnHttpCallback(mContext) {
                            @Override
                            public void onSuccess(String info, String result) {
                                T.showShort(mContext,"修改成功");
                                finish();
                            }
                        }

                );
            }
        });

    }



    private double getTotalPrice(){

        if (!TextUtils.isEmpty(et_freight.getText().toString().trim())){
            freight = Double.parseDouble(et_freight.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(et_cheapen.getText().toString().trim())){
            cheapen = Double.parseDouble(et_cheapen.getText().toString().trim());
        }
        totalPrice = price + freight - cheapen;

        return totalPrice;
    }

    private double getXnFeeRate(){
        return getTotalPrice()*feeRate;
    }
}
