package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.util.SDCardUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;


//第二启动页 (放广告的页面)
public class AdsActivity extends Activity {

    @Bind(R.id.iv_ad)
    ImageView ivAd;
    @Bind(R.id.ads_activity)
    RelativeLayout adsActivity;

    private RelativeLayout splash_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        ButterKnife.bind(this);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        //找到splash整个页面的linearlayout
//        splash_layout = (RelativeLayout) findViewById(R.id.ads_activity);
//
//        //渐进的效果 两秒钟
//        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
//        aa.setDuration(1000);
//        splash_layout.startAnimation(aa);
//        // 完成窗体的全屏显示 // 取消掉状态栏
//
//        //用线程让splash页面停留2秒钟再进入RegistActivity
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
////                Intent intent = new Intent();
////                Class clazz;
////                if (loginState){
////                    clazz = MainActivity.class;
////                }else{
////                    clazz = QuickLoginActivity.class;
////                }
////                intent.setClass(SplashActivity.this,clazz);
////                startActivity(intent);
//                startActivity(new Intent(AdsActivity.this, MainActivity.class));
//                AdsActivity.this.finish();
//            }
//
//        }, 2000);


//        Picasso.with(AdsActivity.this)
//                .load(new File(SDCardUtils.getADImage()))
//                .into(ivAd);


        File adImageFile = new File(SDCardUtils.getADImage());

        if (adImageFile.exists()) {
            ivAd.setImageURI(Uri.fromFile(adImageFile));
        } else  {
            getSharedPreferences("AppConfig", MODE_PRIVATE).edit().putInt("ad_version", 0);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(AdsActivity.this, MainActivity.class));
                        finish();

                    }
                });

            }
        }).start();

    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
