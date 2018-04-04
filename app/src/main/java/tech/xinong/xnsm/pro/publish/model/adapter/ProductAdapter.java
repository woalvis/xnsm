package tech.xinong.xnsm.pro.publish.model.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;


/**
 * Created by xiao on 2017/10/31.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductDTO> products;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public ProductAdapter(List<ProductDTO> products){

        this.products = products;
    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mInflater = LayoutInflater.from(mContext);
        View itemView = mInflater.inflate(R.layout.item_test_textview,parent,false);
        return new ProductViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.setTv(products.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void refresh(List<ProductDTO> products){
        this.products = products;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ProductAdapter mParent;
        private TextView tv;

        public ProductViewHolder(View itemView,ProductAdapter adapter) {
            super(itemView);
            mParent = adapter;
            tv = itemView.findViewById(R.id.tv_show);
            tv.setOnClickListener(this);
        }

        public void setTv(String s){
            tv.setText(s);
        }

        public String getTvText(){return tv.getText().toString();}

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = mParent.listener;
            if (listener!=null){
                listener.onItemClick(this,getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(ProductViewHolder holder,int position);
    }
}
