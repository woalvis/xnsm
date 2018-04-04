package tech.xinong.xnsm.pro.publish.view;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.publish.model.LogisticMethod;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_select_logistic_method)
public class SelectLogisticMethodActivity extends BaseActivity {
    @ViewInject(R.id.select_logistic_method_grid)
    private GridView logisticMethodGrid;
    @ViewInject(R.id.select_logistic_method_bt)
    private Button logisticMethodBt;
    private List<String> resultList;


    @Override
    public void initWidget() {
        super.initWidget();
        logisticMethodBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultList==null||resultList.size()==0){
                    T.showShort(mContext, "您还没有选择物流方式");
                    return;
                }else {
                    StringBuffer sb = new StringBuffer("");
                    for (String str : resultList){
                        sb.append(str+",");
                    }

                    Intent intent = new Intent();
                    intent.putExtra("result",sb.toString().substring(0,sb.toString().length()-1));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void initData() {
        resultList = new ArrayList<>();
        showProgress();
        XinongHttpCommend.getInstance(this).providerSupports(new AbsXnHttpCallback(mContext){
            @Override
            public void onSuccess(String info, final String result) {
                cancelProgress();
                List<LogisticMethod> logisticMethodList = JSON.parseArray(result,LogisticMethod.class);
                logisticMethodGrid.setAdapter(new CommonAdapter<LogisticMethod>(mContext,
                        R.layout.item_border_text,
                        logisticMethodList) {
                    @Override
                    protected void fillItemData(final CommonViewHolder viewHolder, int position, final LogisticMethod item) {

                        viewHolder.setTextForTextView(R.id.tv_show,item.getItem());
                        viewHolder.setOnClickListener(R.id.tv_show, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String desc = item.getItem();
                                if (resultList.contains(desc)){
                                    viewHolder.setTextColor(R.id.tv_show,getResources().getColor(R.color.text_color));
                                    viewHolder.getView(R.id.tv_show).setBackgroundResource(R.drawable.textview_border);
                                    resultList.remove(desc);
                                }else {
                                    viewHolder.setTextColor(R.id.tv_show,getResources().getColor(R.color.white));
                                    viewHolder.getView(R.id.tv_show).setBackgroundColor(getResources().getColor(R.color.primaryGreen));
                                    resultList.add(desc);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public String setToolBarTitle() {
        return "服务方式";
    }
}
