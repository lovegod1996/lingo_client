package com.lovegod.newbuy.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.lovegod.newbuy.MainActivity;
import com.lovegod.newbuy.R;
import com.lovegod.newbuy.bean.Commodity;
import com.lovegod.newbuy.commen.Commen;
import com.lovegod.newbuy.utils.system.SpUtils;
import com.lovegod.newbuy.view.welcome.WelcomeGuideActivity;
import com.squareup.picasso.Picasso;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by PandaQ on 2017/2/21.
 * 闪屏页，优化启动体验
 */

public class SplashActivity extends BaseActivity {
//    @BindView(R.id.iv_splash_image)
//    ImageView mIvSplashImage;
    @BindView(R.id.iv_background)
    ImageView mIvBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, Commen.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //隐藏ActionBar
        getSupportActionBar().hide();

        ButterKnife.bind(this);
        String[] images = getResources().getStringArray(R.array.splash_background);
        int position = new Random().nextInt(images.length - 1) % (images.length);
        Picasso.with(this)
                .load(images[position])
                .into(mIvBackground);
//        Picasso.with(this)
//                .load("file://" + ViewUtils.getAppFile(this, "images/user.png"))
//                .error(getResources().getDrawable(R.drawable.userimage))
//                .into(mIvSplashImage);
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(1000);
        mIvBackground.startAnimation(animation);
        animation.setAnimationListener(new AnimationImpl());

    }

    private class AnimationImpl implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            //如果启动app的Intent中带有额外的参数，表明app是从点击通知栏的动作中启动的
            //将参数取出，传递到MainActivity中
            if(getIntent().getSerializableExtra("commodity") != null){
                Commodity commodity= (Commodity) getIntent().getSerializableExtra("commodity");
                Bundle bundle=new Bundle();
                bundle.putSerializable("commodity",commodity);
                intent.putExtras(bundle);
            }
            startActivity(intent);
            SplashActivity.this.finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

}
