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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.adapter.CourseNameAdapter;
import com.example.robin.papers.demo.model.CourseName;

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

public class ResourceFragment extends Fragment {

    private ListView courseNamelistView;      //课程名的listview
    private List<CourseName> courseNameList;   //存放课程名的数组list
                                     //http://7xmwa8.com1.z0.glb.clouddn.com/coursename.json
                                     //http://coursename-course.stor.sinaapp.com/coursename.json

    public static final String path = "http://coursename-course.stor.sinaapp.com/coursename.json"; //读取课程名的json文件的网络路径
    private LinearLayout shuaxin; //刷新时转动的小圆圈

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_resource, container, false); //fragment加载布局文件
        shuaxin = (LinearLayout) view.findViewById(R.id.shuxin1); //刷新页面的小圆圈
        courseNamelistView = (ListView) view.findViewById(R.id.courseName);
        courseNamelistView.setVisibility(View.GONE);
        shuaxin.setVisibility(View.VISIBLE);

        //异步加载
        new MyAsyncTask().execute(path);


        //跳转到试卷列表页
        courseNamelistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String papersurl = courseNameList.get(position).papersurl;
                Intent toPapersListIntent = new Intent(getActivity(),PapersListActivity.class);
                toPapersListIntent.putExtra("url",papersurl);
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
        courseNameList = new ArrayList<>();
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
                    courseName.papersurl = jsonObject.getString("papersurl");
                    courseNameList.add(courseName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return courseNameList;
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
        protected void onPostExecute(List<CourseName> courseNameslist) {
            super.onPostExecute(courseNameslist);
            CourseNameAdapter adapter = new CourseNameAdapter(getActivity(),courseNameslist);
            courseNamelistView.setAdapter(adapter);
            shuaxin.setVisibility(View.GONE);
            courseNamelistView.setVisibility(View.VISIBLE);

        }
    }





}
