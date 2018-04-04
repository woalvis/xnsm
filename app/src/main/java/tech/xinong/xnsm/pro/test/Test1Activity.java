package tech.xinong.xnsm.pro.test;

import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.home.model.AdsModel;
import tech.xinong.xnsm.pro.home.view.ImageAdapter;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.CircleIndicator;

/**
 * Created by xiao on 2017/11/17.
 */
@ContentView(R.layout.test1)
public class Test1Activity extends BaseActivity{
    @ViewInject(R.id.iv)
    private SimpleDraweeView iv;
    private List<AdsModel> ads;

    private ViewPager viewPagerBanner;
    private CircleIndicator indianCalendarBanner;
    private int currentPosition;
    @Override
    public void initData() {
        viewPagerBanner = (ViewPager)findViewById(R.id.view_pager);
        indianCalendarBanner = (CircleIndicator)findViewById(R.id.circle_indicator);

        setViewPagerBanner();


//        iv.setController(controller);
        iv.setImageURI("https://cang.xinongtech.com/img/bg.jpg");
    }

    ControllerListener listener = new BaseControllerListener(){
        @Override
        public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {
            super.onIntermediateImageFailed(id, throwable);
        }
    };




    /**
     * 初始化viewpager，给他设置轮播监听
     * @param
     */
    private void setViewPagerBanner(){
        String[] productNames = {""};
        XinongHttpCommend.getInstance(mContext).campaigns(productNames, new AbsXnHttpCallback(mContext) {
            @Override
            public void onSuccess(String info, String result) {
                //JSONObject jb = JSON.parseObject(result);
                ads = JSON.parseArray(result,AdsModel.class);
                List<View> views = new ArrayList<>();
                for (int i=0;i<ads.size();i++){
                    View view = LayoutInflater.from(mContext).inflate(R.layout.item_banner,null,false);
                    SimpleDraweeView sdv = (SimpleDraweeView) view.findViewById(R.id.im_banner);
                    sdv.setController(setListener(ads.get(i).getCoverImg()));
                    views.add(view);
                }

                viewPagerBanner.setAdapter(new ImageAdapter(mContext,ads,views));
                indianCalendarBanner = (CircleIndicator)findViewById(R.id.circle_indicator);
                indianCalendarBanner.setViewPager(viewPagerBanner);
                autoPlay();
                viewPagerBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if(position==0){    //判断当切换到第0个页面时把currentPosition设置为images.length,即倒数第二个位置，小圆点位置为length-1
                            currentPosition = ads.size();
                        }else if(position==ads.size()+1){//当切换到最后一个页面时currentPosition设置为第一个位置，小圆点位置为0
                            currentPosition=1;
                        }else{
                            currentPosition=position;
                        }
                    }
                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if(state== ViewPager.SCROLL_STATE_IDLE){
                            viewPagerBanner.setCurrentItem(currentPosition,false);
                        }
                    }
                });
            }
        });
    }


    /**
     * 设置线程，自动轮播的时间间隔
     */
    private void autoPlay() {
        new Thread(){
            @Override
            public void run() {
                super.run();

                while(true){
                    SystemClock.sleep(3000);
                    if (currentPosition>=ads.size()-1){
                        currentPosition = -1;
                    }
                    currentPosition++;
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                viewPagerBanner.setCurrentItem(currentPosition,false);
            }
        }
    };



    private DraweeController setListener(String uri){
        DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(listener)
                .build();
        return controller1;
    }







}
