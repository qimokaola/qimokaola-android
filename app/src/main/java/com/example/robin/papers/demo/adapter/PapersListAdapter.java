package com.example.robin.papers.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.model.PapersInfo;

import java.util.List;

/**
 * Created by Robin on 2015/9/26.
 */
public class PapersListAdapter extends BaseAdapter {

    private LayoutInflater minflater;
    private Context context;
    private List<PapersInfo> mlist;

    public PapersListAdapter(Context mcontext, List<PapersInfo> mlist) {
        this.context = mcontext;
        this.mlist = mlist;
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

        ViewHolder viewHolder =null;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = minflater.inflate(R.layout.item_papers_list,null);
            viewHolder.docTypeImgView = (ImageView) convertView.findViewById(R.id.docType);
            viewHolder.paperName = (TextView) convertView.findViewById(R.id.papersName);
            viewHolder.paperSize = (TextView) convertView.findViewById(R.id.papersSize);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mlist.get(position).type.equals("doc")){
            viewHolder.docTypeImgView.setImageResource(R.drawable.doc);
        }
        if (mlist.get(position).type.equals("docx")){
            viewHolder.docTypeImgView.setImageResource(R.drawable.doc);
        }
        if (mlist.get(position).type.equals("ppt")){
            viewHolder.docTypeImgView.setImageResource(R.drawable.ppt);
        }
        if (mlist.get(position).type.equals("pptx")){
            viewHolder.docTypeImgView.setImageResource(R.drawable.ppt);
        }
        if (mlist.get(position).type.equals("pdf")){
            viewHolder.docTypeImgView.setImageResource(R.drawable.pdf);
        }
        if (mlist.get(position).type.equals("zip")){
            viewHolder.docTypeImgView.setImageResource(R.drawable.zip);
        }



        viewHolder.paperName.setText(mlist.get(position).papername);
        viewHolder.paperSize.setText("大小:"+mlist.get(position).size+" ");

        return convertView;
    }

    class ViewHolder{
        public ImageView docTypeImgView;
        public TextView paperName;
        public TextView paperSize;
    }
}
