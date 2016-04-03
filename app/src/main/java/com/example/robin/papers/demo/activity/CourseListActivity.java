package com.example.robin.papers.demo.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.adapter.CourseNameAdapter;
import com.example.robin.papers.demo.model.CourseName;
import com.example.robin.papers.demo.util.SystemBarTintManager;
import com.example.robin.papers.demo.util.UrlUnicode;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


// 课程列表
public class CourseListActivity extends Activity {

    //专业列表的listView
    private ListView majorNameListView;

    //存放专业名的CourseName实体组
    private List<CourseName> majorNameList;

    private ImageView backIv;
    private TextView baocuo;
    private LinearLayout shuxin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_list);

        backIv = (ImageView) findViewById(R.id.back_major_activity);
        baocuo = (TextView) findViewById(R.id.uploadImg_course);
        baocuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至报错web
                Intent toWebIntent = new Intent(CourseListActivity.this, WebViewActivity.class);
                toWebIntent.putExtra("url", "http://robinchen.mikecrm.com/f.php?t=yFA9QI");
                toWebIntent.putExtra("title", "报错");
                startActivity(toWebIntent);
            }
        });
        shuxin = (LinearLayout) findViewById(R.id.refreshMajorList);
        shuxin.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String majorListPath = getIntent().getStringExtra("url");

        majorNameListView = (ListView) findViewById(R.id.majorListView);
        majorNameListView.setVisibility(View.GONE);

        new MajorAsnycTask().execute(majorListPath);

        //点击监听 由学院跳转到专业列表页
        majorNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String academyUrl = majorNameList.get(position).papersurl;
                Intent toPapersListIntent = new Intent(CourseListActivity.this, PapersListActivity.class);
                toPapersListIntent.putExtra("url", UrlUnicode.encode(academyUrl));
                startActivity(toPapersListIntent);
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


    //给 json 文件的url地址 返回 塞满<实体类>的 List
    private List<CourseName> getJsonData(String url){
        majorNameList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject;
            CourseName courseName;
            try {
                jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0;i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    courseName = new CourseName();
                    courseName.coursename = jsonObject.getString("coursename");
                    courseName.papersurl = jsonObject.getString("url");


                    majorNameList.add(courseName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return majorNameList;
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



    //异步下载课程名列表json文件 并 返回保存课程名的list 并加载到listview里
    class MajorAsnycTask extends AsyncTask<String, Void, List<CourseName>> {

        @Override
        protected List<CourseName> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<CourseName> courseNameslist) {
            super.onPostExecute(courseNameslist);
            CourseNameAdapter adapter = new CourseNameAdapter(CourseListActivity.this,courseNameslist);
            majorNameListView.setAdapter(adapter);
            shuxin.setVisibility(View.GONE);
            majorNameListView.setVisibility(View.VISIBLE);
        }
    }



}
