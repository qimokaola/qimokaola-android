package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.robin.papers.R;
import com.example.robin.papers.demo.adapter.OrderAdapter;
import com.example.robin.papers.demo.db.OrderDB;

public class OrdersSubmitActivity extends Activity {

    private Button submitOrdersBtn; //提交按钮
    private ListView ordersLv;     //订单列表
    private ImageView ordersBackIv;  //返回键
    private Cursor cursor;
    private OrderDB orderDB;
    private OrderAdapter adapter;
    private SQLiteDatabase dbReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_submit);

        ordersBackIv = (ImageView) findViewById(R.id.shopCarBack);
        submitOrdersBtn = (Button) findViewById(R.id.submitOrdersBtn);
        ordersLv = (ListView) findViewById(R.id.ordersLv);
        orderDB = new OrderDB(OrdersSubmitActivity.this);
        dbReader = orderDB.getWritableDatabase();

        //提交订单按钮事件
        submitOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(OrdersSubmitActivity.this,OrderAddressActivity.class));
                //弹出填写个人资料的页面     宿舍号 姓名 手机号 (这三个填写过就直接补上去)  特别需求说明
                //读取整个个ordersDB提交
            }
        });

        //返回键事件
        ordersBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void selectDB(){
        cursor = dbReader.query(OrderDB.TABLE_NAME, null, null, null, null, null, null);
        adapter = new OrderAdapter(OrdersSubmitActivity.this,cursor);
        ordersLv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        selectDB();
    }

}
