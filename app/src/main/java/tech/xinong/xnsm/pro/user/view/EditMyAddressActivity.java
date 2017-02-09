package tech.xinong.xnsm.pro.user.view;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_edit_my_address)
public class EditMyAddressActivity extends BaseActivity {
    @ViewInject(R.id.my_address_lv_addresses)
    private ListView lvAddresses;
    @ViewInject(R.id.my_address_tv_add_address)
    private TextView tvAddAddress;

    private String phoneNum;

    @Override
    public void initData() {
        XinongHttpCommend.getInstance(this).getCurrentInfo(new AbsXnHttpCallback() {
            @Override
            public void onSuccess(String info, String result) {

            }
        });
    }

    @OnClick({R.id.my_address_tv_add_address})
    public void widgetClick(View view){
        switch (view.getId()){
            case R.id.my_address_tv_add_address:
                skipActivity(AddAddressActivity.class);
                break;
        }
    }
}
