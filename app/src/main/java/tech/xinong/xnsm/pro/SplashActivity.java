package tech.xinong.xnsm.pro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.application.XnsApplication;

public class SplashActivity extends AppCompatActivity {
    private ImageView splash_title;
    private ImageView splash_mask;
    private ImageView splash_logo;
    private ImageView splash_mask_hidden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }



    private void init() {
        splash_logo = (ImageView) findViewById(R.id.splash_logo);
        splash_title = (ImageView) findViewById(R.id.splash_title);
        splash_mask = (ImageView) findViewById(R.id.splash_mask);
        splash_mask_hidden = (ImageView) findViewById(R.id.splash_mask_hidden);


        final Animation mask = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);

        splash_mask_hidden.setAnimation(mask);

        mask.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splash_mask_hidden.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        ObjectAnimator objectAnimatorLogo = ObjectAnimator.ofFloat(splash_logo,"alpha",0.0f,1.0f);
        ObjectAnimator objectAnimatorTitle = ObjectAnimator.ofFloat(splash_title,"alpha",0.0f,1.0f);
        objectAnimatorLogo.setDuration(3000);
        objectAnimatorTitle.setDuration(3000);
        objectAnimatorLogo.start();
        objectAnimatorTitle.start();

        objectAnimatorLogo.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent;
                if (TextUtils.isEmpty(XnsApplication.mInstance.token)){
                    intent = new Intent(SplashActivity.this,GuideActivity.class);
                }else {
                    intent = new Intent(SplashActivity.this,MainActivity.class);
                }


                startActivity(intent);
                SplashActivity.this.finish();
            }

        });




//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        },2000);
    }
}
