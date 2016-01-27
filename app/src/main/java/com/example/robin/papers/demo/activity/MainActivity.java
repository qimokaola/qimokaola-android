package com.example.robin.papers.demo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends FragmentActivity {

    //底部的tab控件
    private FragmentTabHost mTabHost;
    private LayoutInflater mLayoutInflater;

    //三个切换的页面的fragment.
    private Class mFragmentArray[] = { TimeTableFragment.class,ResourceFragment.class, MyFragment.class,
            FindFragment.class };

    //tab栏图标 未点击时的图片
    private int mImageArray[] = { R.drawable.select_kebiao,R.drawable.select_bar_ziyuan, R.drawable.select_bar_fuwu,  R.drawable.select_img_bottom_bar_wolo};


    //tab栏的字
    private String mTextArray[] = { "课表","资源", "我的", "更多"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //透明状态栏   要搞沉浸式状态栏的 失败了 这几行代码
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
        setContentView(R.layout.activity_main);
        initView();
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
