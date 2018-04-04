package tech.xinong.xnsm.pro.publish.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yanzhenjie.album.Album;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.BaseDTO;
import tech.xinong.xnsm.pro.base.model.WeightUnit;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.ListingDetailsDTO;
import tech.xinong.xnsm.pro.buy.model.ListingDocDTO;
import tech.xinong.xnsm.pro.buy.model.SpecificationConfigDTO;
import tech.xinong.xnsm.pro.buy.view.SpecActivity;
import tech.xinong.xnsm.pro.publish.model.ListingDocFile;
import tech.xinong.xnsm.pro.publish.model.ListingSellCreator;
import tech.xinong.xnsm.pro.publish.model.adapter.ImageShowAdapter;
import tech.xinong.xnsm.util.FileUtil;
import tech.xinong.xnsm.util.NumUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_publish_sell)
public class PublishSellActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    /*货品分类*/
    @ViewInject(R.id.publish_sell_goods_product)
    private TextView tvGoodsProduct;
    /*货品品种*/
    @ViewInject(R.id.publish_sell_goods_spec)
    private TextView tvGoodsSpec;
    /*货品规格*/
    @ViewInject(R.id.publish_sell_goods_specification)
    private TextView tvGoodsSpecification;
    /*货品单价*/
    @ViewInject(R.id.publish_sell_goods_unit_price)
    private TextView tvUnitPrice;
    /*供货时间*/
    @ViewInject(R.id.publish_sell_term_begin_date)
    private TextView tvTermBeginDate;
    /*供货量*/
    @ViewInject(R.id.publish_sell_total_quantity)
    private TextView tvTotalQuantity;
    /*供货量的编辑框*/
    @ViewInject(R.id.publish_sell_et_total_quantity)
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
    /*要上传的照片的展示GridView*/
    @ViewInject(R.id.publish_sell_gv_goods_photos)
    private GridView gvGoodsPhoto;
    @ViewInject(R.id.ll_freeshipping)
    private LinearLayout ll_freeshipping;
    /*发布提交按钮*/
    @ViewInject(R.id.publish_sell_submit)
    private Button publishSellSubmit;
    //    @ViewInject(R.id.publish_sell_rg_broker_allowed)
//    private RadioGroup rgBrokerAllowed;
    @ViewInject(R.id.tv_center)
    private TextView tvCenter;
    @ViewInject(R.id.is_freight)
    private RadioButton is_freight;
    @ViewInject(R.id.no_freight)
    private RadioButton no_freight;

    private EasyPopup mPopup;
    private String productId;
    private String productName;
    private ArrayList<String> photoList;
    private ArrayList<String> uploadFileStrList;
    private ArrayList<String> uploadFileIds;
    private boolean isPressedPublishBt = false;
    private ListingSellCreator sellCreator;
    private List<ListingDocFile> fileList;
    @ViewInject(R.id.unit)
    private TextView unit;//供货量的单位
    private String unitStr = "斤";
    private ListingDetailsDTO dto;
    private boolean isSubmit = true;
    private String note;
    private ListingDetailsDTO updateDto;
    private ImageShowAdapter adapter;
    private String listingId;

    /**
     * 请求码
     */
    public static final int REQ_CODE_SELECT_SPEC = 0x1001;//选择品类
    public static final int REQ_CODE_SELECT_SPECIFICATION = 0x1002;//选择规格
    public static final int REQ_CODE_SELECT_UNIT_PRICE = 0x1003;//选择单价
    public static final int REQ_CODE_SELECT_TERM_TIME = 0x1004;//选择时间
    public static final int REQ_CODE_SELECT_ADDRESS = 0x1005;//选择发货地址
    public static final int REQ_CODE_SELECT_ORIGIN = 0x1006;//选择货源地址
    public static final int REQ_CODE_SELECT_LOGISTIC_METHOD = 0x1007;//选择运输方法
    public static final int REQ_CODE_EDIT_GOODS_NOTE = 0x1008;//填写货物说明
    public static final int REQ_CODE_SELECT_PHOTOS = 0x1009;//选择图片


    @Override
    public void initWidget() {
        tvCenter.setText("发布供货");
        tvCenter.setVisibility(View.VISIBLE);
        /*为编辑供货量的editText添加监听*/
        etTotalQuantity.addTextChangedListener(myTextWatcher);
    }

    @Override
    public void initData() {
        photoList = new ArrayList<>();
        uploadFileStrList = new ArrayList<>();
        uploadFileIds = new ArrayList<>();
        updateDto = new ListingDetailsDTO();
        fileList = new ArrayList<>();
        adapter = new ImageShowAdapter(mContext);

        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Album.startAlbum(PublishSellActivity.this, REQ_CODE_SELECT_PHOTOS
                        , 9-adapter.getCount()+1                                                    // 指定选择数量。
                        , ContextCompat.getColor(mContext, R.color.colorPrimary)        // 指定Toolbar的颜色。
                        , ContextCompat.getColor(mContext, R.color.colorPrimaryDark));  // 指定状态栏的颜色。
            }
        });
        adapter.setDelListener(new ImageShowAdapter.OnDelListener() {
            @Override
            public void onDel(int position) {
                fileList.remove(position);
            }
        });
        gvGoodsPhoto.setAdapter(adapter);
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        if (TextUtils.isEmpty(productId)) {
            listingId = intent.getStringExtra("updateId");
            isSubmit = false;
            initUpdate();
        }

        productName = intent.getStringExtra("productName");
        tvGoodsProduct.setText(productName);

        sellCreator = new ListingSellCreator();

        BaseDTO baseBean = new BaseDTO();

        baseBean.setId(productId);
        sellCreator.setProduct(baseBean);
    }

    private void initUpdate() {
        showProgress();
        XinongHttpCommend.getInstance(this).getProductListings(listingId, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                cancelProgress();
                dto = JSON.parseObject(result, ListingDetailsDTO.class);
                initPage(dto);
            }
        });
    }

    private void initPage(ListingDetailsDTO dto) {

        productId = dto.getProductId();
        /*货品分类*/
        tvGoodsProduct.setText(dto.getProductName());
        tvGoodsSpec.setText(dto.getProductSpecName());
        tvGoodsSpec.setOnClickListener(null);
        unit.setText(dto.getWeightUnit().getDisplayName());

        is_freight.setChecked(dto.getFreeShipping());
        no_freight.setChecked(!dto.getFreeShipping());


        String str = "";
        for (SpecificationConfigDTO specConfig : dto.getSpecificationConfigs()) {
            str += specConfig.getItem() + " ";
        }
        tvGoodsSpecification.setText(str);
        tvGoodsSpecification.setVisibility(View.VISIBLE);
        tvUnitPrice.setText(dto.getUnitPrice() + dto.getWeightUnit().getDisplayName());
        tvUnitPrice.setVisibility(View.VISIBLE);
        tvTermBeginDate.setText("上市时间：" + dto.getTermBeginDate() + "\n下市时间：" + dto.getTermEndDate());
        tvTermBeginDate.setVisibility(View.VISIBLE);
        etTotalQuantity.setText(dto.getTotalQuantity() + "");
        etTotalQuantity.setVisibility(View.VISIBLE);
        unit.setText(dto.getWeightUnit().getDisplayName());
        unit.setVisibility(View.VISIBLE);
        tvAddress.setVisibility(View.VISIBLE);
        tvAddress.setText(dto.getAddress());
        tvLogisticMethod.setVisibility(View.VISIBLE);
        tvLogisticMethod.setText(dto.getProvideSupport());
        note = dto.getNotes();
        tvOrigin.setVisibility(View.VISIBLE);
        tvOrigin.setText(dto.getOriginProvinceName() + " " + dto.getOriginCityName());
        Set<ListingDocDTO> fileSet = dto.getListingDocs();
        List<ListingDocDTO> result = new ArrayList<>(fileSet);

//        gvGoodsPhoto.setAdapter(new CommonAdapter<ListingDocDTO>(mContext, R.layout.item_gv_goods_photo, result) {
//            @Override
//            protected void fillItemData(CommonViewHolder viewHolder, int position, ListingDocDTO item) {
//                ImageView imageView = (ImageView) viewHolder.getView(R.id.image);
//                Glide.with(mContext).load(ImageUtil.getImgUrl(item.getDocName())).into(imageView);
//            }
//        });

        adapter.setResultList(result);
        tvGoodsNotes.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(dto.getNotes())){
            tvGoodsNotes.setText("点击添加描述");
        }else {
            tvGoodsNotes.setText(dto.getNotes());
            tvGoodsNotes.setTextColor(getColorById(R.color.primaryGreen));
        }

    }


    @OnClick({
            R.id.publish_sell_goods_spec,
            R.id.publish_sell_goods_specification,
            R.id.publish_sell_goods_unit_price,
            R.id.publish_sell_origin,
            R.id.publish_sell_logistic_method,
            R.id.publish_sell_goods_notes,
            R.id.publish_sell_submit,
            R.id.unit,
            R.id.publish_sell_term_begin_date
    })
    public void click(View view) {
        switch (view.getId()) {
            /*货品品类选择*/
            case R.id.publish_sell_goods_spec:
                Intent intentSelectSpec = new Intent(this, SpecActivity.class);
                intentSelectSpec.putExtra("productId", productId);
                startActivityForResult(intentSelectSpec, REQ_CODE_SELECT_SPEC);
                break;
            /*货品规格选择*/
            case R.id.publish_sell_goods_specification:
                Intent intentSelectSpecification = new Intent(this, SelectSpecificationActivity.class);
                intentSelectSpecification.putExtra("productId", productId);
//                intentSelectSpecification.putExtra("spec_config",dto.getSpecificationConfigs().toArray());
                startActivityForResult(intentSelectSpecification, REQ_CODE_SELECT_SPECIFICATION);
                break;
            /*单价选择*/
            case R.id.publish_sell_goods_unit_price:
                Intent intentSelectPrice = new Intent(mContext, SelectUnitPriceActivity.class);
                startActivityForResult(intentSelectPrice, REQ_CODE_SELECT_UNIT_PRICE);
                break;
            /*原产地选择*/
            case R.id.publish_sell_origin:
//                Intent intentSelectOrigin = new Intent(mContext, SelectAddressActivity.class);
//                startActivityForResult(intentSelectOrigin, REQ_CODE_SELECT_ORIGIN);
                Intent intent = new Intent(mContext, TestSelectAddressActivity.class);
                startActivityForResult(intent, REQ_CODE_SELECT_ORIGIN);
                break;
            /*物流方式选择*/
            case R.id.publish_sell_logistic_method:
                Intent intentSelectLogisticMethod = new Intent(mContext, SelectLogisticMethodActivity.class);
                startActivityForResult(intentSelectLogisticMethod, REQ_CODE_SELECT_LOGISTIC_METHOD);
                tvGoodsNotes.setVisibility(View.VISIBLE);
                break;
            /*填写货物描述*/
            case R.id.publish_sell_goods_notes:
                Intent intentEditGoodsNote = new Intent(mContext, EditGoodsNoteActivity.class);
                if (dto != null && !TextUtils.isEmpty(dto.getNotes()))
                    intentEditGoodsNote.putExtra("note", dto.getNotes());
                startActivityForResult(intentEditGoodsNote, REQ_CODE_EDIT_GOODS_NOTE);
                break;
            /*发布卖出货品*/
            case R.id.publish_sell_submit:
                publishSellSubmit(isSubmit);
                break;
            case R.id.unit:
                //selectUnit(view);
                break;
            case R.id.publish_sell_term_begin_date:
                Intent intentSelectTermTime = new Intent(mContext, SelectTermTimeActivity.class);
                startActivityForResult(intentSelectTermTime, REQ_CODE_SELECT_TERM_TIME);
                break;

        }
    }


    /**
     * 提交发布信息的按钮
     */
    private void publishSellSubmit(boolean isSubmitBt) {
        if (check()) {
            sellCreator.setFreeShipping(is_freight.isChecked());
            if (isSubmitBt) {
                setSellCreator();
                sellCreator.setTotalQuantity(new BigDecimal(etTotalQuantity.getText().toString().trim()));
                showProgress("正在上传");
                XinongHttpCommend.getInstance(mContext).publishSellInfo(sellCreator, new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        cancelProgress();
                        //T.showShort(mContext, result);
                        finish();
                    }
                });
            } else {
                showProgress("正在上传");
                if (adapter != null) {
                    sellCreator.setUpdateDocs(adapter.getUpdateDocs());
                }
                sellCreator.setTotalQuantity(new BigDecimal(etTotalQuantity.getText().toString().trim()));
                XinongHttpCommend.getInstance(mContext).updateSell(sellCreator, listingId, new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        cancelProgress();
                        T.showShort(mContext, result);
                        finish();
                    }
                });
            }
        }
    }

    private void setSellCreator() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQ_CODE_SELECT_SPEC:
                    /*从选择的Activity中得到用户选择的结果*/
                    if (isSubmit) {
                        tvGoodsSpec.setText(data.getStringExtra("spec"));
                        tvGoodsSpec.setTextColor(getResources().getColor(R.color.colorPrimary));
                        BaseDTO baseDTO = new BaseDTO();
                        baseDTO.setId(data.getStringExtra("id"));
                        sellCreator.setProductSpec(baseDTO);
                        sellCreator.setTitle(data.getStringExtra("spec"));
                        tvGoodsSpecification.setVisibility(View.VISIBLE);
                    } else {

                    }
                    break;

                case REQ_CODE_SELECT_SPECIFICATION:
                    String specConfigStr = data.getStringExtra("result").replace(",", " ");
                    tvGoodsSpecification.setText(specConfigStr);
                    tvGoodsSpecification.setTextColor(getResources().getColor(R.color.colorPrimary));
                    String id = data.getStringExtra("ids");
                    List<BaseDTO> baseDTOs = new ArrayList<>();
                    for (String i : id.split(",")) {
                        baseDTOs.add(new BaseDTO(i));
                    }
                    sellCreator.setSpecificationConfigs(baseDTOs);
                    if (isSubmit) {
                        sellCreator.setTitle(sellCreator.getTitle().split(" ")[0] + " " + specConfigStr);
                        tvUnitPrice.setText("点这里填写价格");
                        tvUnitPrice.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvUnitPrice.setClickable(true);
                    } else {
                        String[] titles = dto.getTitle().split(" ");
                        sellCreator.setTitle(titles[0] + " " + specConfigStr);
                    }
                    break;
                case REQ_CODE_SELECT_UNIT_PRICE:
                    String unitPrice = data.getStringExtra("unitPrice");
                    String minQuantity = data.getStringExtra("minQuantity");
                    String unitString = data.getStringExtra("unit");
                    sellCreator.setUnitPrice(new BigDecimal(unitPrice));
                    sellCreator.setMinQuantity(Integer.parseInt(minQuantity));
                    unit.setText(unitString);
                    unit.setVisibility(View.VISIBLE);
                    sellCreator.setWeightUnit(WeightUnit.getWeightUnit(unitString));
                    tvUnitPrice.setText(unitPrice + "元/" + unitString);//"   " + minQuantity + unitString +"起"
                    tvUnitPrice.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (isSubmit) {
                        tvTermBeginDate.setVisibility(View.VISIBLE);
                        tvTermBeginDate.setText(getResources().getString(R.string.publish_sell_edit_term_time));
                        tvTermBeginDate.setClickable(true);
                        tvTermBeginDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvTermBeginDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intentSelectTermTime = new Intent(mContext, SelectTermTimeActivity.class);
                                startActivityForResult(intentSelectTermTime, REQ_CODE_SELECT_TERM_TIME);
                            }
                        });
                    }
                    break;
                case REQ_CODE_SELECT_TERM_TIME:
                    String beginDate = data.getStringExtra("beginDate");
                    String endDate = data.getStringExtra("endDate");
                    tvTermBeginDate.setText("上市时间：" + beginDate + "\n下市时间：" + endDate);
                    sellCreator.setTermBeginDate(beginDate);
                    sellCreator.setTermEndDate(endDate);
                    unit.setVisibility(View.VISIBLE);
                    etTotalQuantity.setVisibility(View.VISIBLE);
                    break;
                case REQ_CODE_SELECT_ADDRESS:
                    String address = data.getStringExtra("address");
                    String[] ids = data.getStringExtra("ids").split(",");
                    switch (ids.length) {
                        case 3:
                            sellCreator.setDistrict(new BaseDTO(ids[2]));
                        case 2:
                            sellCreator.setCity(new BaseDTO(ids[1]));
                        case 1:
                            sellCreator.setProvince(new BaseDTO(ids[0]));
                            break;
                    }
                    tvAddress.setText(address);
//
                    tvOrigin.setVisibility(View.VISIBLE);
                    break;
                case REQ_CODE_SELECT_ORIGIN:
                    String originAddress = data.getStringExtra("address");
                    String[] idsOrigin = data.getStringExtra("ids").split(",");
                    sellCreator.setOriginProvince(idsOrigin[0]);
                    if (idsOrigin.length>1){
                        sellCreator.setOriginCity(idsOrigin[1]);
                    }


                    tvOrigin.setText(originAddress);
                    tvLogisticMethod.setVisibility(View.VISIBLE);
                    break;
                case REQ_CODE_SELECT_LOGISTIC_METHOD:
                    String logisticMethods = data.getStringExtra("result");
                    sellCreator.setProvideSupport(logisticMethods);
                    tvLogisticMethod.setText(logisticMethods);
                    tvGoodsNotes.setVisibility(View.VISIBLE);
                    break;
                case REQ_CODE_EDIT_GOODS_NOTE:
                    String goodsNote = data.getStringExtra("result");
                    tvGoodsNotes.setText(goodsNote);
                    sellCreator.setNotes(goodsNote);
                    break;
                case REQ_CODE_SELECT_PHOTOS:
                    if (Build.VERSION.SDK_INT >= 23) {
                        int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
                        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PublishSellActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
                            return;
                        } else {
                            openCamera(data);//调用具体方法
                        }
                    } else {
                        openCamera(data);//调用具体方法
                    }
                    //   ArrayList<String> strs = (ArrayList<String>) data.getExtras().get("data");

                    break;

            }

        }
    }


    private void openCamera(Intent data) {
        List<String> strs = Album.parseResult(data);
        photoList = adapter.getPhotoList();
        if (strs != null && strs.size() > 0) {
            for (String str : strs) {
                if (!photoList.contains(str)) {
                    photoList.add(str);
                }
                if (!uploadFileStrList.contains(str)) {
                    ListingDocFile lFile = new ListingDocFile();
                    File file = FileUtil.compressImage(new File(str));
                    lFile.setFile(file);
                    fileList.add(lFile);
                    uploadFileStrList.add(str);
                }
            }
            sellCreator.setDocs(fileList);
            adapter.setPhotoList(photoList);
        }

    }

    TextWatcher myTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (NumUtil.isPositiveNumber(etTotalQuantity.getText().toString()) >= 0) {
                ll_freeshipping.setVisibility(View.VISIBLE);
                tvAddress.setVisibility(View.VISIBLE);
                tvAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, TestSelectAddressActivity.class);
                        startActivityForResult(intent, REQ_CODE_SELECT_ADDRESS);
                    }
                });
            } else {
                tvAddress.setVisibility(View.INVISIBLE);
                T.showShort(mContext, "请输入正确的数字");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (publishSellSubmit.getVisibility() == View.GONE)
            publishSellSubmit.setVisibility(View.VISIBLE);
    }


    public void selectUnit(View view) {
        mPopup = new EasyPopup(mContext)
                .setContentView(R.layout.test_popuop)
                .setAnimationStyle(R.style.PopAnim)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(false)
                .createPopup();
        mPopup.getView(R.id.jin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unit.setText("斤");
                unitStr = "斤";
                mPopup.dismiss();
            }
        });

        mPopup.getView(R.id.gongjin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unit.setText("公斤");
                unitStr = "公斤";
                mPopup.dismiss();
            }
        });

        mPopup.getView(R.id.dun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unit.setText("吨");
                unitStr = "吨";
                mPopup.dismiss();
            }
        });

        mPopup.showAtAnchorView(view, VerticalGravity.BELOW, HorizontalGravity.CENTER, 0, 0);

    }

    private boolean check() {
        if (checkTextView(tvGoodsProduct)) {
            T.showShort(mContext, "货品分类不能为空");
            return false;
        }
        if (checkTextView(tvGoodsSpec)) {
            T.showShort(mContext, "货品品种不能为空");
            return false;
        }
        if (checkTextView(tvGoodsSpecification)) {
            T.showShort(mContext, "货品规格不能为空");
            return false;
        }
        if (checkTextView(tvUnitPrice)) {
            T.showShort(mContext, "货品单价不能为空");
            return false;
        }
        if (checkTextView(tvTermBeginDate)) {
            T.showShort(mContext, "供货时间不能为空");
            return false;
        }

        if (checkTextView(etTotalQuantity)) {
            T.showShort(mContext, "供货量不能为空");
            return false;
        }
        if (checkTextView(tvAddress)) {
            T.showShort(mContext, "发货地址不能为空");
            return false;
        }
        if (checkTextView(tvOrigin)) {
            T.showShort(mContext, "原产地不能为空");
            return false;
        }
        if (checkTextView(tvLogisticMethod)) {
            T.showShort(mContext, "支持服务不能为空");
            return false;
        }

        if (checkTextView(tvLogisticMethod)) {
            T.showShort(mContext, "支持服务不能为空");
            return false;
        }

        if (checkTextView(tvGoodsNotes)) {
            T.showShort(mContext, "货品描述不能为空");
            return false;
        }


        return true;
    }
}
