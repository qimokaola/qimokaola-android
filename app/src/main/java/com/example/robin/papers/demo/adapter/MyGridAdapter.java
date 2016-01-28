package com.example.robin.papers.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.util.BaseViewHolder;

/**
 * Created by Robin on 2016/1/28.
 */
public class MyGridAdapter extends BaseAdapter {
    private Context mContext;

    public String[] img_text = { "单期绩点", "大物实验", "打印上门", "快递代领", "器材正装", "表白神器",
            "福大纪念品", "服务入驻" ,"投诉反馈"};
    public int[] imgs = { R.drawable.zcpm,
            R.drawable.qsy,
            R.drawable.ydy,
            R.drawable.kddl,
            R.drawable.hdqczl,
            R.drawable.fxq,
            R.drawable.jyhhz,
            R.drawable.jyhhz,
            R.drawable.jyhhz };

    public MyGridAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return img_text.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.grid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
        ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
        iv.setBackgroundResource(imgs[position]);

        tv.setText(img_text[position]);
        return convertView;
    }


}
