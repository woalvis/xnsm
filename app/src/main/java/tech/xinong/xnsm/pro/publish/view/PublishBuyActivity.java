package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zyyoona7.lib.EasyPopup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.BaseBean;
import tech.xinong.xnsm.pro.base.model.WeightUnit;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.pro.buy.view.SpecActivity;
import tech.xinong.xnsm.pro.publish.model.PublishBuyInfoModel;
import tech.xinong.xnsm.pro.sell.model.BuyerListing;
import tech.xinong.xnsm.util.DeviceInfoUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.ImageTextView;

/**
 * Created by xiao on 2017/11/15.
 */
@ContentView(R.layout.activity_publish_buy)
public class PublishBuyActivity extends BaseActivity {

    private String productId;
    private String productName;
    @ViewInject(R.id.sl_layout)
    private ScrollView sl_layout;
    @ViewInject(R.id.publish_buy_goods)
    private TextView publish_buy_goods;
    @ViewInject(R.id.publish_buy_spec)
    private TextView publish_buy_spec;
    @ViewInject(R.id.publish_buy_specification)
    private TextView publish_buy_specification;
    @ViewInject(R.id.publish_buy_price)
    private TextView publish_buy_price;
    @ViewInject(R.id.publish_buy_origin)
    private TextView publish_buy_origin;
    @ViewInject(R.id.publish_buy_et_amount)
    private EditText publish_buy_et_amount;
    @ViewInject(R.id.publish_buy_notes)
    private EditText publish_buy_notes;
    @ViewInject(R.id.tv_unit)
    private TextView tv_unit;
    @ViewInject(R.id.unit)
    private ImageTextView unit;
    @ViewInject(R.id.btn_submit)
    private Button btn_submit;
    @ViewInject(R.id.et_min_price)
    private EditText et_min_price;
    @ViewInject(R.id.et_max_price)
    private EditText et_max_price;
    @ViewInject(R.id.publish_buy_address)
    private TextView publish_buy_address;
    @ViewInject(R.id.is_freight)
    private RadioButton is_freight;
    @ViewInject(R.id.no_freight)
    private RadioButton no_freight;
    //    @ViewInject(R.id.publish_buy_tel)
//    private TextView publish_buy_tel;
    @ViewInject(R.id.publish_buy_contract)
    private EditText publish_buy_contract;
    @ViewInject(R.id.ll_buy_time)
    private LinearLayout ll_buy_time;
    @ViewInject(R.id.publish_buy_duration)
    private TextView publish_buy_duration;
    @ViewInject(R.id.period)
    private ImageTextView period;
    public static final int REQ_CODE_SELECT_SPEC = 0x1001;//选择品类
    public static final int REQ_CODE_SELECT_SPECIFICATION = 0x1002;//选择规格
    public static final int REQ_CODE_SELECT_UNIT_PRICE = 0x1003;//选择单价
    public static final int REQ_CODE_SELECT_TERM_TIME = 0x1004;//选择时间
    public static final int REQ_CODE_SELECT_ADDRESS = 0x1005;//选择发货地址
    public static final int REQ_CODE_SELECT_ORIGIN = 0x1006;//选择货源地址
    public static final int REQ_CODE_SELECT_LOGISTIC_METHOD = 0x1007;//选择运输方法
    public static final int REQ_CODE_EDIT_GOODS_NOTE = 0x1008;//填写货物说明
    public static final int REQ_CODE_SELECT_PHOTOS = 0x1009;//选择图片

    private PublishBuyInfoModel publishBuyInfoModel;
    private EasyPopup mPopup;
    private String unitStr;
    private String periodStr;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String[] contents = "7天,三个月,六个月".split(",");
    private int[] times = {7, 90, 180};
    private boolean isPublish = true;
    private BuyerListing dto;

    private String listingId;

    @Override
    public void initWidget() {
        super.initWidget();
        publishBuyInfoModel = new PublishBuyInfoModel();
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        if (TextUtils.isEmpty(productId)) {
            listingId = intent.getStringExtra("updateId");
            isPublish = false;
            initUpdate();
        }
        productName = intent.getStringExtra("productName");
        publishBuyInfoModel.setProductId(productId);
        publish_buy_goods.setText(productName);
    }

    private void initUpdate() {
        showProgress();
        XinongHttpCommend.getInstance(this).getBuyListings(listingId, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                cancelProgress();
                dto = JSON.parseObject(result, BuyerListing.class);
                productId = dto.getProduct().getId();
                initPage(dto);
            }
        });
    }

    private void initPage(BuyerListing dto) {

        publish_buy_goods.setText(dto.getProduct().getName());
        if (TextUtils.isEmpty(dto.getProductSpec().getName())) {
            publish_buy_spec.setText("不限");
        } else {
            publish_buy_spec.setText(dto.getProductSpec().getName());
        }

        StringBuilder str = new StringBuilder();
        for (SpecificationConfigDTO specConfig : dto.getSpecConfigs()) {
            str.append(specConfig.getItem() + " ");
        }
        if (TextUtils.isEmpty(str.toString())) {
            publish_buy_specification.setText("不限");
        } else {
            publish_buy_specification.setText(str.toString());
        }
        publish_buy_et_amount.setText(dto.getAmount() + "");
        et_min_price.setText(dto.getMinPrice() + "");
        et_max_price.setText(dto.getMaxPrice() + "");
        period.setText(dto.getPeriod());
        publish_buy_origin.setText(dto.getOriginProvince() + dto.getOriginCity());
        publish_buy_notes.setText(dto.getNotes());
        publish_buy_address.setText(dto.getProvince() + dto.getCity());
        publish_buy_contract.setText(dto.getBuyer().getFullName());
        unit.setText(dto.getWeightUnit().getDisplayName());
        is_freight.setChecked(dto.getFreeShipping());
        no_freight.setChecked(!dto.getFreeShipping());
        //publish_buy_duration.setText(dto.getListingEnd().toString());
    }

    @OnClick({R.id.publish_buy_spec,
            R.id.publish_buy_specification,
            R.id.publish_buy_price,
            R.id.publish_buy_origin,
            R.id.unit,
            R.id.publish_buy_address,
            R.id.btn_submit,
            R.id.period,
            R.id.ll_buy_time
    })
    public void weigetClick(View view) {
        switch (view.getId()) {
            case R.id.publish_buy_spec:
                Intent intentSelectSpec = new Intent(this, SpecActivity.class);
                intentSelectSpec.putExtra("productId", productId);
                startActivityForResult(intentSelectSpec, REQ_CODE_SELECT_SPEC);
                break;

            case R.id.publish_buy_specification:
                Intent intentSelectSpecification = new Intent(this, SelectSpecificationActivity.class);
                intentSelectSpecification.putExtra("productId", productId);
                startActivityForResult(intentSelectSpecification, REQ_CODE_SELECT_SPECIFICATION);
                break;
            case R.id.publish_buy_price:

                break;
            case R.id.publish_buy_origin:
                Intent intent = new Intent(mContext, TestSelectAddressActivity.class);
                startActivityForResult(intent, REQ_CODE_SELECT_ORIGIN);
                break;
            case R.id.unit:
                selectUnit(view);
                break;
            case R.id.publish_buy_address:
                Intent intent1 = new Intent(mContext, TestSelectAddressActivity.class);
                startActivityForResult(intent1, REQ_CODE_SELECT_ADDRESS);
                break;
            case R.id.btn_submit://提交按钮
                if (check()) {
                    publishBuyInfoModel.setFreeShipping(is_freight.isChecked());
                    if (isPublish) {
                        submit();
                    } else {
                        XinongHttpCommend.getInstance(mContext).updatePublishBuy(listingId, publishBuyInfoModel, new AbsXnHttpCallback(mContext) {
                            @Override
                            public void onSuccess(String info, String result) {
                                finish();
                            }
                        });
                    }

                }
                break;
            case R.id.period:
                setPeriod();
                break;
            case R.id.ll_buy_time:

                showDialogButton("采购时长", "7天,三个月,六个月", new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        publish_buy_duration.setText(contents[position]);
                        long timeLong = System.currentTimeMillis() + (long) 3600 * 1000 * 24 * times[position];
                        String endDateString = sdf.format(new Date(timeLong));
                        publishBuyInfoModel.setListingEnd(endDateString);
                    }
                });
                break;
        }
    }


    private void submit() {
        if (publishBuyInfoModel.getWeightUnit()==null){
            publishBuyInfoModel.setWeightUnit(WeightUnit.JIN);
        }
        XinongHttpCommend.getInstance(mContext).publishBuy(publishBuyInfoModel, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {

                finish();
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(publish_buy_et_amount.getText().toString())) {
            T.showShort(mContext, "请您填写需求量");
        } else {
            publishBuyInfoModel.setAmount(Integer.parseInt(publish_buy_et_amount.getText().toString()));
        }
        if (TextUtils.isEmpty(et_min_price.getText().toString())) {
            T.showShort(mContext, "请您填写期望最低价格");
        } else {
            publishBuyInfoModel.setMinPrice(Double.parseDouble(et_min_price.getText().toString()));
        }
        if (TextUtils.isEmpty(et_max_price.getText().toString())) {
            T.showShort(mContext, "请您填写期望最高价格");
        } else {
            publishBuyInfoModel.setMaxPrice(Double.parseDouble(et_max_price.getText().toString()));
        }
        publishBuyInfoModel.setNotes(publish_buy_notes.getText().toString());
        publishBuyInfoModel.setPeriod(period.getText().toString());
        return true;
    }


    @Override
    public String setToolBarTitle() {
        return "发布采购";
    }

    @Override
    public void initData() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQ_CODE_SELECT_SPEC:
                    publishBuyInfoModel.setProductSpecId(data.getStringExtra("id"));
                    String specName = data.getStringExtra("spec");
                    publish_buy_spec.setText(specName);
                    publishBuyInfoModel.setTitle(productName + " " + specName);
                    publish_buy_spec.setTextColor(getResources().getColor(R.color.colorPrimary));
                    publish_buy_specification.setVisibility(View.VISIBLE);
                    break;

                case REQ_CODE_SELECT_SPECIFICATION:
                    String specConfigStr = data.getStringExtra("result").replace(",", " ");
                    publish_buy_specification.setText(specConfigStr);
                    publish_buy_specification.setTextColor(getResources().getColor(R.color.colorPrimary));
                    String id = data.getStringExtra("ids");
                    List<BaseBean> beans = new ArrayList<>();
                    for (String i : id.split(",")) {
                        BaseBean baseBean = new BaseBean();
                        baseBean.setId(i);
                        beans.add(baseBean);
                    }
                    publishBuyInfoModel.setSpecConfigs(beans);
//                    if (!isPublish)
//                    publishBuyInfoModel.setTitle(dto.getTitle().split(" ")[0]+" "+specConfigStr);
                    publish_buy_et_amount.setVisibility(View.VISIBLE);
                    unit.setVisibility(View.VISIBLE);
                    break;

                case REQ_CODE_SELECT_TERM_TIME:
                    String beginDate = data.getStringExtra("beginDate");
                    String endDate = data.getStringExtra("endDate");

                    break;

                case REQ_CODE_SELECT_ORIGIN:
                    String originAddress = data.getStringExtra("address");
                    String[] originAddresses = originAddress.split(" ");
                    String[] idsOrigin = data.getStringExtra("ids").split(",");
                    publishBuyInfoModel.setOriginCity(originAddresses[1]);
                    publishBuyInfoModel.setOriginProvince(originAddresses[0]);
                    publish_buy_origin.setText(originAddress);
                    break;
                case REQ_CODE_SELECT_ADDRESS:
                    String address = data.getStringExtra("address");
                    String[] addresses = address.split(" ");
                    String[] idsAddress = data.getStringExtra("ids").split(",");
                    switch (addresses.length){
                        case 3:
                        case 2:
                            publishBuyInfoModel.setCity(addresses[1]);
                        case 1:
                            publishBuyInfoModel.setProvince(addresses[0]);
                            break;
                    }
                    publish_buy_address.setText(address);

                    //publish_buy_tel.setText(mSharedPreferences.getString(XnsConstant.LOGIN_NAME," "));

                    String fullName = mSharedPreferences.getString(XnsConstant.FULL_NAME,"");
                    if(!TextUtils.isEmpty(fullName)){
                        publish_buy_contract.setText(fullName);
                    }
                    break;

            }

        }
    }

    private void setPeriod() {
        mPopup = new EasyPopup(mContext)
                .setContentView(R.layout.popup_set_period)
                .setAnimationStyle(R.style.PopAnim)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .createPopup();
        mPopup.getView(R.id.tv_day).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.dismiss();
                periodStr = "每天";
                period.setText("每天");
            }
        });

        mPopup.getView(R.id.tv_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(mContext, "每月");
                mPopup.dismiss();
                periodStr = "每月";
                period.setText(periodStr);
            }
        });

        mPopup.getView(R.id.tv_week).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(mContext, "每周");
                mPopup.dismiss();
                periodStr = "每周";
                period.setText(periodStr);
            }
        });


        LinearLayout tv = mPopup.getView(R.id.ll_layout);
        tv.measure(0,0);
        mPopup.showAsDropDown(sl_layout,  0, -tv.getMeasuredHeight());
    }


    public void selectUnit(View view) {
        mPopup = new EasyPopup(mContext)
                .setContentView(R.layout.test_popuop)
                .setAnimationStyle(R.style.PopAnim)
                //是否允许点击PopupWindow之外的地方消失
                .setWidth(DeviceInfoUtil.getScreenWidth())
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .createPopup();
        mPopup.getView(R.id.jin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unit.setText("斤");
                unitStr = "斤";
                tv_unit.setText("每斤");
                publishBuyInfoModel.setWeightUnit(WeightUnit.JIN);
                mPopup.dismiss();
            }
        });

        mPopup.getView(R.id.gongjin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unit.setText("公斤");
                unitStr = "公斤";
                tv_unit.setText("每公斤");
                publishBuyInfoModel.setWeightUnit(WeightUnit.KG);
                mPopup.dismiss();
            }
        });
        mPopup.getView(R.id.dun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unit.setText("吨");
                unitStr = "吨";
                tv_unit.setText("每吨");
                publishBuyInfoModel.setWeightUnit(WeightUnit.TON);
                mPopup.dismiss();
            }
        });
        CardView tv = mPopup.getView(R.id.popup_layout);
        tv.measure(0,0);
        mPopup.showAsDropDown(sl_layout,  0, -tv.getMeasuredHeight());
    }


}
