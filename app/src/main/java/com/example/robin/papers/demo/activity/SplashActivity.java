package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.example.robin.papers.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;


//如果要去掉短信页面  一定要先置入友盟统计

public class SplashActivity extends Activity {

    private LinearLayout splash_layout;
    private boolean loginState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                SplashActivity.this.startActivity(mainIntent);
//                SplashActivity.this.finish();
//            }
//
//        }, 3000);






        //找到splash整个页面的linearlayout
        splash_layout = (LinearLayout) findViewById(R.id.splash_layout);

        //渐进的效果 两秒钟
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(1000);
        splash_layout.startAnimation(aa);
        // 完成窗体的全屏显示 // 取消掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //获取登录状态
        SharedPreferences preferences = getSharedPreferences("loginState",MODE_PRIVATE);
        loginState = preferences.getBoolean("state",false);


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
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                SplashActivity.this.finish();
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
