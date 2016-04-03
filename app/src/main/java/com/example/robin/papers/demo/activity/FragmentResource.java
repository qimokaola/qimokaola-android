package com.example.robin.papers.demo.activity;


// "资源"fragment页面
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.adapter.AcademyNameAdapter;
import com.example.robin.papers.demo.model.CourseName;
import com.example.robin.papers.demo.util.UrlUnicode;

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

public class FragmentResource extends Fragment {

    //学院列表的listView
    private ListView academyNameListView;

    //存放学院名的数组list
    private List<CourseName> academyNameList;

    //读取学院列表的json文件的网络路径  roots.json
    public static final String path = "http://121.42.177.33/jsonlist/academy.json";

    //刷新时转动的小圆圈
    private LinearLayout refreshCircle;

    private ImageView uploadImg;

    //该页面每个item是 学院名 和 该学院的每个课程的 json文件路径  从roots里读取课程名 拼接left和right

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //fragment加载布局文件
        View view = inflater.inflate(R.layout.fragment_resource, container, false);

        //刷新页面的小圆圈
        refreshCircle = (LinearLayout) view.findViewById(R.id.shuxin1);

        academyNameListView = (ListView) view.findViewById(R.id.courseName);

        uploadImg = (ImageView) view.findViewById(R.id.uploadImg_academy);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至上传web
                Intent toWebIntent = new Intent(getActivity(), WebViewActivity.class);
                toWebIntent.putExtra("url", "http://robinchen.mikecrm.com/f.php?t=ZmhFim");
                toWebIntent.putExtra("title", "上传你的资源");
                startActivity(toWebIntent);
            }
        });

        //加载列表时 显示刷新圈圈 隐藏列表  加载完毕显示列表 隐藏圈圈
        academyNameListView.setVisibility(View.GONE);
        refreshCircle.setVisibility(View.VISIBLE);

        //异步加载
        new MyAsyncTask().execute(path);

        //点击监听 由学院跳转到专业列表页
        academyNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String academyUrl = academyNameList.get(position).papersurl;
                Intent toPapersListIntent = new Intent(getActivity(), CourseListActivity.class);
                toPapersListIntent.putExtra("url", UrlUnicode.encode(academyUrl));
                startActivity(toPapersListIntent);
            }
        });
        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    //给 json 文件的url地址 返回 塞满<实体类>的 List
    private List<CourseName> getJsonData(String url){
        academyNameList = new ArrayList<>();
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
                    courseName.coursename = jsonObject.getString("academyname");
                    courseName.papersurl = jsonObject.getString("url");
                    academyNameList.add(courseName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return academyNameList;
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
    class MyAsyncTask extends AsyncTask<String, Void, List<CourseName>>{

        @Override
        protected List<CourseName> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(List<CourseName> academyNameslist) {
            super.onPostExecute(academyNameslist);
            AcademyNameAdapter adapter = new AcademyNameAdapter(getActivity(),academyNameslist);
            academyNameListView.setAdapter(adapter);
            refreshCircle.setVisibility(View.GONE);
            academyNameListView.setVisibility(View.VISIBLE);

        }
    }





}
