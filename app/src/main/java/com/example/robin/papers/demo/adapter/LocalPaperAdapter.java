package com.example.robin.papers.demo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.db.NotesDB;

/**
 * Created by Robin on 2015/10/2.
 */
public class LocalPaperAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private LinearLayout layout;
    private ImageView typeImg;
    private TextView nameTv;
    private TextView timeTv;


    public LocalPaperAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.item_papers_list, null);
        typeImg = (ImageView) layout.findViewById(R.id.docType);
        nameTv = (TextView) layout.findViewById(R.id.papersName);
        timeTv = (TextView) layout.findViewById(R.id.papersSize);
        cursor.moveToPosition(position);
        String name = cursor.getString(cursor.getColumnIndex(NotesDB.PAPERNAME));
        String time = cursor.getString(cursor.getColumnIndex(NotesDB.TIME));
        String type = cursor.getString(cursor.getColumnIndex(NotesDB.TYPE));
        timeTv.setText(time);
        nameTv.setText(name);
        typeImg.setImageResource(R.drawable.document_type_unknow);
        if (type.equals("doc")){
            typeImg.setImageResource(R.drawable.document_type_word);
        }
        if (type.equals("ppt")){
            typeImg.setImageResource(R.drawable.document_type_ppt);
        }
        if (type.equals("pdf")){
            typeImg.setImageResource(R.drawable.document_type_pdf);
        }
        if (type.equals("pptx")){
            typeImg.setImageResource(R.drawable.document_type_ppt);
        }
        if (type.equals("docx")){
            typeImg.setImageResource(R.drawable.document_type_word);
        }

        return layout;
    }
}
