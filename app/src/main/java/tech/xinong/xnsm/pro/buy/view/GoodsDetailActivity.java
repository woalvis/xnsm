package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.publish.model.PublishInfoModel;

public class GoodsDetailActivity extends BaseActivity {
    private TextView productDescription;//产品描述
    private TextView productNum;

    @Override
    protected int bindView() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void initWidget() {
        productDescription = (TextView) findViewById(R.id.product_description);
        productNum = (TextView)findViewById(R.id.product_num);

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        PublishInfoModel publishInfoModel = (PublishInfoModel) intent.getSerializableExtra("detail");
        Log.e("xx",publishInfoModel.toString());
        productDescription.setText(publishInfoModel.getSpecification());
        productNum.setText(publishInfoModel.getMaxQuantity()+"斤现货");

        XinongHttpCommend.getInstence(this).getProductListings(publishInfoModel.getId(), new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {
                Log.e("xx",result);
            }
        });
    }
}
