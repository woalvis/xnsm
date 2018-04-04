package tech.xinong.xnsm.pro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;

public class GuideActivity extends Activity{
    //引导页的资源集合
    private List<Integer> mImages;
    //引导页的图片集合
    private List<ImageView> mImageList;
    //分页用的ViewPager
    private ViewPager mViewPager;
    private GuideActivity context;
    private int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        context = this;
        initImageList();
        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(new GuideAdapter(this,mImages,mImageList));
    }

    /**
     * 初始化数据到集合中
     */
    private void initImageList() {
        mImages = new ArrayList<>();
        //添加资源到集合中
        mImages.add(R.mipmap.introduce_16_9_page_1);
        mImages.add(R.mipmap.introduce_16_9_page_2);
        mImages.add(R.mipmap.introduce_16_9_page_3);

        mImageList = new ArrayList<>();
        for (Integer idRes : mImages){
            ImageView imageView = new ImageView(this);
            mImageList.add(imageView);
        }

    }

    /**
     * Adapter数据
     * 目的：创建并且显示每一个分页
     */
    public class GuideAdapter extends PagerAdapter{

        private Context mContext;
        private List<ImageView> mImageViewList;
        private List<Integer> mImages;

        public GuideAdapter(Context context,List<Integer> images,List<ImageView> imageViewList){
            mContext = context;
            mImageViewList = imageViewList;
            mImages = images;
        }


        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }


        //创建和绑定每一个分页
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViewList.get(position);
            currentPosition = position;
            if (position==mImageViewList.size()-1){
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      mContext.startActivity(new Intent(mContext,MainActivity.class));
                      context.finish();
                    }
                });
            }else {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition++;
                    }
                });
            }
            imageView.setImageResource(mImages.get(currentPosition));
            //填充视图，超过的部分就截取掉---而且保持图片不变形
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //绑定视图
            container.addView(imageView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }

        //销毁
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViewList.get(position));
        }
    }
}
