package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.OnClick;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_select_unit_price)
public class SelectUnitPriceActivity extends BaseActivity {
    @ViewInject(R.id.unit_price)
    private EditText unitPrice;
    @ViewInject(R.id.min_quantity)
    private EditText minQuantity;
    @ViewInject(R.id.select_price_submit)
    private Button submit;

    @OnClick(R.id.select_price_submit)
    public void widgetClick(View view) {
        if (TextUtils.isEmpty(unitPrice.getText().toString().trim())){
            T.showShort(mContext, "请填写单价");
            return;
        }
        if(TextUtils.isEmpty(minQuantity.getText().toString().trim())){
            T.showShort(mContext, "请填写起批量");
            return;
        }
        Intent myIntent = new Intent();
        myIntent.putExtra("unitPrice",unitPrice.getText().toString().trim());
        myIntent.putExtra("minQuantity",minQuantity.getText().toString().trim());
        setResult(RESULT_OK,myIntent);
        finish();
    }

}
