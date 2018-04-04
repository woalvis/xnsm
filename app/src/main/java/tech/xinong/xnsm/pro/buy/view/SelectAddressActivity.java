package tech.xinong.xnsm.pro.buy.view;

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
import tech.xinong.xnsm.pro.user.view.AddAddressActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2018/1/10.
 */
@ContentView(R.layout.activity_select_address_buy)
public class SelectAddressActivity extends BaseActivity {
    @ViewInject(R.id.lv_address)
    private ListView lv_address;
    @ViewInject(R.id.tv_add_address)
    private TextView tv_add_address;
    private List<Address> addresses;
    private CommonAdapter<Address> addressCommonAdapter;

    public final static int REQ_CODE_ADD_ADDRESS = 1001;
    @Override
    public void initData() {
        addresses = new ArrayList<>();

        XinongHttpCommend.getInstance(this).address(getCustmerId(),new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(final String info, String result) {

                addresses = JSON.parseArray(result,Address.class);
                addressCommonAdapter =  new CommonAdapter<Address>(mContext,R.layout.item_address_buy,addresses) {
                    @Override
                    protected void fillItemData(CommonViewHolder viewHolder,
                                                final int position,
                                                final Address item) {
                        viewHolder.setTextForTextView(R.id.tv_receiver,item.getReceiver());
                        viewHolder.setTextForTextView(R.id.tv_phone,item.getReceiverPhone());
                        viewHolder.setTextForTextView(R.id.tv_address_show,item.getProvince()+item.getCity()+item.getDistrict()+item.getStreet());
                        if (item.isPrimary()){
                            viewHolder.setTextForTextView(R.id.tv_default,"[默认地址]");
                            ((TextView)viewHolder.getView(R.id.tv_default)).setTextColor(getColorById(R.color.orange));
                        }else {
                            viewHolder.getView(R.id.tv_default).setVisibility(View.GONE);
                        }
                        viewHolder.setOnClickListener(R.id.layout_address, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.putExtra("address",item);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        });

                    }

                };
                lv_address.setAdapter(addressCommonAdapter);
                cancelProgress();
            }
        });


        tv_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddAddressActivity.class);
                intent.putExtra("isBuyNow",true);
                startActivityForResult(intent,REQ_CODE_ADD_ADDRESS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public String setToolBarTitle() {
        return "选择收货地址";
    }
}
