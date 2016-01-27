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

public class CloudPrintActivity extends Activity {
    private ImageView bar_cloud_print_back_iv;
    private LinearLayout cloud_print_weixin_ll,cloud_print_qq_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_print);

        bar_cloud_print_back_iv = (ImageView) findViewById(R.id.bar_cloud_print_back_iv);
        cloud_print_qq_ll = (LinearLayout) findViewById(R.id.cloud_print_qq_ll);
        cloud_print_weixin_ll = (LinearLayout) findViewById(R.id.cloud_print_weixin_ll);

        cloud_print_weixin_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) CloudPrintActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("961727923");
                Toast.makeText(getApplicationContext(), "微信打印帐号已复制到粘帖板", Toast.LENGTH_SHORT).show();
            }
        });

        cloud_print_qq_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击复制字符串到粘贴板
                ClipboardManager copy = (ClipboardManager) CloudPrintActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText("961727923");
                Toast.makeText(getApplicationContext(), "QQ打印帐号已复制到粘帖板", Toast.LENGTH_SHORT).show();
            }
        });



        bar_cloud_print_back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
