package tech.xinong.xnsm.pro.wallet;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.wallet.model.BankModel;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_banks)
public class BanksActivity extends BaseActivity {
    @ViewInject(R.id.lv_banks)
    private ListView lv_banks;
    @ViewInject(R.id.tv_add)
    private TextView tv_add;
    private List<BankModel> bankModels;

    @Override
    public void initData() {

    }

    public void  init(){
        XinongHttpCommend.getInstance(mContext).getBanks(new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                bankModels = JSON.parseArray(result, BankModel.class);
                if (bankModels.size() > 0) {
                    lv_banks.setVisibility(View.VISIBLE);
                    tv_add.setVisibility(View.GONE);
                    lv_banks.setAdapter(new CommonAdapter<BankModel>(mContext,R.layout.item_bank,bankModels) {
                        @Override
                        protected void fillItemData(CommonViewHolder viewHolder, int position, final BankModel item) {
                            viewHolder.setTextForTextView(R.id.tv_account,item.getAccountNumber());
                            viewHolder.setTextForTextView(R.id.tv_full_name,item.getAccountName());
                            viewHolder.setOnClickListener(R.id.tv_update, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext,AddBankAccountActivity.class);
                                    intent.putExtra("bank",item);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                } else {
                    lv_banks.setVisibility(View.GONE);
                    tv_add.setVisibility(View.VISIBLE);
                    tv_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            skipActivity(AddBankAccountActivity.class);
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    @Override
    public String setToolBarTitle() {
        return "收款账号";
    }
}
