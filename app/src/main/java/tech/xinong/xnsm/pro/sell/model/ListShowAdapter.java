package tech.xinong.xnsm.pro.sell.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;
import tech.xinong.xnsm.util.ImageUtil;

/**
 * Created by xiao on 2017/12/25.
 */

public class ListShowAdapter extends RecyclerView.Adapter<ListShowAdapter.ListViewHolder> {

    private List<SellerListingInfoDTO> datas;
    private Context mContext;
    private LayoutInflater inflater;
    private OnLlClickListener mListener;




    public ListShowAdapter(Context context,List<SellerListingInfoDTO> datas){
        this.datas = datas;
        mContext = context;
    }


    public void setListener(OnLlClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_show_product,parent,false);
        return new ListViewHolder(view,this);
    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {

        holder.setIm_head(ImageUtil.getImgUrl(datas.get(position).getCoverImg()));
        if (!TextUtils.isEmpty((datas.get(position).getCoverImg()))){
            String imageUrl = ImageUtil.getImgUrl(datas.get(position).getCoverImg());
            holder.setIm_head(ImageUtil.getImgUrl(imageUrl));
        } else {
            XinongHttpCommend.getInstance(mContext).getProductImg(new AbsXnHttpCallback(mContext) {
                @Override
                public void onSuccess(String info, String result) {
                    holder.setIm_head(ImageUtil.getProductImg(result));
                }
            }, datas.get(position).id);
        }


        holder.setTv_price(datas.get(position).getUnitPrice().toString());
        holder.setTv_title(datas.get(position).getTitle());
        if (datas.get(position).isSelect()){
            holder.setImSelect(R.drawable.date_select);
        }else {
            holder.setImSelect(R.drawable.date_default);
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ListShowAdapter mParent;
        private SimpleDraweeView im_head;
        private ImageView im_select;
        private TextView tv_title;
        private TextView tv_price;
        private LinearLayout ll_show;
        public ListViewHolder(View itemView,ListShowAdapter parent) {
            super(itemView);
            mParent = parent;
            im_head = itemView.findViewById(R.id.im_head);
            im_select = itemView.findViewById(R.id.im_select);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
            ll_show = itemView.findViewById(R.id.ll_show);
            ll_show.setOnClickListener(this);
        }


        public void setImSelect(int drawableId){
            im_select.setImageResource(drawableId);
        }

        public void setIm_head(String url){
            im_head.setImageURI(url);
        }

        public void setTv_title(String title){
            tv_title.setText(title);
        }

        public void setTv_price(String price){
            tv_price.setText(price);
        }

        @Override
        public void onClick(View v) {
            final ListShowAdapter.OnLlClickListener listener = mParent.mListener;
            if (listener!=null){
                listener.onItemClick(this,getAdapterPosition());
            }
        }
    }


    public interface OnLlClickListener{
        void onItemClick(ListViewHolder holder,int position);
    }
}
