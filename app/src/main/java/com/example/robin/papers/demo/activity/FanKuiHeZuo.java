package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.robin.papers.R;

public class FanKuiHeZuo extends Activity {
    private LinearLayout fan_kui_he_zuo_qqnumber_ll;
    private ImageView fan_kui_he_zuo_back_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_kui_he_zuo);

        fan_kui_he_zuo_qqnumber_ll = (LinearLayout) findViewById(R.id.fan_kui_he_zuo_qqnumber_ll);
        fan_kui_he_zuo_back_iv = (ImageView) findViewById(R.id.fan_kui_he_zuo_back_iv);

        fan_kui_he_zuo_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fan_kui_he_zuo_qqnumber_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) FanKuiHeZuo.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("596954885");
                Toast.makeText(getApplicationContext(), "QQ号已复制到粘帖板", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
