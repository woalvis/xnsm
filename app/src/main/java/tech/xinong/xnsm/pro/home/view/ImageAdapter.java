package tech.xinong.xnsm.pro.home.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tech.xinong.xnsm.pro.home.model.AdsModel;

/**
 * Created by xiao on 2016/11/7.
 */

public class ImageAdapter  extends PagerAdapter {

    private Context mContext;

    private List<AdsModel> adsModels;
    private List<View> views;

    public ImageAdapter(Context context, List<AdsModel> adsModels,List<View> views){
        super();
        mContext = context;
        this.adsModels = adsModels;
        this.views = views;
    }

    @Override
    public int getCount() {
        return adsModels.size();
    }



    /**
     * 如果在viewpager内一次显示超过一页的内容，那么需要重写该方法
     */
    @Override
    public float getPageWidth(int position) {
        return 1f;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        //检查从instantiateItem()返回的对象与添加到容器相应位置的视图是否是同一个
        //对象。我们的实例在这两个地方使用的是同一个对象
        return (view == object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_banner,container,false);
//
//
//        //创建一个新的ImageView并把它添加到提供的容器中
//        SimpleDraweeView iv = (SimpleDraweeView) view.findViewById(R.id.im_banner);
//        //设置此位置的内容
//        iv.setImageURI(adsModels.get(position).getCoverImg());
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        //iv.setBackgroundColor(COLORS[position]);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//        iv.setLayoutParams(params);


        /**
         * 给活动轮番页添加点击事件
         */
//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("url",adsModels.get(position).getContentUrl());
//                mContext.startActivity(intent);
//            }
//        });
        //这里你必须自己添加视图，Android框架是不会为你添加的
        container.addView(views.get(position));
        //将这个视图作为这个位置的键对象返回
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
