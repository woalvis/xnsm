package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.SpecModel;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_publish_sell)
public class PublishSellActivity extends BaseActivity {
    /*货品名称*/
    @ViewInject(R.id.publish_sell_goods_name)
    private TextView tvGoodsName;
    /*货品规格*/
    @ViewInject(R.id.publish_sell_goods_specification)
    private TextView tvGoodsSpecification;
    /*货品单价*/
    @ViewInject(R.id.publish_sell_goods_unit_price)
    private TextView tvUnitPrice;
    /*是否现货*/
    @ViewInject(R.id.publish_sell_goods_in_stock)
    private TextView tvInStock;
    /*供货时间*/
    @ViewInject(R.id.publish_sell_term_begin_date)
    private TextView tvtermBeginDate;
    /*供货量*/
    @ViewInject(R.id.publish_sell_total_quantity)
    private TextView tvTotalQuantity;
    /*发货地址*/
    @ViewInject(R.id.publish_sell_address)
    private TextView tvAddress;
    /*原产地*/
    @ViewInject(R.id.select_order)
    private TextView tvOrigin;
    /*物流方式*/
    @ViewInject(R.id.publish_sell_logistic_method)
    private TextView tvLogisticMethod;
    /*货品描述*/
    @ViewInject(R.id.publish_sell_goods_notes)
    private TextView tvGoodsNotes;
    /*货品照片*/
    @ViewInject(R.id.publish_sell_goods_photos)
    private TextView tvGoodsPhotos;

    private String productId;

    public static final int REQ_CODE_SELECT_SPEC = 0x1001;

    @Override
    public void initWidget() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        SpecModel spec = (SpecModel)intent.getSerializableExtra("spec");
        tvGoodsName.setText(spec.getProduct()+"  "+spec.getItem());
    }


    @OnClick(R.id.publish_sell_goods_specification)
    public void click(View view){
        switch (view.getId()){
            case R.id.publish_sell_goods_specification:
                Intent intent = new Intent(this,SelectSpecificationActivity.class);
                intent.putExtra("productId",productId);
                startActivityForResult(intent,REQ_CODE_SELECT_SPEC);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
