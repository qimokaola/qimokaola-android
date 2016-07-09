package com.example.robin.papers.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.robin.papers.R;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


//注册页面  暂时弃用
public class RegisterActivity extends Activity implements View.OnClickListener{

    private Button getCode,submit;
    private EditText phoneNum,code;
    public static final String APP_KEY = "a9ff68abceeb";
    public static final String APP_SECRET = "46447e17193b13a87930881e92fd8571";
    public static final String TAG = "RegisterActivity";
    private String phoneNums,coder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        phoneNum = (EditText) findViewById(R.id.phoneNumber);
        getCode = (Button) findViewById(R.id.getCode);
        code = (EditText) findViewById(R.id.code);
        submit = (Button) findViewById(R.id.submit);

        getCode.setOnClickListener(this);
        submit.setOnClickListener(this);

        //初始化短信验证
        SMSSDK.initSDK(this, APP_KEY, APP_SECRET);

        EventHandler eh= new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        //验证成功 写入登录状态值 文件名loginstate key:state
                        SharedPreferences preferences = getSharedPreferences("loginState",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("state",true);
                        editor.commit();


                        //提交的验证码验证成功   跳转到main界面  号码和密码已经在putextra里
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intentToMainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intentToMainActivity);
                                finish();
                            }
                        });



                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        Log.d(TAG,"获取验证码成功,"+event+","+"result");
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                        Log.d(TAG,"返回国家列表,"+event+","+"result");
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调


    }

    //反注册短信验证系统
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    //按钮监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.getCode:
                Toast.makeText(getApplication(),"如果长时间未收到短信, 请重启应用再次发送.",Toast.LENGTH_SHORT).show();
                phoneNums = phoneNum.getText().toString().trim();
                SMSSDK.getVerificationCode("86", phoneNums);
                getCode.setText("已发送,请安候");
                getCode.setEnabled(false);
                break;
            case R.id.submit:
                coder = code.getText().toString().trim();
                SMSSDK.submitVerificationCode("86", phoneNums, coder);
                break;
        }
    }
}
