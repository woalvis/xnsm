package tech.xinong.xnsm.pro.home.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

import tech.xinong.xnsm.pro.home.view.H5Activity;
import tech.xinong.xnsm.util.ImageUtil;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 */
public class NetworkImageHolderView implements Holder<AdsModel> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(final Context context, int position, final AdsModel data) {
//        imageView.setImageResource(R.drawable.ic_default_adimage);
//        ImageLoader.getInstance().displayImage(data,imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                H5Activity.skip(context,data.getContentUrl(),data.getCustomerId());
            }
        });
        Glide.with(context).load(ImageUtil.getImgUrl(data.getCoverImg())).into(imageView);
    }
}
