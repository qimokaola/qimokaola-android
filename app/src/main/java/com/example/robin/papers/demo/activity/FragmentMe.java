package com.example.robin.papers.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.adapter.MyGridAdapter;
import com.example.robin.papers.demo.util.MyGridView;

public class FragmentMe extends Fragment{

    private MyGridView gridview_services,gridView_links;


    //福大服务的名字
    public String[] text_service = { "单期绩点", "抢大物实验", "打印上门", "快递代领", "器材正装", "表白神器",
            "福大纪念品", "服务入驻" ,"投诉反馈"};
    //福大服务是网页还是本地activity  1为网页 2为activity 3是无响应
    public int[] serviceWebOrActivity = {2,1,1,1,1,1,1,1,1};
    //福大服务的图标
    public int[] imgs_service = { R.drawable.zcpm,
            R.drawable.qsy,
            R.drawable.ydy,
            R.drawable.kddl,
            R.drawable.hdqczl,
            R.drawable.fxq,
            R.drawable.jyhhz,
            R.drawable.jyhhz,
            R.drawable.jyhhz };
    //福大服务的网页路径
    public String[] urls_service = {
            "",
            "http://robinchen.mikecrm.com/f.php?t=CFGtnt",
            "http://robinchen.mikecrm.com/f.php?t=hNU2E7",
            "http://robinchen.mikecrm.com/f.php?t=CFGtnt",
            "http://robinchen.mikecrm.com/f.php?t=hNU2E7",
            "http://robinchen.mikecrm.com/f.php?t=CFGtnt",
            "http://robinchen.mikecrm.com/f.php?t=hNU2E7",
            "http://robinchen.mikecrm.com/f.php?t=CFGtnt",
            "http://robinchen.mikecrm.com/f.php?t=hNU2E7"
    };


    //友情链接的图标和名字
    public String[] text_links = { "Papers主页","新庭芳苑", "福大易班", "教务处",
            "大物实验","学工系统","人才联合网","申请友链"," "
            };
    //友情链接是网页还是本地activity  1为网页 2为activity 3是无响应
    public int[] linksWebOrActivity = {1,1,1,1,1,1,1,1,3};
    public int[] imgs_links = { R.drawable.zcpm,
            R.drawable.xtfy,
            R.drawable.yb,
            R.drawable.fdjwc,
            R.drawable.zhaopin,
            R.drawable.zhaopin,
            R.drawable.zhaopin,
            R.drawable.zhaopin,
            R.drawable.zhaopin
            };
    public String[] urls_links = {
            "http://robinchen.mikecrm.com/f.php?t=CFGtnt",
            "http://robinchen.mikecrm.com/f.php?t=hNU2E7",
            "http://robinchen.mikecrm.com/f.php?t=CFGtnt",
            "http://robinchen.mikecrm.com/f.php?t=hNU2E7",
            "http://robinchen.mikecrm.com/f.php?t=CFGtnt",
            "http://robinchen.mikecrm.com/f.php?t=hNU2E7",
            "http://robinchen.mikecrm.com/f.php?t=CFGtnt",
            "http://robinchen.mikecrm.com/f.php?t=hNU2E7",
            "http://robinchen.mikecrm.com/f.php?t=hNU2E7"
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_me,container,false);

        gridview_services =(MyGridView) view.findViewById(R.id.gridviewServices);
        gridView_links = (MyGridView) view.findViewById(R.id.gridviewLinks);

        gridview_services.setAdapter(new MyGridAdapter(getActivity(), text_service, imgs_service));
        gridView_links.setAdapter(new MyGridAdapter(getActivity(), text_links, imgs_links));

        //gridview监听事件
        gridview_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (serviceWebOrActivity[position]==1) {
                    //跳转至web
                    Intent toWebIntent = new Intent(getActivity(), WebViewActivity.class);
                    toWebIntent.putExtra("url", urls_service[position]);
                    toWebIntent.putExtra("title", text_service[position]);
                    startActivity(toWebIntent);
                } else if (serviceWebOrActivity[position]==2){
                    //跳转到activity
                    if (text_service[position].equals("单期绩点")){
                        startActivity(new Intent(getActivity(),SinglePeriodGPA.class));
                    }
                }else if (serviceWebOrActivity[position]==3){
                    //无响应
                }
            }
        });

        //gridView_links监听事件
        gridView_links.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (linksWebOrActivity[position]==1){
                    //跳转至web
                    Intent toWebIntent = new Intent(getActivity(), WebViewActivity.class);
                    toWebIntent.putExtra("url", urls_links[position]);
                    toWebIntent.putExtra("title",text_links[position]);
                    startActivity(toWebIntent);
                }else if (linksWebOrActivity[position]==2){
                    //跳转到activity
                }else if (linksWebOrActivity[position]==3){
                    //无响应
                }
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

}
