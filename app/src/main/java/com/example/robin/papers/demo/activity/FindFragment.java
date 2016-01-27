package com.example.robin.papers.demo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.robin.papers.R;


public class FindFragment extends Fragment {

    private LinearLayout upLoad,buyExp,xtfy,gradeRank,cloudPrint,fdyb,hdqqzl,fdjwc,fankui,kddl,plan,fdrclhw;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find,container,false);


        //找到控件
        upLoad =(LinearLayout) view.findViewById(R.id.upLoad);
        buyExp = (LinearLayout) view.findViewById(R.id.buyExp);
        xtfy = (LinearLayout) view.findViewById(R.id.xtfy);
        gradeRank = (LinearLayout) view.findViewById(R.id.gradeRank);
        cloudPrint = (LinearLayout) view.findViewById(R.id.cloudPrint);
        fdyb = (LinearLayout) view.findViewById(R.id.fdyb);
        hdqqzl = (LinearLayout) view.findViewById(R.id.hdqqzl);
        fdjwc = (LinearLayout) view.findViewById(R.id.fdjwc);
        fankui = (LinearLayout) view.findViewById(R.id.fankui);
        kddl = (LinearLayout) view.findViewById(R.id.kddl);
        plan = (LinearLayout) view.findViewById(R.id.plan);
        fdrclhw = (LinearLayout) view.findViewById(R.id.fdrclhw);


        //福建人才联合网
        fdrclhw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.fjrclh.com/calendar/");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        //飞行器租赁 http://robinchen.mikecrm.com/f.php?t=tlJJC6
        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(),HangPaiActivity.class));
//            Toast.makeText(getActivity(),"福大航拍团队提供, 酷炫到炸!",Toast.LENGTH_SHORT).show();

            }
        });

        //快递代领 http://form.mikecrm.com/f.php?t=39tiOz
        kddl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), KuaiDiActivity.class));
            }
        });

        //绩点排名 要写Activity  登录和 显示排名
        gradeRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"单个学期的绩点及排名, 超级有用!",Toast.LENGTH_SHORT).show();
            }
        });

        //云打印 要写Activity 微信号 http://form.mikecrm.com/f.php?t=l7zy0t
        cloudPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),CloudPrintActivity.class));
            }
        });


        //跳转到福大易班首页
        fdyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://59.77.233.33/");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        //各类活动器材租赁
        hdqqzl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"即将上线~~活动晚会不再愁!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),QiCaiZuLinActivity.class));
            }
        });


        //建议或合作 Activity 留qq http://form.mikecrm.com/f.php?t=jFGpR4
        fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),FanKuiHeZuo.class));
            }
        });

        //跳转到新庭芳苑的点击事件
        xtfy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = Uri.parse("http://bbs.fzu.edu.cn/forum.php?forumlist=1&mobile=2");
//                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(it);
                startActivity(new Intent(getActivity(),XtfyActivity.class));
            }
        });

        //跳转到福大教务处
        fdjwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://jwch.fzu.edu.cn/");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        //抢大物实验的页面    要重写!!!!  一键提交 http://robinchen.mikecrm.com/f.php?t=CFGtnt
        buyExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BuyExpActivity.class));
            }
        });

        //上传试卷的页面
        upLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),UpLoadActivity.class));
            }
        });
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
