package com.example.robin.papers.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robin.papers.R;
import com.example.robin.papers.db.NotesDB;
import com.example.robin.papers.db.OrderDB;
import com.example.robin.papers.util.DownLoader;
import com.example.robin.papers.util.SystemBarTintManager;
import com.example.robin.papers.util.UrlUnicode;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


// 资源页最后一级的 操作页面   下载到手机或者发送到电脑
public class OpActivity extends Activity {
    private Button downloadBtn,sendBtn,printBtn;
    private ImageView typeIv,opBack;
    private TextView nameTv;
    private String paperName, paperurl, wpurl, type;
    private OrderDB orderDB;
    private SQLiteDatabase dbWriter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papers_option);

        //PapersLIstActivity传过来的试卷信息
        paperName = getIntent().getStringExtra("paperName");
        paperurl = getIntent().getStringExtra("paperurl");
        wpurl = getIntent().getStringExtra("wpurl");
        type = getIntent().getStringExtra("type");


        //初始化各控件
        downloadBtn = (Button) findViewById(R.id.opDownload);
        sendBtn = (Button) findViewById(R.id.opSend);
        printBtn = (Button) findViewById(R.id.addToPrint);
        typeIv = (ImageView) findViewById(R.id.opDetailType);
        nameTv = (TextView) findViewById(R.id.opDetailname);
        opBack = (ImageView) findViewById(R.id.opBack);


        //如果是压缩包,隐藏下载按钮
        if (type.equals("zip")||type.equals("rar")||type.equals("7z")){
            downloadBtn.setVisibility(View.GONE);
        }

        //设置试卷名
        nameTv.setText(paperName);

        //设置类型图片
        if (type.equals("doc")){
            typeIv.setImageResource(R.drawable.document_type_word);
        }
        if (type.equals("docx")){
            typeIv.setImageResource(R.drawable.document_type_word);
        }
        if (type.equals("ppt")){
            typeIv.setImageResource(R.drawable.document_type_ppt);
        }
        if (type.equals("pptx")){
            typeIv.setImageResource(R.drawable.document_type_ppt);
        }
        if (type.equals("pdf")){
            typeIv.setImageResource(R.drawable.document_type_pdf);
        }
        if (type.equals("zip")){
            typeIv.setImageResource(R.drawable.document_type_zip);
        }

        //返回按钮事件
        opBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //下载按钮点击事件
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadBtn.setText("已开始下载~");
                downloadBtn.setEnabled(false);
                if (type.equals("zip")){
                    Toast.makeText(getApplicationContext(),"zip文件只支持发送到电脑噢",Toast.LENGTH_LONG).show();
                }
                if (type.equals("doc")||type.equals("ppt")||type.equals("pdf")||type.equals("pptx")||type.equals("docx")){
                    new downAsnycTask().execute(paperurl, paperName, type, wpurl);
                    Toast.makeText(getApplicationContext()," 开始下载,请稍后到\"下载\"中查看 ",Toast.LENGTH_LONG).show();
                }
            }
        });


        //发送按钮点击事件
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    sendBtn.setEnabled(false);
                    //发送paperurl到我的电脑;
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setPackage("com.tencent.mobileqq");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                    intent.putExtra(Intent.EXTRA_TEXT, paperName + ": " + UrlUnicode.encode(wpurl));
                    intent.putExtra(Intent.EXTRA_TITLE, "发至电脑");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, "选择\"发送到我的电脑\""));
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"你没有安装QQ",Toast.LENGTH_LONG).show();
                }
            }
        });

        //加入打印订单
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //TODO: Check Error
//                SQLiteDatabase dbWriter;
//                dbWriter = orderDB.getWritableDatabase();
//                ContentValues cv = new ContentValues();
//                cv.put(OrderDB.PAPERNAME, paperName);
//                cv.put(OrderDB.PAGE, -1);
//                cv.put(OrderDB.COUNT, 1);
//                dbWriter.insert(OrderDB.TABLE_NAME, null, cv);
                  if (!type.equals("zip")){
                      addToOrderDB();
                  }else {
                      Toast.makeText(getApplicationContext(),"zip文件不支持打印!!",Toast.LENGTH_LONG).show();
                  }

            }
        });

        //
        orderDB = new OrderDB(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //状态栏透明 需要在创建SystemBarTintManager 之前调用。
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //使StatusBarTintView 和 actionbar的颜色保持一致，风格统一。
            tintManager.setStatusBarTintResource(R.color.barcolorAndContent);
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

    //加入到打印订单(ordersDB)
    public void addToOrderDB() {
        ContentValues cv = new ContentValues();
        cv.put(OrderDB.PAPERNAME,paperName);
        cv.put(OrderDB.TYPE,type);

        cv.put(OrderDB.PAGE,10);//10要换成具体的数值

        cv.put(OrderDB.COUNT, 1);
        orderDB = new OrderDB(OpActivity.this);
        dbWriter2 = orderDB.getWritableDatabase();
        dbWriter2.insert(OrderDB.TABLE_NAME,null,cv);
        Toast.makeText(getApplicationContext(),"已加入到打印订单",Toast.LENGTH_SHORT).show();

    }





    //异步下载文件
    class downAsnycTask extends AsyncTask<String,Void,File> {

        private String url,paperName,type,wpurl,time,localurl;
        private NotesDB notesDB;
        private SQLiteDatabase dbWriter;
        @Override
        protected File doInBackground(String... params) {
            url = params[0];
            paperName = params[1];
            type = params[2];
            wpurl =params[3];
            time = getTime();

            localurl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+paperName;
            File file;

            if (type.equals("doc")){
                localurl += ".doc";
                file = new File(localurl);
                try {
                    file = DownLoader.downloadFile(file, UrlUnicode.encode(url));
                    addDB();
                    return file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (type.equals("docx")){
                localurl += ".docx";
                file = new File(localurl);
                try {
                    file = DownLoader.downloadFile(file, UrlUnicode.encode(url));
                    addDB();
                    return file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (type.equals("ppt")){
                localurl += ".ppt";
                file = new File(localurl);
                try {
                    file = DownLoader.downloadFile(file,UrlUnicode.encode(url));
                    addDB();
                    return file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (type.equals("pptx")){
                localurl += ".pptx";
                file = new File(localurl);
                try {
                    file = DownLoader.downloadFile(file,UrlUnicode.encode(url));
                    addDB();
                    return file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (type.equals("pdf")){
                localurl += ".pdf";
                file = new File(localurl);
                try {
                    file = DownLoader.downloadFile(file,UrlUnicode.encode(url));
                    addDB();
                    return file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final File file) {
        }




        private void addDB() {
            ContentValues cv = new ContentValues();
            cv.put(NotesDB.PAPERNAME,paperName);
            cv.put(NotesDB.QNURL,url);
            cv.put(NotesDB.WPURL,wpurl);
            cv.put(NotesDB.LOCALURL,localurl);
            cv.put(NotesDB.TYPE,type);
            cv.put(NotesDB.TIME,time);
            notesDB = new NotesDB(OpActivity.this);
            dbWriter = notesDB.getWritableDatabase();
            dbWriter.insert(NotesDB.TABLE_NAME,null,cv);
        }





    }





    //获取时间
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }


}
