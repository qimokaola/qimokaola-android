package com.example.robin.papers.demo.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.util.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends FragmentActivity {

    //底部的tab控件
    private FragmentTabHost mTabHost;
    private LayoutInflater mLayoutInflater;

    //三个切换的页面的fragment.
    private Class mFragmentArray[] = { FragmentResource.class,
            FragmentLocalResource.class, FragmentMe.class };
    //tab栏图标
    private int mImageArray[] = { R.drawable.select_bar_ziyuan, R.drawable.select_bar_bendi,  R.drawable.select_img_bottom_bar_fuwu};

    //tab栏的字
    private String mTextArray[] = { "资源", "本地", "服务"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        initView();

        //        //透明状态栏   要搞沉浸式状态栏的 失败了 这几行代码
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);




//        //友盟推送
//        PushAgent mPushAgent = PushAgent.getInstance(getApplication());
//        mPushAgent.enable();
//        Log.d("MainActivity", mPushAgent.isEnabled() ? "Enable" : "No");
//		mPushAgent.setPushCheck(true);    //默认不检查集成配置文件
//		mPushAgent.setLocalNotificationIntervalLimit(false);  //默认本地通知间隔最少是10分钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏透明 需要在创建SystemBarTintManager 之前调用。
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
            tintManager.setStatusBarTintResource(R.color.barcolor);
            // 设置状态栏的文字颜色
            tintManager.setStatusBarDarkMode(true, this);
        }

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView() {
        mLayoutInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        mTabHost.getTabWidget().setShowDividers(0);



        int count = mFragmentArray.length;
        for (int i = 0; i < count; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i])
                    .setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }

    private View getTabItemView(int index) {
        View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageArray[index]);
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)  {
            showExitDialog();
        }
        return false;
    }




    private void showExitDialog()  {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("退出福大历年卷?!!");
        builder.setNegativeButton("不要", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setPositiveButton("是的!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                System.exit(0);
            }
        });
        builder.create().show();
    }

}
