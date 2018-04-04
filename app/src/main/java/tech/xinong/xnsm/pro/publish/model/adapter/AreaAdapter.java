package tech.xinong.xnsm.pro.publish.model.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.model.Area;

/**
 * Created by xiao on 2017/11/30.
 */

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaHolder> {
    private List<Area> areas;
    private Context mContext;
    public OnItemClickListener listener;
    private LayoutInflater inflater;
    private int defItem = -1;

    public AreaAdapter(List<Area> areas){
        this.areas = areas;
    }

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public OnItemClickListener getListener(){
        return listener;
    }

    @Override
    public AreaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_text_model,parent,false);
        return new AreaHolder(view,this);
    }

    @Override
    public void onBindViewHolder(AreaHolder holder, int position) {
        holder.setTv(areas.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return areas==null?0:areas.size();
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    public void reFresh(List<Area> areas){
        this.areas = areas;
        notifyDataSetChanged();
    }

    public static class AreaHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private AreaAdapter mParent;
        private TextView tv;
        public AreaHolder(View itemView,AreaAdapter adapter) {
            super(itemView);
            mParent = adapter;
            tv = itemView.findViewById(R.id.tv);
            tv.setOnClickListener(this);
        }

        public void setTv(String text){
            tv.setText(text);
        }

        @Override
        public void onClick(View view) {
            final OnItemClickListener listener = mParent.getListener();
            if (listener!=null){
                listener.onClick(this,getAdapterPosition());
            }
        }
    }


    public interface OnItemClickListener{
        void onClick(AreaHolder holder,int position);
    }
}
