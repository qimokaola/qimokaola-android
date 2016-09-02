package com.example.robin.papers.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.robin.papers.R;
import com.example.robin.papers.model.RemoteAppInfo;
import com.example.robin.papers.util.DownLoader;
import com.example.robin.papers.util.LogUtils;
import com.example.robin.papers.util.OkHttpClientManager;
import com.example.robin.papers.util.SDCardUtils;
import com.squareup.okhttp.Request;
import com.umeng.analytics.MobclickAgent;

import java.io.File;


// splash页面
public class SplashActivity extends Activity {

    private static final String Tag = "SplashActivityTag";
    private SharedPreferences sp;

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

                        if (!response.isDisplay()) {

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
                                            nextActivity(LoginActivity.class);   //不显示广告,跳入mainactivity
                                        }
                                    });
                                }
                            }).start();

                            return;
                        }

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

        Class clazz = adImageFile.exists() ? AdsActivity.class : MainActivity.class;    //本地图片不存在, 跳转至mainactivity

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
