package tech.xinong.xnsm.pro.home.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import tech.xinong.xnsm.R;

/**
 * Created by xiao on 2016/11/7.
 */

public class ImageAdapter  extends PagerAdapter {

    private Context mContext;
    public static final int[] IMAGES = {
            R.mipmap.banner1,
            R.mipmap.banner2,
            R.mipmap.banner3,
            R.mipmap.banner4
    };
    private static final int[] COLORS = {Color.GREEN, Color.RED, Color.WHITE, Color.BLUE,Color.MAGENTA};

    public ImageAdapter(Context context){
        super();
        mContext = context;
    }

    @Override
    public int getCount() {
        return IMAGES.length;
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
    public Object instantiateItem(ViewGroup container, int position) {
        //创建一个新的ImageView并把它添加到提供的容器中
        ImageView iv = new ImageView(mContext);
        //设置此位置的内容
        iv.setImageResource(IMAGES[position]);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        //iv.setBackgroundColor(COLORS[position]);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(params);
        //这里你必须自己添加视图，Android框架是不会为你添加的
        container.addView(iv);
        //将这个视图作为这个位置的键对象返回
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
