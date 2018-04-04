package tech.xinong.xnsm.pro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.List;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.buy.model.SellerListingInfoDTO;

/**
 * Created by xiao on 2017/11/16.
 */

public class ProductShowAdapter extends BaseAdapter {

    private List<SellerListingInfoDTO> infos;
    private Context mContext;
    private LayoutInflater inflater;

    public ProductShowAdapter(Context context,List<SellerListingInfoDTO> infos){
        mContext = context;
        this.infos = infos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public SellerListingInfoDTO getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_product_show,null);
            vh = new MyViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (MyViewHolder) convertView.getTag();
        }
        SellerListingInfoDTO item = getItem(position);
        vh.product_address.setText(item.getAddress());
        vh.product_desc.setText(item.getTitle()+position);
        vh.product_price.setText(item.getUnitPrice()+"元/"+item.getWeightUnit().getDisplayName());
        if (!TextUtils.isEmpty(item.getCoverImg())) {
            vh.draweeView.setImageURI(HttpConstant.HOST+"/documents/"+item.getCoverImg());
        }else{
            vh.draweeView.setImageResource(R.mipmap.default_pic_bg);
        }
        return convertView;
    }
    public static class MyViewHolder{
        SimpleDraweeView draweeView;
        TextView product_desc;
        TextView product_address;
        TextView product_price;
        public MyViewHolder(View convertView){
            draweeView = (SimpleDraweeView) convertView.findViewById(R.id.product_iv_show);
            product_desc = (TextView) convertView.findViewById(R.id.product_desc);
            product_address = (TextView) convertView.findViewById(R.id.product_address);
            product_price = (TextView) convertView.findViewById(R.id.product_price);
        }
    }
}
