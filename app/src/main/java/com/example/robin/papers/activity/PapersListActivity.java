package com.example.robin.papers.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robin.papers.R;
import com.example.robin.papers.adapter.PapersListAdapter;
import com.example.robin.papers.db.NotesDB;
import com.example.robin.papers.model.PapersInfo;
import com.example.robin.papers.util.DownLoader;
import com.example.robin.papers.util.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// "试卷 课件 答案" 页面
public class PapersListActivity extends Activity {

    //试卷实体类集合
    private List<PapersInfo> papersInfoList;

    //试卷列表listView
    private ListView papersListView;

    private ImageView backIv;
    private TextView baocuo;
    private LinearLayout shuxin2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_papers_list);


        backIv = (ImageView) findViewById(R.id.back);
        shuxin2 = (LinearLayout) findViewById(R.id.shuxin2);
        shuxin2.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        baocuo = (TextView) findViewById(R.id.uploadImg_papersList);
        baocuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至报错web
                Intent toWebIntent = new Intent(PapersListActivity.this, WebViewActivity.class);
                toWebIntent.putExtra("url", "http://robinchen.mikecrm.com/f.php?t=yFA9QI");
                toWebIntent.putExtra("title", "报错");
                startActivity(toWebIntent);
            }
        });

        String path = getIntent().getStringExtra("url");

        papersListView = (ListView) findViewById(R.id.papersListView);
        papersListView.setVisibility(View.GONE);

        new PapersAsnycTask().execute(path);

        papersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //url要重新组合
                String paperurl =  papersInfoList.get(position).url;
                String paperName = papersInfoList.get(position).papername;
                String type = papersInfoList.get(position).type;
                String wpurl = paperurl;
                Intent opPage = new Intent(PapersListActivity.this,OpActivity.class);
                opPage.putExtra("paperurl",paperurl);
                opPage.putExtra("type",type);
                opPage.putExtra("paperName",paperName);
                opPage.putExtra("wpurl",wpurl);
                startActivity(opPage);



            }
        });

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

    //显示下载还是发送到电脑的Dailog
    private void showDownPanel(final Context context,final String paperurl,final String paperName, final String type, final String wpurl) {

        //zip
        if (type.equals("zip")){

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("复杂文件");
            builder.setMessage("zip为压缩文件,含多个多种文件,仅支持发送到电脑");
            builder.setPositiveButton("发到电脑!   ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    try {
                        //发送paperurl到我的电脑;
                        Intent intent=new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.setPackage("com.tencent.mobileqq");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                        intent.putExtra(Intent.EXTRA_TEXT, wpurl);
                        intent.putExtra(Intent.EXTRA_TITLE, "发至电脑");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, "选择\"发送到我的电脑\""));
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"你没有安装QQ",Toast.LENGTH_LONG).show();
                    }


                }

            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            builder.setCancelable(true);
            builder.create().show();

        }



        if (type.equals("doc")||type.equals("ppt")||type.equals("pdf")||type.equals("pptx")){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("选择");
        builder.setMessage("下载到手机,发送到电脑, 或按返回键返回");
        builder.setNegativeButton("发到电脑!   ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                try {
                    //发送paperurl到我的电脑;
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setPackage("com.tencent.mobileqq");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                    intent.putExtra(Intent.EXTRA_TEXT, paperName+wpurl);
                    intent.putExtra(Intent.EXTRA_TITLE, "发至电脑");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, "选择\"发送到我的电脑\""));
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"你没有安装QQ",Toast.LENGTH_LONG).show();
                }


            }

        });

        builder.setPositiveButton("下载到手机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new downAsnycTask().execute(paperurl, paperName, type, wpurl);
                Toast.makeText(getApplicationContext()," 开始下载,请到\"我的\"中查看 ",Toast.LENGTH_LONG).show();
            }
        });

        builder.setCancelable(true);
        builder.create().show();
    }
    }
    //下载试卷的异步任务
    class downAsnycTask extends AsyncTask<String,Void,File>{

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
                    file = DownLoader.downloadFile(file,url);
                    addDB();
                    return file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (type.equals("docx")){
                localurl += ".docx";
                file = new File(localurl);
                try {
                    file = DownLoader.downloadFile(file,url);
                    addDB();
                    return file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (type.equals("ppt")){
                localurl += ".ppt";
                file = new File(localurl);
                try {
                    file = DownLoader.downloadFile(file,url);addDB();
                    return file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (type.equals("pptx")){
                localurl += ".pptx";
                file = new File(localurl);
                try {
                    file = DownLoader.downloadFile(file,url);addDB();
                    return file;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (type.equals("pdf")){
                localurl += ".pdf";
                file = new File(localurl);
                try {
                    file = DownLoader.downloadFile(file,url);addDB();
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
            notesDB = new NotesDB(PapersListActivity.this);
            dbWriter = notesDB.getWritableDatabase();
            dbWriter.insert(NotesDB.TABLE_NAME,null,cv);
        }
    }



    private List<PapersInfo> getPapersJson(String url){
        papersInfoList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(url).openStream());

            JSONObject jsonObject;
            PapersInfo papersInfo;
            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0;i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    papersInfo = new PapersInfo();

                    papersInfo.papername = jsonObject.getString("papername");
                    papersInfo.type = jsonObject.getString("type");
                    papersInfo.size = jsonObject.getString("size");
                    papersInfo.url = jsonObject.getString("url");

                    papersInfoList.add(papersInfo);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return papersInfoList;
    }

    private String readStream(InputStream is){
        InputStreamReader isr;
        String result="";
        try {
            String line = "";
            isr = new InputStreamReader(is,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            while((line=br.readLine())!=null){
                result+=line;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }






    //获取时间
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    

    //读取单个课程的试卷列表
    class PapersAsnycTask extends AsyncTask<String,Void,List<PapersInfo>>{

        @Override
        protected List<PapersInfo> doInBackground(String... params) {
            return getPapersJson(params[0]);
        }

        @Override
        protected void onPostExecute(List<PapersInfo> papersInfos) {
            super.onPostExecute(papersInfos);
            PapersListAdapter adapter = new PapersListAdapter(PapersListActivity.this,papersInfos);
            papersListView.setAdapter(adapter);
            papersListView.setVisibility(View.VISIBLE);
            shuxin2.setVisibility(View.GONE);
        }
    }
}
