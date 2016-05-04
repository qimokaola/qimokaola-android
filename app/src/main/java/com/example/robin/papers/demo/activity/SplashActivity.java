package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.Space;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.model.RemoteAppInfo;
import com.example.robin.papers.demo.util.DownLoader;
import com.example.robin.papers.demo.util.LogUtils;
import com.example.robin.papers.demo.util.OkHttpClientManager;
import com.example.robin.papers.demo.util.SDCardUtils;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


// splash页面
public class SplashActivity extends Activity {

    private static final String Tag = "SplashActivityTag";

    private LinearLayout splash_layout;
    private boolean loginState;

    private SharedPreferences sp;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_splash);
//
////        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
////                WindowManager.LayoutParams.FLAG_FULLSCREEN);
////
////
////        new Handler().postDelayed(new Runnable() {
////
////            @Override
////            public void run() {
////                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
////                SplashActivity.this.startActivity(mainIntent);
////                SplashActivity.this.finish();
////            }
////
////        }, 3000);
//
//
//
//
//
//
//        //找到splash整个页面的linearlayout
//        splash_layout = (LinearLayout) findViewById(R.id.splash_layout);
//
//        //渐进的效果 两秒钟
//        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
//        aa.setDuration(1000);
//        splash_layout.startAnimation(aa);
//        // 完成窗体的全屏显示 // 取消掉状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        //获取登录状态
//        SharedPreferences preferences = getSharedPreferences("loginState",MODE_PRIVATE);
//        loginState = preferences.getBoolean("state",false);
//
//
//        //用线程让splash页面停留2秒钟再进入AdsActivity
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
//                startActivity(new Intent(SplashActivity.this,AdsActivity.class));
//                SplashActivity.this.finish();
//            }
//
//        }, 1000);
//
//
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sp = getSharedPreferences("AppConfig", MODE_PRIVATE);

        OkHttpClientManager.getAsyn(getResources().getString(R.string.remote_app_info_url),
                new OkHttpClientManager.ResultCallback<RemoteAppInfo>() {
                    @Override
                    public void onError(Request request, Exception e) {

                        LogUtils.d(Tag, "出现错误");

                        checkLocalADImage();

                    }

                    @Override
                    public void onResponse(final RemoteAppInfo response) {

                        LogUtils.d(Tag, response.getAdversion() + "");

                        int loaclAdVersion = sp.getInt("ad_version", 0);

                        if (loaclAdVersion < response.getAdversion()) {

                            LogUtils.d(Tag, "本地广告版本小于服务器版本,下载新广告图像");

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        DownLoader.downloadFile(new File(SDCardUtils.getADImage()),
                                                getResources().getString(R.string.ad_image_url));



                                        sp.edit().putInt("ad_version", response.getAdversion()).apply();


                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                afterDownloadADImage(true);
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                afterDownloadADImage(false);
                                            }
                                        });

                                    }



                                }
                            }).start();


                        } else {

                            LogUtils.d(Tag, "本地广告版本不小于服务器版本,跳过下载");

                            checkLocalADImage();

                        }

                    }
                });

    }

    private void afterDownloadADImage(boolean isDownloadedSuccess) {

        Class clazz = isDownloadedSuccess ? AdsActivity.class : MainActivity.class;

        nextActivity(clazz);
    }

    private void nextActivity(Class clazz) {
        final Intent intent = new Intent(SplashActivity.this, clazz);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                });

            }
        }).start();
    }

    /**
     *
     * 获取远程信息失败或者广告版本为最新时, 检查本地广告图片是否存在,若存在则显示广告页,不存在跳至主页
     *
     */
    private void checkLocalADImage() {

        LogUtils.d(Tag, "检测本地广告图像是否存在");

        File adImageFile = new File(SDCardUtils.getADImage());

        Class clazz = adImageFile.exists() ? AdsActivity.class : MainActivity.class;

        nextActivity(clazz);

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
