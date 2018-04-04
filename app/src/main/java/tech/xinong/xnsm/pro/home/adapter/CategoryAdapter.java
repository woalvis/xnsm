package tech.xinong.xnsm.pro.home.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.buy.model.CategoryModel;

/**
 * Created by xiao on 2017/10/30.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<CategoryModel> categoryModels;
    private Context mContext;
    private OnItemClickListener listener;
    private LayoutInflater mInflater;

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public CategoryAdapter(List<CategoryModel> categoryModels){
        this.categoryModels = categoryModels;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mInflater = LayoutInflater.from(mContext);
        View itemView = mInflater.inflate(R.layout.item_follow,parent,false);
        return new CategoryViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.setTvNmae(categoryModels.get(position).getName());
        holder.setImg(categoryModels.get(position).getCoverImg());
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvNmae;
        private SimpleDraweeView img;
        private CategoryAdapter mParent;

        public String getName(){
            return tvNmae.getText().toString().trim();
        }

        public void setTvNmae(String string){
            tvNmae.setText(string);
        }


        public void setImg(String imgPath){
            String imgpath = HttpConstant.HOST+imgPath;
            Uri uri = Uri.parse(imgpath);
            img.setImageURI(uri);
        }

        public CategoryViewHolder(View itemView,CategoryAdapter categoryAdapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            mParent = categoryAdapter;
            tvNmae = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = mParent.listener;
            if (listener!=null){
                listener.onItemClick(this,getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(CategoryViewHolder holder,int position);
    }
}
