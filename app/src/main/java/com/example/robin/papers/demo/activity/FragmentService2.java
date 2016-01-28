package com.example.robin.papers.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.adapter.MyGridAdapter;
import com.example.robin.papers.demo.util.MyGridView;

public class FragmentService2 extends Fragment{

    private MyGridView gridview_services,gridView_links;


    //福大服务的图标和名字
    public String[] img_text_service = { "单期绩点", "抢大物实验", "打印上门", "快递代领", "器材正装", "表白神器",
            "福大纪念品", "服务入驻" ,"投诉反馈"};
    public int[] imgs_service = { R.drawable.zcpm,
            R.drawable.qsy,
            R.drawable.ydy,
            R.drawable.kddl,
            R.drawable.hdqczl,
            R.drawable.fxq,
            R.drawable.jyhhz,
            R.drawable.jyhhz,
            R.drawable.jyhhz };


    //友情链接的图标和名字
    public String[] img_text_links = { "Papers主页","新庭芳苑", "福大易班", "教务处", "大物实验","学工系统","人才联合网","申请友链"," "
            };
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_service2,container,false);

        gridview_services =(MyGridView) view.findViewById(R.id.gridviewServices);
        gridView_links = (MyGridView) view.findViewById(R.id.gridviewLinks);

        gridview_services.setAdapter(new MyGridAdapter(getActivity(), img_text_service, imgs_service));
        gridView_links.setAdapter(new MyGridAdapter(getActivity(), img_text_links, imgs_links));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

}
