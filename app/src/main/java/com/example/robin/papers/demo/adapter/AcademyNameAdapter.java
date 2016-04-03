package com.example.robin.papers.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.model.CourseName;

import java.util.List;

/**
 * Created by Robin on 2015/9/25.
 * 学院名列表适配器
 */
public class AcademyNameAdapter extends BaseAdapter {

    private List<CourseName> mlist;
    private LayoutInflater minflater;

    public AcademyNameAdapter(Context context, List<CourseName> data) {
        mlist = data;
        minflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = minflater.inflate(R.layout.item_academy_name,null);
            viewHolder.tv_coursename = (TextView) convertView.findViewById(R.id.bookName);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_coursename.setText(mlist.get(position).coursename);
        return convertView;
    }

    class ViewHolder{
        public TextView tv_coursename;
    }
}
