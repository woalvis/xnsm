package tech.xinong.xnsm.pro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Properties;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.Util.ProperTies;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;


/**
 * 闪屏页，程序的入口
 */
@ContentView(R.id.activity_welcome)
public class WelcomeActivity extends BaseActivity {

    private ImageView splash_title;
    private ImageView splash_mask;
    private ImageView splash_logo;

    @Override
    public void initWidget() {
        init();
    }

    @Override
    public void initData() {

    }

    private void init() {
        getFavs();
        splash_logo =findViewById(R.id.splash_logo);
        splash_title = findViewById(R.id.splash_title);
        splash_mask = findViewById(R.id.splash_mask);
        ObjectAnimator objectAnimatorLogo = ObjectAnimator.ofFloat(splash_logo,"alpha",1.0f,0.0f);
        ObjectAnimator objectAnimatorTitle = ObjectAnimator.ofFloat(splash_title,"alpha",0.0f,1.0f);
        objectAnimatorLogo.setDuration(3000);
        objectAnimatorTitle.setDuration(3000);
        objectAnimatorLogo.start();
        objectAnimatorTitle.start();

        objectAnimatorLogo.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);



                Intent intent = new Intent(WelcomeActivity.this,GuideActivity.class);
                startActivity(intent);
            }

        });


        final Animation mask = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },2000);
    }



    public String[] getFavs(){
        Properties proper = ProperTies.getProperties(mContext);
        String favString = proper.getProperty(XnsConstant.FAVS);
        Log.i("TAG", "favString=" + favString);
        mSharedPreferences = getSharedPreferences(XnsConstant.SP_NAME, MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.putString(XnsConstant.FAVS,favString);
        editor.commit();
        return favString.split(",");
    }
}
