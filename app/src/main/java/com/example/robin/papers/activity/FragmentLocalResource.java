package com.example.robin.papers.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.robin.papers.R;
import com.example.robin.papers.adapter.LocalPaperAdapter;
import com.example.robin.papers.db.NotesDB;
import com.example.robin.papers.db.OrderDB;

// "已下载"页面    三个主tab之一
public class FragmentLocalResource extends Fragment {

    private ListView localPapersListView;    //本地试卷列表view
    private LocalPaperAdapter adapter;
    private Cursor cursor;
    private NotesDB notesDB;
    private OrderDB orderDB;
    private SQLiteDatabase dbReader;
    private ImageView shopCar,nofilesImg;

//    private Button myCart;
//    private Handler handler;


    // 从数据库读取  本地试卷的  路径 网盘地址  最好加上长按删除或者滑动删除

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        localPapersListView = (ListView) view.findViewById(R.id.localPapersList);
        nofilesImg = (ImageView) view.findViewById(R.id.nofilesImg);
        //myCart = (Button) view.findViewById(R.id.myCart);
//        shopCar = (ImageView) view.findViewById(R.id.printShop);
        notesDB = new NotesDB(getActivity());
        orderDB = new OrderDB(getActivity());
        dbReader = notesDB.getWritableDatabase();

//        dbReader = notesDB.getReadableDatabase();

        if (isEmpty(dbReader)){
            nofilesImg.setVisibility(View.VISIBLE);
        }

//        //购物车图标事件 进入订单详情页
//        shopCar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent toShopCar = new Intent(getActivity(),OrdersSubmitActivity.class);
//                startActivity(toShopCar);
//            }
//        });

//        localPapersListView.setItemsCanFocus(new Exception());

//        shopCar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        localPapersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String localUrl = cursor.getString(cursor.getColumnIndex(NotesDB.LOCALURL));
                String type = cursor.getString(cursor.getColumnIndex(NotesDB.TYPE));
                String ids = cursor.getString(cursor.getColumnIndex(NotesDB.ID));
                String names = cursor.getString(cursor.getColumnIndex(NotesDB.PAPERNAME));
                Intent paperDetailIntent = new Intent(getActivity(),LoadedPaperOptionActivity.class);
                paperDetailIntent.putExtra("localUrl",localUrl);
                paperDetailIntent.putExtra("type",type);
                paperDetailIntent.putExtra("id",ids);
                paperDetailIntent.putExtra("name",names);
                startActivity(paperDetailIntent);

            }

        });

        return view;
    }

    private boolean isEmpty(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + NotesDB.TABLE_NAME, null);
        return !cursor.moveToFirst();
    }
//    public void deleteDate(String id) {
//        dbWriter.delete(NotesDB.TABLE_NAME,
//                "_id=" + id, null);
//    }

    public void selectDB(){
        cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null,
                null, null);
        adapter = new LocalPaperAdapter(getActivity(),cursor);
        localPapersListView.setAdapter(adapter);


    }


    @Override
    public void onResume() {
        super.onResume();
        selectDB();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
