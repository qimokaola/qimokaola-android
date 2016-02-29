package com.example.robin.papers.demo.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.db.NotesDB;
import com.example.robin.papers.demo.util.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

public class LoadedPaperOptionActivity extends Activity {

    private String type,ids,localurl,name;
    private ImageView ivType,back;
    private Button btnOpen,btnDelete;
    private TextView tvName;
    private SQLiteDatabase dbWriter;
    private NotesDB notesDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_paper_option);
        type = getIntent().getStringExtra("type");
        ids = getIntent().getStringExtra("id");
        localurl = getIntent().getStringExtra("localUrl");
        name = getIntent().getStringExtra("name");
        btnOpen = (Button) findViewById(R.id.dakai);
        btnDelete = (Button) findViewById(R.id.shanchu);
        ivType = (ImageView) findViewById(R.id.detailType);
        back = (ImageView) findViewById(R.id.detailBack);
        tvName = (TextView) findViewById(R.id.detailname);
        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();
        tvName.setText(name);

        if (type.equals("doc")){
            ivType.setImageResource(R.drawable.doc);
        }
        if (type.equals("docx")){
            ivType.setImageResource(R.drawable.doc);
        }
        if (type.equals("ppt")){
            ivType.setImageResource(R.drawable.ppt);
        }
        if (type.equals("pptx")){
            ivType.setImageResource(R.drawable.ppt);
        }
        if (type.equals("pdf")){
            ivType.setImageResource(R.drawable.pdf);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //打开下载好的试卷的代码
                    Uri uri = Uri.fromFile(new File(localurl));
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDate(ids);
            }
        });
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

    public void deleteDate(String ids) {
        dbWriter.delete(NotesDB.TABLE_NAME, "_id=" + ids, null);
        finish();
    }

}
