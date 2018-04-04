package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.user.model.Address;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_edit_my_address)
public class EditMyAddressActivity extends BaseActivity {
    @ViewInject(R.id.my_address_lv_addresses)
    private ListView lvAddresses;
    @ViewInject(R.id.my_address_tv_add_address)
    private TextView tvAddAddress;
    private List<Address> addresses;
    private String phoneNum;
    private CommonAdapter<Address> addressCommonAdapter;
    private boolean isInit = true;

    @Override
    public String setToolBarTitle() {
        return "收货地址管理";
    }

    @Override
    public void initData() {

        addresses = new ArrayList<>();
        showProgress();
        XinongHttpCommend.getInstance(this).address(getCustmerId(),new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(final String info, String result) {
                addresses = JSON.parseArray(result,Address.class);
                addressCommonAdapter =  new CommonAdapter<Address>(mContext,R.layout.item_address,addresses) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder,
                                                int position,
                                                final Address item) {
                        viewHolder.setTextForTextView(R.id.tv_receiver,item.getReceiver());
                        viewHolder.setTextForTextView(R.id.tv_phone,item.getReceiverPhone());
                        viewHolder.setTextForTextView(R.id.tv_address_show,item.getProvince()+item.getCity()+item.getDistrict()+item.getStreet());
                        if (item.isPrimary()){
                            viewHolder.setImageForView(R.id.im_select,R.drawable.date_select);
                        }else {
                            viewHolder.setImageForView(R.id.im_select,R.drawable.date_default);
                            viewHolder.setOnClickListener(R.id.im_select, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                  XinongHttpCommend.getInstance(mContext).updateAddress(getCustmerId(),
                                          item.getId(),
                                          true,
                                          new AbsXnHttpCallback(mContext) {
                                              @Override
                                              public void onSuccess(String info, String result) {
                                                    initData();
                                              }
                                          });
                                }
                            });
                        }

                        viewHolder.setOnClickListener(R.id.tv_edit, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext,AddAddressActivity.class);
                                intent.putExtra("address",item);
                                startActivity(intent);
                            }
                        });

                        viewHolder.setOnClickListener(R.id.tv_del, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                XinongHttpCommend.getInstance(mContext).delAddress(
                                        mSharedPreferences.getString(XnsConstant.CUSTOMERID,""),item.getId(),
                                        new AbsXnHttpCallback(mContext) {
                                            @Override
                                            public void onSuccess(String info, String result) {
//                                                T.showShort(mContext,"删除成功");
//                                                addresses.remove(item);
//                                                addressCommonAdapter.refresh(addresses);
                                                initData();
                                            }
                                        });
                            }
                        });
                    }

                };
                lvAddresses.setAdapter(addressCommonAdapter);
                isInit = false;
                cancelProgress();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isInit)
        XinongHttpCommend.getInstance(this).address(getCustmerId(),new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                addresses = JSON.parseArray(result, Address.class);
                addressCommonAdapter.refresh(addresses);
            }
        });

    }

    @OnClick({R.id.my_address_tv_add_address})
    public void widgetClick(View view){
        switch (view.getId()){
            case R.id.my_address_tv_add_address:
                skipActivity(AddAddressActivity.class,false);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isInit = true;
    }
}
