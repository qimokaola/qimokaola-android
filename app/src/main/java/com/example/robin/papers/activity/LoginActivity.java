package com.example.robin.papers.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.util.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.user_phone_num)
    AppCompatAutoCompleteTextView userPhoneNum;
    @Bind(R.id.user_psw)
    AppCompatAutoCompleteTextView userPsw;
    @Bind(R.id.login_btn)
    Button loginBtn;
    @Bind(R.id.user_info)
    LinearLayout userInfo;
    @Bind(R.id.forget_psw)
    TextView forgetPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏透明 需要在创建SystemBarTintManager 之前调用。
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
            tintManager.setStatusBarTintResource(R.color.white);
            // 设置状态栏的文字颜色
            tintManager.setStatusBarDarkMode(true, this);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //"忘记密码" 加下划线
        forgetPsw.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        forgetPsw.getPaint().setAntiAlias(true);//抗锯齿
        

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

    @OnClick({R.id.back, R.id.user_phone_num, R.id.user_psw, R.id.login_btn, R.id.user_info, R.id.forget_psw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.user_phone_num:
                break;
            case R.id.user_psw:
                break;
            case R.id.login_btn:
                break;
            case R.id.user_info:
                break;
            case R.id.forget_psw:
                break;
        }
    }
}
