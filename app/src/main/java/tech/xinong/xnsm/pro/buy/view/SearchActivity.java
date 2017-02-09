package tech.xinong.xnsm.pro.buy.view;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {
    @ViewInject(R.id.im_left)
    private ImageView imLeft;
    @ViewInject(R.id.tv_right)
    private TextView tvRight;
    @ViewInject(R.id.search)
    private EditText etSearch;
    @ViewInject(R.id.info)
    private TextView tvInfo;
    @ViewInject(R.id.result)
    private TextView tvResult;

    @Override
    public void initWidget() {
        initNavigation();
    }

    private void initNavigation() {
        imLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);

        tvRight.setText("搜索");

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textStr = etSearch.getText().toString().trim();
                XinongHttpCommend.getInstance(mContext).searchText(textStr, new AbsXnHttpCallback() {
                    @Override
                    public void onSuccess(String info, String result) {
//                        tvInfo.setText(info);
//                        tvResult.setText(result);

                        if (info.equals(HttpConstant.OK)){
                            Intent intent = new Intent();
                            intent.putExtra("result",result);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}
