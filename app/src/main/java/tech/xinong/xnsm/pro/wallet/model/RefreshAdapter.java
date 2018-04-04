package tech.xinong.xnsm.pro.wallet.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import tech.xinong.xnsm.R;

/**
 * Created by xiao on 2018/1/26.
 */

public class RefreshAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //定义一个集合，接收从Activity中传递过来的数据和上下文
    private List<FinanceRecordsModel> mList;
    private Context mContext;

    public RefreshAdapter(Context context, List<FinanceRecordsModel> list){
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_bill, parent, false);
        return new MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder){
            ((MyHolder) holder).setData(mList.get(position));
        }
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        TextView tv_type;
        TextView tv_time;
        TextView tv_amount;

        public void setData(FinanceRecordsModel model){
            String type = model.getPath().equals("IN")?"收入":"提现";
            tv_type.setText(type);
            tv_time.setText(model.getCreateTime());
            tv_amount.setText(model.getAmount().doubleValue()+"");
        }


        public MyHolder(View itemView) {
            super(itemView);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_amount = itemView.findViewById(R.id.tv_amount);
        }
    }
}

