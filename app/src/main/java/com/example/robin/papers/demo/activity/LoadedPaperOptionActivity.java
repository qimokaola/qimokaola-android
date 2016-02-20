package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.db.NotesDB;

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

    }

    public void deleteDate(String ids) {
        dbWriter.delete(NotesDB.TABLE_NAME, "_id=" + ids, null);
        finish();
    }

}
