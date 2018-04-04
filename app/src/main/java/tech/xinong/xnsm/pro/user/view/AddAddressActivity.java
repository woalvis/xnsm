package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.publish.view.TestSelectAddressActivity;
import tech.xinong.xnsm.pro.user.model.Address;
import tech.xinong.xnsm.util.RegexpUtils;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_add_address)
public class AddAddressActivity extends BaseActivity {
    @ViewInject(R.id.text_input_contact)
    private TextInputLayout inputContact;
    @ViewInject(R.id.text_input_phone_num)
    private TextInputLayout inputPhoneNum;
    @ViewInject(R.id.text_input_location)
    private TextInputLayout inputLocation;
    @ViewInject(R.id.text_input_address)
    private TextInputLayout inputAddress;
    @ViewInject(R.id.text_input_postalCode)
    private TextInputLayout inputPostCode;
    @ViewInject(R.id.my_address_tv_add_address)
    private TextView tvAddAddress;
    @ViewInject(R.id.tv_address_show)
    private TextView tvAddressShow;
    @ViewInject(R.id.text_input_address_tag)
    private TextInputLayout inputAddressTag;

    public static final int REQ_CODE_SELECT_ORIGIN = 0x1001;
    private EditText etContact;
    private EditText etPhoneNum;
    private EditText etAddress;
    private EditText etLocation;
    private EditText etAddressTag;
    private EditText etPostCode;
    private Address address;
    private String username;
    private boolean isAdd = true;
    private String[] addresses = new String[3];
    private boolean isBuyNow = false;

    @Override
    public void initData() {
        isBuyNow = getIntent().getBooleanExtra("isBuyNow",false);
    }


    @Override
    public void initWidget() {
        inputContact.setHint(getResources().getString(R.string.contact_hint));
        etContact = inputContact.getEditText();
        inputPhoneNum.setHint(getResources().getString(R.string.phone_number_hint));
        etPhoneNum = inputPhoneNum.getEditText();
        inputAddress.setHint(getResources().getString(R.string.address_hint));
        etAddress = inputAddress.getEditText();
        inputPostCode.setHint(getResources().getString(R.string.postcode_hint));
        etPostCode = inputPostCode.getEditText();
        etPhoneNum.setText(username);
        inputAddressTag.setHint(getResources().getString(R.string.address_tag_hint));
        etAddressTag = inputAddressTag.getEditText();
        etLocation = inputLocation.getEditText();
        etLocation.setHint(getResources().getString(R.string.location_hint));
        etLocation.setFocusable(false);
        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TestSelectAddressActivity.class);
                startActivityForResult(intent, REQ_CODE_SELECT_ORIGIN);
            }
        });
        if (getIntent().getSerializableExtra("address") != null) {
            isAdd = false;
            address = (Address) getIntent().getSerializableExtra("address");
            etContact.setText(address.getReceiver());
            etPhoneNum.setText(address.getReceiverPhone());
            etAddress.setText(address.getStreet());
            etAddressTag.setText(address.getTag());
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(address.getProvince())){
                sb.append(address.getProvince());
                addresses[0] = address.getProvince();
            }
            if (!TextUtils.isEmpty(address.getCity())){
                sb.append(" "+address.getCity());
                addresses[1] = address.getCity();
            }

            if (!TextUtils.isEmpty(address.getDistrict())){
                sb.append(" "+address.getDistrict());
                addresses[2] = address.getCity();
            }


            etLocation.setText(sb.toString());
            etPostCode.setText(address.getPostalCode());

        } else {
            address = new Address();
            username = mSharedPreferences.getString(XnsConstant.USER_NAME, "");
        }

        etContact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(etLocation.getText().toString().trim())){
                    etAddressTag.setText(s.toString()+" "+addresses[0]);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        super.initWidget();
    }

    @Override
    @OnClick({R.id.my_address_tv_add_address})
    public void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.my_address_tv_add_address:
                commitAddAddress();
                break;
        }
    }

    private void commitAddAddress() {
        if (checkParam()) {
            if (isBuyNow){
                showProgress();
                address.setPrimary(true);
                XinongHttpCommend.getInstance(this).addAddress(address, mSharedPreferences.getString(XnsConstant.CUSTOMERID, ""), new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        cancelProgress();
                        T.showShort(mContext, "地址添加成功");
                        address.setId(result);
                        Intent intent = new Intent();
                        intent.putExtra("address",address);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });

            }else {
                if (isAdd) {
                    showProgress();
                    address.setPrimary(true);
                    XinongHttpCommend.getInstance(this).addAddress(address, mSharedPreferences.getString(XnsConstant.CUSTOMERID, ""), new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            cancelProgress();
                            T.showShort(mContext, "地址添加成功");
                            finish();
                        }
                    });
                }else {
                    showProgress();
                    XinongHttpCommend.getInstance(this).updateAddress(getCustmerId(),address, new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            cancelProgress();
                            T.showShort(mContext, "地址修改成功");
                            finish();
                        }
                    });
                }
            }
        }
    }

    private boolean checkParam() {
        boolean isEffective = true;
        String receiverStr = inputContact.getEditText().getText().toString().trim();
        if (!TextUtils.isEmpty(receiverStr)) {
            address.setReceiver(receiverStr);
        } else {
            T.showShort(mContext, "请您填写联系人");
            return false;
        }

        String receiverPhoneStr = inputPhoneNum.getEditText().getText().toString().trim();
        if (!TextUtils.isEmpty(receiverPhoneStr)) {
            if (!RegexpUtils.pPhone.matcher(receiverPhoneStr).matches()){
                T.showShort(mContext,"请填写正确的手机号");
                return false;
            }

            address.setReceiverPhone(receiverPhoneStr);
        } else {
            T.showShort(mContext, "请您填写联系人电话");
            return false;
        }

        String inputPostCodeString = inputPostCode.getEditText().getText().toString().trim();
        if (!TextUtils.isEmpty(receiverPhoneStr)) {
            address.setPostalCode(inputPostCodeString);
        } else {
            address.setPostalCode("");
        }
        String inputAddressTagString = inputAddressTag.getEditText().getText().toString().trim();
        if (!TextUtils.isEmpty(inputAddressTagString)) {
            address.setTag(inputAddressTagString);
        } else {
            T.showShort(mContext, "请您填写地址简称");
            return false;
        }

        String streetString = inputAddress.getEditText().getText().toString().trim();
        if (!TextUtils.isEmpty(streetString)) {
            address.setStreet(streetString);
        } else {
            T.showShort(mContext, "请您填写详细地址");
            return false;
        }


        return isEffective;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_SELECT_ORIGIN) {
                addresses = data.getStringExtra("address").split(" ");
                String[] ids = data.getStringExtra("ids").split(",");
                switch (ids.length) {
                    case 3:
                        address.setDistrict(addresses[2]);
                    case 2:
                        address.setCity(addresses[1]);
                    case 1:
                        address.setProvince(addresses[0]);
                        break;
                }
                if (!TextUtils.isEmpty(etContact.getText().toString().trim())){
                    etAddressTag.setText(etContact.getText().toString().trim()+" "+addresses[0]);
                }

                etLocation.setText(data.getStringExtra("address"));
            }
        }
    }


    @Override
    public String setToolBarTitle() {
        if (isAdd) {
            return "新增地址";
        } else {
            return "修改地址";
        }

    }
}
