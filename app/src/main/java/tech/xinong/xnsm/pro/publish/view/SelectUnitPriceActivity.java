package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.math.BigDecimal;

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
    @ViewInject(R.id.select_unit)
    private TextView select_unit;
    private EasyPopup mPopup;
    @ViewInject(R.id.ll)
    private LinearLayout ll;
    @ViewInject(R.id.min_quantity_str)
    private TextView min_quantity_str;
    private String unitStr = "斤";


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
        myIntent.putExtra("unit",unitStr);
        setResult(RESULT_OK,myIntent);
        finish();
    }

    @OnClick(R.id.select_unit)
    public void selectUnit(View view){
        mPopup =  new EasyPopup(mContext)
                .setContentView(R.layout.test_view_layout)
                .setAnimationStyle(R.style.PopAnim)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .createPopup();
        mPopup.getView(R.id.jin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectUnit("斤");
                mPopup.dismiss();
            }
        });

        mPopup.getView(R.id.gongjin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectUnit("公斤");
                mPopup.dismiss();
            }
        });

        mPopup.getView(R.id.dun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectUnit("吨");
                mPopup.dismiss();
            }
        });

        mPopup.showAtAnchorView(view, VerticalGravity.BELOW, HorizontalGravity.CENTER, 0, 0);

    }


    private void selectUnit(String str){
        select_unit.setText("元/"+str);
        min_quantity_str.setText(str+"起批");
        unitStr = str;
    }

    @Override
    public String setToolBarTitle() {
        return "货品价格";
    }

    @Override
    public void initData() {
        BigDecimal unitPriceText = (BigDecimal) getIntent().getSerializableExtra("unitPrice");
        BigDecimal minQuantityText = (BigDecimal) getIntent().getSerializableExtra("minQuantity");
        String unitStr = getIntent().getStringExtra("unit");
        if (unitPriceText!=null){
            unitPrice.setText(unitPriceText.doubleValue()+"");
        }
        if(minQuantityText!=null){
            minQuantity.setText(minQuantityText.doubleValue()+"");
        }
        if (!TextUtils.isEmpty(unitStr)){
            selectUnit(unitStr);
        }
    }
}
