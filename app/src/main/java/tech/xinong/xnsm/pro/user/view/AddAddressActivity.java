package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.publish.view.SelectAddressActivity;
import tech.xinong.xnsm.pro.user.model.Address;
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

    private String username;

    @Override
    public void initData() {
        username =  mSharedPreferences.getString(XnsConstant.USER_NAME,"");

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
        etPhoneNum.setText(username);
        inputAddressTag.setHint(getResources().getString(R.string.address_tag_hint));
        etAddressTag = inputAddressTag.getEditText();
        etLocation = inputLocation.getEditText();
        etLocation.setHint(getResources().getString(R.string.location_hint));
        etLocation.setFocusable(false);
        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAddressActivity.this, SelectAddressActivity.class);
                startActivityForResult(intent,REQ_CODE_SELECT_ORIGIN);
            }
        });


    }

    @Override
    @OnClick({R.id.my_address_tv_add_address})
    public void widgetClick(View view) {
       switch (view.getId()){
           case R.id.my_address_tv_add_address:
               commitAddAddress();
               break;
       }
    }

    private void commitAddAddress() {
        if (checkParam()){
            Address address = new Address();

            String[] areas = etLocation.getText().toString().trim().split("    ");
//            address.setProvince(areas[1]);
//            address.setCity(areas[2]);
//            address.set
            XinongHttpCommend.getInstance(this).addAddress(address, new AbsXnHttpCallback() {
                @Override
                public void onSuccess(String info, String result) {

                }
            });
        }
    }

    private boolean checkParam() {
        boolean isEffective = false;


        return isEffective;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode == REQ_CODE_SELECT_ORIGIN){
                String address = data.getStringExtra("address");

                etLocation.setText(address);
            }
        }
    }
}
