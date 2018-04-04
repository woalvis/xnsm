package tech.xinong.xnsm.pro.publish.model.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;

/**
 * Created by xiao on 2017/10/26.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryModel> categories;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener mListener;
    public Context mContext;
    private int defItem = -1;

    public CategoryAdapter(List<CategoryModel> categories){
        this.categories = categories;
    }
    public OnItemClickListener getListener(){
        return mListener;
    }
    public void setListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(parent.getContext());
        View view = mLayoutInflater.inflate(R.layout.item_text,parent,false);
        return new CategoryViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.setText(categories.get(position).getDescription());
        if (defItem != -1) {
            if (defItem == position) {
                holder.tv.setTextColor(mContext.getResources().getColor(R.color.green_btn_color));
                holder.tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.select_bg));
            } else {
                holder.tv.setTextColor(Color.parseColor("#000000"));
                holder.tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.item_text_bg));
            }
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CategoryAdapter mParent;
        private TextView tv;

        public CategoryViewHolder(View view,CategoryAdapter adapter) {
            super(view);
            mParent = adapter;
            tv = view.findViewById(R.id.item_tv);
            view.setOnClickListener(this);
        }

        public void setText(String string){
            tv.setText(string);
        }

        public void selected(){
           tv.setTextColor(mParent.mContext.getResources().getColor(R.color.green_btn_color));
        }

        public String getText(){
            return tv.getText().toString().trim();
        }
        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = mParent.getListener();
            if (listener!=null){
                listener.onItemClick(this,getAdapterPosition());
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(CategoryViewHolder item,int position);
    }
}
