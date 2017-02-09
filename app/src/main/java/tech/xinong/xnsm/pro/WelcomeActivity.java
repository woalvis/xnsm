package tech.xinong.xnsm.pro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import tech.xinong.xnsm.R;


/**
 * 闪屏页，程序的入口
 */

public class WelcomeActivity extends Activity {

    private ImageView splash_title;
    private ImageView splash_mask;
    private ImageView splash_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //init();
    }



    private void init() {
        splash_logo = (ImageView) findViewById(R.id.splash_logo);
        splash_title = (ImageView) findViewById(R.id.splash_title);
        splash_mask = (ImageView) findViewById(R.id.splash_mask);
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
}
