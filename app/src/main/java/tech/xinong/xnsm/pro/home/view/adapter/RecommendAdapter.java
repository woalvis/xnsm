package tech.xinong.xnsm.pro.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.home.model.AdsModel;

/**
 * Created by xiao on 2017/10/23.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder> {

    private List<AdsModel> adsModels;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public RecommendAdapter(Context context,List<AdsModel> datas){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        adsModels = datas;
    }

    @Override
    public RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_recommend,parent,false);
        return new RecommendViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(RecommendViewHolder holder, int position) {
        holder.setImg(adsModels.get(position).getCoverImg());
    }

    @Override
    public int getItemCount() {
        return adsModels.size();
    }


    public static class RecommendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RecommendAdapter mParent;
        private SimpleDraweeView imRecommend;


        public RecommendViewHolder(View view, RecommendAdapter recommendAdapter) {
           super(view);
            view.setOnClickListener(this);
            mParent = recommendAdapter;
            imRecommend = (SimpleDraweeView) view.findViewById(R.id.img_recommend);
        }

        public void setImg(String imgPath){
            imRecommend.setImageURI(imgPath);
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = mParent.getmOnItemClickListener();
            if (listener!=null)
                listener.onItemClick(this,getAdapterPosition());
        }
    }

    private OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 单独处理程序接口。与AdapterView不同
     * RecyclerView没有自己的内置接口
     */
    public interface OnItemClickListener{
        void onItemClick(RecommendViewHolder item,int position);
    }

}
