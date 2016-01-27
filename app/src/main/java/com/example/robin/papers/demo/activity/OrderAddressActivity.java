package com.example.robin.papers.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.robin.papers.R;

public class OrderAddressActivity extends Activity {
    private Button putBtn;
    private EditText userAddress,userName,userPhone, otherNeeds;
    private String userAddressString, userNameString, userPhoneString, otherString;
    private ImageView back_orderInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_address);

        //初始化控件
        putBtn = (Button) findViewById(R.id.putBtn);
        userAddress = (EditText) findViewById(R.id.userAddress);
        userName = (EditText) findViewById(R.id.userName);
        userPhone = (EditText) findViewById(R.id.userPhone);
        otherNeeds = (EditText) findViewById(R.id.otherNeeds);
        back_orderInfo = (ImageView) findViewById(R.id.Back_edit_info);

        back_orderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //        handler = new Handler();



        //填写完地址和联系方式后的确认按钮的点击事件
        //此事件为 1.提交地址信息和试卷订单信息到服务器  2.清空试卷订单(orderDB)
        putBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAddressString = userAddress.getText().toString().trim();
                userNameString = userName.getText().toString().trim();
                userPhoneString = userPhone.getText().toString().trim();
                otherString = otherNeeds.getText().toString().trim();

                //myCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        final String ret;
//                        String json="";
//                        JSONObject tObj = new JSONObject();
//                        try{
//                            tObj.put("detail", orderDB.getJSONArray());
//                            tObj.put("name", "安卓");
//                            tObj.put("remark", "安卓");
//                            tObj.put("addr", "地址1#3423");
//                            tObj.put("phone", "12345678901");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        json = tObj.toString();
//                        try {
//                            ret = HTTPPoster.postHttpString("http://fzupapers.sinaapp.com/order.php", "json", json);
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getActivity(), ret, Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//            }
//        });

            }
        });

    }
}
