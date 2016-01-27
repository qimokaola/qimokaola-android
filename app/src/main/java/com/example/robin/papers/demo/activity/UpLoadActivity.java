package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robin.papers.R;
//上传页面
public class UpLoadActivity extends Activity {
    private ImageView back,copyIvUp;
    private TextView copyCue,copyTvUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        back = (ImageView) findViewById(R.id.sleepBack2);
        copyIvUp = (ImageView) findViewById(R.id.copyIvUp);
        copyCue = (TextView) findViewById(R.id.copyCue);
        copyTvUp = (TextView) findViewById(R.id.copyTvUp);

        //qq号码的那个tv
        copyTvUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) UpLoadActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("596954885");
                Toast.makeText(getApplicationContext(),"QQ号已复制到粘帖板",Toast.LENGTH_SHORT).show();
            }
        });

        //点击复制QQ号的那个textview
        copyCue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) UpLoadActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("596954885");
                Toast.makeText(getApplicationContext(),"QQ号已复制到粘帖板",Toast.LENGTH_SHORT).show();
            }

        });

        //qq图标点击复制QQ号
        copyIvUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) UpLoadActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("596954885");
                Toast.makeText(getApplicationContext(),"QQ号已复制到粘帖板",Toast.LENGTH_SHORT).show();
            }
        });

        //返回键
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
