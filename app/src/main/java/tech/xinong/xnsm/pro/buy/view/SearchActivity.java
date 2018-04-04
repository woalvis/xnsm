package tech.xinong.xnsm.pro.buy.view;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.SearchListModel;
import tech.xinong.xnsm.util.ArrayUtil;
import tech.xinong.xnsm.util.SPUtils;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;


@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.et_search_content)
    private EditText etSearch;
    @ViewInject(R.id.tv_search)
    private TextView tvSearch;
    @ViewInject(R.id.lv_search_history)
    private ListView lv_search_history;
    @ViewInject(R.id.tv_clear)
    private TextView tv_clear;
    @ViewInject(R.id.ll_history)
    private LinearLayout ll_history;
    @ViewInject(R.id.ll_content)
    private RelativeLayout ll_content;
    private EasyPopup easyPopup;
    @ViewInject(R.id.lv_search)
    private PullToRefreshListView lv_search;
    private CommonAdapter adapter;
    private List<SearchListModel> listModels;

    @Override
    public void initWidget() {
        super.initWidget();
        back.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        etSearch.addTextChangedListener(etWatcher);
        initPopup();
        resetHistory();
    }

    @Override
    public void initData() {
        listModels = new ArrayList<>();
    }

    /*初始化弹出框*/
    private void initPopup() {

    }

    private void resetHistory(){
        String s = (String) SPUtils.get(mContext,"search","");
        if (s.equals("")){
            ll_history.setVisibility(View.GONE);
        }else {
            lv_search_history.setVisibility(View.VISIBLE);
            String[] historyStrs = s.split(",");
            lv_search_history.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,
                    historyStrs));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                if (!checkTextView(etSearch)){
                    searchTitle(etSearch.getText().toString());
                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_clear:
                twoButtonDialog("确认清除搜索历史", "是否铲除所有的历史搜索记录",
                        "确认清除", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SPUtils.remove(mContext,"search");
                                resetHistory();
                            }
                        },null);
                break;
        }
    }

    private void searchTitle(String text) {
        XinongHttpCommend.getInstance(mContext).searchTitle(text, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                SearchShowActivity.skip(result,SearchActivity.this,null);
            }
        });
    }

    private void search(String productId, String specId, final SearchListModel item) {
        String content = etSearch.getText().toString();
        String o = (String) SPUtils.get(mContext,"search","");
        String[] os = o.split(",");
        etSearch.setText("");
        if (!ArrayUtil.c(os,content)&&!TextUtils.isEmpty(content)) {
            SPUtils.put(mContext, "search", content + "," + o);
        }
        showProgress("正在搜索");
        if (TextUtils.isEmpty(productId)){
            XinongHttpCommend.getInstance(mContext).searchTextByspecId(specId,
                    new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            cancelProgress();
                            SearchShowActivity.skip(result,SearchActivity.this,item);
                        }
                    });
        }else {
            XinongHttpCommend.getInstance(mContext).searchTextByProductId(productId,
                    new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            cancelProgress();
                            SearchShowActivity.skip(result,SearchActivity.this,item);
                        }
                    });
        }

    }


    TextWatcher etWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String content = s.toString();
            if (!TextUtils.isEmpty(content)){
                ll_content.setVisibility(View.GONE);
                lv_search.setVisibility(View.VISIBLE);
                XinongHttpCommend.getInstance(mContext).search(content,
                        new AbsXnHttpCallback(mContext) {
                            @Override
                            public void onSuccess(String info, String result) {
                               listModels = JSON.parseArray(result,SearchListModel.class);
                                adapter = new CommonAdapter<SearchListModel>(mContext,R.layout.item_search_title,listModels) {
                                    @Override
                                    protected void fillItemData(CommonViewHolder viewHolder, int position, final SearchListModel item) {
                                        StringBuilder sb = new StringBuilder();
                                        if (item.getCategory()!=null){
                                            sb.append(item.getCategory().getName()+">");
                                        }
                                        if (item.getProduct()!=null){
                                            sb.append(item.getProduct().getName()+">");
                                        }
                                        if (item.getSpec()!=null){
                                            sb.append(item.getSpec().getName());
                                        }else {
                                            sb.substring(0,sb.length()-1);
                                        }
                                        lv_search.setVisibility(View.VISIBLE);
                                        viewHolder.setTextForTextView(R.id.tv_result,sb.toString());
                                        viewHolder.setOnClickListener(R.id.ll_search, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (item.getSpec()==null){
                                                    search(item.getProduct().getId(),"",item);
                                                }else {
                                                    search("",item.getSpec().getId(),item);
                                                }
                                            }
                                        });
                                    }
                                };
                               lv_search.setAdapter(adapter);
                            }
                        });
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
