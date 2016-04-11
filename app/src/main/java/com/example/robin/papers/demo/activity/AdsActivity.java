package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.robin.papers.R;
import com.umeng.analytics.MobclickAgent;


//第二启动页 (放广告的页面)
public class AdsActivity extends Activity {
    private RelativeLayout splash_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        //找到splash整个页面的linearlayout
        splash_layout = (RelativeLayout) findViewById(R.id.ads_activity);

        //渐进的效果 两秒钟
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(1000);
        splash_layout.startAnimation(aa);
        // 完成窗体的全屏显示 // 取消掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //用线程让splash页面停留2秒钟再进入RegistActivity
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                Intent intent = new Intent();
//                Class clazz;
//                if (loginState){
//                    clazz = MainActivity.class;
//                }else{
//                    clazz = QuickLoginActivity.class;
//                }
//                intent.setClass(SplashActivity.this,clazz);
//                startActivity(intent);
                startActivity(new Intent(AdsActivity.this, MainActivity.class));
                AdsActivity.this.finish();
            }

        }, 2000);
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
