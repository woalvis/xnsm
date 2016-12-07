package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.BaseBean;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.SpecModel;
import tech.xinong.xnsm.pro.publish.model.PublishSellInfoModel;
import tech.xinong.xnsm.util.NumUtil;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_publish_sell)
public class PublishSellActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
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
    @ViewInject(R.id.publish_sell_rg_in_stock)
    private RadioGroup rgInstock;
    /*供货时间*/
    @ViewInject(R.id.publish_sell_term_begin_date)
    private TextView tvTermBeginDate;
    /*供货量*/
    @ViewInject(R.id.publish_sell_total_quantity)
    private TextView tvTotalQuantity;
    @ViewInject(R.id.publish_sell_et_total_quantity)//供货量的编辑框
    private EditText etTotalQuantity;
    /*发货地址*/
    @ViewInject(R.id.publish_sell_address)
    private TextView tvAddress;
    /*原产地*/
    @ViewInject(R.id.publish_sell_origin)
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
    @ViewInject(R.id.publish_sell_submit)
    private Button publishSellSubmit;
    @ViewInject(R.id.publish_sell_rb_yes)
    private RadioButton inStock;
    @ViewInject(R.id.publish_sell_brokerallowed_yes)
    private RadioButton brokerAllowed;
    @ViewInject(R.id.publish_sell_brokerallowed_no)
    private RadioButton noBrokerAllowed;
    @ViewInject(R.id.publish_sell_rg_broker_allowed)
    private RadioGroup rgBrokerAllowed;

    private String productId;
    private String id;
    private PublishSellInfoModel sellInfo;


    /**
     * 请求码
     */
    public static final int REQ_CODE_SELECT_SPEC = 0x1001;
    public static final int REQ_CODE_SELECT_UNIT_PRICE = 0x1002;
    public static final int REQ_CODE_SELECT_TERM_TIME = 0x1003;
    public static final int REQ_CODE_SELECT_ADDRESS = 0x1004;
    public static final int REQ_CODE_SELECT_ORIGIN = 0x1005;
    public static final int REQ_CODE_SELECT_OGISTIC_METHOD = 0x1006;
    public static final int REQ_CODE_EDIT_GOODS_NOTE = 0x1007;




    @Override
    public void initWidget() {
        rgInstock.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tvTermBeginDate.setText(getResources().getString(R.string.publish_sell_edit_term_time));
                tvTermBeginDate.setVisibility(View.VISIBLE);
                tvTermBeginDate.setClickable(true);
                tvTermBeginDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                tvTermBeginDate.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intentSelectTermTime = new Intent(mContext, SelectTermTimeActivity.class);
                        startActivityForResult(intentSelectTermTime, REQ_CODE_SELECT_TERM_TIME);
                    }
                });
            }
        });

        /*为编辑供货量的editText添加监听*/
        etTotalQuantity.addTextChangedListener(myTextWatcher);
        brokerAllowed.setOnCheckedChangeListener(this);
        noBrokerAllowed.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        productId = intent.getStringExtra("productId");
        SpecModel spec = (SpecModel) intent.getSerializableExtra("spec");
        tvGoodsName.setText(spec.getProduct() + "  " + spec.getItem());
        sellInfo = new PublishSellInfoModel();
        BaseBean baseBean = new BaseBean();
        baseBean.setId(id);
        sellInfo.setProductId(baseBean);
    }


    @OnClick({R.id.publish_sell_goods_specification, R.id.publish_sell_goods_unit_price,
            R.id.publish_sell_origin,R.id.publish_sell_logistic_method
    ,R.id.publish_sell_goods_notes,R.id.publish_sell_submit})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.publish_sell_goods_specification:
                Intent intentSelectSpec = new Intent(this, SelectSpecificationActivity.class);
                intentSelectSpec.putExtra("productId", productId);
                startActivityForResult(intentSelectSpec, REQ_CODE_SELECT_SPEC);
                break;
            case R.id.publish_sell_goods_unit_price:
                Intent intentSelectPrice = new Intent(mContext, SelectUnitPriceActivity.class);
                startActivityForResult(intentSelectPrice, REQ_CODE_SELECT_UNIT_PRICE);
                break;
            case R.id.publish_sell_origin:
                Intent intentSelectOrigin = new Intent(mContext,SelectAddressActivity.class);
                startActivityForResult(intentSelectOrigin,REQ_CODE_SELECT_ORIGIN);
                break;
            case R.id.publish_sell_logistic_method:
                Intent intentSelectLogisticMethod = new Intent(mContext,SelectLogisticMethodActivity.class);
                startActivityForResult(intentSelectLogisticMethod,REQ_CODE_SELECT_OGISTIC_METHOD);
                tvGoodsNotes.setVisibility(View.VISIBLE);
                break;
            case R.id.publish_sell_goods_notes:
                Intent intentEditGoodsNote = new Intent(mContext,EditGoodsNoteActivity.class);
                startActivityForResult(intentEditGoodsNote,REQ_CODE_EDIT_GOODS_NOTE);
                break;
            case R.id.publish_sell_submit:
                publishSellSubmit();
                break;
        }
    }

    private void publishSellSubmit() {
        sellInfo.setTotalQuantity(Integer.parseInt(etTotalQuantity.getText().toString()));
        sellInfo.setInStock(inStock.isChecked());
        sellInfo.setBrokerAllowed(brokerAllowed.isChecked());
        XinongHttpCommend.getInstence(this).pulishSellInfo(sellInfo, new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {


                Toast.makeText(mContext, info, Toast.LENGTH_SHORT).show();
                PublishSellActivity.this.finish();
            }

            @Override
            public void onError(String info) {
                super.onError(info);
                Log.d("xx",info);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQ_CODE_SELECT_SPEC:
                    /*从选择的Activity中得到用户选择的结果*/
                    tvGoodsSpecification.setText(data.getStringExtra("result"));
                    String ids = data.getStringExtra("ids");
                    /*将用户选择的结果的id封装进数据模型*/
                    sellInfo.setSpecificationConfigsForids(ids);
                    tvGoodsSpecification.setTextColor(Color.BLACK);
                    tvUnitPrice.setText("点这里填写价格");
                    tvUnitPrice.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tvUnitPrice.setClickable(true);
                    break;
                case REQ_CODE_SELECT_UNIT_PRICE:
                    String unitPrice = data.getStringExtra("unitPrice");
                    String minQuantity = data.getStringExtra("minQuantity");
                    sellInfo.setUnitPrice(new BigDecimal(unitPrice));
                    sellInfo.setMinQuantity(Integer.parseInt(minQuantity));
                    sellInfo.setQuantityUnit("JIN");
                    tvUnitPrice.setText(unitPrice+"元/斤"+"   "+minQuantity+"斤起");


                    tvUnitPrice.setTextColor(Color.BLACK);
                    rgInstock.setVisibility(View.VISIBLE);
                    break;
                case REQ_CODE_SELECT_TERM_TIME:
                    String beginDate = data.getStringExtra("beginDate");
                    String endDate = data.getStringExtra("endDate");

                    tvTermBeginDate.setText("上市时间："+beginDate+"\n下市时间："+endDate);

                    sellInfo.setTermBeginDate(beginDate);
                    sellInfo.setTermEndDate(endDate);

                    etTotalQuantity.setVisibility(View.VISIBLE);
                    break;
                case REQ_CODE_SELECT_ADDRESS:
                    String address = data.getStringExtra("address");
                    tvAddress.setText(address);
                    sellInfo.setAddress(address.replace(" ", ""));
                    tvOrigin.setVisibility(View.VISIBLE);
                    break;
                case REQ_CODE_SELECT_ORIGIN:
                    String originAddress = data.getStringExtra("address");
                    tvOrigin.setText(originAddress);
                    sellInfo.setOrigin(originAddress.replace(" ",""));
                    tvLogisticMethod.setVisibility(View.VISIBLE);
                    break;
                case REQ_CODE_SELECT_OGISTIC_METHOD:
                    String logisticMethods = data.getStringExtra("result");
                    sellInfo.setLogisticMethodTags(logisticMethods);
                    tvLogisticMethod.setText(logisticMethods);
                    tvGoodsNotes.setVisibility(View.VISIBLE);
                    break;
                case REQ_CODE_EDIT_GOODS_NOTE:
                    String goodsNote = data.getStringExtra("result");
                    tvGoodsNotes.setText(goodsNote);
                    sellInfo.setNotes(goodsNote);
                    rgBrokerAllowed.setVisibility(View.VISIBLE);
                    break;

            }

        }
    }

   TextWatcher myTextWatcher =   new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (NumUtil.isPositiveNumber(etTotalQuantity.getText().toString())>=0){
                tvAddress.setVisibility(View.VISIBLE);
                tvAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,SelectAddressActivity.class);
                        startActivityForResult(intent,REQ_CODE_SELECT_ADDRESS);
                    }
                });
            }else {
                tvAddress.setVisibility(View.INVISIBLE);
                Toast.makeText(mContext, "请输入正确的数字", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (publishSellSubmit.getVisibility()==View.GONE)
        publishSellSubmit.setVisibility(View.VISIBLE);
    }
}
